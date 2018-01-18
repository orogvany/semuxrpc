package de.phash.semuxrpc.dialog;

import java.awt.BorderLayout;

import javax.swing.JDialog;
import javax.swing.JLabel;

import de.phash.semuxrpc.MainFrame;
import de.phash.semuxrpc.gui.GUIMessages;
import de.phash.semuxrpc.gui.SwingUtil;

public class AboutDialog extends JDialog {
    private static final long serialVersionUID = 1L;

    public AboutDialog(MainFrame mainFrame) {
        super(mainFrame);
        setSize(600,400);
        
        JLabel textPane = new JLabel(GUIMessages.get("AboutHtml"));
        getContentPane().add(textPane, BorderLayout.CENTER);
        
        
        JLabel lblBanner = new JLabel("");
        lblBanner.setIcon(SwingUtil.loadImage("banner", 125, 200));
        getContentPane().add(lblBanner, BorderLayout.WEST);
        
    }

}
