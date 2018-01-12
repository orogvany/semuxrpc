package de.phash.semuxrpc;

import java.io.IOException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

import org.semux.core.Transaction;
import org.semux.crypto.CryptoException;
import org.semux.crypto.EdDSA;
import org.semux.crypto.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.phash.semux.swagger.client.ApiClient;
import de.phash.semux.swagger.client.ApiException;
import de.phash.semux.swagger.client.api.DefaultApi;
import de.phash.semux.swagger.client.model.GetAccountResponse;
import de.phash.semux.swagger.client.model.SendTransactionResponse;
import de.phash.semuxrpc.gui.SwingUtil;

public class RPCServiceImpl implements RPCService {
    private static final Logger logger = LoggerFactory.getLogger(SwingUtil.class);

    private String getAuthString(Server server) {
        String authString = server.getRpcUser() + ":" + server.getPassword();
        byte[] authEncBytes = Base64.getEncoder().encode(authString.getBytes());
        return new String(authEncBytes);
    }

    @Override
    public GetAccountResponse getAccountInfo(String address, Server server) throws IOException, ApiException {
        return getApi(server).getAccount(address);
    }

    public DefaultApi getApi(Server server) {
        ApiClient client = getApiClient(server);
        return new DefaultApi(client);
    }

    private ApiClient getApiClient(Server server) {
        ApiClient a = new ApiClient();
        a.setUsername(server.getRpcUser());
        a.setPassword(server.getPassword());

        return a.setBasePath(server.getServerAddress() + ":" + server.getServerPort());
    }

    @Override
    public SendTransactionResponse sendTransaction(Transaction transaction, Server server)
            throws IOException, ApiException, InvalidKeySpecException, CryptoException {
        return getApi(server).sendTransaction(signTransaction(transaction, server.getPrivateKey()));
    }

    private String signTransaction(Transaction transaction, String privateKey)
            throws InvalidKeySpecException, CryptoException {
        transaction.sign(new EdDSA(Hex.decode0x(privateKey)));
        return Hex.encode0x(transaction.toBytes());
    }

}
