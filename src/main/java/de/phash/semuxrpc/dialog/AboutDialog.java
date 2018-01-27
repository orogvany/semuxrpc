package de.phash.semuxrpc.dialog;

import java.awt.BorderLayout;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTable;

import de.phash.semuxrpc.MainFrame;
import de.phash.semuxrpc.gui.GUIMessages;
import de.phash.semuxrpc.gui.SwingUtil;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;

import org.semux.gui.dialog.DelegateDialog;
import org.semux.gui.model.WalletDelegate;

public class AboutDialog extends JDialog {
    private static final long serialVersionUID = 1L;

    public AboutDialog(MainFrame mainFrame) {
        super(mainFrame);
        setSize(600,400);
        
        JLabel textPane = new JLabel(GUIMessages.get("AboutHtml"));
        
        
        JLabel lblBanner = new JLabel("");
        lblBanner.setIcon(SwingUtil.loadImage("banner", 125, 200));
        
        JLabel support = new JLabel(GUIMessages.get("Support"));
        support.setToolTipText(GUIMessages.get("Donate"));
        support.addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent me) {
                mainFrame.donate();
            }
        });
        GroupLayout groupLayout = new GroupLayout(getContentPane());
        groupLayout.setHorizontalGroup(
            groupLayout.createParallelGroup(Alignment.LEADING)
                .addGroup(groupLayout.createSequentialGroup()
                    .addComponent(lblBanner)
                    .addPreferredGap(ComponentPlacement.RELATED, 26, Short.MAX_VALUE)
                    .addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
                        .addComponent(textPane, GroupLayout.PREFERRED_SIZE, 414, GroupLayout.PREFERRED_SIZE)
                        .addComponent(support))
                    .addGap(19))
        );
        groupLayout.setVerticalGroup(
            groupLayout.createParallelGroup(Alignment.LEADING)
                .addGroup(groupLayout.createSequentialGroup()
                    .addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
                        .addComponent(lblBanner, GroupLayout.PREFERRED_SIZE, 361, GroupLayout.PREFERRED_SIZE)
                        .addGroup(groupLayout.createSequentialGroup()
                            .addGap(11)
                            .addComponent(textPane, GroupLayout.PREFERRED_SIZE, 284, GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(ComponentPlacement.RELATED)
                            .addComponent(support)))
                    .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        getContentPane().setLayout(groupLayout);
        
    }

}
