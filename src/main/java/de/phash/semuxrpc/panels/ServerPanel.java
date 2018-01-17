package de.phash.semuxrpc.panels;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;

import de.phash.semuxrpc.RPCService;
import de.phash.semuxrpc.Server;
import de.phash.semuxrpc.SimpleDocumentListener;

public class ServerPanel extends JPanel {

    public Server getServer() {
        return new Server(textFieldServer.getText(), textFieldPort.getText(), textFieldRPCUser.getText(),
                String.copyValueOf(passwordField.getPassword()), String.copyValueOf(pwdPrivatekey.getPassword()));
    }

    private JTextField textFieldServer;
    private JFormattedTextField textFieldPort;
    private JTextField textFieldRPCUser;
    private JPasswordField passwordField;
    private JTextField textFieldOwnAddress;
    private JPasswordField pwdPrivatekey;
    private RPCService rpcService;

    public ServerPanel(RPCService rpcService) {
        super();
        this.rpcService = rpcService;
        JLabel lblServer = new JLabel("Server");

        textFieldServer = new JTextField();
        textFieldServer.setText("http://192.168.178.52");
        textFieldServer.setColumns(10);
        textFieldServer.getDocument().addDocumentListener((SimpleDocumentListener) e -> {
            updateServer();
        });
        JLabel lblPort = new JLabel("Port");

        textFieldPort = new JFormattedTextField();
        textFieldPort.setText("5171");
        textFieldPort.setColumns(10);
        textFieldServer.getDocument().addDocumentListener((SimpleDocumentListener) e -> {
            updateServer();
        });
        JLabel lblRPCUser = new JLabel("RPC User");

        textFieldRPCUser = new JTextField();
        textFieldRPCUser.setText("phash");
        textFieldRPCUser.setColumns(10);
        textFieldRPCUser.getDocument().addDocumentListener((SimpleDocumentListener) e -> {
            updateServer();
        });
        JLabel lblRpcPass = new JLabel("RPC Pass");

        passwordField = new JPasswordField("x");
        passwordField.getDocument().addDocumentListener((SimpleDocumentListener) e -> {
            updateServer();
        });
        JLabel lblOwnAddress = new JLabel("Own Address");

        textFieldOwnAddress = new JTextField();
        textFieldOwnAddress.setColumns(10);
        textFieldOwnAddress.getDocument().addDocumentListener((SimpleDocumentListener) e -> {
            updateServer();
        });

        JLabel lblPrivateKey = new JLabel("Private Key");

        pwdPrivatekey = new JPasswordField();
        pwdPrivatekey.getDocument().addDocumentListener((SimpleDocumentListener) e -> {
            updateServer();
        });

        GroupLayout gl_serverPanel = new GroupLayout(this);
        gl_serverPanel.setHorizontalGroup(gl_serverPanel.createParallelGroup(Alignment.LEADING)
                .addGroup(Alignment.TRAILING, gl_serverPanel.createSequentialGroup().addContainerGap()
                        .addGroup(gl_serverPanel.createParallelGroup(Alignment.LEADING)
                                .addComponent(lblRPCUser, GroupLayout.PREFERRED_SIZE, 67, GroupLayout.PREFERRED_SIZE)
                                .addComponent(lblRpcPass).addComponent(lblOwnAddress).addComponent(lblServer)
                                .addComponent(lblPort).addComponent(lblPrivateKey))
                        .addGap(14)
                        .addGroup(gl_serverPanel.createParallelGroup(Alignment.TRAILING)
                                .addComponent(pwdPrivatekey, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 349,
                                        Short.MAX_VALUE)
                                .addComponent(textFieldPort, Alignment.LEADING, GroupLayout.PREFERRED_SIZE,
                                        GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addComponent(textFieldRPCUser, GroupLayout.DEFAULT_SIZE, 349, Short.MAX_VALUE)
                                .addComponent(passwordField, GroupLayout.DEFAULT_SIZE, 349, Short.MAX_VALUE)
                                .addComponent(textFieldOwnAddress, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 349,
                                        Short.MAX_VALUE)
                                .addComponent(textFieldServer, GroupLayout.DEFAULT_SIZE, 349, Short.MAX_VALUE))
                        .addContainerGap()));
        gl_serverPanel.setVerticalGroup(gl_serverPanel.createParallelGroup(Alignment.LEADING).addGroup(gl_serverPanel
                .createSequentialGroup().addGap(29)
                .addGroup(gl_serverPanel
                        .createParallelGroup(Alignment.BASELINE).addComponent(lblServer).addComponent(textFieldServer,
                                GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(ComponentPlacement.UNRELATED)
                .addGroup(gl_serverPanel
                        .createParallelGroup(Alignment.BASELINE).addComponent(lblPort).addComponent(textFieldPort,
                                GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(ComponentPlacement.UNRELATED)
                .addGroup(gl_serverPanel
                        .createParallelGroup(Alignment.BASELINE).addComponent(lblRPCUser).addComponent(textFieldRPCUser,
                                GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(ComponentPlacement.UNRELATED)
                .addGroup(gl_serverPanel
                        .createParallelGroup(Alignment.BASELINE).addComponent(lblRpcPass).addComponent(passwordField,
                                GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(ComponentPlacement.UNRELATED)
                .addGroup(gl_serverPanel.createParallelGroup(Alignment.BASELINE).addComponent(lblOwnAddress)
                        .addComponent(textFieldOwnAddress, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
                                GroupLayout.PREFERRED_SIZE))
                .addGap(18)
                .addGroup(gl_serverPanel.createParallelGroup(Alignment.BASELINE).addComponent(lblPrivateKey)
                        .addComponent(pwdPrivatekey, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
                                GroupLayout.PREFERRED_SIZE))
                .addContainerGap(95, Short.MAX_VALUE)));
        setLayout(gl_serverPanel);
        rpcService.setServer(this.getServer());
    }

    private void updateServer() {
        rpcService.setServer(getServer());
    }
}
