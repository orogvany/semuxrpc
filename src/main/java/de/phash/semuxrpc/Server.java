package de.phash.semuxrpc;

import java.io.Serializable;

public class Server implements Serializable {

    private static final long serialVersionUID = 1L;
    private String serverAddress;
    private String serverPort;
    private String rpcUser;
    private String password;
    public static final String SERVERADDRESS = "server";
    public static final String PORT = "port";
    public static final String RPCUSER = "rpcuser";
    public static final String RPCPASSWORD = "rpcpassword";

    public String getServerAddress() {
        return serverAddress;
    }

    public void setServerAddress(String serverAddress) {
        this.serverAddress = serverAddress;
    }

    public String getServerPort() {
        return serverPort;
    }

    public void setServerPort(String serverPort) {
        this.serverPort = serverPort;
    }

    public String getRpcUser() {
        return rpcUser;
    }

    public void setRpcUser(String rpcUser) {
        this.rpcUser = rpcUser;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @param serverAddress
     * @param serverPort
     * @param rpcUser
     * @param password
     * @param privateKey
     */
    public Server(String serverAddress, String serverPort, String rpcUser, String password) {
        super();
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
        this.rpcUser = rpcUser;
        this.password = password;
    }

}
