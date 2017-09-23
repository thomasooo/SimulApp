package simulapp.EconSimul.savers;

import kotlin.Pair;
import simulapp.EconSimul.members.cProcessingPoint;
import simulapp.EconSimul.members.cStartPoint;
import simulapp.graphics.canvasObjects.members.cShape;
import simulapp.util.timeDependerVal;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

import static simulapp.EconSimul.manipulation.simulManipulator.CPROCESSINGPOINT;
import static simulapp.EconSimul.manipulation.simulManipulator.CSTARTENDPOINT;

/**
 * Created by Tomas Ballon on 8.4.2017.
 */
public class cShapeSaver implements java.io.Serializable {

    public String index;
    public String value;
    public String type;
    public int X;
    public int Y;
    public int width;
    public int height;
    public boolean crawlerStart;
    public Color color;
    public Color fontColor;
    public Color connectionColor;
    public String currentDistribution;
    public double paramDistribution;
    public String imgSrc;
    public boolean destroyed;
    public ArrayList<String> children;
    public HashMap<String, Double> visitProbabilities;

    //PP
    public double moneyConsumptionMin;
    public double moneyConsumptionMax;
    public int timeConsumptionMin;
    public int timeConsumptionMax;
    public int capacity;

    //SP
    public int personsPerCreatingMin;
    public int personsPerCreatingMax;
    public int countTicksToCreatingMin;
    public int countTicksToCreatingMax;
    public double personMoneyAvailableMax;
    public double personMoneyAvailableMin;
    public int personTimeAvailableMax;
    public int personTimeAvailableMin;
    public int personsPriority;
    public Color personsColor;

    public ArrayList<timeDependerVal<Pair<Integer, Integer>>> personsPerCreatingTimeDependent; //personsPerCreating time dependent
    public ArrayList<timeDependerVal<Pair<Integer, Integer>>> countTicksToCreatingTimeDependent; //countTicksToCreating time dependent

    public ArrayList<timeDependerVal<Pair<Integer, Integer>>> moneyConsumptionTimeDependent; //personsPerCreating time dependent
    public ArrayList<timeDependerVal<Pair<Integer, Integer>>> timeConsumptionTimeDependent; //personsPerCreating time dependent
    public ArrayList<timeDependerVal<Pair<Integer, Integer>>> capacityTimeDependent; //personsPerCreating time dependent

    public cShapeSaver(cShape sh){
        index = sh.getIndex();
        value = sh.getText();
        type = sh.getType();
        X = sh.getX();
        Y = sh.getY();
        width = sh.getWidth();
        height = sh.getHeight();
        crawlerStart = sh.isCrawlerStart();
        color = sh.getColor();
        fontColor = sh.getFontColor();
        connectionColor = sh.getConnectionColor();
        currentDistribution = sh.getCurrnetDistribution();
        paramDistribution = sh.getParamDistribution();
        imgSrc = sh.getImgSrc();
        destroyed = sh.isDestroyed();

        children = new ArrayList<>();
        visitProbabilities = new HashMap<>();

        for(HashMap.Entry<String, Double> e: sh.getVisitProbabilities().entrySet()){
            visitProbabilities.put(e.getKey(), e.getValue());
        }

        for(HashMap.Entry<String, cShape> s: sh.getChildren().entrySet()){
            children.add(s.getValue().getIndex());
        }
    }

    public cShapeSaver(cStartPoint sh){
        index = sh.getIndex();
        value = sh.getText();
        type = sh.getType();
        X = sh.getX();
        Y = sh.getY();
        width = sh.getWidth();
        height = sh.getHeight();
        crawlerStart = sh.isCrawlerStart();
        color = sh.getColor();
        fontColor = sh.getFontColor();
        connectionColor = sh.getConnectionColor();
        currentDistribution = sh.getCurrnetDistribution();
        paramDistribution = sh.getParamDistribution();
        imgSrc = sh.getImgSrc();
        destroyed = sh.isDestroyed();

        children = new ArrayList<>();
        visitProbabilities = new HashMap<>();

        for(HashMap.Entry<String, Double> e: sh.getVisitProbabilities().entrySet()){
            visitProbabilities.put(e.getKey(), e.getValue());
        }

        for(HashMap.Entry<String, cShape> s: sh.getChildren().entrySet()){
            children.add(s.getValue().getIndex());
        }

        //SP
        personsPerCreatingMin = sh.getPersonsPerCreatingMin();
        personsPerCreatingMax = sh.getPersonsPerCreatingMax();
        countTicksToCreatingMin = sh.getCountTicksToCreatingMin();
        countTicksToCreatingMax = sh.getCountTicksToCreatingMax();
        personMoneyAvailableMax = sh.getPersonMoneyAvailableMax();
        personMoneyAvailableMin = sh.getPersonMoneyAvailableMin();
        personTimeAvailableMax = sh.getPersonTimeAvailableMax();
        personTimeAvailableMin = sh.getPersonTimeAvailableMin();
        personsPriority = sh.getPersonsPriority();
        personsColor = sh.getPersonsColor();

        personsPerCreatingTimeDependent = sh.personsPerCreatingTimeDependent.getAll();
        countTicksToCreatingTimeDependent = sh.countTicksToCreatingTimeDependent.getAll();
    }

