package de.phash.semuxrpc;

import java.awt.EventQueue;
import java.awt.HeadlessException;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.semux.core.Wallet;
import org.semux.crypto.Key;
import org.semux.crypto.Hex;
import org.semux.util.SystemUtil;

import de.phash.semuxrpc.dialog.InputDialog;
import de.phash.semuxrpc.gui.GUIMessages;
import de.phash.semuxrpc.panels.ServerDialog;
import de.phash.semuxrpc.panels.ServerPanel;

public class RpcGUI {

    private static final long serialVersionUID = 1L;
    private RPCService rpcService;

    private Wallet wallet;
    private String dataDir = ".";

    /**
     * @throws HeadlessException
     */
    public RpcGUI() throws HeadlessException {
        super();

        wallet = new Wallet(new File(getDataDir(), "wallet.data"));
        if (!wallet.exists()) {
            showWelcome();
        } else {
            for (int i = 0;; i++) {
                InputDialog dialog = new InputDialog(null, i == 0 ? GUIMessages.get("EnterPassword") + ":"
                        : GUIMessages.get("WrongPasswordPleaseTryAgain") + ":", true);
                String pwd = dialog.getInput();

                if (pwd == null) {
                    SystemUtil.exitAsync(-1);
                } else if (wallet.unlock(pwd)) {
                    break;
                }
            }
            showMain();
        }

    }

    public void showWelcome() {
        // start welcome frame
        WelcomeFrame frame = new WelcomeFrame(wallet);
        frame.setVisible(true);
        frame.join();
        frame.dispose();

        showMain();
    }

    public void showMain() {
        if (wallet.size() > 1) {
            String message = GUIMessages.get("AccountSelection");
            List<Object> options = new ArrayList<>();
            List<Key> list = wallet.getAccounts();
            for (int i = 0; i < list.size(); i++) {
                options.add(Hex.PREF + list.get(i).toAddressString() + ", " + GUIMessages.get("AccountNumShort", i));
            }

        } else if (wallet.size() == 0) {
            wallet.addAccount(new Key());
            wallet.flush();
        }
        rpcService = new RPCServiceImpl(wallet);
        
        // start main frame
        EventQueue.invokeLater(() -> {
            MainFrame frame = new MainFrame(this, rpcService);
            frame.setVisible(true);
        });

    }

    public RPCService getService() {
        return rpcService;
    }

    public String getDataDir() {
        return dataDir;
    }

    /**
     * Selects an tabbed panel to display.
     *
     * @param panel
     * @param button
     */

}
