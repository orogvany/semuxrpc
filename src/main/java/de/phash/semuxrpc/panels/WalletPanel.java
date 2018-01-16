package de.phash.semuxrpc.panels;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

import org.semux.gui.panel.ReceivePanel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.zxing.WriterException;

import de.phash.semux.swagger.client.ApiException;
import de.phash.semux.swagger.client.model.GetAccountResponse;
import de.phash.semuxrpc.Action;
import de.phash.semuxrpc.RPCService;
import de.phash.semuxrpc.RpcGUI;
import de.phash.semuxrpc.gui.GUIMessages;
import de.phash.semuxrpc.gui.SwingUtil;

public class WalletPanel extends JPanel implements ActionListener {

    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(ReceivePanel.class);
    private JTextField textFieldAddress;
    private JTextField textFieldBalance;
    private JTextField textFieldLocked;
    private JLabel lblQr;
    private RpcGUI rpcGUI;
    private RPCService rpcService;
    private GetAccountResponse accountInfo;

    public WalletPanel(RPCService service) {
        this.rpcService = service;
        try {
            accountInfo = rpcService.getAccountInfo(rpcService.getSelectedWalletAccount().toAddressString());
        } catch (IOException | ApiException e) {
            e.printStackTrace();
        }

        JLabel lblAddressLabel = new JLabel("Address");

        textFieldAddress = new JTextField(rpcService.getSelectedWalletAccount().toAddressString());// account.getAddress());
        textFieldAddress.setEditable(false);
        textFieldAddress.setColumns(10);

        JLabel lblBalance = new JLabel("Balance");
        textFieldBalance = new JTextField("");
        textFieldBalance.setEditable(false);
        textFieldBalance.setColumns(10);

        JLabel lblLocked = new JLabel("Locked");
        textFieldLocked = new JTextField();
        textFieldLocked.setEditable(false);
        textFieldLocked.setColumns(10);

        JButton btnRefresh = SwingUtil.createDefaultButton(GUIMessages.get("Refresh"), this, Action.REFRESH);

        lblQr = new JLabel("");
        lblQr.setIcon(SwingUtil.emptyImage(200, 200));
        lblQr.setBorder(new LineBorder(Color.LIGHT_GRAY));
        GroupLayout groupLayout = new GroupLayout(this);
        groupLayout.setHorizontalGroup(
                groupLayout.createParallelGroup(Alignment.TRAILING)
                        .addGroup(groupLayout.createSequentialGroup()
                                .addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
                                        .addGroup(groupLayout.createSequentialGroup()
                                                .addGap(115)
                                                .addComponent(btnRefresh))
                                        .addGroup(groupLayout.createSequentialGroup()
                                                .addGap(32)
                                                .addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
                                                        .addComponent(lblLocked)
                                                        .addGroup(groupLayout.createSequentialGroup()
                                                                .addGroup(groupLayout
                                                                        .createParallelGroup(Alignment.TRAILING)
                                                                        .addComponent(lblAddressLabel)
                                                                        .addComponent(lblBalance))
                                                                .addPreferredGap(ComponentPlacement.UNRELATED)
                                                                .addGroup(groupLayout
                                                                        .createParallelGroup(Alignment.LEADING)
                                                                        .addComponent(textFieldAddress)
                                                                        .addComponent(textFieldLocked)
                                                                        .addComponent(textFieldBalance,
                                                                                GroupLayout.DEFAULT_SIZE, 320,
                                                                                Short.MAX_VALUE)
                                                                        .addGroup(groupLayout.createSequentialGroup()
                                                                                .addComponent(lblQr,
                                                                                        GroupLayout.PREFERRED_SIZE, 200,
                                                                                        Short.MAX_VALUE)
                                                                                .addGap(120)))))))
                                .addGap(40)));
        groupLayout.setVerticalGroup(
                groupLayout.createParallelGroup(Alignment.LEADING)
                        .addGroup(groupLayout.createSequentialGroup()
                                .addGap(24)
                                .addComponent(lblQr, GroupLayout.PREFERRED_SIZE, 200, GroupLayout.PREFERRED_SIZE)
                                .addGap(18)
                                .addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
                                        .addComponent(lblAddressLabel)
                                        .addComponent(textFieldAddress, GroupLayout.PREFERRED_SIZE,
                                                GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addGap(18)
                                .addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
                                        .addComponent(lblBalance)
                                        .addComponent(textFieldBalance, GroupLayout.PREFERRED_SIZE,
                                                GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addGap(18)
                                .addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
                                        .addComponent(lblLocked)
                                        .addComponent(textFieldLocked, GroupLayout.PREFERRED_SIZE,
                                                GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addGap(18)
                                .addComponent(btnRefresh)
                                .addContainerGap()));
        updateAccountInfo();
        setLayout(groupLayout);
    }

    private void setQRcode() {
        try {

            BufferedImage bi = SwingUtil
                    .createQrImage("semux://" + rpcService.getSelectedWalletAccount().toAddressString(), 200, 200);
            lblQr.setIcon(new ImageIcon(bi));

        } catch (WriterException exception) {
            logger.error("Unable to generate QR code", exception);
        }

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        updateAccountInfo();
    }

    private void updateAccountInfo() {
        try {

            accountInfo = rpcService.getAccountInfo("0x"+ rpcService.getSelectedWalletAccount().toAddressString());
            textFieldBalance.setText(
                    accountInfo == null ? "" : Long.toString(accountInfo.getResult().getAvailable()));
            textFieldLocked .setText(
                    accountInfo == null ? "" : Long.toString(accountInfo.getResult().getLocked()));
            setQRcode();
        } catch (IOException | ApiException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

    }
}
