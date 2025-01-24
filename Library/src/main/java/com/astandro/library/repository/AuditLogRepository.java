package com.astandro.library.repository;

import com.astandro.library.repository.entity.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
    public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
}
