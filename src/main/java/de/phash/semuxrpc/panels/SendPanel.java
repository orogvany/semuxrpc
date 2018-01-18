package de.phash.semuxrpc.panels;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.IOException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;

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
    public class Item {
        EdDSA edDSA;

        public Item(EdDSA edDSA) {
            this.edDSA = edDSA;
        }

        public EdDSA getEdDSA() {
            return edDSA;
        }

        @Override
        public String toString() {
            return Hex.PREF + edDSA.toAddressString();
        }

    }

    private RPCService rpcService;
    JComboBox<TransactionType> comboBoxTransactionType;
    private static final Logger logger = LoggerFactory.getLogger(SendPanel.class);
    private JLabel lblResult;

    public SendPanel(RPCService rpcService) {
        this.rpcService = rpcService;

        JLabel lblFrom = new JLabel(GUIMessages.get("From"));

        JLabel lblTo = new JLabel(GUIMessages.get("To"));

        JLabel lblAmount = new JLabel(GUIMessages.get("Amount"));

        JLabel lblFee = new JLabel(GUIMessages.get("Fee"));

        JLabel lblData = new JLabel(GUIMessages.get("Data"));

        textFieldTo = new JTextField();
        textFieldTo.setColumns(10);

        textFieldAmount = new JTextField();
        textFieldAmount.setColumns(10);

        textFieldFee = new JTextField(Long.toString(rpcService.getConfig().minTransactionFee()));
        textFieldFee.setColumns(10);

        textFieldData = new JTextField();
        textFieldData.setColumns(10);

        comboBoxTransactionType = new JComboBox<>();
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

        JButton btnConfirm = new JButton(GUIMessages.get("Confirm"));
        btnConfirm.addActionListener(this);
        btnConfirm.setActionCommand(Action.OK.name());

        JComboBox<Item> accountss = new JComboBox<>();
        for (Item ele : getAccounts()) {

            accountss.addItem(ele);
        }
        accountss.setEditable(false);
        accountss.setSelectedItem(rpcService.getSelectedWalletAccount());

        accountss.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent event) {
                if (event.getStateChange() == ItemEvent.SELECTED) {
                    Object item = event.getItem();
                    Item lineItem = (Item) item;

                    rpcService.setSelectedWalletAccount(lineItem.getEdDSA());
                    logger.info(
                            "selected account changed to: " + rpcService.getSelectedWalletAccount().toAddressString());
                }
            }
        });

        lblResult = new JLabel("");

        JLabel lblTransactionType = new JLabel(GUIMessages.get("Type"));

        GroupLayout groupLayout = new GroupLayout(this);
        groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(Alignment.LEADING).addGroup(groupLayout
                .createSequentialGroup().addGap(53)
                .addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
                        .addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
                                .addComponent(lblTo, GroupLayout.PREFERRED_SIZE, 99, GroupLayout.PREFERRED_SIZE)
                                .addComponent(lblFrom, GroupLayout.PREFERRED_SIZE, 98, GroupLayout.PREFERRED_SIZE))
                        .addGroup(groupLayout.createSequentialGroup()
                                .addGroup(groupLayout.createParallelGroup(Alignment.LEADING).addComponent(lblAmount)
                                        .addGroup(groupLayout.createParallelGroup(Alignment.TRAILING, false)
                                                .addComponent(lblData, Alignment.LEADING, GroupLayout.DEFAULT_SIZE,
                                                        GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(lblFee, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 65,
                                                        Short.MAX_VALUE)))
                                .addGap(39))
                        .addComponent(lblTransactionType))
                .addGap(23)
                .addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
                        .addGroup(groupLayout.createSequentialGroup().addGap(119).addComponent(lblResult))
                        .addComponent(comboBoxTransactionType, 0, 248, Short.MAX_VALUE).addComponent(btnConfirm)
                        .addComponent(textFieldAmount, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 248,
                                Short.MAX_VALUE)
                        .addComponent(textFieldFee, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 248, Short.MAX_VALUE)
                        .addComponent(textFieldData, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 248, Short.MAX_VALUE)
                        .addComponent(textFieldTo, GroupLayout.DEFAULT_SIZE, 378, Short.MAX_VALUE)
                        .addComponent(accountss, 0, 378, Short.MAX_VALUE))
                .addGap(42)));
        groupLayout.setVerticalGroup(groupLayout.createParallelGroup(Alignment.LEADING).addGroup(groupLayout
                .createSequentialGroup().addGap(41)
                .addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
                        .addComponent(accountss, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
                                GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblFrom))
                .addGap(18)
                .addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
                        .addComponent(textFieldTo, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
                                GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblTo))
                .addGap(18)
                .addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
                        .addComponent(textFieldAmount, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
                                GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblAmount))
                .addGap(18)
                .addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
                        .addComponent(textFieldFee, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
                                GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblFee))
                .addGap(18)
                .addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
                        .addComponent(textFieldData, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
                                GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblData))
                .addPreferredGap(ComponentPlacement.UNRELATED)
                .addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
                        .addComponent(comboBoxTransactionType, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
                                GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblTransactionType))
                .addGap(18).addComponent(btnConfirm).addPreferredGap(ComponentPlacement.RELATED, 136, Short.MAX_VALUE)
                .addComponent(lblResult).addContainerGap()));
        setLayout(groupLayout);
    }

    private List<Item> getAccounts() {
        List<Item> items = new ArrayList<>();
        List<EdDSA> accounts = rpcService.getAccounts();
        for (EdDSA edDSA : accounts) {
            items.add(new Item(edDSA));
        }
        return items;
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
        Action action = Action.valueOf(event.getActionCommand());
        switch (action) {
        case OK:
            try {
                Long amount = Long.parseLong(textFieldAmount.getText());
                int ret = JOptionPane.showConfirmDialog(this,
                        GUIMessages.get("TransferInfo", SwingUtil.formatValue(amount), textFieldTo.getText()),
                        GUIMessages.get("ConfirmTransfer"), JOptionPane.YES_NO_OPTION);
                if (ret != JOptionPane.YES_OPTION) {
                    break;
                }
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
