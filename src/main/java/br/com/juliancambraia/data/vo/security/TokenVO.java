package br.com.juliancambraia.data.vo.security;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

public class TokenVO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private String username;
    private Boolean authenticated;
    private Date created;
    private Date expiration;
    private String accessToken;
    private String refreshToken;

    public TokenVO() {
    }

    public TokenVO(String username, Boolean authenticated, Date created, Date expiration, String accessToken, String refreshToken) {
        this.username = username;
        this.authenticated = authenticated;
        this.created = created;
        this.expiration = expiration;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Boolean getAuthenticated() {
        return authenticated;
    }

    public void setAuthenticated(Boolean authenticated) {
        this.authenticated = authenticated;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getExpiration() {
        return expiration;
    }

    public void setExpiration(Date expiration) {
        this.expiration = expiration;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        TokenVO tokenVO = (TokenVO) o;

        return new EqualsBuilder()
                .append(username, tokenVO.username)
                .append(authenticated, tokenVO.authenticated)
                .append(created, tokenVO.created)
                .append(expiration, tokenVO.expiration)
                .append(accessToken, tokenVO.accessToken)
                .append(refreshToken, tokenVO.refreshToken)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(username)
                .append(authenticated)
                .append(created)
                .append(expiration)
                .append(accessToken)
                .append(refreshToken)
                .toHashCode();
    }
}
