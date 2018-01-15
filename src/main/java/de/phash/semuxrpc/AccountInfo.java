package de.phash.semuxrpc;

import java.io.Serializable;

import de.phash.semux.swagger.client.model.AccountType;

public class AccountInfo implements Serializable {

    private String address;

    private Long balance;
    private Long locked;

    private Long nonce;

    /**
     * @param address
     * @param balance
     * @param locked
     * @param nonce
     */
    AccountInfo(String address, Long balance, Long locked, Long nonce) {
        super();
        this.address = address;
        this.balance = balance;
        this.locked = locked;
        this.nonce = nonce;
    }

    public AccountInfo(AccountType result) {
        this.address = result.getAddress();
        this.balance = result.getAvailable();
        this.locked = result.getLocked();
        this.nonce = result.getNonce();
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Long getBalance() {
        return balance;
    }

    public void setBalance(Long balance) {
        this.balance = balance;
    }

    public Long getLocked() {
        return locked;
    }

    public void setLocked(Long locked) {
        this.locked = locked;
    }

    public Long getNonce() {
        return nonce;
    }

    public void setNonce(Long nonce) {
        this.nonce = nonce;
    }

}
