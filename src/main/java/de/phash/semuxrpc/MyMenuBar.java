package de.phash.semuxrpc;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import de.phash.semuxrpc.gui.GUIMessages;
import de.phash.semuxrpc.panels.ServerDialog;

public class MyMenuBar extends JMenuBar implements ActionListener {
    private JMenu mnFile;
    ServerDialog serverDialog;
    private RPCService rpcService;
    private JFrame frame;

    public MyMenuBar(RPCService rpcService, JFrame frame) {
        super();
        this.frame = frame;
        this.rpcService = rpcService;
        serverDialog = new ServerDialog(this.rpcService);
        mnFile = new JMenu("File");
        add(mnFile);

        JMenuItem mntmServer =  new JMenuItem(GUIMessages.get("ShowServer"));
        mntmServer.setActionCommand(Action.SHOWSERVER.name());
        mntmServer.addActionListener(this);
        mnFile.add(mntmServer);
        
        mnFile.add(mntmServer);

        JMenuItem mntmImportPrivateKey = new JMenuItem(GUIMessages.get("ImportPrivateKey"));
        mntmImportPrivateKey.setActionCommand(Action.IMPORTPRIVATEKEY.name());
        mntmImportPrivateKey.addActionListener(this);
        mnFile.add(mntmImportPrivateKey);

        JMenuItem itemExit = new JMenuItem(GUIMessages.get("Exit"));
        itemExit.setActionCommand(Action.EXIT.name());
        itemExit.addActionListener(this);
        mnFile.add(itemExit);

        JMenu mnHelp = new JMenu("Help");
        add(mnHelp);

        JMenu menuHelp = new JMenu(GUIMessages.get("Help"));
        this.add(menuHelp);

        JMenuItem mntmHelp = new JMenuItem(GUIMessages.get("Help"));
        mntmHelp.setActionCommand(Action.HELP.name());
        mntmHelp.addActionListener(this);
        mnHelp.add(mntmHelp);

        JMenuItem mntmAbout = new JMenuItem(GUIMessages.get("About"));
        mntmAbout.setActionCommand(Action.ABOUT.name());
        mntmAbout.addActionListener(this);
        mnHelp.add(mntmAbout);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Action action = Action.valueOf(e.getActionCommand());

        switch (action) {
        case ABOUT:
            break;
        case SHOWSERVER:
            serverDialog.setVisible(true);
            break;
        case HELP:
            break;
        case IMPORT_PRIVATE_KEY:
            break;
        case EXIT:
            frame.dispose();
            
            break;

        }
    }

}
