/**
 * 
 */
package com.aishield.zerotrust.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.aishield.zerotrust.security.JwtUtil;
import com.aishield.zerotrust.service.RiskEngine;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author sasikumar
 *
 */
@RestController
@RequiredArgsConstructor
@Slf4j
public class AuthController {

  private final RiskEngine riskEngine;
  private final JwtUtil jwtUtil;

  @PostMapping("/login")
  public String login(@RequestParam("username") String username) {

    // Calculate Risk
    double score = riskEngine.calculateRiskScore(username);

    // PDP Decision
    String riskLevel = riskEngine.classifyRisk(score);

    log.info("Risk Level: {}", riskLevel);

    if ("HIGH".equals(riskLevel)) {
      return "ACCESS DENIED - High Risk";
    }

    if ("MEDIUM".equals(riskLevel)) {
      return "MFA REQUIRED - Medium Risk";
    }

    // JWT generation on low risk
    String token = jwtUtil.generateToken(username, riskLevel);
    return "JWT: " + token;
  }

}
