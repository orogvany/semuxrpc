package de.phash.semuxrpc;

public class Server {

    private String serverAddress;
    private String serverPort;
    private String rpcUser;
    private String password;
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
     */
    Server(String serverAddress, String serverPort, String rpcUser, String password) {
        super();
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
        this.rpcUser = rpcUser;
        this.password = password;
    }

}
