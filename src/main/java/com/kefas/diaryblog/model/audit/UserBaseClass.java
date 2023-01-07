package com.kefas.diaryblog.model.audit;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@MappedSuperclass
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public abstract class UserBaseClass extends BaseClass{

    @CreatedBy
    @Column(updatable = false)
    private Long createdBy;

    @LastModifiedBy
    private Long updatedBy;

    public UserBaseClass(Long id, LocalDateTime createDate, LocalDateTime updateDate, Long createdBy, Long updatedBy){
        super(id, createDate, updateDate);
        this.createdBy = createdBy;
        this.updatedBy = updatedBy;
    }
}
