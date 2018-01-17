package de.phash.semuxrpc.panels;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.IOException;
import java.security.spec.InvalidKeySpecException;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.semux.core.TransactionType;
import org.semux.crypto.CryptoException;
import org.semux.crypto.EdDSA;
import org.semux.message.GUIMessages;

import de.phash.semux.swagger.client.ApiException;
import de.phash.semux.swagger.client.model.SendTransactionResponse;
import de.phash.semuxrpc.Action;
import de.phash.semuxrpc.RPCService;
import de.phash.semuxrpc.gui.SwingUtil;
import javax.swing.JPasswordField;

public class TransferPanel extends JPanel implements ActionListener {
    private RPCService rpcService;

    private static final long serialVersionUID = 1L;
    private JTextField textFieldTo;
    private JTextField formattedTextFieldAmount;
    private JTextField formattedTextFieldFee;
    private JCheckBox chckbxAutoFee;

    private JLabel lblResult;
    private JTextField textFieldData;
    private JPasswordField privateKeyField;

    public TransferPanel(RPCService rpcService) {
        this.rpcService = rpcService;
        JLabel lblFrom = new JLabel("from");

        JLabel lblTo = new JLabel("to");

        JLabel lblAmount = new JLabel("amount");

        JLabel lblFee = new JLabel("fee");

        JLabel lblAutofee = new JLabel("auto-fee");

        textFieldTo = new JTextField();
        textFieldTo.setColumns(25);

        formattedTextFieldAmount = new JTextField();

        formattedTextFieldFee = new JTextField("50000000");

        chckbxAutoFee = new JCheckBox("");

        JButton btnSend = new JButton("f");// SwingUtil.createDefaultButton("send", this, Action.TRANSFER);

        lblResult = new JLabel("");

        JLabel lblNewLabel = new JLabel("New label");

        textFieldData = new JTextField();
        textFieldData.setColumns(25);
        
        privateKeyField = new JPasswordField();
        
        JLabel label = new JLabel("New label");

        GroupLayout groupLayout = new GroupLayout(this);
        groupLayout.setHorizontalGroup(
            groupLayout.createParallelGroup(Alignment.LEADING)
                .addGroup(groupLayout.createSequentialGroup()
                    .addGap(29)
                    .addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
                        .addComponent(lblFrom)
                        .addComponent(lblTo)
                        .addComponent(lblAmount)
                        .addComponent(lblFee)
                        .addComponent(lblAutofee)
                        .addComponent(lblNewLabel))
                    .addGap(37)
                    .addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
                        .addGroup(groupLayout.createSequentialGroup()
                            .addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
                                .addComponent(formattedTextFieldFee, GroupLayout.DEFAULT_SIZE, 315, Short.MAX_VALUE)
                                .addComponent(formattedTextFieldAmount, 315, 315, Short.MAX_VALUE)
                                .addComponent(textFieldData, GroupLayout.DEFAULT_SIZE, 315, Short.MAX_VALUE)
                                .addComponent(textFieldTo, GroupLayout.DEFAULT_SIZE, 315, Short.MAX_VALUE)
                                .addComponent(privateKeyField, GroupLayout.DEFAULT_SIZE, 315, Short.MAX_VALUE))
                            .addGap(19))
                        .addGroup(groupLayout.createSequentialGroup()
                            .addComponent(chckbxAutoFee)
                            .addGap(52)
                            .addComponent(lblResult)
                            .addGap(38)
                            .addComponent(btnSend)
                            .addGap(44)
                            .addComponent(label)
                            .addGap(76))))
        );
        groupLayout.setVerticalGroup(
            groupLayout.createParallelGroup(Alignment.LEADING)
                .addGroup(groupLayout.createSequentialGroup()
                    .addGap(28)
                    .addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
                        .addComponent(lblFrom)
                        .addComponent(privateKeyField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                    .addGap(18)
                    .addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
                        .addComponent(lblTo)
                        .addComponent(textFieldTo, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                    .addGap(18)
                    .addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
                        .addComponent(lblAmount)
                        .addComponent(formattedTextFieldAmount, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                    .addGap(18)
                    .addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
                        .addComponent(lblFee)
                        .addComponent(formattedTextFieldFee, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                    .addGap(18)
                    .addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
                        .addComponent(lblNewLabel)
                        .addComponent(textFieldData, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                    .addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
                        .addGroup(groupLayout.createSequentialGroup()
                            .addGap(27)
                            .addComponent(lblResult))
                        .addGroup(groupLayout.createSequentialGroup()
                            .addGap(18)
                            .addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
                                .addComponent(chckbxAutoFee)
                                .addComponent(lblAutofee)
                                .addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
                                    .addComponent(btnSend)
                                    .addComponent(label)))))
                    .addContainerGap(47, Short.MAX_VALUE))
        );
        setLayout(groupLayout);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        Action action = Action.valueOf(e.getActionCommand());

        switch (action) {
        case TRANSFER:
            lblResult.setIcon(SwingUtil.loadImage("yellow", 20, 20));
            try {
                Long amount = Long.parseLong(formattedTextFieldAmount.getText());
                
                //should work without selected Account since this is held in rpcService
                SendTransactionResponse result = rpcService.sendTransactionRaw(
                        TransactionType.TRANSFER, textFieldTo.getText(), amount, textFieldData.getText(), new String( privateKeyField.getPassword()));
            } catch (IOException | ApiException | InvalidKeySpecException | CryptoException e1) {
                lblResult.setIcon(SwingUtil.loadImage("red", 20, 20));
                JOptionPane.showMessageDialog(this,
                        GUIMessages.get("SomeThingWrong") + " " + e1.getMessage());
                e1.printStackTrace();
            }

            lblResult.setIcon(SwingUtil.loadImage("green", 20, 20));

            break;

        default:
            lblResult.setIcon(SwingUtil.loadImage("red", 20, 20));
            throw new IllegalStateException("No such action");
        }

    }
}
