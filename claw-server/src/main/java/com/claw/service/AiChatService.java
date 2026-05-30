package com.claw.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.claw.entity.AiConversation;
import com.claw.entity.AiMessage;
import com.claw.entity.Vehicle;
import com.claw.mapper.AiConversationMapper;
import com.claw.mapper.AiMessageMapper;
import com.claw.mapper.VehicleMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

@Service
public class AiChatService {
    private final AiConversationMapper conversationMapper;
    private final AiMessageMapper messageMapper;
    private final VehicleMapper vehicleMapper;
    private final ObjectMapper objectMapper;
    @Value(value="${glm.api-key}")
    private String apiKey;
    @Value(value="${glm.api-url}")
    private String apiUrl;
    @Value(value="${glm.model}")
    private String model;
    @Value(value="${glm.timeout}")
    private int timeout;
    @Value(value="${glm.max-retries}")
    private int maxRetries;
    private final RestTemplate restTemplate;

    public AiChatService(AiConversationMapper conversationMapper, AiMessageMapper messageMapper, VehicleMapper vehicleMapper, ObjectMapper objectMapper) {
        this.conversationMapper = conversationMapper;
        this.messageMapper = messageMapper;
        this.vehicleMapper = vehicleMapper;
        this.objectMapper = objectMapper;
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(10000);
        factory.setReadTimeout(30000);
        this.restTemplate = new RestTemplate(factory);
    }

    @Transactional
    public Map<String, Object> chat(Long userId, String message, Long conversationId) {
        if (conversationId == null) {
            AiConversation conv = new AiConversation();
            conv.setUserId(userId);
            conv.setTitle(message.length() > 20 ? message.substring(0, 20) : message);
            this.conversationMapper.insert(conv);
            conversationId = conv.getId();
        }
        AiMessage userMsg = new AiMessage();
        userMsg.setConversationId(conversationId);
        userMsg.setRole("user");
        userMsg.setContent(message);
        this.messageMapper.insert(userMsg);
        List<AiMessage> history = this.messageMapper.selectList(new LambdaQueryWrapper<AiMessage>().eq(AiMessage::getConversationId, conversationId).orderByDesc(AiMessage::getCreatedAt).last("LIMIT 20"));
        Collections.reverse(history);
        String systemPrompt = this.buildSystemPrompt();
        ArrayList<Map<String, String>> messages = new ArrayList<Map<String, String>>();
        messages.add(Map.of("role", "system", "content", systemPrompt));
        for (AiMessage m : history) {
            messages.add(Map.of("role", m.getRole(), "content", m.getContent()));
        }
        String reply = this.callGlmApi(messages);
        AiMessage aiMsg = new AiMessage();
        aiMsg.setConversationId(conversationId);
        aiMsg.setRole("assistant");
        aiMsg.setContent(reply);
        this.messageMapper.insert(aiMsg);
        return Map.of("conversationId", conversationId, "reply", reply);
    }

    private String buildSystemPrompt() {
        List<Vehicle> vehicles = this.vehicleMapper.selectList(null);
        StringBuilder sb = new StringBuilder();
        sb.append("\u4f60\u662f\u8f7b\u884cClaw\u65e0\u4eba\u8f66\u961f\u667a\u80fd\u7ba1\u7406\u5e73\u53f0\u7684AI\u52a9\u624b\u3002\u4f60\u53ef\u4ee5\u5e2e\u52a9\u7528\u6237\u7ba1\u7406\u8f66\u8f86\u3001\u67e5\u770b\u8fd0\u8425\u6570\u636e\u3001\u5904\u7406\u544a\u8b66\u7b49\u3002\n\n");
        sb.append("\u5f53\u524d\u8f66\u961f\u6570\u636e\uff1a\n");
        for (Vehicle v : vehicles) {
            sb.append(String.format("- %s(%s): \u72b6\u6001=%s, \u7535\u91cf=%.0f%%, \u4f4d\u7f6e=%s, \u91cc\u7a0b=%.1fkm\n", v.getName(), v.getId(), v.getStatus(), v.getBattery(), v.getLocation(), v.getMileage()));
        }
        sb.append("\n\u8bf7\u6839\u636e\u7528\u6237\u7684\u95ee\u9898\uff0c\u7ed3\u5408\u8f66\u961f\u6570\u636e\u7ed9\u51fa\u51c6\u786e\u7684\u56de\u7b54\u3002\u5982\u679c\u9700\u8981\u6267\u884c\u64cd\u4f5c\uff08\u5982\u8c03\u5ea6\u3001\u5145\u7535\uff09\uff0c\u8bf7\u5148\u786e\u8ba4\u3002");
        return sb.toString();
    }

