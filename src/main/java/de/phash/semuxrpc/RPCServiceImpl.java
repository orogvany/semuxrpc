package de.phash.semuxrpc;

import java.io.IOException;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Base64;
import java.util.Scanner;

import org.json.JSONObject;

import de.phash.semuxrpc.dto.Transaction;

public class RPCServiceImpl implements RPCService {

    @Override
    public AccountInfo getAccountInfo(String address, Server server) throws IOException {

        URL url = new URL(
                server.getServerAddress() + ":" + server.getServerPort() + "/get_account?address=" + address);
        HttpURLConnection conn = getHttpUrlConn(server, url);

        Scanner scan = new Scanner(conn.getInputStream());
        StringBuffer sb = new StringBuffer();
        while (scan.hasNext())
            sb.append(scan.nextLine());
        scan.close();

        JSONObject obj = new JSONObject(sb.toString());
        JSONObject res = obj.getJSONObject("result");

        return new AccountInfo(address, res.getBigInteger("available").toString(), res.getBigInteger("locked").toString(), res.getInt("nonce"));

        
    }

    @Override
    public String transferValue(Transaction transaction, Server server) throws IOException {
       
        BigInteger value = transaction.getValue();
        BigInteger fee = transaction.getFee();
        if (!transaction.isAdd()) value = value.subtract(fee); 
        
        
        URL url = new URL( server.getServerAddress() + ":" + server.getServerPort() +
                "/transfer?from=" + transaction.getFrom() +
                "&to=" + transaction.getTo() +
                "&value=" + transaction.getValue().toString() +
                "&fee=" + transaction.getFee().toString() +
                "&data=" + transaction.getData()
                
                );
        HttpURLConnection conn = getHttpUrlConn(server, url);

        Scanner scan = new Scanner(conn.getInputStream());
        StringBuffer sb = new StringBuffer();
        while (scan.hasNext())
            sb.append(scan.nextLine());
        scan.close();

        JSONObject obj = new JSONObject(sb.toString());
        //JSONObject res = obj.getJSONObject("result");
        return obj.getString("result");
    }

    private HttpURLConnection getHttpUrlConn(Server server, URL url) throws IOException, ProtocolException {
        String authStringEnc = getAuthString(server);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setDoOutput(true);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Authorization", "Basic " + authStringEnc);
        return conn;
    }

    private String getAuthString(Server server) {
        String authString = server.getRpcUser() + ":" + server.getPassword();
        byte[] authEncBytes = Base64.getEncoder().encode(authString.getBytes());
        return new String(authEncBytes);
    }

}
