package com.geo.news.controller;

import com.geo.news.model.CountryInfo;
import com.geo.news.service.CountryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/country")
public class CountryController {

    private final CountryService countryService;

    public CountryController(CountryService countryService) {
        this.countryService = countryService;
    }

    @GetMapping("/{code}")
    public ResponseEntity<CountryInfo> getCountryInfo(@PathVariable String code) {
        CountryInfo info = countryService.getCountryInfo(code);
        return ResponseEntity.ok(info);
    }
}
