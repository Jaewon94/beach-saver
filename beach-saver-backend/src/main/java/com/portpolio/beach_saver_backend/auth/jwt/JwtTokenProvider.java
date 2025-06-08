package com.portpolio.beach_saver_backend.auth.jwt;

import com.portpolio.beach_saver_backend.config.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import javax.crypto.SecretKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class JwtTokenProvider {

  private final SecretKey key;
  private final Long expiresIn;

  public JwtTokenProvider(JwtProperties jwtProperties) {
    String secret = jwtProperties.getSecret();
    this.expiresIn = jwtProperties.getExpiresIn();
    this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
  }

  /**
   * 사용자 정보를 기반으로 JWT 토큰을 생성합니다.
   *
   * @param subject 토큰에 담을 정보 (보통 유저 ID)
   * @return 생성된 JWT 토큰 문자열
   */
  public String createToken(String subject) {
    Date now = new Date();
    Date validity = new Date(now.getTime() + this.expiresIn);

    return Jwts.builder()
        .subject(subject)
        .issuedAt(now)
        .expiration(validity)
        .signWith(this.key)
        .compact();
  }

  /**
   * 토큰을 파싱하여 클레임(정보)을 추출합니다.
   *
   * @param token 검증 및 파싱할 토큰
   * @return 토큰의 payload에 담긴 클레임
   */
  private Claims getClaims(String token) {
    return Jwts.parser().verifyWith(this.key).build().parseSignedClaims(token).getPayload();
  }

  /**
   * 토큰에서 사용자 정보(subject)를 추출합니다.
   *
   * @param token 정보룰 추출할 토큰
   * @return 사용자 정보(subject)
   */
  public String getSubject(String token) {
    return getClaims(token).getSubject();
  }

  /**
   * 토큰의 유효성을 검증합니다.
   *
   * @param token 검증할 토큰
   * @return 토큰이 유효하면 true, 아니면 false
   */
  public boolean validateToken(String token) {
    try {
      Jwts.parser().verifyWith(this.key).build().parseSignedClaims(token);
      return true;
    } catch (SecurityException | MalformedJwtException e) {
      log.error("잘못된 JWT 서명입니다.", e);
    } catch (ExpiredJwtException e) {
      log.error("만료된 JWT 토큰입니다.", e);
    } catch (UnsupportedJwtException e) {
      log.error("지원되지 않는 JWT 토큰입니다.", e);
    } catch (IllegalArgumentException e) {
      log.error("JWT 토큰이 잘못되었습니다.", e);
    }
    return false;
  }
}
