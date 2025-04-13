package com.Inventory.demo.controller;

import com.Inventory.demo.dto.DashboardSummaryDTO;
import com.Inventory.demo.dto.StockReportFilterDTO;
import com.Inventory.demo.dto.StockReportResponseDTO;
import com.Inventory.demo.dto.ProductStockSummaryDTO;
import com.Inventory.demo.services.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @GetMapping("/stock-movements")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SELLER')")
    public ResponseEntity<StockReportResponseDTO> getStockMovementReport(
            @RequestParam Long storeId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {

        StockReportFilterDTO filter = new StockReportFilterDTO(storeId, startDate, endDate);
        StockReportResponseDTO report = reportService.generateStockReport(filter);
        return ResponseEntity.ok(report);
    }

    @GetMapping("/stock-summary")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SELLER')")
    public ResponseEntity<List<ProductStockSummaryDTO>> getStockSummary(
            @RequestParam Long storeId) {

        List<ProductStockSummaryDTO> summary = reportService.getProductStockSummary(storeId);
        return ResponseEntity.ok(summary);
    }

    @GetMapping("/dashboard")
    //@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DashboardSummaryDTO> getDashboardSummary() {
        DashboardSummaryDTO summary = reportService.getDashboardSummary();
        return ResponseEntity.ok(summary);
    }

    @PostMapping("/custom-report")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<StockReportResponseDTO> generateCustomReport(
            @RequestBody StockReportFilterDTO filter) {

        StockReportResponseDTO report = reportService.generateStockReport(filter);
        return ResponseEntity.ok(report);
    }
}