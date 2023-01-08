package com.kefas.diaryblog.payload;

import lombok.Data;

@Data
public abstract class UserDateAuditPayload extends DateAuditPayload{

    private Long createBy;

    private Long updateBy;
}
