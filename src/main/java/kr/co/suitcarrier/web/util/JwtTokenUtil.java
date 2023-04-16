package kr.co.suitcarrier.web.util;

import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import kr.co.suitcarrier.web.entity.User;

@Component
public class JwtTokenUtil {

    private static final long JWT_ACCESS_EXPIRATION_TIME = 3600; // 1 hour
    private static final long JWT_REFRESH_EXPIRATION_TIME = 3600*24*21; // 3 weeks
    @Value("${jwt_access.secret}")
    private String JWT_ACCESS_SECRET_KEY;
    @Value("${jwt_access.secret}")
    private String JWT_REFRESH_SECRET_KEY;

    // Generate token for user
    public String generateAccessToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        // 필요한 정보를 claims에 넣어준다.
        claims.put("role", user.getRole());
        claims.put("email", user.getEmail());
        return createAccessToken(claims, user.getEmail());
    }

    // Create token
    private String createAccessToken(Map<String, Object> claims, String subject) {
        Date now = new Date();
        // 1 hour
        Date expiryDate = new Date(now.getTime() + JWT_ACCESS_EXPIRATION_TIME * 1000);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(generalKey(JWT_ACCESS_SECRET_KEY))
                .compact();
    }

    // Generate token for user
    public String generateRefreshToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        // 필요한 정보를 claims에 넣어준다.
        claims.put("role", user.getRole());
        claims.put("email", user.getEmail());
        return createRefreshToken(claims, user.getEmail());
    }

    // Create token
    private String createRefreshToken(Map<String, Object> claims, String subject) {
        Date now = new Date();
        // 3 weeks
        Date expiryDate = new Date(now.getTime() + JWT_REFRESH_EXPIRATION_TIME * 1000);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(generalKey(JWT_REFRESH_SECRET_KEY), io.jsonwebtoken.SignatureAlgorithm.HS512)
                .compact();
    }

    // Validate Access token
    public boolean validateAccessToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromAccessToken(token);
        return (username.equals(userDetails.getUsername()) && !isAccessTokenExpired(token));
    }

    // Validate Refresh token
    public boolean validateRefreshToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromRefreshToken(token);
        return (username.equals(userDetails.getUsername()) && !isRefreshTokenExpired(token));
    }

    // Get username from Access token
    public String getUsernameFromAccessToken(String token) {
        return Jwts.parserBuilder().setSigningKey(generalKey(JWT_ACCESS_SECRET_KEY)).build().parseClaimsJws(token).getBody().getSubject();
    }

    // Get username from Refresh token
    public String getUsernameFromRefreshToken(String token) {
        return Jwts.parserBuilder().setSigningKey(generalKey(JWT_ACCESS_SECRET_KEY)).build().parseClaimsJws(token).getBody().getSubject();
    }

    // Get expiration date from Access token
    public Date getExpirationDateFromAccessToken(String token) {
        return Jwts.parserBuilder().setSigningKey(generalKey(JWT_ACCESS_SECRET_KEY)).build().parseClaimsJws(token).getBody().getExpiration();
    }

    // Get expiration date from Refresh token
    public Date getExpirationDateFromRefreshToken(String token) {
        return Jwts.parserBuilder().setSigningKey(generalKey(JWT_ACCESS_SECRET_KEY)).build().parseClaimsJws(token).getBody().getExpiration();
    }

    // Check if Access token is expired
    private boolean isAccessTokenExpired(String token) {
        final Date expiration = getExpirationDateFromAccessToken(token);
        // 현재 시간이 만료 시간보다 뒤에 있으면 true
        return expiration.before(new Date());
    }

    // Check if Refresh token is expired
    private boolean isRefreshTokenExpired(String token) {
        final Date expiration = getExpirationDateFromAccessToken(token);
        // 현재 시간이 만료 시간보다 뒤에 있으면 true
        return expiration.before(new Date());
    }

    public static SecretKey generalKey(String key) {
        byte[] encodedKey = Base64.getDecoder().decode(key);
        // SecretKeySpec(byte[] key, int offset, int len, String algorithm)
        SecretKey returnKey = new SecretKeySpec(encodedKey, 0, encodedKey.length, "HmacSHA512");
        return returnKey;
    }
    
}
