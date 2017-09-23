package simulapp.graphics.canvasObjects.manipulation;

import simulapp.EconSimul.savers.cShapesSaver;
import simulapp.graphics.canvasObjects.members.cShape;

import java.awt.*;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Created by Tomas Ballon on 22.1.2017.
 */
public class cShapes {

    protected static LinkedHashMap<String, cShape> shapes = new LinkedHashMap<>();
    protected boolean hasSelected; //ci je selectnuta aspon jedna

    public cShapes(){
        hasSelected = false;
    }

    public boolean isUniqueCShapeIndex(String index){
        return shapes.containsKey(index);
    }

    public void addCShape(String index, cShape t){
        shapes.put(index, t);
    }

    public void deleteCShape(String index){
        cShape s = shapes.get(index);
        s.destroy();
        shapes.remove(index);
    }

    public HashMap.Entry<String, cShape> getCShapetOnPos(int x, int y){
        for(HashMap.Entry<String, cShape> entry : shapes.entrySet()) {
            if(entry.getValue().isClicked(x,y)) {
                return entry;
            }
        }

        return null;
    }

    public cShape getShapeByIndex(String index){
        return shapes.get(index);
    }

    public boolean hasSelectedItem(){
        return hasSelected;
    }

    public  HashMap<String, cShape> getShapes(){
        return shapes;
    }

    public void setShapes(LinkedHashMap<String, cShape> sh){
        shapes = sh;
    }

    public void unselectAll(){
        shapes.values().forEach(shape -> shape.setSelected(false));
        hasSelected = false;
    }

    public void setSelected(int x, int y){
        for(HashMap.Entry<String, cShape> entry : shapes.entrySet()) {
            if(entry.getValue().isClicked(x, y)){
                hasSelected = false;
                unselectAll();
                entry.getValue().setSelected(true);
                hasSelected = true;
                return;
            }
        }

        return;
    }

    public void childToCurrentClick(int x, int y, boolean set){
        if(!hasSelected){
            return;
        }
        cShape shapeOnPos = null;
        HashMap.Entry<String, cShape> shapeInfo = getCShapetOnPos(x, y);
        if(shapeInfo == null){
            return;
        }
        shapeOnPos = shapeInfo.getValue();
        if(shapeOnPos == null || shapeOnPos.isSelected()){
            return;
        }
        for(cShape entry : shapes.values()) {
            if(entry.isSelected()){
                if(set) {
                    entry.addChild(shapeOnPos);
                }else{
                    entry.deleteChild(shapeOnPos);
                }
            }
        }
    }

    public void visitProbability(int x, int y){
        return;
    }

    //paintovanie vsetkych objektov
    public void paint(Graphics g){
        try {
            for (cShape s : shapes.values()) {
                s.paintChildrenConnections(g);
            }
            for (cShape s : shapes.values()) {
                s.paint(g);
            }
        } catch (ConcurrentModificationException e) {
            System.out.println("SKIP PAINTING (cShapes)");
        }
    }

    public void tick(){

    }

    public void getMetrics(int x, int y) {
    }

    public cShapesSaver save(){
        return new cShapesSaver(this.shapes);
    }
}
