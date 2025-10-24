package com.example.demo.presentation.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * View Controller
 * Serves the main SPA (Single Page Application) views
 */
@Controller
public class ViewController {
    
    /**
     * Serve the main index.html for the SPA
     */
    @GetMapping("/")
    public String index() {
        return "forward:/index.html";
    }
}
