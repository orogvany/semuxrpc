package de.phash.semuxrpc;

import java.io.Serializable;

public class AccountInfo implements Serializable{

    private String address;
    
    private String balance;
    private String locked;
    
    private Long nonce;

    /**
     * @param address
     * @param balance
     * @param locked
     * @param nonce
     */
    AccountInfo(String address, String balance, String locked, Long nonce) {
        super();
        this.address = address;
        this.balance = balance;
        this.locked = locked;
        this.nonce = nonce;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getLocked() {
        return locked;
    }

    public void setLocked(String locked) {
        this.locked = locked;
    }

    public Long getNonce() {
        return nonce;
    }

    public void setNonce(Long nonce) {
        this.nonce = nonce;
    }

    
}
