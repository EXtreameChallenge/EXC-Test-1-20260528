/*
 * Decompiled with CFR 0.152.
 */
package com.claw.dto.response;

import java.util.Set;

public class LoginResponse {
    private String accessToken;
    private String refreshToken;
    private Long expiresIn;
    private UserInfo user;

    public static LoginResponseBuilder builder() {
        return new LoginResponseBuilder();
    }

    public String getAccessToken() {
        return this.accessToken;
    }

    public String getRefreshToken() {
        return this.refreshToken;
    }

    public Long getExpiresIn() {
        return this.expiresIn;
    }

    public UserInfo getUser() {
        return this.user;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public void setExpiresIn(Long expiresIn) {
        this.expiresIn = expiresIn;
    }

    public void setUser(UserInfo user) {
        this.user = user;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof LoginResponse)) {
            return false;
        }
        LoginResponse other = (LoginResponse)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Long this$expiresIn = this.getExpiresIn();
        Long other$expiresIn = other.getExpiresIn();
        if (this$expiresIn == null ? other$expiresIn != null : !((Object)this$expiresIn).equals(other$expiresIn)) {
            return false;
        }
        String this$accessToken = this.getAccessToken();
        String other$accessToken = other.getAccessToken();
        if (this$accessToken == null ? other$accessToken != null : !this$accessToken.equals(other$accessToken)) {
            return false;
        }
        String this$refreshToken = this.getRefreshToken();
        String other$refreshToken = other.getRefreshToken();
        if (this$refreshToken == null ? other$refreshToken != null : !this$refreshToken.equals(other$refreshToken)) {
            return false;
        }
        UserInfo this$user = this.getUser();
        UserInfo other$user = other.getUser();
        return !(this$user == null ? other$user != null : !((Object)this$user).equals(other$user));
    }

    protected boolean canEqual(Object other) {
        return other instanceof LoginResponse;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Long $expiresIn = this.getExpiresIn();
        result = result * 59 + ($expiresIn == null ? 43 : ((Object)$expiresIn).hashCode());
        String $accessToken = this.getAccessToken();
        result = result * 59 + ($accessToken == null ? 43 : $accessToken.hashCode());
        String $refreshToken = this.getRefreshToken();
        result = result * 59 + ($refreshToken == null ? 43 : $refreshToken.hashCode());
        UserInfo $user = this.getUser();
        result = result * 59 + ($user == null ? 43 : ((Object)$user).hashCode());
        return result;
    }

    public String toString() {
        return "LoginResponse(accessToken=" + this.getAccessToken() + ", refreshToken=" + this.getRefreshToken() + ", expiresIn=" + this.getExpiresIn() + ", user=" + this.getUser() + ")";
    }

    public LoginResponse() {
    }

    public LoginResponse(String accessToken, String refreshToken, Long expiresIn, UserInfo user) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.expiresIn = expiresIn;
        this.user = user;
    }

    public static class LoginResponseBuilder {
        private String accessToken;
        private String refreshToken;
        private Long expiresIn;
        private UserInfo user;

        LoginResponseBuilder() {
        }

        public LoginResponseBuilder accessToken(String accessToken) {
            this.accessToken = accessToken;
            return this;
        }

        public LoginResponseBuilder refreshToken(String refreshToken) {
            this.refreshToken = refreshToken;
            return this;
        }

        public LoginResponseBuilder expiresIn(Long expiresIn) {
            this.expiresIn = expiresIn;
            return this;
        }

        public LoginResponseBuilder user(UserInfo user) {
            this.user = user;
            return this;
        }

        public LoginResponse build() {
            return new LoginResponse(this.accessToken, this.refreshToken, this.expiresIn, this.user);
        }

        public String toString() {
            return "LoginResponse.LoginResponseBuilder(accessToken=" + this.accessToken + ", refreshToken=" + this.refreshToken + ", expiresIn=" + this.expiresIn + ", user=" + this.user + ")";
        }
    }

    public static class UserInfo {
        private Long id;
        private String username;
        private String name;
        private String phone;
        private String avatar;
        private String roleKey;
        private Set<String> permissions;

        public static UserInfoBuilder builder() {
            return new UserInfoBuilder();
        }

        public Long getId() {
            return this.id;
        }

        public String getUsername() {
            return this.username;
        }

        public String getName() {
            return this.name;
        }

        public String getPhone() {
            return this.phone;
        }

        public String getAvatar() {
            return this.avatar;
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

        public void setName(String name) {
            this.name = name;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
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
            if (!(o instanceof UserInfo)) {
                return false;
            }
            UserInfo other = (UserInfo)o;
            if (!other.canEqual(this)) {
                return false;
            }
            Long this$id = this.getId();
            Long other$id = other.getId();
            if (this$id == null ? other$id != null : !((Object)this$id).equals(other$id)) {
                return false;
            }
            String this$username = this.getUsername();
            String other$username = other.getUsername();
            if (this$username == null ? other$username != null : !this$username.equals(other$username)) {
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
            String this$avatar = this.getAvatar();
            String other$avatar = other.getAvatar();
            if (this$avatar == null ? other$avatar != null : !this$avatar.equals(other$avatar)) {
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
            return other instanceof UserInfo;
        }

        public int hashCode() {
            int PRIME = 59;
            int result = 1;
            Long $id = this.getId();
            result = result * 59 + ($id == null ? 43 : ((Object)$id).hashCode());
            String $username = this.getUsername();
            result = result * 59 + ($username == null ? 43 : $username.hashCode());
            String $name = this.getName();
            result = result * 59 + ($name == null ? 43 : $name.hashCode());
            String $phone = this.getPhone();
            result = result * 59 + ($phone == null ? 43 : $phone.hashCode());
            String $avatar = this.getAvatar();
            result = result * 59 + ($avatar == null ? 43 : $avatar.hashCode());
            String $roleKey = this.getRoleKey();
            result = result * 59 + ($roleKey == null ? 43 : $roleKey.hashCode());
            Set<String> $permissions = this.getPermissions();
            result = result * 59 + ($permissions == null ? 43 : ((Object)$permissions).hashCode());
            return result;
        }

        public String toString() {
            return "LoginResponse.UserInfo(id=" + this.getId() + ", username=" + this.getUsername() + ", name=" + this.getName() + ", phone=" + this.getPhone() + ", avatar=" + this.getAvatar() + ", roleKey=" + this.getRoleKey() + ", permissions=" + this.getPermissions() + ")";
        }

        public UserInfo() {
        }

        public UserInfo(Long id, String username, String name, String phone, String avatar, String roleKey, Set<String> permissions) {
            this.id = id;
            this.username = username;
            this.name = name;
            this.phone = phone;
            this.avatar = avatar;
            this.roleKey = roleKey;
            this.permissions = permissions;
        }

        public static class UserInfoBuilder {
            private Long id;
            private String username;
            private String name;
            private String phone;
            private String avatar;
            private String roleKey;
            private Set<String> permissions;

            UserInfoBuilder() {
            }

            public UserInfoBuilder id(Long id) {
                this.id = id;
                return this;
            }

            public UserInfoBuilder username(String username) {
                this.username = username;
                return this;
            }

            public UserInfoBuilder name(String name) {
                this.name = name;
                return this;
            }

            public UserInfoBuilder phone(String phone) {
                this.phone = phone;
                return this;
            }

            public UserInfoBuilder avatar(String avatar) {
                this.avatar = avatar;
                return this;
            }

            public UserInfoBuilder roleKey(String roleKey) {
                this.roleKey = roleKey;
                return this;
            }

            public UserInfoBuilder permissions(Set<String> permissions) {
                this.permissions = permissions;
                return this;
            }

            public UserInfo build() {
                return new UserInfo(this.id, this.username, this.name, this.phone, this.avatar, this.roleKey, this.permissions);
            }

            public String toString() {
                return "LoginResponse.UserInfo.UserInfoBuilder(id=" + this.id + ", username=" + this.username + ", name=" + this.name + ", phone=" + this.phone + ", avatar=" + this.avatar + ", roleKey=" + this.roleKey + ", permissions=" + this.permissions + ")";
            }
        }
    }
}
