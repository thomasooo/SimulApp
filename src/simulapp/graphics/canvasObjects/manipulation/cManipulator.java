package simulapp.graphics.canvasObjects.manipulation;

import simulapp.graphics.canvasObjects.members.cEllipse;
import simulapp.graphics.canvasObjects.members.cRectangle;
import simulapp.graphics.canvasObjects.members.cShape;
import simulapp.graphics.canvasObjects.members.cText;
import simulapp.graphics.forms.cShapeForm;
import simulapp.main.simulApp;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

/**
 * Created by Tomas Ballon on 24.1.2017.
 */
public class cManipulator {
    //konstanty a zaroven vypisy v buttonoch
    public static final String ADDING = "Add or move";
    public static final String EDITING = "Edit";
    public static final String DELETING = "Delete";
    public static final String SELECTING = "Select";
    public static final String ADDINGCHILD = "Set child";
    public static final String DELETINGCHILD = "Delete child";
    public static final String CRAWLING = "RUN";
    public static final String VISITPROBABILITY = "Next probability";
    public static final String METRICS = "Metrics";

    public cShapeCrawlers crawler;
    public cShapeForm cObForm = null;

    public cShapes shapes; //vsetky objekty vo vnutri sa repaintuju automaticky
    protected simulApp app;
    protected Map.Entry<String, cShape> processedCShape = null;
    protected JFrame addEditForm;
    protected int uniquePostfix;
    protected int moveSpeed;

    protected int ticker;

    public cManipulator(simulApp app){
        this.app = app;
        shapes = new cShapes();
        crawler = new cShapeCrawlers(app);
        addEditForm = new JFrame();
        ticker = 0;
        uniquePostfix = 1;
        moveSpeed = 1;
    }

    public cManipulator(simulApp app, boolean noinit){
        this.app = app;
        ticker = 0;
        uniquePostfix = 1;
        moveSpeed = 1;
    }

    public void tick(Point mousePosition){
        if(mousePosition != null){
            moveCShape(mousePosition.x, mousePosition.y);
        }
        crawler.move();
    }

    public void setMoveSpeed(int speed){
        moveSpeed = speed;
    }

    public int getMoveSpeed(){
        return moveSpeed;
    }

    public void mouseDownRight(int x, int y){
        shapes.setSelected(x, y);
    }

    public boolean mouseDown(int x, int y){
        if(simulApp.mainGui.getCurrentMode().equals(CRAWLING)){
            return true;
        }
        if(simulApp.mainGui.getCurrentMode().equals(SELECTING)){
            shapes.setSelected(x, y);
            return true;
        }

        if(simulApp.mainGui.getCurrentMode().equals(ADDINGCHILD)){
            shapes.childToCurrentClick(x, y, true);
            return true;
        }

        if(simulApp.mainGui.getCurrentMode().equals(DELETINGCHILD)){
            shapes.childToCurrentClick(x, y, false);
            return true;
        }

        Map.Entry<String, cShape> shape = shapes.getCShapetOnPos(x, y);
        boolean objFound = false;
        String formName = "Insert object";

        if(shape != null){
            objFound = true;
            //adding == moving
            if(simulApp.mainGui.getCurrentMode().equals(ADDING)) {
                processedCShape = shape;
                return true;
            }else if(simulApp.mainGui.getCurrentMode().equals(EDITING)){
                cObForm = new cShapeForm(app, shape.getValue().getX(), shape.getValue().getY(), shape.getValue().getType(), shape.getValue().getIndex(), shape.getValue().getText(), shape.getValue().getCrawlerstartString());
            }else if(simulApp.mainGui.getCurrentMode().equals(DELETING)){
                int dialogButton = JOptionPane.YES_NO_OPTION;
                int dialogResult = JOptionPane.showConfirmDialog (null, "Would You Like to delete \"" + shape.getValue().getText() + "\"?","Warning", dialogButton);
                if(dialogResult == JOptionPane.YES_OPTION){
                    shapes.deleteCShape(shape.getValue().getIndex());
                }
                return true;
            }
        }

        if(!objFound && (simulApp.mainGui.getCurrentMode().equals(EDITING )|| simulApp.mainGui.getCurrentMode().equals(DELETING))){
            return true;
        }

        if(!objFound) {
            cObForm = new cShapeForm(app, x, y);
        }

        if(simulApp.mainGui.getCurrentMode().equals(EDITING)){
            formName = "Edit object";
        }

        if(addEditForm.isVisible()){
            addEditForm.toFront();
            return false;
        }

        //form pre pridavanie objektov
        addEditForm.setName(formName);
        addEditForm.setContentPane(cObForm.panelMain);
        addEditForm.pack();

        //centrovanie okna na stred - current display
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        int winW = addEditForm.getSize().width;
        int winH = addEditForm.getSize().height;
        int xx = (gd.getDisplayMode().getWidth() - winW) / 2;
        int yy = (gd.getDisplayMode().getHeight() - winH) / 2;

        addEditForm.setLocation(xx, yy);
        addEditForm.setVisible(true);

        cObForm.valueTextField.requestFocusInWindow(); //focus kurzoru
        if(cObForm.formattedTextField1.getText().equals("")){
            cObForm.formattedTextField1.setText("auto_" + String.valueOf(uniquePostfix));
            uniquePostfix++;
        }

        return false;
    }

    public void mouseUp(int x, int y){
        if(x > -1 && y > -1) {
            moveCShape(x, y);
        }
        processedCShape = null;
    }

    public void moveCShape(int x, int y){
        if(processedCShape == null){
            return;
        }
        cShape s = processedCShape.getValue();
        String index = processedCShape.getKey();

        s.setPosition(x, y);
        shapes.addCShape(index, s);
    }

    public void addCShape(String type, String index, String value, String isCrawlerStart, int x, int y){
        if(index.equals("")){
            return;
        }

        boolean crStart = false;
        if(isCrawlerStart.equals(cShape.CRAWLERSTART)){
            crStart = true;
        }

        //ak sa edituje - nech nestraca data
        Map.Entry<String, cShape> shape = shapes.getCShapetOnPos(x, y);
        if(shape != null && shape.getValue().getIndex().equals(index)){
            shape.getValue().setText(value);
            shape.getValue().setCrawlerStart(crStart);
            shapes.addCShape(index, shape.getValue());
            return;
        }

        cShape t = null;
        if(type.equals(cShape.CTEXT)){
            t = new cText(index, value, x, y);
        }
        if(type.equals(cShape.CRECTANGLE)){
            t = new cRectangle(index, value, x, y);
        }
        if(type.equals(cShape.CELLIPSE)){
            t = new cEllipse(index, value, x, y);
        }

        if(t == null){
            return;
        }

        t.setCrawlerStart(crStart);
        shapes.addCShape(index, t);
    }

    public void onChangeMode(String mode){

    }

    public void addCustomText(String index, String text, int x, int y){
        cText t = new cText(index, text, x, y);
        shapes.addCShape(index, t);
    }

    public void deleteCustomText(String index){
        shapes.deleteCShape(index);
    }

    //funkcia pre canvas, tu dostava vsetky objecty
    public cShapes getCObjects(){
        return shapes;
    }


    public void initClock() {
    }

    public void removeCrawler(String index) {
    }
}
