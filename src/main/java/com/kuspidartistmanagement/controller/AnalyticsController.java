package com.kuspidartistmanagement.controller;

import com.kuspidartistmanagement.dto.response.AnalyticsResponse;
import com.kuspidartistmanagement.dto.response.ApiResponse;
import com.kuspidartistmanagement.service.AnalyticsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Analytics", description = "Analytics and reporting endpoints")
@RestController
@RequestMapping("/api/analytics")
@RequiredArgsConstructor
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    @Operation(summary = "Get overall analytics")
    @GetMapping
    public ResponseEntity<ApiResponse<AnalyticsResponse>> getAnalytics() {
        AnalyticsResponse response = analyticsService.getOverallAnalytics();
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}