/**
 * 
 */
package com.aishield.zerotrust.service;

import java.util.Random;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

/**
 * @author sasikumar
 *
 */
@Service
@Slf4j
public class RiskEngine {

  public double calculateRiskScore(String username) {

    // Simulated feature scores (0 to 1)
    double loginDeviation = new Random().nextDouble();
    double deviceTrust = new Random().nextDouble();
    double failedAttempts = new Random().nextDouble();
    double apiBurst = new Random().nextDouble();

    double riskScore = (0.35 * loginDeviation) + (0.25 * deviceTrust) + (0.25 * failedAttempts)
        + (0.15 * apiBurst);

    log.info("Feature Scores:");
    log.info("LoginDeviation: {}", loginDeviation);
    log.info("DeviceTrust: {}", deviceTrust);
    log.info("FailedAttempts: {}", failedAttempts);
    log.info("ApiBurst: {}", apiBurst);

    log.info("Final Risk Score: {}", riskScore);

    return riskScore;
  }

  public String classifyRisk(double score) {
    if (score < 0.3)
      return "LOW";
    if (score < 0.7)
      return "MEDIUM";
    return "HIGH";
  }

}
