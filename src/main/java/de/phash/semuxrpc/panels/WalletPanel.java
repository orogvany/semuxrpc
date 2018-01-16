package de.phash.semuxrpc.panels;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.LineBorder;

import org.semux.crypto.EdDSA;
import org.semux.gui.SwingUtil;
import org.semux.gui.exception.QRCodeException;
import org.semux.gui.panel.ReceivePanel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.phash.semux.swagger.client.ApiException;
import de.phash.semuxrpc.AccountInfo;
import de.phash.semuxrpc.RPCService;
import de.phash.semuxrpc.RpcGUI;

public class WalletPanel extends JPanel {

    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(ReceivePanel.class);
    private JTextField textFieldAddress;
    private JTextField textFieldBalance;
    private JTextField textFieldLocked;
    private JLabel lblQr;
    private RpcGUI rpcGUI;
    private RPCService rpcService;

    public WalletPanel(RPCService service) {
        this.rpcService = service;
        AccountInfo account = null;
        try {
            account = rpcService.getAccount();
        } catch (IOException | ApiException e) {
            e.printStackTrace();
        }

        JLabel lblAddressLabel = new JLabel("Address");

        textFieldAddress = new JTextField(rpcService.getSelectedWalletAccount().toAddressString());// account.getAddress());
        textFieldAddress.setEditable(false);
        textFieldAddress.setColumns(10);

        JLabel lblBalance = new JLabel("Balance");
        textFieldBalance = new JTextField(account == null ? "" : Long.toString(account.getBalance()));
        textFieldBalance.setEditable(false);
        textFieldBalance.setColumns(10);

        JLabel lblLocked = new JLabel("Locked");

        textFieldLocked = new JTextField(account == null ? "" : Long.toString(account.getLocked()));
        textFieldLocked.setEditable(false);
        textFieldLocked.setColumns(10);

        JButton btnRefresh = new JButton("refresh");

        lblQr = new JLabel("");
        lblQr.setIcon(SwingUtil.emptyImage(200, 200));
        lblQr.setBorder(new LineBorder(Color.LIGHT_GRAY));
        GroupLayout groupLayout = new GroupLayout(this);
        groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(Alignment.TRAILING).addGroup(groupLayout
                .createSequentialGroup()
                .addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
                        .addGroup(groupLayout.createSequentialGroup().addGap(115).addComponent(btnRefresh))
                        .addGroup(groupLayout.createSequentialGroup().addGap(32).addGroup(groupLayout
                                .createParallelGroup(Alignment.LEADING).addComponent(lblLocked)
                                .addGroup(groupLayout.createSequentialGroup()
                                        .addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
                                                .addComponent(lblAddressLabel).addComponent(lblBalance))
                                        .addPreferredGap(ComponentPlacement.UNRELATED)
                                        .addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false)
                                                .addComponent(textFieldAddress).addComponent(textFieldLocked)
                                                .addComponent(textFieldBalance, GroupLayout.DEFAULT_SIZE, 320,
                                                        Short.MAX_VALUE)
                                                .addComponent(lblQr, GroupLayout.PREFERRED_SIZE, 250,
                                                        GroupLayout.PREFERRED_SIZE))))))
                .addContainerGap(40, Short.MAX_VALUE)));
        groupLayout.setVerticalGroup(groupLayout.createParallelGroup(Alignment.LEADING)
                .addGroup(groupLayout.createSequentialGroup().addGap(24)
                        .addComponent(lblQr, GroupLayout.PREFERRED_SIZE, 215, GroupLayout.PREFERRED_SIZE).addGap(18)
                        .addGroup(groupLayout.createParallelGroup(Alignment.BASELINE).addComponent(lblAddressLabel)
                                .addComponent(textFieldAddress, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
                                        GroupLayout.PREFERRED_SIZE))
                        .addGap(18)
                        .addGroup(groupLayout.createParallelGroup(Alignment.BASELINE).addComponent(lblBalance)
                                .addComponent(textFieldBalance, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
                                        GroupLayout.PREFERRED_SIZE))
                        .addGap(18)
                        .addGroup(groupLayout.createParallelGroup(Alignment.BASELINE).addComponent(lblLocked)
                                .addComponent(textFieldLocked, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
                                        GroupLayout.PREFERRED_SIZE))
                        .addGap(18).addComponent(btnRefresh).addContainerGap()));

        setQRcode();
        setLayout(groupLayout);
    }

    private void setQRcode() {
        try {
            EdDSA acc = rpcService.getSelectedWalletAccount();
            if (acc != null) {
                System.out.println(acc.toAddressString());
                BufferedImage bi = SwingUtil.generateQR("semux://" + acc.toAddressString(), 200);
                lblQr.setIcon(new ImageIcon(bi));
            }
        } catch (QRCodeException exception) {
            logger.error("Unable to generate QR code", exception);
        }

    }
}
