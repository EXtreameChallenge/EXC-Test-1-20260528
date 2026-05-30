package com.claw.controller;

import com.claw.common.Result;
import com.claw.entity.AiConversation;
import com.claw.entity.AiMessage;
import com.claw.service.AiChatService;
import com.claw.service.VoiceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.Map;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Tag(name="AI\u667a\u80fd\u4f53")
@RestController
@RequestMapping(value={"/api/v1/ai"})
public class AiChatController {
    private final AiChatService chatService;

    @Operation(summary="AI\u5bf9\u8bdd")
    @PostMapping(value={"/chat"})
    public Result<Map<String, Object>> chat(@RequestBody Map<String, Object> body, Authentication auth) {
        Long userId = (Long)auth.getPrincipal();
        String message = (String)body.get("message");
        if (message == null || message.trim().isEmpty()) {
            return Result.error(400, "消息内容不能为空");
        }
        Long conversationId = body.get("conversationId") != null ? Long.valueOf(((Number)body.get("conversationId")).longValue()) : null;
        return Result.success(this.chatService.chat(userId, message, conversationId));
    }

    @Operation(summary="\u5bf9\u8bdd\u5217\u8868")
    @GetMapping(value={"/conversations"})
    public Result<List<AiConversation>> conversations(Authentication auth) {
        Long userId = (Long)auth.getPrincipal();
        return Result.success(this.chatService.getConversations(userId));
    }

    @Operation(summary="\u5bf9\u8bdd\u6d88\u606f")
    @GetMapping(value={"/conversations/{id}/messages"})
    public Result<List<AiMessage>> messages(@PathVariable Long id) {
        return Result.success(this.chatService.getMessages(id));
    }

    @Operation(summary="\u5220\u9664\u5bf9\u8bdd")
    @DeleteMapping(value={"/conversations/{id}"})
    public Result<Void> deleteConversation(@PathVariable Long id) {
        this.chatService.deleteConversation(id);
        return Result.success();
    }

    private final VoiceService voiceService;

    @Operation(summary="语音识别")
    @PostMapping(value={"/voice"})
    public Result<Map<String, String>> voice(@RequestParam("audio") MultipartFile audio) {
        try {
            String text = this.voiceService.transcribe(audio);
            return Result.success(Map.of("text", text));
        } catch (Exception e) {
            return Result.error(500, "语音识别失败: " + e.getMessage());
        }
    }

    public AiChatController(AiChatService chatService, VoiceService voiceService) {
        this.chatService = chatService;
        this.voiceService = voiceService;
    }
}
