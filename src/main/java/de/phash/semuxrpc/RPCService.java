package de.phash.semuxrpc;

import java.io.IOException;
import java.security.spec.InvalidKeySpecException;

import org.semux.core.Transaction;
import org.semux.crypto.CryptoException;

import de.phash.semux.swagger.client.ApiException;
import de.phash.semux.swagger.client.model.GetAccountResponse;
import de.phash.semux.swagger.client.model.GetAccountTransactionsResponse;
import de.phash.semux.swagger.client.model.SendTransactionResponse;

public interface RPCService {

    GetAccountResponse getAccountInfo(String address, Server server) throws IOException, ApiException;

    SendTransactionResponse sendTransaction(Transaction transaction, Server server) throws IOException, ApiException, InvalidKeySpecException, CryptoException;

}
