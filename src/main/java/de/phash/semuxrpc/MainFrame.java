package de.phash.semuxrpc;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.WindowConstants;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import de.phash.semuxrpc.gui.SwingUtil;
import de.phash.semuxrpc.panels.AccountInfoPanel;
import de.phash.semuxrpc.panels.SendPanel;
import de.phash.semuxrpc.panels.ServerPanel;
import de.phash.semuxrpc.panels.SignPanel;
import de.phash.semuxrpc.panels.TransferPanel;
import de.phash.semuxrpc.panels.WalletPanel;

public class MainFrame extends JFrame implements ActionListener {
    private JPanel activePanel = new JPanel();
    private JButton activeButton = new JButton();
    private ServerPanel serverPanel;
    private AccountInfoPanel accountInfoPanel;
    private TransferPanel transferPanel;
    private SignPanel signPanel;
    private WalletPanel walletPanel;
    private SendPanel sendPanel;
    private JButton btnInfo;
    private JButton btnTransfer;
    private JButton btnSend;
    private JButton btnSign;
    private JButton btnAccount;
    private RPCService rpcService;

    public MainFrame(RpcGUI rpcGUI, RPCService rpcService) {
        this.rpcService = rpcService;
        this.setTitle("SemuxRPC");
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setMinimumSize(new Dimension(900, 600));
        activePanel.setBorder(new EmptyBorder(0, 15, 15, 15));
        activePanel.setLayout(new BorderLayout(0, 0));
        activePanel.add(new AccountInfoPanel(rpcService));

        serverPanel = new ServerPanel(rpcService);
        accountInfoPanel = new AccountInfoPanel(rpcService);
        transferPanel = new TransferPanel(rpcService);
        signPanel = new SignPanel(rpcService);
        walletPanel = new WalletPanel(rpcService);
        sendPanel = new SendPanel(rpcService);

        JPanel menuPanel = new JPanel();
        GroupLayout groupLayout = new GroupLayout(getContentPane());
        groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(Alignment.LEADING).addGroup(groupLayout
                .createSequentialGroup().addContainerGap()
                .addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
                        .addGroup(groupLayout.createSequentialGroup()
                                .addComponent(serverPanel, GroupLayout.PREFERRED_SIZE, 257, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(activePanel, GroupLayout.PREFERRED_SIZE, 612, GroupLayout.PREFERRED_SIZE))
                        .addComponent(menuPanel, GroupLayout.PREFERRED_SIZE, 858, GroupLayout.PREFERRED_SIZE))
                .addContainerGap()));
        groupLayout.setVerticalGroup(groupLayout.createParallelGroup(Alignment.LEADING)
                .addGroup(groupLayout.createSequentialGroup()
                        .addComponent(menuPanel, GroupLayout.PREFERRED_SIZE, 108, GroupLayout.PREFERRED_SIZE).addGap(5)
                        .addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
                                .addComponent(serverPanel, GroupLayout.PREFERRED_SIZE, 432, GroupLayout.PREFERRED_SIZE)
                                .addComponent(activePanel, GroupLayout.PREFERRED_SIZE, 448, GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));

        btnInfo = SwingUtil.createDefaultButton("Account Info", this, Action.SHOW_ACCOUNTINFO);
        activeButton = btnInfo;
        btnTransfer = SwingUtil.createDefaultButton("Transfer", this, Action.SHOW_TRANSFER);

        btnSend = SwingUtil.createDefaultButton("send", this, Action.SHOW_SEND);
        // btnSign = SwingUtil.createDefaultButton("sign", this, Action.SHOW_SIGN);
        btnAccount = SwingUtil.createDefaultButton("Wallet", this, Action.SHOW_ACC);

        GroupLayout glMenuPanel = new GroupLayout(menuPanel);
        glMenuPanel.setHorizontalGroup(glMenuPanel.createParallelGroup(Alignment.LEADING)
                .addGroup(glMenuPanel.createSequentialGroup().addGap(46).addComponent(btnInfo).addGap(18)
                        .addComponent(btnTransfer).addGap(18).addComponent(btnAccount).addGap(18).addComponent(btnSend)

                        .addContainerGap(563, Short.MAX_VALUE)));
        glMenuPanel.setVerticalGroup(glMenuPanel.createParallelGroup(Alignment.LEADING)
                .addGroup(glMenuPanel.createSequentialGroup().addGap(42)
                        .addGroup(glMenuPanel.createParallelGroup(Alignment.BASELINE).addComponent(btnInfo)
                                .addComponent(btnTransfer).addComponent(btnAccount).addComponent(btnSend))
                        .addContainerGap(43, Short.MAX_VALUE)));
        menuPanel.setLayout(glMenuPanel);
        getContentPane().setLayout(groupLayout);

        activePanel.revalidate();
        activePanel.repaint();
    }

    private static final long serialVersionUID = 1L;

    private static final Border BORDER_NORMAL = new CompoundBorder(new LineBorder(new Color(180, 180, 180)),
            new EmptyBorder(0, 5, 0, 10));
    private static final Border BORDER_FOCUS = new CompoundBorder(new LineBorder(new Color(51, 153, 255)),
            new EmptyBorder(0, 5, 0, 10));

    protected void select(JPanel panel, JButton button) {
        if (activeButton != null) {
            activeButton.setBorder(BORDER_NORMAL);
        }
        activeButton = button;
        activeButton.setBorder(BORDER_FOCUS);

        activePanel.removeAll();
        activePanel.add(panel);

        activePanel.revalidate();
        activePanel.repaint();
    }

    private Server getServer() {
        return serverPanel.getServer();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Action action = Action.valueOf(e.getActionCommand());

        switch (action) {
        case SHOW_ACCOUNTINFO:
            select(accountInfoPanel, btnInfo);
            break;
        case SHOW_TRANSFER:
            select(transferPanel, btnTransfer);
            break;
        case SHOW_SIGN:
            select(signPanel, btnSign);
            break;
        case SHOW_ACC:
            select(walletPanel, btnAccount);
            break;
        case SHOW_SEND:
            select(sendPanel, btnSend);
            break;
        default:
            throw new IllegalStateException("No such action");
        }

    }

}
