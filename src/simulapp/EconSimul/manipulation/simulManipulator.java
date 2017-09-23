package simulapp.EconSimul.manipulation;

import simulapp.EconSimul.forms.econObjForm;
import simulapp.EconSimul.members.cClock;
import simulapp.EconSimul.members.cProcessingPoint;
import simulapp.EconSimul.members.cStartPoint;
import simulapp.EconSimul.savers.cShapesSaver;
import simulapp.graphics.canvasObjects.manipulation.cManipulator;
import simulapp.graphics.canvasObjects.members.cShape;
import simulapp.main.simulApp;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

/**
 * Created by Tomas Ballon on 1.2.2017.
 */
public class simulManipulator extends cManipulator {

    //nove druhy pointov
    public static final String CPROCESSINGPOINT = "cProcessingPoint";
    public static final String CSTARTENDPOINT = "cStartPoint";
    public static final String CPERSON = "cPerson";
    public static final String CCLOCK = "cClock";

    public static cClock clock;
    public econObjForm eObForm = null;

    public simulManipulator(simulApp app){
        super(app, true);

        shapes = new staticObjects();
        crawler = new personObjects(app);
        addEditForm = new JFrame();

        initClock();
    }

    public void initClock(){
        clock = new cClock("clock", 450, 10, app);
        shapes.addCShape("clock", clock);
        simulApp.mainGui.simulationSpeedTextField.setText(String.valueOf(clock.getTickSpeed()));
    }

    public void tick(Point mousePosition){
        if(mousePosition != null){
            moveCShape(mousePosition.x, mousePosition.y);
        }

        boolean ticked = false;
        int nextTime = clock.getTime() + clock.getTickSpeed();
        try {
            while (clock != null && clock.getTime() < nextTime && simulApp.mainGui.getCurrentMode().equals(cManipulator.CRAWLING)) {
                ticked = true;
                shapes.tick();
                crawler.move();
                clock.setTime(clock.getTime() + 1);
            }
            if (ticked && clock.getTime() < nextTime && clock != null) {
                clock.setTime(0);
            }
        }catch (NullPointerException e){
            System.out.println("Clock reset");
        }
    }

    public boolean mouseDown(int x, int y){
        if(simulApp.mainGui.getCurrentMode().equals(CRAWLING)){
            return true;
        }
        if(simulApp.mainGui.getCurrentMode().equals(METRICS)){
            shapes.getMetrics(x, y);
            return true;
        }
        if(simulApp.mainGui.getCurrentMode().equals(VISITPROBABILITY)){
            shapes.visitProbability(x, y);
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

        simulApp.manipulator.crawler.clear();

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
                if(shape.getValue().getType().equals(CSTARTENDPOINT)){
                    eObForm = new econObjForm(app, (cStartPoint)shape.getValue());
                }else if(shape.getValue().getType().equals(CPROCESSINGPOINT)){
                    eObForm = new econObjForm(app, (cProcessingPoint) shape.getValue());
                }
                //eObForm = new cShapeForm(app, shape.getValue().getX(), shape.getValue().getY(), shape.getValue().getType(), shape.getValue().getIndex(), shape.getValue().getText(), shape.getValue().getCrawlerstartString());
            }else if(simulApp.mainGui.getCurrentMode().equals(DELETING)){
                int dialogButton = JOptionPane.YES_NO_OPTION;
                int dialogResult = JOptionPane.showConfirmDialog (null, "Would You Like to delete \"" + shape.getValue().getText() + "\"?","Warning", dialogButton);
                if(dialogResult == JOptionPane.YES_OPTION){
                    shapes.deleteCShape(shape.getValue().getIndex());
                }
                return true;
            }
        }else{
            app.resetClockNoChangemode();
        }

        if(!objFound && (simulApp.mainGui.getCurrentMode().equals(EDITING )|| simulApp.mainGui.getCurrentMode().equals(DELETING))){
            return true;
        }

        if(!objFound) {
            eObForm = new econObjForm(app, x, y);
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
        addEditForm.setContentPane(eObForm.panel1);
        addEditForm.pack();

        //centrovanie okna na stred - current display
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        int winW = addEditForm.getSize().width;
        int winH = addEditForm.getSize().height;
        int xx = (gd.getDisplayMode().getWidth() - winW) / 2;
        int yy = (gd.getDisplayMode().getHeight() - winH) / 2;

        addEditForm.setLocation(xx, yy);
        addEditForm.setVisible(true);

        eObForm.labelTextField1.requestFocusInWindow(); //focus kurzoru
        return false;
    }

    public void onChangeMode(String mode){

    }

    public cShapesSaver save(){
        return shapes.save();
    }

}
