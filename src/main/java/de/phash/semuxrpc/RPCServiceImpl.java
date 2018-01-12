package de.phash.semuxrpc;

import java.io.IOException;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Base64;
import java.util.Scanner;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.phash.semuxrpc.dto.Transaction;
import de.phash.semuxrpc.gui.SwingUtil;

public class RPCServiceImpl implements RPCService {
    private static final Logger logger = LoggerFactory.getLogger(SwingUtil.class);

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

        return new AccountInfo(address, res.getBigInteger("available").toString(),
                res.getBigInteger("locked").toString(), res.getLong("nonce"));

    }

    @Override
    public void sendRawTransaction(String raw, Server server) throws IOException {
        logger.info("raw: " + raw);
        URL url = new URL(
                server.getServerAddress() + ":" + server.getServerPort() + "/send_transaction?raw=" + raw);
        HttpURLConnection conn = getHttpUrlConn(server, url);

        Scanner scan = new Scanner(conn.getInputStream());
        StringBuffer sb = new StringBuffer();
        while (scan.hasNext())
            sb.append(scan.nextLine());
        scan.close();

    }

    @Override
    public String transferValue(Transaction transaction, Server server) throws IOException {

        BigInteger value = transaction.getValue();
        BigInteger fee = transaction.getFee();
        if (!transaction.isAdd())
            value = value.subtract(fee);

        URL url = new URL(server.getServerAddress() + ":" + server.getServerPort() +
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
        // JSONObject res = obj.getJSONObject("result");
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
