package de.phash.semuxrpc.dialog;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextPane;

import de.phash.semuxrpc.MainFrame;
import de.phash.semuxrpc.gui.GUIMessages;
import de.phash.semuxrpc.gui.SwingUtil;

import java.awt.BorderLayout;

public class HelpDialog extends JDialog {

    public HelpDialog(MainFrame mainFrame) {
        super(mainFrame);
        setSize(600,400);
        JLabel txtpnSf = new JLabel(GUIMessages.get("HelpHtml"));
        getContentPane().add(txtpnSf, BorderLayout.CENTER);
        
        JLabel lblBanner = new JLabel("");
        lblBanner.setIcon(SwingUtil.loadImage("banner", 125, 200));
        getContentPane().add(lblBanner, BorderLayout.WEST);
    }

}
