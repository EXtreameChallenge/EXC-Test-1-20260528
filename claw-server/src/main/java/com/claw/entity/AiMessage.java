package com.claw.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;

@TableName(value="ai_message")
public class AiMessage {
    @TableId(type=IdType.AUTO)
    private Long id;
    private Long conversationId;
    private String role;
    private String content;
    @TableField(fill=FieldFill.INSERT)
    private LocalDateTime createdAt;

    public Long getId() {
        return this.id;
    }

    public Long getConversationId() {
        return this.conversationId;
    }

    public String getRole() {
        return this.role;
    }

    public String getContent() {
        return this.content;
    }

    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setConversationId(Long conversationId) {
        this.conversationId = conversationId;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof AiMessage)) {
            return false;
        }
        AiMessage other = (AiMessage)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Long this$id = this.getId();
        Long other$id = other.getId();
        if (this$id == null ? other$id != null : !((Object)this$id).equals(other$id)) {
            return false;
        }
        Long this$conversationId = this.getConversationId();
        Long other$conversationId = other.getConversationId();
        if (this$conversationId == null ? other$conversationId != null : !((Object)this$conversationId).equals(other$conversationId)) {
            return false;
        }
        String this$role = this.getRole();
        String other$role = other.getRole();
        if (this$role == null ? other$role != null : !this$role.equals(other$role)) {
            return false;
        }
        String this$content = this.getContent();
        String other$content = other.getContent();
        if (this$content == null ? other$content != null : !this$content.equals(other$content)) {
            return false;
        }
        LocalDateTime this$createdAt = this.getCreatedAt();
        LocalDateTime other$createdAt = other.getCreatedAt();
        return !(this$createdAt == null ? other$createdAt != null : !((Object)this$createdAt).equals(other$createdAt));
    }

    protected boolean canEqual(Object other) {
        return other instanceof AiMessage;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Long $id = this.getId();
        result = result * 59 + ($id == null ? 43 : ((Object)$id).hashCode());
        Long $conversationId = this.getConversationId();
        result = result * 59 + ($conversationId == null ? 43 : ((Object)$conversationId).hashCode());
        String $role = this.getRole();
        result = result * 59 + ($role == null ? 43 : $role.hashCode());
        String $content = this.getContent();
        result = result * 59 + ($content == null ? 43 : $content.hashCode());
        LocalDateTime $createdAt = this.getCreatedAt();
        result = result * 59 + ($createdAt == null ? 43 : ((Object)$createdAt).hashCode());
        return result;
    }

    public String toString() {
        return "AiMessage(id=" + this.getId() + ", conversationId=" + this.getConversationId() + ", role=" + this.getRole() + ", content=" + this.getContent() + ", createdAt=" + this.getCreatedAt() + ")";
    }
}
