package com.github.wallet.restwebservice.service.models;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;


@Data
@Setter
@Getter
@Builder
public class TransactionDTO {

    /*private String globalId;*/
    private int typeId;
    private double amount;
    private long walletId;
    private List<Long> lectureIds;
    private Long teacherId;
    private Long groupId;
    private Date lastUpdated;


    public TransactionDTO(){ }

    public TransactionDTO(/*String globalId, */int typeId, double amount, long walletId, List<Long> lectureIds, long teacherId, long groupId, Date lastUpdated){
        /*this.globalId = globalId;*/

        this.typeId = typeId;
        this.amount = amount;
        this.walletId = walletId;
        this.lectureIds = lectureIds;
        this.teacherId = teacherId;
        this.groupId = groupId;
        this.lastUpdated = lastUpdated;

    }
}
