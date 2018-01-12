package de.phash.semuxrpc.panels;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;

import de.phash.semuxrpc.AccountInfo;
import de.phash.semuxrpc.Action;
import de.phash.semuxrpc.RpcGUI;
import de.phash.semuxrpc.gui.SwingUtil;

public class AccountInfoPanel extends JPanel implements ActionListener {

    private static final long serialVersionUID = 1L;
    private JLabel lblResult;
    private JTextField textFieldAddress;
    private JTextField textFieldBalance;
    private JTextField textFieldLocked;
    private RpcGUI rpcGUI;
    private JTextField textFieldNonce;

    public AccountInfoPanel(RpcGUI rpcGUI) {
        super();
        this.rpcGUI = rpcGUI;

        textFieldAddress = new JTextField();
        textFieldAddress.setText("0x09c5f2794d69717d538bfcc150644f7685945cfa");
        textFieldAddress.setColumns(10);

        textFieldBalance = new JTextField();
        textFieldBalance.setColumns(10);

        JLabel lblAddress = new JLabel("Address");

        JLabel lblBalance = new JLabel("Balance");

        textFieldLocked = new JTextField();
        textFieldLocked.setColumns(10);

        JLabel label = new JLabel("Balance locked");

        JButton btnQuery = new JButton("query");
        btnQuery.addActionListener(this);
        btnQuery.setActionCommand(Action.ACCOUNTINFO.name());

        JLabel lblNonce = new JLabel("Nonce");

        textFieldNonce = new JTextField();
        textFieldNonce.setColumns(10);

        lblResult = new JLabel("");

        GroupLayout groupLayout = new GroupLayout(this);
        groupLayout.setHorizontalGroup(
                groupLayout.createParallelGroup(Alignment.TRAILING)
                        .addGroup(groupLayout.createSequentialGroup()
                                .addContainerGap(69, Short.MAX_VALUE)
                                .addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
                                        .addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false)
                                                .addComponent(label, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE,
                                                        Short.MAX_VALUE)
                                                .addComponent(lblBalance, GroupLayout.DEFAULT_SIZE,
                                                        GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(lblAddress, GroupLayout.DEFAULT_SIZE, 94,
                                                        Short.MAX_VALUE))
                                        .addComponent(lblNonce, GroupLayout.PREFERRED_SIZE, 63,
                                                GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(ComponentPlacement.RELATED)
                                .addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false)
                                        .addGroup(groupLayout.createSequentialGroup()
                                                .addComponent(btnQuery, GroupLayout.PREFERRED_SIZE, 72,
                                                        GroupLayout.PREFERRED_SIZE)
                                                .addGap(18)
                                                .addComponent(lblResult))
                                        .addComponent(textFieldBalance, Alignment.TRAILING)
                                        .addComponent(textFieldLocked, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE,
                                                268, Short.MAX_VALUE)
                                        .addComponent(textFieldAddress, GroupLayout.DEFAULT_SIZE, 326, Short.MAX_VALUE)
                                        .addComponent(textFieldNonce))
                                .addGap(16)));
        groupLayout.setVerticalGroup(
                groupLayout.createParallelGroup(Alignment.LEADING)
                        .addGroup(groupLayout.createSequentialGroup()
                                .addGap(62)
                                .addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
                                        .addComponent(textFieldAddress, GroupLayout.PREFERRED_SIZE,
                                                GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(lblAddress))
                                .addGap(18)
                                .addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
                                        .addComponent(btnQuery)
                                        .addComponent(lblResult))
                                .addGap(18)
                                .addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
                                        .addComponent(textFieldBalance, GroupLayout.PREFERRED_SIZE,
                                                GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(lblBalance))
                                .addGap(18)
                                .addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
                                        .addComponent(textFieldLocked, GroupLayout.PREFERRED_SIZE,
                                                GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(label))
                                .addGap(18)
                                .addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
                                        .addComponent(lblNonce)
                                        .addComponent(textFieldNonce, GroupLayout.PREFERRED_SIZE,
                                                GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(69, Short.MAX_VALUE)));
        setLayout(groupLayout);

        // @formatter:off
       
    }
    
    
    @Override
    public void actionPerformed(ActionEvent e) {
        Action action = Action.valueOf(e.getActionCommand());

        switch (action) {
        case ACCOUNTINFO:
            lblResult.setIcon( SwingUtil.loadImage("yellow", 20, 20));
            AccountInfo info;
            try {
                info = rpcGUI.getAccountInfo(textFieldAddress.getText());
                updateAccountInfo(info);
                lblResult.setIcon( SwingUtil.loadImage("green", 20, 20));
            } catch (IOException e1) {
                lblResult.setIcon( SwingUtil.loadImage("red", 20, 20));
                e1.printStackTrace();
            }
            
            break;
     
        default:
            throw new IllegalStateException("No such action");
        }
        
    }

    private void updateAccountInfo(AccountInfo info) {
        textFieldBalance.setText(info.getBalance().toString());
        textFieldLocked.setText(info.getLocked().toString());
        textFieldNonce.setText(info.getNonce().toString());
    }
}
