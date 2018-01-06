package de.phash.semuxrpc.dto;

import java.math.BigInteger;

public class Transaction {
    
    private String from;
    private String to;
    private BigInteger value;
    private BigInteger fee;
    private String data;
    
    private boolean add = false;
    
    
    public boolean isAdd() {
        return add;
    }
    public void setAdd(boolean add) {
        this.add = add;
    }
    public String getFrom() {
        return from;
    }
    public void setFrom(String from) {
        this.from = from;
    }
    public String getTo() {
        return to;
    }
    public void setTo(String to) {
        this.to = to;
    }
    public BigInteger getValue() {
        return value;
    }
    public void setValue(BigInteger value) {
        this.value = value;
    }
    public BigInteger getFee() {
        return fee;
    }
    public void setFee(BigInteger fee) {
        this.fee = fee;
    }
    public String getData() {
        return data;
    }
    public void setData(String data) {
        this.data = data;
    }
    /**
     * @param from
     * @param to
     * @param value
     * @param fee
     * @param data
     * @param add
     */
    public Transaction(String from, String to, BigInteger value, BigInteger fee, String data, boolean add) {
        super();
        this.from = from;
        this.to = to;
        this.value = value;
        this.fee = fee;
        this.data = data;
        this.add = add;
    }
    
    
    
  
}
