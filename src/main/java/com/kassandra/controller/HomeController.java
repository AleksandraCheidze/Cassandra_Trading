package com.kassandra.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Home", description = "Home controller endpoints")
public class HomeController {

    @Operation(summary = "Home endpoint", description = "Returns a welcome message")
    @ApiResponse(responseCode = "200", description = "Welcome message returned successfully")
    @GetMapping
    public String home() {
        return "welcome to treading platform";
    }

    @Operation(summary = "Secure endpoint", description = "Returns a welcome message for authenticated users")
    @ApiResponse(responseCode = "200", description = "Welcome message returned successfully")
    @ApiResponse(responseCode = "401", description = "Unauthorized - JWT token is missing or invalid")
    @GetMapping("/api")
    public String secure() {
        return "welcome to treading platform secure";
    }
}
