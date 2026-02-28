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
  public String login(@RequestParam("username") String username,
      @RequestParam(value = "risk", required = false) String forcedRisk) {

    double score;
    String riskLevel;

    if (forcedRisk != null) {
      riskLevel = forcedRisk.toUpperCase();
      score = switch (riskLevel) {
        case "LOW" -> 0.2;
        case "MEDIUM" -> 0.5;
        case "HIGH" -> 0.9;
        default -> 0.5;
      };
    } else {
      // Calculate Risk
      score = riskEngine.calculateRiskScore(username);

      // PDP Decision
      riskLevel = riskEngine.classifyRisk(score);
    }

    log.info("Risk Level: {}", riskLevel);

    if ("HIGH".equals(riskLevel)) {
      return "ACCESS DENIED - High Risk";
    }

    if ("MEDIUM".equals(riskLevel)) {
      return "MFA REQUIRED - Call /mfa endpoint with username";
    }

    // JWT generation on low risk
    String token = jwtUtil.generateToken(username, riskLevel);
    return "JWT: " + token;
  }

  @PostMapping("/mfa")
  public String verifyMfa(@RequestParam("username") String username,
      @RequestParam("otp") String otp) {

    // Simulated OTP validation
    if (!"123456".equals(otp)) {
      return "MFA FAILED - Invalid OTP";
    }

    // After successful MFA → issue LOW-risk JWT
    String token = jwtUtil.generateToken(username, "LOW");

    return "MFA SUCCESS - JWT: " + token;
  }

}
