package de.phash.semuxrpc;

public class AccountInfo {

    private String address;
    
    private String balance;
    private String locked;
    
    private Integer nonce;

    /**
     * @param address
     * @param balance
     * @param locked
     * @param nonce
     */
    AccountInfo(String address, String balance, String locked, Integer nonce) {
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

    public Integer getNonce() {
        return nonce;
    }

    public void setNonce(Integer nonce) {
        this.nonce = nonce;
    }

    
}
