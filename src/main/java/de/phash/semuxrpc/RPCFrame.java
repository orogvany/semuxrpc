package de.phash.semuxrpc;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import java.awt.BorderLayout;

public class RPCFrame extends JFrame{
    public RPCFrame() {
        
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);
        
        JPanel panel = new JPanel();
        getContentPane().add(panel, BorderLayout.NORTH);
        
        JPanel panel_1 = new JPanel();
        getContentPane().add(panel_1, BorderLayout.CENTER);
    }

}
