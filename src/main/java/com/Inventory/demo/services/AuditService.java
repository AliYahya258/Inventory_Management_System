package com.Inventory.demo.services;

import com.Inventory.demo.dto.AuditLogDTO;
import com.Inventory.demo.model.AuditLog;
import com.Inventory.demo.repositories.AuditLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuditService {

    private final AuditLogRepository auditLogRepository;

    @Autowired
    public AuditService(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    @Async
    public void logAction(String action, String entityType, Long entityId, String performedBy, String description) {
        AuditLog log = new AuditLog(action, entityType, entityId, performedBy, description);
        auditLogRepository.save(log);
    }

    public List<AuditLogDTO> getRecentLogs(int limit) {
        List<AuditLog> logs = auditLogRepository.findTop100ByOrderByTimestampDesc()
                .stream()
                .limit(limit)
                .collect(Collectors.toList());

        return logs.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private AuditLogDTO convertToDTO(AuditLog log) {
        return new AuditLogDTO(
                log.getId(),
                log.getAction(),
                log.getEntityType(),
                log.getEntityId(),
                log.getPerformedBy(),
                log.getTimestamp(),
                log.getDescription()
        );
    }
}
