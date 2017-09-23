package simulapp.EconSimul.manipulation;

import simulapp.EconSimul.members.cPerson;
import simulapp.EconSimul.members.cProcessingPoint;
import simulapp.EconSimul.members.cStartPoint;
import simulapp.graphics.canvasObjects.manipulation.cManipulator;
import simulapp.graphics.canvasObjects.manipulation.cShapeCrawlers;
import simulapp.graphics.canvasObjects.members.cCrawlerPoint;
import simulapp.graphics.canvasObjects.members.cShape;
import simulapp.main.simulApp;

import java.awt.*;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Queue;

/**
 * Created by Tomas Ballon on 4.2.2017.
 */
public class personObjects extends cShapeCrawlers {

    private int lastTime;

    public personObjects(simulApp app){
        super(app);
        lastTime = 0;
    }

    public void run(){
        running = true;
    }

    public void startNewPersons(){
        for(cShape entry : startShapes.values()) {
            if(entry.getChildren().isEmpty()) {
                continue;
            }
            ArrayList<cPerson> persons = ((cStartPoint) entry).getPersons();
            if(persons.size() > 0){
                setNextPoint(persons, entry);
            }
        }
    }

    public void continueOldPersons(){
        for(cShape entry : shapes.values()) {
            if(!entry.getType().equals(simulManipulator.CPROCESSINGPOINT)){
                continue;
            }
            Queue<cPerson> persons = ((cProcessingPoint) entry).getPersons();
            if(persons.size() > 0){
                setNextPoint(persons, entry);
            }
        }
    }

    private void setNextPoint(ArrayList<cPerson> persons,  cShape currentShape) {
        for(cPerson person : persons) {
            if(currentShape.getChildren().isEmpty()) {
                if(currentShape.getType().equals(simulManipulator.CPROCESSINGPOINT)) {
                    ((cProcessingPoint) currentShape).exitPerson(person);
                    ((cProcessingPoint) currentShape).exitPersons(persons);
                }
                return;
            }
            cShape endShape = currentShape.getNextShapeToVisitByProbability();
            if(endShape == null) {
                if(currentShape.getType().equals(simulManipulator.CPROCESSINGPOINT)) {
                    ((cProcessingPoint) currentShape).exitPerson(person);
                }
                //return;
            }else{
                person.setOnRouteState();
            }
            person.setEndShape(endShape);
            person.setOnRouteState();
            crawlers.putIfAbsent(person.getIndex(), person);
        }
    }

    private void setNextPoint(Queue<cPerson> persons, cShape currentShape) {
        cPerson person = persons.poll();
        while(person != null) {
            if(currentShape.getChildren().isEmpty()) {
                if(currentShape.getType().equals(simulManipulator.CPROCESSINGPOINT)) {
                    ((cProcessingPoint) currentShape).exitPerson(person);
                    ((cProcessingPoint) currentShape).exitPersons(persons);
                }
                return;
            }
            cShape endShape = currentShape.getNextShapeToVisitByProbability();
            if(endShape == null) {
                if(currentShape.getType().equals(simulManipulator.CPROCESSINGPOINT)) {
                    ((cProcessingPoint) currentShape).exitPerson(person);
                }
                //return;
            }else{
                person.setOnRouteState();
            }
            person.setEndShape(endShape);
            crawlers.putIfAbsent(person.getIndex(), person);
            person = persons.poll();
        }
    }

    public void move(){
        startNewPersons();
        continueOldPersons();

        if(!simulApp.mainGui.getCurrentMode().equals(cManipulator.CRAWLING)){
            return;
        }

        int currentTime = simulManipulator.clock.getTime();
        boolean runned = false;

        HashMap<String, cCrawlerPoint> cr = new HashMap<>(crawlers);
        while (lastTime < currentTime || !runned) {
            if(lastTime < currentTime){
                lastTime++;
            }
            runned = true;
            for (cCrawlerPoint c : cr.values()) {
                for (int i = 0; i < simulApp.manipulator.getMoveSpeed(); i++) {
                    c.move(1);
                    c.tick(lastTime);
                    simulApp.canvas.repaint();
                    processInFinish(c);
                }
            }
        }
        cr.clear();
    }

    //paintovanie vsetkych objektov
    public void paint(Graphics g){
        try {
            for (cCrawlerPoint c : crawlers.values()) {
                c.paint(g);
            }
        }catch (ConcurrentModificationException e){
            System.out.println("SKIP PAINT (personObjects)");
        }
    }

    private boolean processInFinish(cCrawlerPoint c){
        if (c.inFinish()) {
            if (c.getEndShape() == null) {
                crawlers.remove(c.getIndex());
                ((cPerson) c).reset();
                simulApp.indexes.addToReuse(c.getIndex());
                return false;
            } else if (c.getEndShape().getType().equals(simulManipulator.CSTARTENDPOINT)) {
                crawlers.remove(c.getIndex());
                ((cPerson) c).reset();
                simulApp.indexes.addToReuse(c.getIndex());
                return false;
            } else {
                ((cProcessingPoint) c.getEndShape()).addPerson((cPerson) crawlers.get(c.getIndex()));
            }
            return true;
        }
        return false;
    }

}
