package simulapp.graphics.canvas;

import simulapp.main.simulApp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import static com.sun.java.accessibility.util.AWTEventMonitor.addWindowListener;

/**
 * Created by Tomas Ballon on 22.1.2017.
 */
public class canvasPanel extends JPanel {
    protected simulApp app;
    protected int width;
    protected int height;

    public canvasPanel(int w, int h, simulApp app) {
        width = w;
        height = h;
        this.app = app;

        setBorder(BorderFactory.createLineBorder(Color.black));

        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON3) {
                    app.manipulator.mouseDownRight(e.getX(), e.getY());
                }else{
                    app.mouseDown(e.getX(), e.getY());
                }
            }

            public void mouseReleased(MouseEvent e) {
                app.mouseUp(e.getX(), e.getY());
            }
        });

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                JOptionPane.showMessageDialog(null, "You cannot close this window");
            }
        });

    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        simulApp.manipulator.getCObjects().paint(g);
        simulApp.manipulator.crawler.paint(g);
    }
}
