/**
 * 
 */
package com.aishield.zerotrust.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author sasikumar
 *
 */
@RestController
public class ServiceController {

  @GetMapping("/service/basic")
  public String basicService() {
    return "Accessed Basic Service";
  }

  @GetMapping("/service/sensitive")
  public String sensitiveService() {
    return "Accessed Sensitive Service";
  }

}
