package de.phash.semuxrpc;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.math.BigInteger;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import de.phash.semuxrpc.dto.Transaction;
import de.phash.semuxrpc.gui.SwingUtil;

public class TransferPanel extends JPanel implements ActionListener {
    private RpcGUI rpcGUI;

    private static final long serialVersionUID = 1L;
    private JTextField textFieldFrom;
    private JTextField textFieldTo;
    private JTextField formattedTextFieldAmount;
    private JTextField formattedTextFieldFee;
    private JCheckBox chckbxAutoFee;

    private JLabel lblResult;

    public TransferPanel(RpcGUI rpcGUI) {
        this.rpcGUI = rpcGUI;
        JLabel lblFrom = new JLabel("from");

        JLabel lblTo = new JLabel("to");

        JLabel lblAmount = new JLabel("amount");

        JLabel lblFee = new JLabel("fee");

        JLabel lblAutofee = new JLabel("auto-fee");

        textFieldFrom = new JTextField();
        textFieldFrom.setColumns(10);

        textFieldTo = new JTextField();
        textFieldTo.setColumns(10);

        formattedTextFieldAmount = new JTextField();

        formattedTextFieldFee = new JTextField("50000000");

        chckbxAutoFee = new JCheckBox("");

        JButton btnSend = SwingUtil.createDefaultButton("send", this, Action.TRANSFER);

        lblResult = new JLabel("");
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
                                        .addComponent(lblAutofee))
                                .addGap(45)
                                .addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
                                        .addGroup(groupLayout.createSequentialGroup()
                                                .addComponent(btnSend)
                                                .addGap(18)
                                                .addComponent(lblResult)
                                                .addGap(215))
                                        .addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
                                                .addGroup(groupLayout.createSequentialGroup()
                                                        .addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
                                                                .addComponent(formattedTextFieldFee,
                                                                        GroupLayout.DEFAULT_SIZE, 315, Short.MAX_VALUE)
                                                                .addComponent(formattedTextFieldAmount, 315, 315, 315)
                                                                .addGroup(groupLayout
                                                                        .createParallelGroup(Alignment.LEADING, false)
                                                                        .addComponent(textFieldTo)
                                                                        .addComponent(textFieldFrom,
                                                                                GroupLayout.DEFAULT_SIZE, 315,
                                                                                Short.MAX_VALUE)))
                                                        .addGap(19))
                                                .addGroup(groupLayout.createSequentialGroup()
                                                        .addComponent(chckbxAutoFee)
                                                        .addContainerGap())))));
        groupLayout.setVerticalGroup(
                groupLayout.createParallelGroup(Alignment.LEADING)
                        .addGroup(groupLayout.createSequentialGroup()
                                .addGap(28)
                                .addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
                                        .addComponent(lblFrom)
                                        .addComponent(textFieldFrom, GroupLayout.PREFERRED_SIZE,
                                                GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addGap(18)
                                .addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
                                        .addComponent(lblTo)
                                        .addComponent(textFieldTo, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
                                                GroupLayout.PREFERRED_SIZE))
                                .addGap(18)
                                .addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
                                        .addComponent(lblAmount)
                                        .addComponent(formattedTextFieldAmount, GroupLayout.PREFERRED_SIZE,
                                                GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addGap(18)
                                .addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
                                        .addComponent(lblFee)
                                        .addComponent(formattedTextFieldFee, GroupLayout.PREFERRED_SIZE,
                                                GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addGap(18)
                                .addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
                                        .addComponent(lblAutofee)
                                        .addComponent(chckbxAutoFee))
                                .addGap(18)
                                .addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
                                        .addComponent(btnSend)
                                        .addComponent(lblResult))
                                .addContainerGap(58, Short.MAX_VALUE)));
        setLayout(groupLayout);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        Action action = Action.valueOf(e.getActionCommand());

        switch (action) {
        case TRANSFER:
            lblResult.setIcon(SwingUtil.loadImage("yellow", 20, 20));
            BigInteger amount = new BigInteger( formattedTextFieldAmount.getText());
            BigInteger fee = new BigInteger( formattedTextFieldFee.getText());
            
            Transaction transaction = new Transaction(textFieldFrom.getText(), textFieldTo.getText(),
                    amount,
                    fee, "", chckbxAutoFee.isSelected());
            try {
                String hash = rpcGUI.transferValue(transaction);
                lblResult.setIcon(SwingUtil.loadImage("green", 20, 20));
            } catch (IOException e1) {
                lblResult.setIcon(SwingUtil.loadImage("red", 20, 20));
                e1.printStackTrace();
            }

            break;

        default:
            throw new IllegalStateException("No such action");
        }

    }
}
