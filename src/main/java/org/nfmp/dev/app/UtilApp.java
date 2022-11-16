package org.nfmp.dev.app;

import org.nfmp.dev.gui.TestFrame;

import javax.swing.*;
import java.awt.*;

public class UtilApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new TestFrame();
            Dimension dimension = new Dimension(250, 250);
            frame.setPreferredSize(dimension);
        });
    }
}
