package com.kent.gmail.com.runtime.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import javax.crypto.SecretKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenUtils {
  private static final Logger logger = LoggerFactory.getLogger(JwtTokenUtils.class);
  private static final String ID = "ID";

  @Value("${security.jwt.issuer:shopify }")
  private String jwtIssuer;

  @Value("${security.jwt.expiration:86400000 }")
  private long jwtDefaultExpiration;

  @Autowired
  @Qualifier("cachedJWTSecret")
  private SecretKey cachedJWTSecret;

  @Autowired private JwtParser jwtParser;

  public String generateAccessToken(UserSecurityContext user) {

    String id = user.getId();
    return Jwts.builder()
        .subject(String.format("%s,%s", id, user.getUsername()))
        .issuer(jwtIssuer)
        .issuedAt(new Date())
        .expiration(new Date(System.currentTimeMillis() + this.jwtDefaultExpiration)) // 1 week
        .claim(ID, id)
        .signWith(cachedJWTSecret)
        .compact();
  }

  public String getId(Jws<Claims> claimsJws) {
    return (String) claimsJws.getPayload().get(ID);
  }

  public Jws<Claims> getClaims(String token) {
    try {
      return jwtParser.parseSignedClaims(token);
    } catch (MalformedJwtException ex) {
      logger.error("Invalid JWT token - {}", ex.getMessage());
    } catch (ExpiredJwtException ex) {
      logger.error("Expired JWT token - {}", ex.getMessage());
    } catch (UnsupportedJwtException ex) {
      logger.error("Unsupported JWT token - {}", ex.getMessage());
    } catch (IllegalArgumentException ex) {
      logger.error("JWT claims string is empty - {}", ex.getMessage());
    }
    return null;
  }

  @Configuration
  public static class JwtConfiguration {

    @Value(
        "${security.jwt.secret:FByCC5nwD8mIJJnSS+hcKyrCRIlBgt6ZDO3XCAsPWb75HwtpmztFtL2symV/FPT/CtMnuAMounMQriPkTUUuVw=="
            + " }")
    private String jwtTokenSecret;

    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
    @Qualifier("cachedJWTSecret")
    public SecretKey cachedJWTSecret() {
      return getJWTSecret();
    }

    @Bean
    public JwtParser jwtParser(@Qualifier("cachedJWTSecret") SecretKey cachedJWTSecret) {
      return Jwts.parser().verifyWith(cachedJWTSecret).build();
    }

    private SecretKey getJWTSecret() {

      return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtTokenSecret));
    }
  }
}
