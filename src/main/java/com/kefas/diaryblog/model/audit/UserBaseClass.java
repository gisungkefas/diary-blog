package com.kefas.diaryblog.model.audit;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedBy;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class UserBaseClass {

    @CreatedBy
    @Column(updatable = false)
    private long createdBy;



}
