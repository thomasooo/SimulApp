package simulapp.EconSimul.manipulation;

import simulapp.EconSimul.forms.graphDataForm;
import simulapp.EconSimul.forms.visitProbForm;
import simulapp.EconSimul.savers.staticObjectsSaver;
import simulapp.graphics.canvasObjects.manipulation.cShapes;
import simulapp.graphics.canvasObjects.members.cShape;
import simulapp.main.simulApp;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

import static simulapp.util.math.parseIntFromString;

/**
 * Created by Tomas Ballon on 4.2.2017.
 */
public class staticObjects  extends cShapes {

    protected JFrame visiProber;
    protected JFrame gfwindow;
    protected visitProbForm vp;
    protected graphDataForm gf;

    public staticObjects(){
        super();
    }

    public void deleteCShape(String index){
        cShape s = shapes.get(index);
        simulApp.indexes.addToReuse(s.getIndex());
        s.destroy();
        shapes.remove(index);
        s = null;
    }

    public void visitProbability(int x, int y){
        Map.Entry<String, cShape> sh = getCShapetOnPos(x, y);

        if(sh == null || sh.getValue().getChildren().size() == 0){
            return;
        }

        if(vp != null){
            vp = null;
        }
        if(visiProber != null){
            visiProber.dispose();
        }

        vp = new visitProbForm(sh.getValue());

        visiProber = new JFrame();
        visiProber.setContentPane(vp.SetProp);
        visiProber.pack();

        //centrovanie okna na stred - current display
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        int winW = visiProber.getSize().width;
        int winH = visiProber.getSize().height;
        int xx = (gd.getDisplayMode().getWidth() - winW) / 2;
        int yy = (gd.getDisplayMode().getHeight() - winH) / 2;

        visiProber.setLocation(xx, yy);
        visiProber.setVisible(true);
    }

    public void getMetrics(int x, int y){
        Map.Entry<String, cShape> sh = getCShapetOnPos(x, y);

        if(sh == null){
            return;
        }

        gf = new graphDataForm(sh.getValue().getMeter().getAll(), sh.getValue().getText(), sh.getValue().getMeter().getLastTime());

        gfwindow = new JFrame();
        gfwindow.setContentPane(gf.panel);
        gfwindow.pack();

        //centrovanie okna na stred - current display
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        int winW = gfwindow.getSize().width;
        int winH = gfwindow.getSize().height;
        int xx = (gd.getDisplayMode().getWidth() - winW) / 2;
        int yy = (gd.getDisplayMode().getHeight() - winH) / 2;

        gfwindow.setLocation(xx, yy);
        gfwindow.setVisible(true);
    }

    public void tick(){
        for (cShape s: shapes.values()) {
            s.tick(simulManipulator.clock.getTime());
        }
    }

    public static int getMaxIndex(){
        int index = 0;

        for (cShape s: shapes.values()) {
            int i = parseIntFromString(s.getIndex().replace("auto_", ""));
            if(i > index){
                index = i;
            }
        }

        return index;
    }

    public staticObjectsSaver save(){
        return new staticObjectsSaver(this.shapes);
    }
}
