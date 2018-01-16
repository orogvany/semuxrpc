package de.phash.semuxrpc.panels;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.security.spec.InvalidKeySpecException;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;

import org.semux.core.Transaction;
import org.semux.core.TransactionType;
import org.semux.crypto.CryptoException;
import org.semux.crypto.Hex;

import de.phash.semux.swagger.client.ApiException;
import de.phash.semux.swagger.client.model.SendTransactionResponse;
import de.phash.semuxrpc.Action;
import de.phash.semuxrpc.RPCService;
import de.phash.semuxrpc.gui.GUIMessages;
import de.phash.semuxrpc.gui.SwingUtil;

public class SendPanel extends JPanel implements ActionListener {
    private RPCService rpcService;
    JComboBox<TransactionType> comboBox;

    public SendPanel(RPCService rpcService) {
        this.rpcService = rpcService;

        JLabel lblFrom = new JLabel("From");

        JLabel lblTo = new JLabel("To");

        JLabel lblAmount = new JLabel("Amount");

        JLabel lblFee = new JLabel("Fee");

        JLabel lblData = new JLabel("Data");

        textFieldFrom = new JTextField(rpcService.getSelectedWalletAccount().toAddressString());
        textFieldFrom.setColumns(10);

        textFieldTo = new JTextField();
        textFieldTo.setColumns(10);

        textFieldAmount = new JTextField();
        textFieldAmount.setColumns(10);

        textFieldFee = new JTextField(Long.toString(rpcService.getConfig().minDelegateFee()));
        textFieldFee.setColumns(10);

        textFieldData = new JTextField();
        textFieldData.setColumns(10);

        JButton btnNewButton = SwingUtil.createDefaultButton(GUIMessages.get("Confirm"), this, getSelectedAction());
        // new JButton("Confirm");//

        comboBox = new JComboBox<>();
        // comboBox.setActionCommand(Action.TRANSACTION_TYPE.name());
        // comboBox.addActionListener(this);
        comboBox.addItem(TransactionType.TRANSFER);
        comboBox.addItem(TransactionType.VOTE);
        comboBox.addItem(TransactionType.UNVOTE);
        comboBox.setSelectedItem(TransactionType.TRANSFER);

        GroupLayout groupLayout = new GroupLayout(this);
        groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(Alignment.LEADING).addGroup(groupLayout
                .createSequentialGroup().addGap(53)
                .addGroup(groupLayout.createParallelGroup(Alignment.LEADING).addGroup(groupLayout
                        .createSequentialGroup()
                        .addGroup(groupLayout
                                .createParallelGroup(Alignment.LEADING).addComponent(lblFrom).addComponent(lblTo))
                        .addGap(18)
                        .addGroup(groupLayout.createParallelGroup(Alignment.LEADING).addComponent(textFieldTo)
                                .addComponent(textFieldFrom))
                        .addGap(1))
                        .addGroup(groupLayout.createSequentialGroup()
                                .addGroup(groupLayout.createParallelGroup(Alignment.LEADING).addComponent(lblAmount)
                                        .addComponent(lblFee).addComponent(lblData))
                                .addPreferredGap(ComponentPlacement.RELATED)
                                .addGroup(groupLayout.createParallelGroup(Alignment.LEADING).addComponent(textFieldData)
                                        .addComponent(textFieldFee).addComponent(textFieldAmount)
                                        .addComponent(btnNewButton).addComponent(comboBox, 0, 116, Short.MAX_VALUE))))
                .addGap(232)));
        groupLayout.setVerticalGroup(groupLayout.createParallelGroup(Alignment.LEADING).addGroup(groupLayout
                .createSequentialGroup().addGap(35)
                .addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
                        .addComponent(textFieldFrom, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
                                GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblFrom))
                .addGap(18)
                .addGroup(groupLayout.createParallelGroup(Alignment.BASELINE).addComponent(lblTo).addComponent(
                        textFieldTo, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addGap(18)
                .addGroup(groupLayout
                        .createParallelGroup(Alignment.BASELINE).addComponent(lblAmount).addComponent(textFieldAmount,
                                GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addGap(18)
                .addGroup(groupLayout.createParallelGroup(Alignment.BASELINE).addComponent(lblFee).addComponent(
                        textFieldFee, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addGap(18)
                .addGroup(groupLayout
                        .createParallelGroup(Alignment.BASELINE).addComponent(lblData).addComponent(textFieldData,
                                GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(ComponentPlacement.UNRELATED)
                .addComponent(comboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
                        GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(ComponentPlacement.RELATED, 10, Short.MAX_VALUE).addComponent(btnNewButton)
                .addContainerGap()));
        setLayout(groupLayout);
    }

    private Action getSelectedAction() {
        TransactionType type = (TransactionType) comboBox.getSelectedItem();
        switch (type) {
        case TRANSFER:
            return Action.TRANSFER;
        case VOTE:
            return Action.VOTE;
        case UNVOTE:
            return Action.UNVOTE;

        default:
            throw new IllegalStateException();
        }
    }

    private static final long serialVersionUID = 1L;
    private JTextField textFieldFrom;
    private JTextField textFieldTo;
    private JTextField textFieldAmount;
    private JTextField textFieldFee;
    private JTextField textFieldData;

    @Override
    public void actionPerformed(ActionEvent event) {

        Action action = Action.valueOf(event.getActionCommand());

        switch (action) {
        case TRANSFER:
            // lblResult.setIcon(SwingUtil.loadImage("yellow", 20, 20));
            try {
                prepareTransaction(TransactionType.TRANSFER);
            } catch (IOException | InvalidKeySpecException | CryptoException | ApiException e1) {
                // lblResult.setIcon(SwingUtil.loadImage("red", 20, 20));
                e1.printStackTrace();
            }
            break;

        case VOTE:
            // lblResult.setIcon(SwingUtil.loadImage("yellow", 20, 20));
            try {
                prepareTransaction(TransactionType.VOTE);
            } catch (IOException | InvalidKeySpecException | CryptoException | ApiException e1) {
                // lblResult.setIcon(SwingUtil.loadImage("red", 20, 20));
                e1.printStackTrace();
            }
            break;
        case UNVOTE:
            // lblResult.setIcon(SwingUtil.loadImage("yellow", 20, 20));
            try {
                prepareTransaction(TransactionType.UNVOTE);
            } catch (IOException | InvalidKeySpecException | CryptoException | ApiException e1) {
                // lblResult.setIcon(SwingUtil.loadImage("red", 20, 20));
                e1.printStackTrace();
            }
            break;
        default:
            throw new IllegalStateException("No such action");
        }

    }

    private void prepareTransaction(TransactionType type) throws IOException, ApiException, InvalidKeySpecException {
        Long amount = Long.parseLong(textFieldAmount.getText());
        Long fee = rpcService.getConfig().minTransactionFee();
        byte[] to = Hex.decode0x(textFieldTo.getText());
        byte[] data = Hex.decode0x("");
        Long nonce = rpcService.getAccountInfo(textFieldFrom.getText()).getResult().getNonce();
        Transaction transaction = new Transaction(type, to, amount, fee, nonce, System.currentTimeMillis(), data);
        SendTransactionResponse result = rpcService.sendTransaction(transaction);

        // lblResult.setIcon(SwingUtil.loadImage("green", 20, 20));
    }
}
