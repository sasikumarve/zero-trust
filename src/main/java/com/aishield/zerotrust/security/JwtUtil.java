/**
 * 
 */
package com.aishield.zerotrust.security;

import java.security.Key;
import java.util.Date;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

/**
 * @author sasikumar
 *
 */
@Component
public class JwtUtil {
  private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

  public String generateToken(String username, String riskLevel) {
    return Jwts.builder().setSubject(username).claim("risk", riskLevel).setIssuedAt(new Date())
        .setExpiration(new Date(System.currentTimeMillis() + 3600000)).signWith(key).compact();
  }

  public Claims validateToken(String token) {
    return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
  }
}
