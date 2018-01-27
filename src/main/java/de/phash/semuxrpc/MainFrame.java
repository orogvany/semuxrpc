package de.phash.semuxrpc;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import org.semux.core.Wallet;
import org.semux.crypto.Hex;
import org.semux.crypto.Key;
import org.semux.gui.SwingUtil;
import org.semux.util.SystemUtil;

import de.phash.semuxrpc.dialog.AboutDialog;
import de.phash.semuxrpc.dialog.ExportPrivateKeyDialog;
import de.phash.semuxrpc.dialog.HelpDialog;
import de.phash.semuxrpc.dialog.InputDialog;
import de.phash.semuxrpc.gui.GUIMessages;
import de.phash.semuxrpc.panels.AccountInfoPanel;
import de.phash.semuxrpc.panels.SendPanel;
import de.phash.semuxrpc.panels.ServerDialog;
import de.phash.semuxrpc.panels.TransferPanel;
import de.phash.semuxrpc.panels.WalletPanel;

public class MainFrame extends JFrame implements ActionListener {

    private JPanel activePanel = new JPanel();
    private JButton activeButton = new JButton();
    private AccountInfoPanel accountInfoPanel;
    private TransferPanel transferPanel;
    private WalletPanel walletPanel;
    private SendPanel sendPanel;
    private JButton btnInfo;
    private JButton btnTransfer;
    private JButton btnSend;
    private JButton btnWallet;
    private RPCService rpcService;
    private ServerDialog serverDialog;

    public MainFrame(RpcGUI rpcGUI, RPCService rpcService) {
        this.rpcService = rpcService;
        this.setTitle("SemuxRPC");
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setMinimumSize(new Dimension(900, 600));
        Dimension gap = new Dimension(15, 0);
        serverDialog = new ServerDialog(this, this.rpcService);
        activePanel.setBorder(new EmptyBorder(0, 15, 15, 15));
        activePanel.setLayout(new BorderLayout(0, 0));
        // menuBar = new MyMenuBar(rpcService, this);
        accountInfoPanel = new AccountInfoPanel(rpcService);
        transferPanel = new TransferPanel(rpcService);
        walletPanel = new WalletPanel(rpcService);
        sendPanel = new SendPanel(rpcService);
        activePanel.add(walletPanel, BorderLayout.CENTER);
        JPanel toolBar = new JPanel();
        FlowLayout layout = new FlowLayout();
        layout.setVgap(0);
        layout.setHgap(0);
        layout.setAlignment(FlowLayout.LEFT);
        toolBar.setLayout(layout);
        toolBar.setBorder(new EmptyBorder(15, 15, 15, 15));

        btnInfo = createButton(GUIMessages.get("AccountInfo"), "home", Action.SHOW_ACCOUNTINFO);

        btnTransfer = createButton(GUIMessages.get("Transfer"), "transactions", Action.SHOW_TRANSFER);

        btnSend = createButton(GUIMessages.get("Send"), "send", Action.SHOW_SEND);

        btnWallet = createButton(GUIMessages.get("Wallet"), "receive", Action.SHOW_ACC);
        toolBar.add(Box.createRigidArea(gap));
        toolBar.add(btnWallet);
        toolBar.add(Box.createRigidArea(gap));
        toolBar.add(btnInfo);
        toolBar.add(Box.createRigidArea(gap));
        toolBar.add(btnSend);
        toolBar.add(Box.createRigidArea(gap));
        toolBar.add(btnTransfer);
        toolBar.add(Box.createRigidArea(gap));
        activeButton = btnInfo;

        // GroupLayout glMenuPanel = new GroupLayout(menuPanel);
        // glMenuPanel.setHorizontalGroup(glMenuPanel.createParallelGroup(Alignment.LEADING)
        // .addGroup(glMenuPanel.createSequentialGroup().addGap(46).addComponent(btnWallet).addGap(18)
        // .addComponent(btnTransfer).addGap(18).addComponent(btnInfo).addGap(18).addComponent(btnSend)
        //
        // .addContainerGap(563, Short.MAX_VALUE)));
        // glMenuPanel.setVerticalGroup(glMenuPanel.createParallelGroup(Alignment.LEADING)
        // .addGroup(glMenuPanel.createSequentialGroup().addGap(42)
        // .addGroup(glMenuPanel.createParallelGroup(Alignment.BASELINE).addComponent(btnWallet)
        // .addComponent(btnTransfer).addComponent(btnInfo).addComponent(btnSend))
        // .addContainerGap(43, Short.MAX_VALUE)));
        // menuPanel.setLayout(glMenuPanel);
        getContentPane().add(toolBar, BorderLayout.NORTH);
        getContentPane().add(activePanel, BorderLayout.CENTER);

        menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        mnFile = new JMenu(GUIMessages.get("File"));
        menuBar.add(mnFile);

        mntmServer = new JMenuItem(GUIMessages.get("Server"));
        mntmServer.setActionCommand(Action.SHOW_SERVER.name());
        mntmServer.addActionListener(this);
        mnFile.add(mntmServer);

        mntmExit = new JMenuItem(GUIMessages.get("Exit"));
        mntmExit.setActionCommand(Action.EXIT.name());
        mntmExit.addActionListener(this);
        mnFile.add(mntmExit);

        mnWallet = new JMenu(GUIMessages.get("Wallet"));
        menuBar.add(mnWallet);

        mntmImportPrivateKey = new JMenuItem(GUIMessages.get("ImportPrivateKey"));
        mntmImportPrivateKey.setActionCommand(Action.IMPORTPRIVATEKEY.name());
        mntmImportPrivateKey.addActionListener(this);
        mnWallet.add(mntmImportPrivateKey);

        mntmExportPrivateKey = new JMenuItem(GUIMessages.get("ExportPrivateKey"));
        mntmExportPrivateKey.setActionCommand(Action.EXPORTPRIVATEKEY.name());
        mntmExportPrivateKey.addActionListener(this);
        mnWallet.add(mntmExportPrivateKey);

        mnHelp = new JMenu(GUIMessages.get("Help"));
        menuBar.add(mnHelp);

        mntmHelp = new JMenuItem(GUIMessages.get("Help"));
        mntmHelp.setActionCommand(Action.HELP.name());
        mntmHelp.addActionListener(this);
        mnHelp.add(mntmHelp);

        mntmAbout = new JMenuItem(GUIMessages.get("About"));
        mntmAbout.setActionCommand(Action.ABOUT.name());
        mntmAbout.addActionListener(this);
        mnHelp.add(mntmAbout);

        activePanel.revalidate();
        activePanel.repaint();
    }

