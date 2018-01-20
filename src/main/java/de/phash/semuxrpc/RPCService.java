package de.phash.semuxrpc;

import java.io.IOException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;

import org.semux.config.Config;
import org.semux.core.Transaction;
import org.semux.core.TransactionType;
import org.semux.core.Wallet;
import org.semux.crypto.CryptoException;
import org.semux.crypto.Key;
import org.semux.gui.model.WalletAccount;

import de.phash.semux.swagger.client.ApiException;
import de.phash.semux.swagger.client.model.GetAccountResponse;
import de.phash.semux.swagger.client.model.SendTransactionResponse;

public interface RPCService {

    GetAccountResponse getAccountInfo(String address) throws IOException, ApiException;


    Key getSelectedWalletAccount();

    AccountInfo getAccount() throws IOException, ApiException;

    Config getConfig();

    void setServer(Server server);


    SendTransactionResponse sendTransaction(Transaction transaction, WalletAccount account)
            throws IOException, ApiException, InvalidKeySpecException, CryptoException;

    SendTransactionResponse sendTransaction(Transaction transaction, Key selectedWalletAccount) throws ApiException;

    List<Key> getAccounts();

    void setSelectedWalletAccount(Key item);


    SendTransactionResponse sendTransaction(TransactionType transfer, String toAddr, Long amount,
            String data) throws IOException, ApiException;



    SendTransactionResponse sendTransactionRaw(TransactionType transfer, String toAddr, Long amount, String dataString,
            String privateKey) throws IOException, ApiException, InvalidKeySpecException, CryptoException;


    Wallet getWallet();



}
