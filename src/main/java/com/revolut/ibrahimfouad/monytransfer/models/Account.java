package com.revolut.ibrahimfouad.monytransfer.models;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author ibrahimfouad
 */
public class Account {
     private static final AtomicInteger AUTOCOUNT = new AtomicInteger();

    private  int id;
    private String username;
    private BigDecimal balance;
    private Currency currency;

    public Account(String username, BigDecimal balance, Currency currency) {
        this.id = AUTOCOUNT.getAndIncrement();
        this.username = username;
        this.balance = balance;
        this.currency = currency;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }
     @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Account account = (Account) o;

        return id == account.id;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", name='" + username + '\'' +
                ", balance=" + balance +
                ", currency=" + currency +
                '}';
    }

    public void withdraw(BigDecimal amount) {
        this.balance = balance.subtract(amount);
    }

    public void deposit(BigDecimal amount) {
        this.balance = balance.add(amount);
    }
    
    
    
}
