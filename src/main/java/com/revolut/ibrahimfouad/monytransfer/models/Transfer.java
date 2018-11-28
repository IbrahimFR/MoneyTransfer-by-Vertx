/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.revolut.ibrahimfouad.monytransfer.models;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author ibrahimfouad
 */
public class Transfer {
    
     private static final AtomicInteger COUNTER = new AtomicInteger();

    private final int id;
    private int senderAccountId;
    private int recieverAccountId;
    private BigDecimal amount;
    private Currency currency;
    private String comment;
    private TransferStatus status;
    
     public Transfer(int sourceAccountId, int destinationAccountId, BigDecimal amount, Currency currency, String comment) {
        this.id = COUNTER.getAndIncrement();
        this.senderAccountId = sourceAccountId;
        this.recieverAccountId = destinationAccountId;
        this.amount = amount;
        this.currency = currency;
        this.comment = comment;
        this.status = TransferStatus.CREATED;
    }

    public Transfer() {
        this.id = COUNTER.getAndIncrement();
        this.status = TransferStatus.CREATED;
    }

    public int getId() {
        return id;
    }

    public int getSourceAccountId() {
        return senderAccountId;
    }

    public void setSourceAccountId(int sourceAccountId) {
        this.senderAccountId = sourceAccountId;
    }

    public int getDestinationAccountId() {
        return recieverAccountId;
    }

    public void setDestinationAccountId(int destinationAccountId) {
        this.recieverAccountId = destinationAccountId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public TransferStatus getStatus() {
        return status;
    }

    public void setStatus(TransferStatus status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Transfer transfer = (Transfer) o;

        return id == transfer.id;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public String toString() {
        return "Transfer{" +
                "id=" + id +
                ", sourceAccountId=" + senderAccountId +
                ", destinationAccountId=" + recieverAccountId +
                ", amount=" + amount +
                ", currency=" + currency +
                ", comment='" + comment + '\'' +
                ", status=" + status +
                '}';
    }

    public enum TransferStatus {
        CREATED,
        EXECUTED,
        FAILED
    }

    
}
