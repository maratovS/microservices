package org.example.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.example.config.PropertyConfig;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/properties")
public class PropertyController {

    @Autowired
    private PropertyConfig config;
    @GetMapping
    public String getTestProperty() {
        return config.getTestProperty();
    }
}
