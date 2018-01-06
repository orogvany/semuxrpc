package de.phash.semuxrpc;

import java.io.IOException;

import de.phash.semuxrpc.dto.Transaction;

public interface RPCService {


    AccountInfo getAccountInfo(String address, Server server) throws IOException;

    String transferValue(Transaction transaction, Server server) throws IOException;

}
