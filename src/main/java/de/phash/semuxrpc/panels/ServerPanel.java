package de.phash.semuxrpc.panels;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

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
import de.phash.semuxrpc.gui.GUIMessages;

public class ServerPanel extends JPanel {

    private static final long serialVersionUID = 1L;
    Server propertiesServer;

    public Server getServer() {
        return propertiesServer;
        /*
         * new Server(textFieldServer.getText(), textFieldPort.getText(),
         * textFieldRPCUser.getText(), String.copyValueOf(passwordField.getPassword()));
         */
    }

    private JTextField textFieldServer;
    private JFormattedTextField textFieldPort;
    private JTextField textFieldRPCUser;
    private JPasswordField passwordField;
    private RPCService rpcService;

    public ServerPanel(RPCService rpcService) {
        super();
        getServerFromProperties();
        this.rpcService = rpcService;
        JLabel lblServer = new JLabel(GUIMessages.get("Server"));

        textFieldServer = new JTextField();
        textFieldServer.setText(propertiesServer.getServerAddress());
        textFieldServer.setColumns(10);
        textFieldServer.getDocument().addDocumentListener((SimpleDocumentListener) e -> {
            updateServer();
        });
        JLabel lblPort = new JLabel(GUIMessages.get("Port"));

        textFieldPort = new JFormattedTextField();
        textFieldPort.setText(propertiesServer.getServerPort());
        textFieldPort.setColumns(10);
        textFieldServer.getDocument().addDocumentListener((SimpleDocumentListener) e -> {
            updateServer();
        });
        JLabel lblRPCUser = new JLabel(GUIMessages.get("RPCUser"));

        textFieldRPCUser = new JTextField();
        textFieldRPCUser.setText(propertiesServer.getRpcUser());
        textFieldRPCUser.setColumns(10);
        textFieldRPCUser.getDocument().addDocumentListener((SimpleDocumentListener) e -> {
            updateServer();
        });
        JLabel lblRpcPass = new JLabel(GUIMessages.get("RPCPass"));

        passwordField = new JPasswordField(propertiesServer.getPassword());
        passwordField.getDocument().addDocumentListener((SimpleDocumentListener) e -> {
            updateServer();
        });

        GroupLayout gl_serverPanel = new GroupLayout(this);
        gl_serverPanel.setHorizontalGroup(gl_serverPanel.createParallelGroup(Alignment.TRAILING)
                .addGroup(gl_serverPanel.createSequentialGroup().addContainerGap()
                        .addGroup(gl_serverPanel.createParallelGroup(Alignment.LEADING)
                                .addComponent(lblRPCUser, GroupLayout.PREFERRED_SIZE, 67, GroupLayout.PREFERRED_SIZE)
                                .addComponent(lblRpcPass).addComponent(lblServer).addComponent(lblPort))
                        .addGap(23)
                        .addGroup(gl_serverPanel.createParallelGroup(Alignment.TRAILING)
                                .addComponent(textFieldPort, Alignment.LEADING, GroupLayout.PREFERRED_SIZE,
                                        GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addComponent(textFieldRPCUser, GroupLayout.DEFAULT_SIZE, 336, Short.MAX_VALUE)
                                .addComponent(passwordField, GroupLayout.DEFAULT_SIZE, 336, Short.MAX_VALUE)
                                .addComponent(textFieldServer, GroupLayout.DEFAULT_SIZE, 336, Short.MAX_VALUE))
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
                .addGroup(gl_serverPanel.createParallelGroup(Alignment.BASELINE).addComponent(lblRpcPass).addComponent(
                        passwordField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
                        GroupLayout.PREFERRED_SIZE))
                .addContainerGap(144, Short.MAX_VALUE)));
        setLayout(gl_serverPanel);
        rpcService.setServer(this.getServer());
    }

    private void getServerFromProperties() {
        Properties prop = new Properties();
        InputStream input = null;
        if (propertiesServer == null)
            propertiesServer = new Server("http://127.0.0.1", "5171", "user", "pass");
        try {

            input = new FileInputStream("server.properties");

            // load a properties file
            prop.load(input);

            propertiesServer = new Server(prop.getProperty(Server.SERVERADDRESS), prop.getProperty(Server.PORT),
                    prop.getProperty(Server.RPCUSER), prop.getProperty(Server.RPCPASSWORD));
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    private void updateServer() {
        writeServerFile();
        rpcService.setServer(getServer());
    }

    private void writeServerFile() {
        Properties prop = new Properties();
        OutputStream output = null;

        try {
            if (propertiesServer == null)
                propertiesServer = new Server("http://127.0.0.1", "5171", "user", "pass");
            output = new FileOutputStream("server.properties");

            
            propertiesServer.setPassword(String.copyValueOf( passwordField.getPassword()));
            propertiesServer.setServerAddress(textFieldServer.getText());
            propertiesServer.setRpcUser(textFieldRPCUser.getText());
            propertiesServer.setServerPort(textFieldPort.getText());
            
            // set the properties value
            prop.setProperty(Server.SERVERADDRESS, propertiesServer.getServerAddress());
            prop.setProperty(Server.PORT, propertiesServer.getServerPort());
            prop.setProperty(Server.RPCUSER, propertiesServer.getRpcUser());
            prop.setProperty(Server.RPCPASSWORD, propertiesServer.getPassword());

            // save properties to project root folder
            prop.store(output, null);

        } catch (IOException io) {
            io.printStackTrace();
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }

    }
}