    public cShapeSaver(cProcessingPoint sh){
        index = sh.getIndex();
        value = sh.getText();
        type = sh.getType();
        X = sh.getX();
        Y = sh.getY();
        width = sh.getWidth();
        height = sh.getHeight();
        crawlerStart = sh.isCrawlerStart();
        color = sh.getColor();
        fontColor = sh.getFontColor();
        connectionColor = sh.getConnectionColor();
        currentDistribution = sh.getCurrnetDistribution();
        paramDistribution = sh.getParamDistribution();
        imgSrc = sh.getImgSrc();
        destroyed = sh.isDestroyed();

        children = new ArrayList<>();
        visitProbabilities = new HashMap<>();

        for(HashMap.Entry<String, Double> e: sh.getVisitProbabilities().entrySet()){
            visitProbabilities.put(e.getKey(), e.getValue());
        }

        for(HashMap.Entry<String, cShape> s: sh.getChildren().entrySet()){
            children.add(s.getValue().getIndex());
        }

        //PP
        moneyConsumptionMax = sh.getMoneyConsumptionMax();
        moneyConsumptionMin = sh.getMoneyConsumptionMin();
        timeConsumptionMin = sh.getTimeConsumptionMin();
        timeConsumptionMax = sh.getTimeConsumptionMax();
        capacity = sh.getCapacity();

        capacityTimeDependent = sh.capacityTimeDependent.getAll();
        moneyConsumptionTimeDependent = sh.moneyConsumptionTimeDependent.getAll();
        timeConsumptionTimeDependent = sh.timeConsumptionTimeDependent.getAll();
    }

    public Object load() {
        if(type.equals(CSTARTENDPOINT)){
            return loadStartPoint();
        }else if(type.equals(CPROCESSINGPOINT)){
            return loadProcessingPoint();
        }

        return null;
    }

    private cStartPoint loadStartPoint(){
        cStartPoint s = new cStartPoint(index, value, X, Y);
        s.setDimensions(width, height);
        s.setCrawlerStart(crawlerStart);
        s.setColor(color);
        s.setFontColor(fontColor);
        s.setConnectionColor(connectionColor);
        s.setChildDistribution(currentDistribution);
        s.setParamDistribution(paramDistribution);
        s.setImgSrc(imgSrc);
        if(destroyed){
            s.destroy();
        }

        /*
        if(visitProbabilities.size() > 0) {
            for(HashMap.Entry<String, Integer> e: visitProbabilities.entrySet()){
                s.setVisitProbabilityForce(e.getKey(), e.getValue());
            }
        }
        */

        s.setPersonsPerCreating(personsPerCreatingMin, personsPerCreatingMax);
        s.setCountTicksToCreating(countTicksToCreatingMin, countTicksToCreatingMax);
        s.setPersonMoneyAvailable(personTimeAvailableMin, personMoneyAvailableMax);
        s.setPersonTimeAvailable(personTimeAvailableMin, personTimeAvailableMax);
        s.setPersonsPriority(personsPriority);
        s.setPersonsColor(personsColor);

        s.personsPerCreatingTimeDependent.addAll(personsPerCreatingTimeDependent);
        s.countTicksToCreatingTimeDependent.addAll(countTicksToCreatingTimeDependent);
        return s;
    }

    private cProcessingPoint loadProcessingPoint(){
        cProcessingPoint p = new cProcessingPoint(index, value, X, Y);
        p.setDimensions(width, height);
        p.setCrawlerStart(crawlerStart);
        p.setColor(color);
        p.setFontColor(fontColor);
        p.setConnectionColor(connectionColor);
        p.setChildDistribution(currentDistribution);
        p.setParamDistribution(paramDistribution);
        p.setImgSrc(imgSrc);
        if(destroyed){
            p.destroy();
        }

        /*
        if(visitProbabilities.size() > 0) {
            for(HashMap.Entry<String, Integer> e: visitProbabilities.entrySet()){
                p.setVisitProbabilityForce(e.getKey(), e.getValue());
            }
        }
        */

        p.setMoneyConsumption(moneyConsumptionMin, moneyConsumptionMax);
        p.setTimeConsumption(timeConsumptionMin, timeConsumptionMax);
        p.setCapacity(capacity);

        p.capacityTimeDependent.addAll(capacityTimeDependent);
        p.timeConsumptionTimeDependent.addAll(timeConsumptionTimeDependent);
        p.moneyConsumptionTimeDependent.addAll(moneyConsumptionTimeDependent);

        return p;
    }
}
