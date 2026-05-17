package com.diacono.diacono.domain.service;

import java.util.UUID;

public interface EscalaStatusDomainService {
    void recalcularStatusPorEscalaEventoId(UUID escalaEventoId);
    void recalcularStatusEvento(UUID eventoId);
}

