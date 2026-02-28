/**
 * 
 */
package com.aishield.zerotrust.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import org.springframework.stereotype.Service;

/**
 * @author sasikumar
 *
 */
@Service
public class EvaluationService {
  public Map<String, Object> runEvaluation(int sampleSize) {

    Random random = new Random();

    int correct = 0;

    int tp = 0; // true positive (HIGH)
    int fp = 0;
    int fn = 0;

    int lowCount = 0;
    int mediumCount = 0;
    int highCount = 0;

    for (int i = 0; i < sampleSize; i++) {

      // Simulated ground truth
      String actualRisk = getRandomRisk(random);

      // Simulated prediction
      String predictedRisk = simulatePrediction(actualRisk, random);

      if (actualRisk.equals(predictedRisk)) {
        correct++;
      }

      if ("HIGH".equals(actualRisk)) {
        if ("HIGH".equals(predictedRisk))
          tp++;
        else
          fn++;
      } else {
        if ("HIGH".equals(predictedRisk))
          fp++;
      }

      switch (predictedRisk) {
        case "LOW" -> lowCount++;
        case "MEDIUM" -> mediumCount++;
        case "HIGH" -> highCount++;
      }
    }

    double accuracy = (double) correct / sampleSize;
    double precision = tp + fp == 0 ? 0 : (double) tp / (tp + fp);
    double recall = tp + fn == 0 ? 0 : (double) tp / (tp + fn);
    double f1 = precision + recall == 0 ? 0 : 2 * (precision * recall) / (precision + recall);

    Map<String, Object> results = new HashMap<>();
    results.put("accuracy", accuracy);
    results.put("precision", precision);
    results.put("recall", recall);
    results.put("f1Score", f1);
    results.put("lowPercentage", (double) lowCount / sampleSize);
    results.put("mediumPercentage", (double) mediumCount / sampleSize);
    results.put("highPercentage", (double) highCount / sampleSize);

    return results;
  }

  private String getRandomRisk(Random random) {
    int value = random.nextInt(100);
    if (value < 70)
      return "LOW";
    if (value < 90)
      return "MEDIUM";
    return "HIGH";
  }

  private String simulatePrediction(String actual, Random random) {
    // 94% accuracy simulation
    if (random.nextDouble() < 0.94) {
      return actual;
    }
    return getRandomRisk(random);
  }
}
