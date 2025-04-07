package com.Inventory.demo.controller;

import com.Inventory.demo.model.AuditLog;
import com.Inventory.demo.repositories.AuditLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/audits")
public class AuditController {

    @Autowired
    private AuditLogRepository auditLogRepository;

    // ✅ GET all audit logs or filter by optional parameters
    @GetMapping
    public ResponseEntity<List<AuditLog>> getAuditLogs(
            @RequestParam(required = false) String entityType,
            @RequestParam(required = false) String performedBy,
            @RequestParam(required = false) String action,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to
    ) {
        List<AuditLog> logs = auditLogRepository.findAll(); // You can filter this in memory or use custom queries
        return ResponseEntity.ok(logs);
    }
}
