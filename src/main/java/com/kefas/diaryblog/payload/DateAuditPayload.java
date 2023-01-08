package com.kefas.diaryblog.payload;

import lombok.Data;

import java.time.Instant;

@Data
public abstract class DateAuditPayload {

    private Instant createAt;

    private Instant updateAt;
}
