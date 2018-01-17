package de.phash.semuxrpc.panels;

import java.awt.BorderLayout;

import javax.swing.JDialog;

import de.phash.semuxrpc.RPCService;

public class ServerDialog extends JDialog {
    public ServerDialog(RPCService rpcService) {
        setSize(600,400);
        getContentPane().setLayout(new BorderLayout(0, 0));
        
        ServerPanel panel = new ServerPanel(rpcService);
        getContentPane().add(panel, BorderLayout.CENTER);
        
    }
    
    public void visible(boolean visible) {
        this.setVisible(visible);
    }

}
