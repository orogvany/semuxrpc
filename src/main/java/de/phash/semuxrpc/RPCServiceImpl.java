package de.phash.semuxrpc;

import java.io.IOException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.semux.config.Config;
import org.semux.config.MainnetConfig;
import org.semux.core.Transaction;
import org.semux.core.TransactionType;
import org.semux.core.Wallet;
import org.semux.crypto.CryptoException;
import org.semux.crypto.Key;
import org.semux.crypto.Hex;
import org.semux.gui.model.WalletAccount;
import org.semux.util.Bytes;
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
    private Server server;

    public RPCServiceImpl(Wallet wallet) {
        super();
        this.wallet = wallet;
    }

    Config c = new MainnetConfig("");

    private Key selectedAccount;

    @Override
    public Config getConfig() {
        return c;
    }

    @Override
    public GetAccountResponse getAccountInfo(String address) throws IOException, ApiException {
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
    public SendTransactionResponse sendTransaction(Transaction transaction, WalletAccount account)
            throws IOException, ApiException, InvalidKeySpecException, CryptoException {
        transaction = transaction.sign(account.getKey());
        return getApi(server).sendTransaction(Hex.encode0x(transaction.toBytes()));
    }

    private String signTransaction(Transaction transaction, String privateKey)
            throws InvalidKeySpecException, CryptoException {
        transaction.sign(new Key(Hex.decode0x(privateKey)));
        return Hex.encode0x(transaction.toBytes());
    }

    public Wallet getWallet() {
        return wallet;
    }

    @Override
    public List<Key> getAccounts() {
        return wallet.getAccounts();
    }

    @Override
    public Key getSelectedWalletAccount() {
        if (selectedAccount == null)

            selectedAccount = wallet.getAccount(0);
        return selectedAccount;
    }

    @Override
    public AccountInfo getAccount() throws IOException, ApiException {
        GetAccountResponse response = getAccountInfo(getSelectedWalletAccount().toAddressString());
        return new AccountInfo(response.getResult());
    }

    @Override
    public void setServer(Server server) {
        this.server = server;

    }

    

    @Override
    public SendTransactionResponse sendTransaction(Transaction transaction, Key selectedWalletAccount)
            throws ApiException {
        transaction.sign(selectedWalletAccount);
        return getApi(server).sendTransaction(Hex.encode0x(transaction.toBytes()));
    }

    @Override
    public void setSelectedWalletAccount(Key item) {
        selectedAccount = item;
    }

    @Override
    public SendTransactionResponse sendTransaction(TransactionType transfer, String toAddr, Long amount,
            String dataString) throws IOException, ApiException {
        Long nonce = getAccountInfo(selectedAccount.toAddressString()).getResult().getNonce();
        byte[] to = Hex.decode0x(toAddr);
        byte[] data = Hex.decode0x(dataString);
        Long fee = getConfig().minTransactionFee();
        Transaction transaction = new Transaction(getConfig().network(),TransactionType.TRANSFER, to, amount, fee, nonce,
                System.currentTimeMillis(), data);
        return sendTransaction(transaction, selectedAccount);
    }

    @Override
    public SendTransactionResponse sendTransactionRaw(TransactionType transfer, String toAddr, Long amount,
            String dataString, String privateKey)
            throws IOException, ApiException, InvalidKeySpecException, CryptoException {
        Key acc = new Key(Hex.decode0x(privateKey));
        Long nonce = getAccountInfo(acc.toAddressString()).getResult().getNonce();
        byte[] to = Hex.decode0x(toAddr);
        if (StringUtils.isEmpty(dataString )) {
            dataString = "";
        }
        byte[] data = Bytes.of(dataString);
        Long fee = getConfig().minTransactionFee();
        Transaction transaction = new Transaction(getConfig().network(), TransactionType.TRANSFER, to, amount, fee, nonce,
                System.currentTimeMillis(), data);
        //String raw = signTransaction(transaction, privateKey);
        return sendTransaction(transaction, acc);
        //return getApi(server).sendTransaction(raw);
    }

  
}