    private JButton createButton(String name, String icon, Action action) {
        JButton btn = new JButton(name);
        btn.setFont(new Font("Lucida Grande", Font.PLAIN, 14));
        btn.setActionCommand(action.name());
        btn.addActionListener(this);
        btn.setIcon(SwingUtil.loadImage(icon, 36, 36));
        btn.setFocusPainted(false);
        btn.setBorder(BORDER_NORMAL);
        btn.setContentAreaFilled(false);

        return btn;
    }

    private static final long serialVersionUID = 1L;

    private static final Border BORDER_NORMAL = new CompoundBorder(new LineBorder(new Color(180, 180, 180)),
            new EmptyBorder(0, 5, 0, 10));
    private static final Border BORDER_FOCUS = new CompoundBorder(new LineBorder(new Color(51, 153, 255)),
            new EmptyBorder(0, 5, 0, 10));
    private JMenuBar menuBar;
    private JMenu mnFile;
    private JMenuItem mntmServer;
    private JMenuItem mntmExit;
    private JMenu mnHelp;
    private JMenuItem mntmHelp;
    private JMenuItem mntmAbout;
    private JMenu mnWallet;
    private JMenuItem mntmImportPrivateKey;
    private JMenuItem mntmExportPrivateKey;
    private AboutDialog aboutDialog;
    private HelpDialog helpDialog;

    protected void select(JPanel panel, JButton button) {
        if (activeButton != null) {
            activeButton.setBorder(BORDER_NORMAL);
        }
        activeButton = button;
        activeButton.setBorder(BORDER_FOCUS);

        activePanel.removeAll();
        activePanel.add(panel, BorderLayout.CENTER);

        activePanel.revalidate();
        activePanel.repaint();
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

        case SHOW_ACC:
            select(walletPanel, btnWallet);
            break;
        case SHOW_SEND:
            select(sendPanel, btnSend);
            break;
        case SHOW_SERVER:
            serverDialog.setVisible(true);
            break;
        case ABOUT:
            if (this.aboutDialog == null)
                this.aboutDialog = new AboutDialog(this);
            aboutDialog.setVisible(true);
            break;
        case EXPORTPRIVATEKEY: {
            ExportPrivateKeyDialog d = new ExportPrivateKeyDialog(rpcService, this);
            d.setVisible(true);
            break;
        }
        case IMPORTPRIVATEKEY: {
            InputDialog dialog = new InputDialog(this, GUIMessages.get("EnterPrivateKey"), false);
            String pk = dialog.getInput();
            if (pk != null) {
                try {
                    Wallet wallet = rpcService.getWallet();
                    Key account = new Key(Hex.decode0x(pk));
                    if (wallet.addAccount(account)) {
                        wallet.flush();
                        JOptionPane.showMessageDialog(this, GUIMessages.get("PrivateKeyImportSuccess"));
                    } else {
                        JOptionPane.showMessageDialog(this, GUIMessages.get("PrivateKeyAlreadyExists"));
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, GUIMessages.get("PrivateKeyImportFailed"));
                }
            }
            break;
        }
        case HELP:
            if (this.helpDialog == null)
                this.helpDialog = new HelpDialog(this);
            helpDialog.setVisible(true);
            break;
        case EXIT: {
            SystemUtil.exitAsync(0);
            break;
        }

        default:
            throw new IllegalStateException("No such action");
        }

    }

    public void donate() {
        select(sendPanel, btnSend);
        sendPanel.donate();
    }

}
