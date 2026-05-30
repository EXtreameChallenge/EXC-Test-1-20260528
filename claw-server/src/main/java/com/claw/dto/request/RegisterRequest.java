package com.claw.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class RegisterRequest {
    @NotBlank(message="\u7528\u6237\u540d\u4e0d\u80fd\u4e3a\u7a7a")
    @Size(min=3, max=50, message="\u7528\u6237\u540d\u957f\u5ea63-50")
    private @NotBlank(message="\u7528\u6237\u540d\u4e0d\u80fd\u4e3a\u7a7a") @Size(min=3, max=50, message="\u7528\u6237\u540d\u957f\u5ea63-50") String username;
    @NotBlank(message="\u5bc6\u7801\u4e0d\u80fd\u4e3a\u7a7a")
    @Size(min=6, max=100, message="\u5bc6\u7801\u957f\u5ea66-100")
    private @NotBlank(message="\u5bc6\u7801\u4e0d\u80fd\u4e3a\u7a7a") @Size(min=6, max=100, message="\u5bc6\u7801\u957f\u5ea66-100") String password;
    private String name;
    private String phone;
    private String roleKey = "viewer";

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }

    public String getName() {
        return this.name;
    }

    public String getPhone() {
        return this.phone;
    }

    public String getRoleKey() {
        return this.roleKey;
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

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setRoleKey(String roleKey) {
        this.roleKey = roleKey;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RegisterRequest)) {
            return false;
        }
        RegisterRequest other = (RegisterRequest)o;
        if (!other.canEqual(this)) {
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
        String this$phone = this.getPhone();
        String other$phone = other.getPhone();
        if (this$phone == null ? other$phone != null : !this$phone.equals(other$phone)) {
            return false;
        }
        String this$roleKey = this.getRoleKey();
        String other$roleKey = other.getRoleKey();
        return !(this$roleKey == null ? other$roleKey != null : !this$roleKey.equals(other$roleKey));
    }

    protected boolean canEqual(Object other) {
        return other instanceof RegisterRequest;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        String $username = this.getUsername();
        result = result * 59 + ($username == null ? 43 : $username.hashCode());
        String $password = this.getPassword();
        result = result * 59 + ($password == null ? 43 : $password.hashCode());
        String $name = this.getName();
        result = result * 59 + ($name == null ? 43 : $name.hashCode());
        String $phone = this.getPhone();
        result = result * 59 + ($phone == null ? 43 : $phone.hashCode());
        String $roleKey = this.getRoleKey();
        result = result * 59 + ($roleKey == null ? 43 : $roleKey.hashCode());
        return result;
    }

    public String toString() {
        return "RegisterRequest(username=" + this.getUsername() + ", password=" + this.getPassword() + ", name=" + this.getName() + ", phone=" + this.getPhone() + ", roleKey=" + this.getRoleKey() + ")";
    }
}
