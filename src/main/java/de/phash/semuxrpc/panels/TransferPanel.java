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
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;

import org.semux.core.TransactionType;
import org.semux.crypto.CryptoException;

import de.phash.semux.swagger.client.ApiException;
import de.phash.semux.swagger.client.model.SendTransactionResponse;
import de.phash.semuxrpc.Action;
import de.phash.semuxrpc.RPCService;
import de.phash.semuxrpc.gui.GUIMessages;
import de.phash.semuxrpc.gui.SwingUtil;

public class TransferPanel extends JPanel implements ActionListener {
    private RPCService rpcService;

    private static final long serialVersionUID = 1L;
    private JTextField textFieldTo;
    private JTextField formattedTextFieldAmount;
    private JTextField formattedTextFieldFee;
    private JCheckBox chckbxAutoFee;
    private JComboBox<TransactionType> comboBoxTransactionType;
    private TransactionType transactionType;

    private JLabel lblResult;
    private JTextField textFieldData;
    private JPasswordField privateKeyField;

    public TransferPanel(RPCService rpcService) {
        this.rpcService = rpcService;
        JLabel lblFrom = new JLabel(GUIMessages.get("FromPrivKey"));

        JLabel lblTo = new JLabel(GUIMessages.get("ToAddress"));

        JLabel lblAmount = new JLabel(GUIMessages.get("Value"));

        JLabel lblFee = new JLabel(GUIMessages.get("Fee"));

        JLabel lblAutofee = new JLabel(GUIMessages.get("AutoFee"));

        textFieldTo = new JTextField();
        textFieldTo.setColumns(25);

        formattedTextFieldAmount = new JTextField();

        formattedTextFieldFee = new JTextField("50000000");

        chckbxAutoFee = new JCheckBox("");

        JButton btnSend = new JButton(GUIMessages.get("Send"));
        btnSend.addActionListener(this);
        btnSend.setActionCommand(Action.TRANSFER.name());

        lblResult = new JLabel("");

        JLabel lblNewLabel = new JLabel("Data");

        textFieldData = new JTextField();
        textFieldData.setColumns(25);

        privateKeyField = new JPasswordField();

        JLabel lblTransferType = new JLabel(GUIMessages.get("Type"));

        comboBoxTransactionType = new JComboBox<>();
        // comboBox.setActionCommand(Action.TRANSACTION_TYPE.name());
        // comboBox.addActionListener(this);
        comboBoxTransactionType.addItem(TransactionType.TRANSFER);
        comboBoxTransactionType.addItem(TransactionType.VOTE);
        comboBoxTransactionType.addItem(TransactionType.UNVOTE);
        comboBoxTransactionType.setSelectedItem(TransactionType.TRANSFER);

        comboBoxTransactionType.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent event) {
                if (event.getStateChange() == ItemEvent.SELECTED) {
                    Object item = event.getItem();
                    transactionType = (TransactionType) item;
                }
            }
        });

        GroupLayout groupLayout = new GroupLayout(this);
        groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(Alignment.LEADING).addGroup(groupLayout
                .createSequentialGroup().addGap(29)
                .addGroup(groupLayout.createParallelGroup(Alignment.LEADING).addComponent(lblFrom).addComponent(lblTo)
                        .addComponent(lblAmount).addComponent(lblFee).addComponent(lblNewLabel)
                        .addComponent(lblTransferType).addComponent(lblAutofee))
                .addGap(29)
                .addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
                        .addGroup(groupLayout.createSequentialGroup().addComponent(chckbxAutoFee).addGap(18)
                                .addComponent(btnSend).addGap(384))
                        .addGroup(groupLayout.createSequentialGroup().addGroup(groupLayout
                                .createParallelGroup(Alignment.LEADING, false)
                                .addComponent(comboBoxTransactionType, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(textFieldData).addComponent(formattedTextFieldFee)
                                .addComponent(formattedTextFieldAmount).addComponent(textFieldTo)
                                .addComponent(privateKeyField, GroupLayout.DEFAULT_SIZE, 431, Short.MAX_VALUE))
                                .addPreferredGap(ComponentPlacement.RELATED, 55, Short.MAX_VALUE)
                                .addComponent(lblResult)))));
        groupLayout.setVerticalGroup(groupLayout.createParallelGroup(Alignment.LEADING).addGroup(groupLayout
                .createSequentialGroup().addGap(28)
                .addGroup(groupLayout
                        .createParallelGroup(Alignment.BASELINE).addComponent(lblFrom).addComponent(privateKeyField,
                                GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addGap(18)
                .addGroup(groupLayout.createParallelGroup(Alignment.BASELINE).addComponent(lblTo).addComponent(
                        textFieldTo, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addGap(18)
                .addGroup(groupLayout.createParallelGroup(Alignment.BASELINE).addComponent(lblAmount).addComponent(
                        formattedTextFieldAmount, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
                        GroupLayout.PREFERRED_SIZE))
                .addGap(18)
                .addGroup(groupLayout.createParallelGroup(Alignment.BASELINE).addComponent(lblFee).addComponent(
                        formattedTextFieldFee, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
                        GroupLayout.PREFERRED_SIZE))
                .addGap(18)
                .addGroup(groupLayout
                        .createParallelGroup(Alignment.BASELINE).addComponent(lblNewLabel).addComponent(textFieldData,
                                GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(ComponentPlacement.RELATED)
                .addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
                        .addGroup(groupLayout.createSequentialGroup().addGap(27).addComponent(lblResult))
                        .addGroup(groupLayout.createSequentialGroup().addGap(18)
                                .addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
                                        .addComponent(lblTransferType).addComponent(comboBoxTransactionType,
                                                GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
                                                GroupLayout.PREFERRED_SIZE))))
                .addGap(18).addGroup(groupLayout.createParallelGroup(Alignment.LEADING).addComponent(btnSend)
                        .addComponent(chckbxAutoFee).addComponent(lblAutofee))
                .addGap(19)));
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
                int ret = JOptionPane.showConfirmDialog(this,
                        GUIMessages.get("TransferInfo", SwingUtil.formatValue(amount), textFieldTo.getText()),
                        GUIMessages.get("ConfirmTransfer"), JOptionPane.YES_NO_OPTION);
                if (ret != JOptionPane.YES_OPTION) {
                    break;
                }
                SendTransactionResponse result = rpcService.sendTransactionRaw(transactionType, textFieldTo.getText(),
                        amount, textFieldData.getText(), new String(privateKeyField.getPassword()));
            } catch (IOException | ApiException | InvalidKeySpecException | CryptoException e1) {
                lblResult.setIcon(SwingUtil.loadImage("red", 20, 20));
                JOptionPane.showMessageDialog(this, GUIMessages.get("SomeThingWrong") + " " + e1.getMessage());
                e1.printStackTrace();
                break;
            }

            lblResult.setIcon(SwingUtil.loadImage("green", 20, 20));

            break;

        default:
            lblResult.setIcon(SwingUtil.loadImage("red", 20, 20));
            throw new IllegalStateException("No such action");
        }

    }
}
