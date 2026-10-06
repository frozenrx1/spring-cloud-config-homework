package ru.netology.configclient;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ConfigController {
  @Value("${message}")
  private String message;

  @GetMapping("/get")
  public String get() {
    return message;
  }
}
