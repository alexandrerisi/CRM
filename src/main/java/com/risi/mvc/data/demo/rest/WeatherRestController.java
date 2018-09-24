package com.risi.mvc.data.demo.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/weather")
public class WeatherRestController {

    @GetMapping("/status")
    public String testWeather() {
        return "Test Weather";
    }
}
