package com.predrag.a.mediaservice.service.impl;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DefaultAuditService implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        final String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return Optional.of(username);
    }
}
