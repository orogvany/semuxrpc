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
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;

import org.semux.core.Transaction;
import org.semux.core.TransactionType;
import org.semux.crypto.CryptoException;
import org.semux.crypto.EdDSA;
import org.semux.crypto.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.phash.semux.swagger.client.ApiException;
import de.phash.semux.swagger.client.model.SendTransactionResponse;
import de.phash.semuxrpc.Action;
import de.phash.semuxrpc.RPCService;
import de.phash.semuxrpc.gui.GUIMessages;
import de.phash.semuxrpc.gui.SwingUtil;

public class SendPanel extends JPanel implements ActionListener {
    private RPCService rpcService;
    JComboBox<TransactionType> comboBoxTransactionType;
    private static final Logger logger = LoggerFactory.getLogger(SendPanel.class);
    private JLabel lblResult;

    public SendPanel(RPCService rpcService) {
        this.rpcService = rpcService;

        JLabel lblFrom = new JLabel("From");

        JLabel lblTo = new JLabel("To");

        JLabel lblAmount = new JLabel("Amount");

        JLabel lblFee = new JLabel("Fee");

        JLabel lblData = new JLabel("Data");

        textFieldTo = new JTextField();
        textFieldTo.setColumns(10);

        textFieldAmount = new JTextField();
        textFieldAmount.setColumns(10);

        textFieldFee = new JTextField(Long.toString(rpcService.getConfig().minTransactionFee()));
        textFieldFee.setColumns(10);

        textFieldData = new JTextField();
        textFieldData.setColumns(10);

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
                    logger.info("transaction type changed to: " + transactionType.name());
                    // rpcService.setSelectedWalletAccount((EdDSA) item);
                }
            }
        });

        JButton btnNewButton = SwingUtil.createDefaultButton(GUIMessages.get("Confirm"), this, getSelectedAction());
        // new JButton("Confirm");//

        JComboBox<EdDSA> accountss = new JComboBox<>();
        for (EdDSA ele : rpcService.getAccounts()) {

            accountss.addItem(ele);
        }
        accountss.setEditable(false);
        accountss.setSelectedItem(rpcService.getSelectedWalletAccount());

        accountss.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent event) {
                if (event.getStateChange() == ItemEvent.SELECTED) {
                    Object item = event.getItem();
                    rpcService.setSelectedWalletAccount((EdDSA) item);
                    logger.info(
                            "selected account changed to: " + rpcService.getSelectedWalletAccount().toAddressString());
                }
            }
        });

        lblResult = new JLabel("");

        GroupLayout groupLayout = new GroupLayout(this);
        groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(Alignment.LEADING).addGroup(groupLayout
                .createSequentialGroup().addGap(53)
                .addGroup(groupLayout.createParallelGroup(Alignment.LEADING).addGroup(groupLayout
                        .createSequentialGroup()
                        .addGroup(groupLayout
                                .createParallelGroup(Alignment.LEADING).addComponent(lblFrom).addComponent(lblTo))
                        .addGap(18)
                        .addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
                                .addComponent(accountss, 0, 306, Short.MAX_VALUE)
                                .addComponent(textFieldTo, GroupLayout.DEFAULT_SIZE, 306, Short.MAX_VALUE))
                        .addGap(1))
                        .addGroup(groupLayout.createSequentialGroup()
                                .addGroup(groupLayout.createParallelGroup(Alignment.LEADING).addComponent(lblAmount)
                                        .addComponent(lblFee).addComponent(lblData))
                                .addPreferredGap(ComponentPlacement.RELATED)
                                .addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
                                        .addComponent(textFieldFee, GroupLayout.DEFAULT_SIZE, 306, Short.MAX_VALUE)
                                        .addComponent(textFieldAmount, GroupLayout.DEFAULT_SIZE, 306, Short.MAX_VALUE)
                                        .addGroup(groupLayout.createSequentialGroup().addComponent(btnNewButton)
                                                .addGap(44).addComponent(lblResult))
                                        .addComponent(comboBoxTransactionType, 0, 306, Short.MAX_VALUE)
                                        .addComponent(textFieldData, GroupLayout.DEFAULT_SIZE, 306, Short.MAX_VALUE))))
                .addGap(42)));
        groupLayout.setVerticalGroup(groupLayout.createParallelGroup(Alignment.LEADING).addGroup(groupLayout
                .createSequentialGroup().addGap(41)
                .addGroup(groupLayout.createParallelGroup(Alignment.BASELINE).addComponent(lblFrom).addComponent(
                        accountss, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
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
                .addComponent(comboBoxTransactionType, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
                        GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(ComponentPlacement.RELATED, 154, Short.MAX_VALUE).addGroup(groupLayout
                        .createParallelGroup(Alignment.BASELINE).addComponent(btnNewButton).addComponent(lblResult))
                .addContainerGap()));
        setLayout(groupLayout);
    }

    private Action getSelectedAction() {
        TransactionType type = (TransactionType) comboBoxTransactionType.getSelectedItem();
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
    private JTextField textFieldTo;
    private JTextField textFieldAmount;
    private JTextField textFieldFee;
    private JTextField textFieldData;
    private TransactionType transactionType;

    @Override
    public void actionPerformed(ActionEvent event) {
        lblResult.setIcon(SwingUtil.loadImage("yellow", 20, 20));
        // Action action = Action.valueOf(event.getActionCommand());
        Action action = getSelectedAction();
        switch (action) {
        case TRANSFER:
        case VOTE:
        case UNVOTE:
            try {
                prepareTransaction();
            } catch (IOException | InvalidKeySpecException | CryptoException | ApiException e1) {
                e1.printStackTrace();
                JOptionPane.showMessageDialog(this, GUIMessages.get("SomeThingWrong") + " " + e1.getMessage());
                lblResult.setIcon(SwingUtil.loadImage("red", 20, 20));
            }
            break;
        default:
            lblResult.setIcon(SwingUtil.loadImage("red", 20, 20));
            throw new IllegalStateException("No such action");
        }
        lblResult.setIcon(SwingUtil.loadImage("green", 20, 20));

    }

    private void prepareTransaction() throws IOException, ApiException, InvalidKeySpecException {

        Long amount = Long.parseLong(textFieldAmount.getText());
        SendTransactionResponse result = rpcService.sendTransaction(transactionType, textFieldTo.getText(), amount,
                textFieldData.getText());

    }
}
