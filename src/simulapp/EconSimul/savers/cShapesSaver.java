package simulapp.EconSimul.savers;

import simulapp.EconSimul.manipulation.staticObjects;
import simulapp.EconSimul.members.cProcessingPoint;
import simulapp.EconSimul.members.cStartPoint;
import simulapp.graphics.canvasObjects.members.cShape;
import simulapp.main.simulApp;

import java.util.HashMap;
import java.util.LinkedHashMap;

import static simulapp.EconSimul.manipulation.simulManipulator.*;
import static simulapp.main.simulApp.indexes;

/**
 * Created by Tomas Ballon on 8.4.2017.
 */
public class cShapesSaver implements java.io.Serializable {

    public LinkedHashMap<String, cShapeSaver> shapes = new LinkedHashMap<>();
    public int indexPostfix;
    public int tickSpeed;
    public int moveSpeed;

    public cShapesSaver(LinkedHashMap<String, cShape> shps){
        indexPostfix = 0;
        tickSpeed = 1;
        moveSpeed = 1;
        if (shps == null){
            return;
        }
        indexPostfix = staticObjects.getMaxIndex() + 1;
        tickSpeed = clock.getTickSpeed();
        moveSpeed = simulApp.manipulator.getMoveSpeed();
        for (HashMap.Entry<String, cShape> s: shps.entrySet()){
            if(s.getValue().getType().equals(CSTARTENDPOINT)){
                cStartPoint sp = (cStartPoint) s.getValue();
                shapes.put(s.getKey(), sp.save());
            }else {
                shapes.put(s.getKey(), s.getValue().save());
            }
        }
    }

    public  LinkedHashMap<String, cShape> load(){
        LinkedHashMap<String, cShape> shps = new LinkedHashMap<>();

        //ZAKLADNE ELEMENTY
        for(HashMap.Entry<String, cShapeSaver> s: shapes.entrySet()){
            if(s.getValue().load() == null){
                continue;
            }
            cShape shp = (cShape) s.getValue().load();
            if(shp.getType().equals(CSTARTENDPOINT)){
                shps.put(s.getKey(), (cStartPoint) s.getValue().load());
            }else if(shp.getType().equals(CPROCESSINGPOINT)) {
                shps.put(s.getKey(), (cProcessingPoint) s.getValue().load());
            }else{
                //shps.put(s.getKey(), (cShape) s.getValue().load());
            }
        }

        //CHILDREN
        for(HashMap.Entry<String, cShape> s: shps.entrySet()){
            cShapeSaver ss = shapes.get(s.getKey());
            if(ss == null || ss.children == null){
                continue;
            }

            for(String c: ss.children) {
                cShape chld = shps.get(c);
                if(chld == null){
                    continue;
                }
                s.getValue().addChild(chld);
            }
        }

        //PROBABILITIES
        for(HashMap.Entry<String, cShape> s: shps.entrySet()){
            cShapeSaver ss = shapes.get(s.getKey());
            if(ss == null || ss.children == null){
                continue;
            }

            for(String c: ss.children) {
                cShape chld = shps.get(c);
                if(chld == null){
                    continue;
                }
                s.getValue().setVisitProbabilities(ss.visitProbabilities);
            }
        }

        indexes.setPostfix(indexPostfix);
        clock.setTickSpeed(tickSpeed);
        simulApp.mainGui.app.manipulator.setMoveSpeed(moveSpeed);
        simulApp.mainGui.simulationSpeedTextField.setText(String.valueOf(tickSpeed));
        simulApp.mainGui.moveSpeedTextField.setText(String.valueOf(moveSpeed));

        return shps;
    }
}
