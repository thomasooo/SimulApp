package simulapp.graphics.canvas;

import simulapp.main.simulApp;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Tomas Ballon on 22.1.2017.
 */
public class simuCanvas {

    private simulApp app;
    private JFrame workspace;
    private canvasPanel cnv;

    public simuCanvas(simulApp app, int w, int h){
        this.app = app;

        cnv = new canvasPanel(w, h, app);
        cnv.setBackground(Color.WHITE);

        workspace = new JFrame("Workspace");
        workspace.add(cnv);
        workspace.setSize(w,h);
        workspace.setDefaultCloseOperation(0);

        //centrovanie okna na stred - current display
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        int winW = workspace.getSize().width;
        int winH = workspace.getSize().height;
        int x = (gd.getDisplayMode().getWidth() - winW) / 2;
        int y = (gd.getDisplayMode().getHeight() - winH) / 2;

        workspace.setLocation(x, y);
    }

    public void show(){
        workspace.setVisible(true);
    }

    public void toggle(){
        workspace.setVisible(!workspace.isVisible());
    }

    public boolean isVisible(){
        return workspace.isVisible();
    }

    public void repaint(){
        cnv.repaint();
    }

    public Point getMousePosition(){
        return workspace.getMousePosition();
    }
}
