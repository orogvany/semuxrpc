package de.phash.semuxrpc;

import java.io.IOException;
import java.security.spec.InvalidKeySpecException;

import org.semux.config.Config;
import org.semux.core.Transaction;
import org.semux.core.Wallet;
import org.semux.core.state.Account;
import org.semux.crypto.CryptoException;
import org.semux.crypto.EdDSA;

import de.phash.semux.swagger.client.ApiException;
import de.phash.semux.swagger.client.model.GetAccountResponse;
import de.phash.semux.swagger.client.model.SendTransactionResponse;
import de.phash.semuxrpc.panels.ServerPanel;

public interface RPCService {

    GetAccountResponse getAccountInfo(String address) throws IOException, ApiException;

    SendTransactionResponse sendTransaction(Transaction transaction) throws IOException, ApiException, InvalidKeySpecException, CryptoException;

    EdDSA getSelectedWalletAccount();

    AccountInfo getAccount() throws IOException, ApiException;

    Config getConfig();

    void setServer(Server server);

}
