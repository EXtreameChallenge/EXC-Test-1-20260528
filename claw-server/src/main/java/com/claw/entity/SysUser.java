package com.claw.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import java.util.Set;

@TableName(value="sys_user")
public class SysUser {
    @TableId(type=IdType.AUTO)
    private Long id;
    private String username;
    private String password;
    private String name;
    private String email;
    private String phone;
    private String avatar;
    private Integer status;
    private LocalDateTime lastLoginAt;
    @TableField(fill=FieldFill.INSERT)
    private LocalDateTime createdAt;
    @TableField(fill=FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
    @TableLogic
    private Integer deleted;
    @TableField(exist=false)
    private String roleKey;
    @TableField(exist=false)
    private Set<String> permissions;

    public Long getId() {
        return this.id;
    }

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }

    public String getName() {
        return this.name;
    }

    public String getEmail() {
        return this.email;
    }

    public String getPhone() {
        return this.phone;
    }

    public String getAvatar() {
        return this.avatar;
    }

    public Integer getStatus() {
        return this.status;
    }

    public LocalDateTime getLastLoginAt() {
        return this.lastLoginAt;
    }

    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return this.updatedAt;
    }

    public Integer getDeleted() {
        return this.deleted;
    }

    public String getRoleKey() {
        return this.roleKey;
    }

    public Set<String> getPermissions() {
        return this.permissions;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public void setLastLoginAt(LocalDateTime lastLoginAt) {
        this.lastLoginAt = lastLoginAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }

    public void setRoleKey(String roleKey) {
        this.roleKey = roleKey;
    }

    public void setPermissions(Set<String> permissions) {
        this.permissions = permissions;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SysUser)) {
            return false;
        }
        SysUser other = (SysUser)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Long this$id = this.getId();
        Long other$id = other.getId();
        if (this$id == null ? other$id != null : !((Object)this$id).equals(other$id)) {
            return false;
        }
        Integer this$status = this.getStatus();
        Integer other$status = other.getStatus();
        if (this$status == null ? other$status != null : !((Object)this$status).equals(other$status)) {
            return false;
        }
        Integer this$deleted = this.getDeleted();
        Integer other$deleted = other.getDeleted();
        if (this$deleted == null ? other$deleted != null : !((Object)this$deleted).equals(other$deleted)) {
            return false;
        }
        String this$username = this.getUsername();
        String other$username = other.getUsername();
        if (this$username == null ? other$username != null : !this$username.equals(other$username)) {
            return false;
        }
        String this$password = this.getPassword();
        String other$password = other.getPassword();
        if (this$password == null ? other$password != null : !this$password.equals(other$password)) {
            return false;
        }
        String this$name = this.getName();
        String other$name = other.getName();
        if (this$name == null ? other$name != null : !this$name.equals(other$name)) {
            return false;
        }
        String this$email = this.getEmail();
        String other$email = other.getEmail();
        if (this$email == null ? other$email != null : !this$email.equals(other$email)) {
            return false;
        }
        String this$phone = this.getPhone();
        String other$phone = other.getPhone();
        if (this$phone == null ? other$phone != null : !this$phone.equals(other$phone)) {
            return false;
        }
        String this$avatar = this.getAvatar();
        String other$avatar = other.getAvatar();
        if (this$avatar == null ? other$avatar != null : !this$avatar.equals(other$avatar)) {
            return false;
        }
        LocalDateTime this$lastLoginAt = this.getLastLoginAt();
        LocalDateTime other$lastLoginAt = other.getLastLoginAt();
        if (this$lastLoginAt == null ? other$lastLoginAt != null : !((Object)this$lastLoginAt).equals(other$lastLoginAt)) {
            return false;
        }
        LocalDateTime this$createdAt = this.getCreatedAt();
        LocalDateTime other$createdAt = other.getCreatedAt();
        if (this$createdAt == null ? other$createdAt != null : !((Object)this$createdAt).equals(other$createdAt)) {
            return false;
        }
        LocalDateTime this$updatedAt = this.getUpdatedAt();
        LocalDateTime other$updatedAt = other.getUpdatedAt();
        if (this$updatedAt == null ? other$updatedAt != null : !((Object)this$updatedAt).equals(other$updatedAt)) {
            return false;
        }
        String this$roleKey = this.getRoleKey();
        String other$roleKey = other.getRoleKey();
        if (this$roleKey == null ? other$roleKey != null : !this$roleKey.equals(other$roleKey)) {
            return false;
        }
        Set<String> this$permissions = this.getPermissions();
        Set<String> other$permissions = other.getPermissions();
        return !(this$permissions == null ? other$permissions != null : !((Object)this$permissions).equals(other$permissions));
    }

    protected boolean canEqual(Object other) {
        return other instanceof SysUser;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Long $id = this.getId();
        result = result * 59 + ($id == null ? 43 : ((Object)$id).hashCode());
        Integer $status = this.getStatus();
        result = result * 59 + ($status == null ? 43 : ((Object)$status).hashCode());
        Integer $deleted = this.getDeleted();
        result = result * 59 + ($deleted == null ? 43 : ((Object)$deleted).hashCode());
        String $username = this.getUsername();
        result = result * 59 + ($username == null ? 43 : $username.hashCode());
        String $password = this.getPassword();
        result = result * 59 + ($password == null ? 43 : $password.hashCode());
        String $name = this.getName();
        result = result * 59 + ($name == null ? 43 : $name.hashCode());
        String $email = this.getEmail();
        result = result * 59 + ($email == null ? 43 : $email.hashCode());
        String $phone = this.getPhone();
        result = result * 59 + ($phone == null ? 43 : $phone.hashCode());
        String $avatar = this.getAvatar();
        result = result * 59 + ($avatar == null ? 43 : $avatar.hashCode());
        LocalDateTime $lastLoginAt = this.getLastLoginAt();
        result = result * 59 + ($lastLoginAt == null ? 43 : ((Object)$lastLoginAt).hashCode());
        LocalDateTime $createdAt = this.getCreatedAt();
        result = result * 59 + ($createdAt == null ? 43 : ((Object)$createdAt).hashCode());
        LocalDateTime $updatedAt = this.getUpdatedAt();
        result = result * 59 + ($updatedAt == null ? 43 : ((Object)$updatedAt).hashCode());
        String $roleKey = this.getRoleKey();
        result = result * 59 + ($roleKey == null ? 43 : $roleKey.hashCode());
        Set<String> $permissions = this.getPermissions();
        result = result * 59 + ($permissions == null ? 43 : ((Object)$permissions).hashCode());
        return result;
    }

    public String toString() {
        return "SysUser(id=" + this.getId() + ", username=" + this.getUsername() + ", name=" + this.getName() + ", email=" + this.getEmail() + ", phone=" + this.getPhone() + ", avatar=" + this.getAvatar() + ", status=" + this.getStatus() + ", lastLoginAt=" + this.getLastLoginAt() + ", createdAt=" + this.getCreatedAt() + ", updatedAt=" + this.getUpdatedAt() + ", deleted=" + this.getDeleted() + ", roleKey=" + this.getRoleKey() + ", permissions=" + this.getPermissions() + ")";
    }
}
