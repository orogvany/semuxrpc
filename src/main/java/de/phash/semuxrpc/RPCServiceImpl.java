package de.phash.semuxrpc;

import java.io.File;
import java.io.IOException;
import java.security.spec.InvalidKeySpecException;

import org.semux.core.Transaction;
import org.semux.core.Wallet;
import org.semux.core.state.Account;
import org.semux.crypto.AES;
import org.semux.crypto.CryptoException;
import org.semux.crypto.EdDSA;
import org.semux.crypto.Hash;
import org.semux.crypto.Hex;
import org.semux.util.Bytes;
import org.semux.util.IOUtil;
import org.semux.util.SimpleDecoder;
import org.semux.util.SystemUtil;
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

    private Wallet wallet;

    public RPCServiceImpl() {
        super();
        wallet = new Wallet(new File(".", "wallet.dat"));
        wallet.unlock("");
        if (wallet.getAccounts() == null || wallet.getAccounts().size() < 1) {
            EdDSA key = new EdDSA();
            wallet.addAccount(key);
            wallet.flush();
        }
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

    public Wallet getWallet() {
        return wallet;
    }

    @Override
    public EdDSA getSelectedWalletAccount() {
        return wallet.getAccount(0);
    }



    @Override
    public AccountInfo getAccount(Server server) throws IOException, ApiException {
        GetAccountResponse response=  getAccountInfo( getSelectedWalletAccount().toAddressString(), server);
        return new AccountInfo(response.getResult());
    }
}
