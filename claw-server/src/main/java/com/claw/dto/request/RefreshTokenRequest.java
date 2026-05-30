package com.claw.dto.request;

import jakarta.validation.constraints.NotBlank;

public class RefreshTokenRequest {
    @NotBlank(message="refreshToken\u4e0d\u80fd\u4e3a\u7a7a")
    private @NotBlank(message="refreshToken\u4e0d\u80fd\u4e3a\u7a7a") String refreshToken;

    public String getRefreshToken() {
        return this.refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RefreshTokenRequest)) {
            return false;
        }
        RefreshTokenRequest other = (RefreshTokenRequest)o;
        if (!other.canEqual(this)) {
            return false;
        }
        String this$refreshToken = this.getRefreshToken();
        String other$refreshToken = other.getRefreshToken();
        return !(this$refreshToken == null ? other$refreshToken != null : !this$refreshToken.equals(other$refreshToken));
    }

    protected boolean canEqual(Object other) {
        return other instanceof RefreshTokenRequest;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        String $refreshToken = this.getRefreshToken();
        result = result * 59 + ($refreshToken == null ? 43 : $refreshToken.hashCode());
        return result;
    }

    public String toString() {
        return "RefreshTokenRequest(refreshToken=" + this.getRefreshToken() + ")";
    }
}
