package de.phash.semuxrpc.panels;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.security.spec.InvalidKeySpecException;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;

import org.semux.core.Transaction;
import org.semux.core.TransactionType;
import org.semux.crypto.CryptoException;
import org.semux.crypto.Hex;
import org.semux.util.Bytes;

import de.phash.semux.swagger.client.ApiException;
import de.phash.semuxrpc.Action;
import de.phash.semuxrpc.RPCService;
import de.phash.semuxrpc.gui.SwingUtil;

public class SignPanel extends JPanel implements ActionListener {
   
    private JTextField textFieldFrom;
    private JTextField textFieldTo;
    private JTextField textFieldAmount;
    private JPasswordField passwordFieldPrivKey;
    private JTextField textFieldData;
    private RPCService rpcService;

    public SignPanel( RPCService rpcService) {
        this.rpcService = rpcService;
        JLabel lblFrom = new JLabel("from");

        JLabel lblTo = new JLabel("to");

        JLabel lblAmount = new JLabel("Amount");

        JLabel lblPrivateKey = new JLabel("private key");

        JLabel lblData = new JLabel("data");

        textFieldFrom = new JTextField("0xc5f3d47ffb6ccfb8052f49249253c5c8e05dda30");
        textFieldFrom.setColumns(30);

        textFieldTo = new JTextField("0x09c5f2794d69717d538bfcc150644f7685945cfa");
        textFieldTo.setColumns(30);

        textFieldAmount = new JTextField(rpcService.getConfig().minTransactionFee() + "");
        textFieldAmount.setColumns(30);

        passwordFieldPrivKey = new JPasswordField();

        textFieldData = new JTextField();
        textFieldData.setColumns(30);

        JButton btnNewButton = SwingUtil.createDefaultButton("send", this, Action.SIGN);
        GroupLayout groupLayout = new GroupLayout(this);
        groupLayout.setHorizontalGroup(
                groupLayout.createParallelGroup(Alignment.LEADING)
                        .addGroup(groupLayout.createSequentialGroup()
                                .addGap(33)
                                .addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
                                        .addComponent(lblFrom)
                                        .addComponent(lblTo)
                                        .addComponent(lblAmount)
                                        .addComponent(lblPrivateKey)
                                        .addComponent(lblData))
                                .addGap(36)
                                .addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
                                        .addComponent(btnNewButton)
                                        .addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false)
                                                .addComponent(textFieldData)
                                                .addComponent(passwordFieldPrivKey)
                                                .addComponent(textFieldAmount)
                                                .addComponent(textFieldTo)
                                                .addComponent(textFieldFrom, GroupLayout.DEFAULT_SIZE, 297,
                                                        Short.MAX_VALUE)))
                                .addContainerGap(30, Short.MAX_VALUE)));
        groupLayout.setVerticalGroup(
                groupLayout.createParallelGroup(Alignment.LEADING)
                        .addGroup(groupLayout.createSequentialGroup()
                                .addGap(27)
                                .addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
                                        .addComponent(lblFrom)
                                        .addComponent(textFieldFrom, GroupLayout.PREFERRED_SIZE,
                                                GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(ComponentPlacement.UNRELATED)
                                .addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
                                        .addComponent(lblTo)
                                        .addComponent(textFieldTo, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
                                                GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(ComponentPlacement.UNRELATED)
                                .addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
                                        .addComponent(lblAmount)
                                        .addComponent(textFieldAmount, GroupLayout.PREFERRED_SIZE,
                                                GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(ComponentPlacement.UNRELATED)
                                .addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
                                        .addComponent(lblPrivateKey)
                                        .addComponent(passwordFieldPrivKey, GroupLayout.PREFERRED_SIZE,
                                                GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(ComponentPlacement.UNRELATED)
                                .addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
                                        .addComponent(lblData)
                                        .addComponent(textFieldData, GroupLayout.PREFERRED_SIZE,
                                                GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addGap(28)
                                .addComponent(btnNewButton)
                                .addContainerGap(78, Short.MAX_VALUE)));
        setLayout(groupLayout);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        Action action = Action.valueOf(e.getActionCommand());

        switch (action) {
        case SIGN:
            try {

                Long value = Long.parseLong(textFieldAmount.getText());
                Long fee = rpcService.getConfig().minTransactionFee();
                byte[] to = Hex.decode0x(textFieldTo.getText());
                Long nonce = rpcService.getAccountInfo(textFieldFrom.getText()).getResult().getNonce();
                Transaction tx = new org.semux.core.Transaction(TransactionType.TRANSFER, to, value, fee,
                        nonce, System.currentTimeMillis(), Bytes.of(textFieldData.getText()));

                // tx.sign(new
                // EdDSA(Hex.decode0x(String.copyValueOf(passwordFieldPrivKey.getPassword()))));
                rpcService.sendTransaction(tx);
            } catch (IOException | InvalidKeySpecException | CryptoException | ApiException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }

            break;

        default:
            throw new IllegalStateException("No such action");
        }

    }
}
