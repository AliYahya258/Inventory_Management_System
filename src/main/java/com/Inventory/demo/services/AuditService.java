package com.Inventory.demo.services;

import com.Inventory.demo.model.AuditLog;
import com.Inventory.demo.repositories.AuditLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuditService {

    private final AuditLogRepository auditLogRepository;

    @Autowired
    public AuditService(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    public void logAction(String action, String entityType, Long entityId, String performedBy, String description) {
        AuditLog log = new AuditLog(action, entityType, entityId, performedBy, description);
        auditLogRepository.save(log);
    }
}
