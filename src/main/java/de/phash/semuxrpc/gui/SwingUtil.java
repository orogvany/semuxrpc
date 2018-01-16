/**
 * Copyright (c) 2017 The Semux Developers
 *
 * Distributed under the MIT software license, see the accompanying file
 * LICENSE or https://opensource.org/licenses/mit-license.php
 */
package de.phash.semuxrpc.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Comparator;
import java.util.Date;
import java.util.EnumMap;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumnModel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import de.phash.semuxrpc.Action;

public class SwingUtil {

    private static final Logger logger = LoggerFactory.getLogger(SwingUtil.class);

    private SwingUtil() {
    }

    /**
     * Put a JFrame in the center of screen.
     * 
     * @param frame
     * @param width
     * @param height
     */
    public static void alignFrameToMiddle(JFrame frame, int width, int height) {
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        int x = ((int) d.getWidth() - width) / 2;
        int y = ((int) d.getHeight() - height) / 2;
        frame.setLocation(x, y);
        frame.setBounds(x, y, width, height);
    }
    /**
     * Generate an QR image for the given text.
     * 
     * @param text
     * @param width
     * @param height
     * @return
     * @throws WriterException
     */
    public static BufferedImage createQrImage(String text, int width, int height) throws WriterException {
        Map<EncodeHintType, Object> hintMap = new EnumMap<>(EncodeHintType.class);
        hintMap.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        hintMap.put(EncodeHintType.MARGIN, 2);
        hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);

        QRCodeWriter writer = new QRCodeWriter();
        BitMatrix matrix = writer.encode(text, BarcodeFormat.QR_CODE, width, height, hintMap);
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        image.createGraphics();

        Graphics2D graphics = (Graphics2D) image.getGraphics();
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, width, height);
        graphics.setColor(Color.BLACK);

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < width; j++) {
                if (matrix.get(i, j)) {
                    graphics.fillRect(i, j, 1, 1);
                }
            }
        }

        return image;
    }
    /**
     * Load an ImageIcon from resource, and rescale it.
     * 
     * @param imageName
     *            image name
     * @return an image icon if exists, otherwise null
     */
    public static ImageIcon loadImage(String imageName, int width, int height) {
        String imgLocation = imageName + ".png";
        URL imageURL = SwingUtil.class.getResource(imgLocation);

        if (imageURL == null) {
            logger.warn("Resource not found: {}", imgLocation);
            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            return new ImageIcon(image);
        } else {
            ImageIcon icon = new ImageIcon(imageURL);
            Image img = icon.getImage();
            Image img2 = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            return new ImageIcon(img2);
        }
    }

    /**
     * Generate an empty image icon.
     * 
     * @param width
     * @param height
     * @return
     */
    public static ImageIcon emptyImage(int width, int height) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        Graphics2D graphics = image.createGraphics();
        graphics.setPaint(new Color(255, 255, 255));
        graphics.fillRect(0, 0, image.getWidth(), image.getHeight());

        return new ImageIcon(image);
    }

    /**
     * Set the preferred width of table columns.
     * 
     * @param table
     * @param total
     * @param widths
     */
    public static void setColumnWidths(JTable table, int total, double... widths) {
        TableColumnModel model = table.getColumnModel();
        for (int i = 0; i < widths.length; i++) {
            model.getColumn(i).setPreferredWidth((int) (total * widths[i]));
        }
    }

    /**
     * Set the alignments of table columns.
     * 
     * @param table
     * @param right
     */
    public static void setColumnAlignments(JTable table, boolean... right) {
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(JLabel.RIGHT);

        TableColumnModel model = table.getColumnModel();
        for (int i = 0; i < right.length; i++) {
            if (right[i]) {
                model.getColumn(i).setCellRenderer(rightRenderer);
            }
        }
    }

    /**
     * Convenience factory method for creating buttons
     * 
     * @param text
     * @param listener
     * @param action
     * @return
     */
    public static JButton createDefaultButton(String text, ActionListener listener, Action action) {
        JButton button = new JButton(text);
        button.setActionCommand(action.name());
        button.addActionListener(listener);
        return button;
    }

    /**
     * 
     * 
     * /** Formats a number as a localized string.
     * 
     * @param number
     * @param decimals
     * @return
     */
    public static String formatNumber(Number number, int decimals) {
        NumberFormat format = NumberFormat.getInstance();
        format.setMinimumFractionDigits(decimals);
        format.setMaximumFractionDigits(decimals);

        return format.format(number);
    }

    /**
     * Format a number with zero decimals.
     * 
     * @param number
     * @return
     */
    public static String formatNumber(Number number) {
        return formatNumber(number, 0);
    }

    

    /**
     * Formats a percentage
     * 
     * @param percentage
     * @return
     */
    public static String formatPercentage(double percentage) {
        return formatNumber(percentage, 1) + " %";
    }

    /**
     * Format a timestamp into date string.
     * 
     * @param timestamp
     * @return
     */
    public static String formatTimestamp(long timestamp) {
        DateFormat format = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT);

        return format.format(new Date(timestamp));
    }

    /**
     * Parse timestamp from its string representation.
     * 
     * @param timestamp
     * @return
     * @throws ParseException
     */
    public static long parseTimestamp(String timestamp) throws ParseException {
        DateFormat format = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT);

        return format.parse(timestamp).getTime();
    }

    /**
     * Timestamp/date string comparator based on its value.
     * 
     * @exception
     */
    public static final Comparator<String> TIMESTAMP_COMPARATOR = (o1, o2) -> {
        try {
            return Long.compare(parseTimestamp(o1), parseTimestamp(o2));
        } catch (ParseException e) {
            throw new NumberFormatException("Invalid number strings: " + o1 + ", " + o2);
        }
    };

}
