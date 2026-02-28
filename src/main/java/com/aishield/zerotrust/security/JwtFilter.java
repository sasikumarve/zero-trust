/**
 * 
 */
package com.aishield.zerotrust.security;

import java.io.IOException;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.Claims;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author sasikumar
 *
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class JwtFilter implements Filter {
  private final JwtUtil jwtUtil;

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {

    HttpServletRequest httpRequest = (HttpServletRequest) request;
    HttpServletResponse httpResponse = (HttpServletResponse) response;

    String uri = httpRequest.getRequestURI();

    if (uri.equals("/login")) {
      chain.doFilter(request, response);
      return;
    }

    String header = httpRequest.getHeader("Authorization");

    if (header == null || !header.startsWith("Bearer ")) {
      log.warn("Unauthorized access attempt to {} - Missing token", uri);
      httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED,
          "Missing or Invalid Authorization Header");
      return;
    }

    String token = header.substring(7);

    try {
      Claims claims = jwtUtil.validateToken(token);
      String riskLevel = claims.get("risk", String.class);

      log.info("JWT validated for URI: {}", uri);
      log.info("Risk Level from token: {}", riskLevel);

      if (uri.contains("/service/sensitive") && !"LOW".equals(riskLevel)) {

        log.warn("Sensitive access denied due to risk level: {}", riskLevel);
        httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN,
            "Access Denied - High Risk for Sensitive Service");
        return;
      }

    } catch (Exception e) {
      log.error("Invalid JWT token for URI: {}", uri);
      httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid Token");
      return;
    }

    chain.doFilter(request, response);
  }
}
