/**
 * 
 */
package com.aishield.zerotrust.controller;

import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.aishield.zerotrust.service.EvaluationService;
import lombok.RequiredArgsConstructor;

/**
 * @author sasikumar
 *
 */
@RestController
@RequiredArgsConstructor
public class MetricsController {

  private final EvaluationService evaluationService;

  @GetMapping("/metrics")
  public Map<String, Object> evaluate(@RequestParam(defaultValue = "1000") int samples) {

    return evaluationService.runEvaluation(samples);
  }

}
