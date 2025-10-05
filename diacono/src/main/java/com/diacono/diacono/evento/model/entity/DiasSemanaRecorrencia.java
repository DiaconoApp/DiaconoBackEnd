package com.diacono.diacono.evento.model.entity;

import java.time.DayOfWeek;

public enum DiasSemanaRecorrencia {
    DOMINGO(DayOfWeek.SUNDAY),
    SEGUNDA(DayOfWeek.MONDAY),
    TERCA(DayOfWeek.TUESDAY),
    QUARTA(DayOfWeek.WEDNESDAY),
    QUINTA(DayOfWeek.THURSDAY),
    SEXTA(DayOfWeek.FRIDAY),
    SABADO(DayOfWeek.SATURDAY);

    private final DayOfWeek dayOfWeek;

    DiasSemanaRecorrencia(DayOfWeek dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }
}