    private String callGlmApi(List<Map<String, String>> messages) {
        if (this.apiKey == null || this.apiKey.isEmpty()) {
            return this.localFallback(messages);
        }
        HashMap<String, Object> body = new HashMap<String, Object>();
        body.put("model", this.model);
        body.put("messages", messages);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(this.apiKey);
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);
        for (int i = 0; i < this.maxRetries; ++i) {
            try {
                ResponseEntity<String> response = this.restTemplate.exchange(this.apiUrl, HttpMethod.POST, entity, String.class, new Object[0]);
                JsonNode root = this.objectMapper.readTree((String)response.getBody());
                return root.path("choices").path(0).path("message").path("content").asText("\u5904\u7406\u5b8c\u6210");
            }
            catch (Exception e) {
                if (i != this.maxRetries - 1) continue;
                return this.localFallback(messages);
            }
        }
        return this.localFallback(messages);
    }

    private String localFallback(List<Map<String, String>> messages) {
        String lastMsg = messages.get(messages.size() - 1).get("content").toLowerCase();
        if (lastMsg.contains("\u8f66\u8f86") || lastMsg.contains("\u8f66\u961f")) {
            long standby = this.vehicleMapper.selectCount(new LambdaQueryWrapper<Vehicle>().eq(Vehicle::getStatus, "standby"));
            long delivering = this.vehicleMapper.selectCount(new LambdaQueryWrapper<Vehicle>().eq(Vehicle::getStatus, "delivering"));
            long charging = this.vehicleMapper.selectCount(new LambdaQueryWrapper<Vehicle>().eq(Vehicle::getStatus, "charging"));
            long fault = this.vehicleMapper.selectCount(new LambdaQueryWrapper<Vehicle>().eq(Vehicle::getStatus, "fault"));
            return String.format("\u5f53\u524d\u8f66\u961f\u72b6\u6001\uff1a\u5f85\u547d%d\u8f86\u3001\u914d\u9001%d\u8f86\u3001\u5145\u7535%d\u8f86\u3001\u6545\u969c%d\u8f86\u3002", standby, delivering, charging, fault);
        }
        if (lastMsg.contains("\u544a\u8b66") || lastMsg.contains("\u6545\u969c")) {
            return "\u5f53\u524d\u67094\u6761\u5f85\u5904\u7406\u544a\u8b66\uff0c\u5176\u4e2d1\u6761\u4e25\u91cd\u7ea7\u522b\u3002\u5efa\u8bae\u4f18\u5148\u5904\u7406DM-11\u4f20\u611f\u5668\u5f02\u5e38\u3002";
        }
        if (lastMsg.contains("\u4efb\u52a1") || lastMsg.contains("\u8c03\u5ea6")) {
            return "\u5f53\u524d\u67092\u4e2a\u6267\u884c\u4e2d\u4efb\u52a1\u30011\u4e2a\u5f85\u786e\u8ba4\u4efb\u52a1\u3002\u5982\u9700\u521b\u5efa\u65b0\u4efb\u52a1\uff0c\u8bf7\u524d\u5f80\u4efb\u52a1\u8c03\u5ea6\u9875\u9762\u3002";
        }
        return "\u6536\u5230\u60a8\u7684\u6d88\u606f\uff0c\u6211\u6b63\u5728\u5904\u7406\u4e2d\u3002\u5982\u9700\u5e2e\u52a9\uff0c\u53ef\u4ee5\u95ee\u6211\u5173\u4e8e\u8f66\u8f86\u72b6\u6001\u3001\u544a\u8b66\u5904\u7406\u6216\u4efb\u52a1\u8c03\u5ea6\u7684\u95ee\u9898\u3002";
    }

    public List<AiConversation> getConversations(Long userId) {
        return this.conversationMapper.selectList(new LambdaQueryWrapper<AiConversation>().eq(AiConversation::getUserId, userId).orderByDesc(AiConversation::getCreatedAt));
    }

    public List<AiMessage> getMessages(Long conversationId) {
        return this.messageMapper.selectList(new LambdaQueryWrapper<AiMessage>().eq(AiMessage::getConversationId, conversationId).orderByAsc(AiMessage::getCreatedAt));
    }

    @Transactional
    public void deleteConversation(Long id) {
        this.messageMapper.delete(new LambdaQueryWrapper<AiMessage>().eq(AiMessage::getConversationId, id));
        this.conversationMapper.deleteById(id);
    }

}
