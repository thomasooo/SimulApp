package simulapp.graphics.canvasObjects.manipulation;

import simulapp.graphics.canvasObjects.members.cCrawlerPoint;
import simulapp.graphics.canvasObjects.members.cShape;
import simulapp.main.simulApp;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Tomas Ballon on 29.1.2017.
 */
public class cShapeCrawlers {
    protected HashMap<String, cShape> shapes = new HashMap<>();
    protected HashMap<String, cShape> startShapes = new HashMap<>();
    protected ConcurrentHashMap<String, cCrawlerPoint> crawlers = new ConcurrentHashMap<>();

    protected int crawlerNamePostfix;
    protected simulApp app;
    protected boolean running;

    public cShapeCrawlers(simulApp app){
        this.app = app;
        crawlerNamePostfix = 0;
        reset();
    }

    public void reset(){
        shapes.clear();
        startShapes.clear();
        crawlers.clear();
        running = false;
    }

    public boolean isRunning(){
        return running;
    }

    public void addShapes(HashMap<String, cShape> sh){
       reset();
       shapes = (HashMap<String, cShape>) sh.clone();

        for(HashMap.Entry<String, cShape> entry : shapes.entrySet()) {
            if(entry.getValue().isCrawlerStart()){
                startShapes.put(entry.getKey(), entry.getValue());
            }
        }
    }

    public void run(){
        running = true;
        for(cShape entry : startShapes.values()) {
            if(entry.getChildren().isEmpty()){
                continue;
            }
            startNewCrawlers(entry, entry.getChildren());
        }
    }

    public void startNewCrawlers(cShape start, HashMap<String, cShape> ends){
        for(cShape entry : ends.values()) {
            String k = "crawler" + start.getIndex() + String.valueOf(crawlerNamePostfix);
            cCrawlerPoint c = new cCrawlerPoint(k, start, entry);
            crawlers.put(k, c);
            crawlerNamePostfix++;
            //ochrana proti preteceniu
            if(crawlerNamePostfix > 10000){
                return;
            }
        }
    }

    public void move(){
        //ochrana proti preteceniu
        if(crawlerNamePostfix > 10000){
            if(running) {
                JOptionPane.showMessageDialog(null, "Too many elements. CRAWLING IS STOPPED!", "OK", JOptionPane.PLAIN_MESSAGE);
            }
            crawlerNamePostfix = 0;
            reset();
            return;
        }
        HashMap<String, cCrawlerPoint> cr = new HashMap<>(crawlers);
        Iterator it = cr.values().iterator();
        while(it.hasNext()){
            cCrawlerPoint c = (cCrawlerPoint) it.next();
            if(c.inFinish()) {
                if (!c.getEndShape().getChildren().isEmpty()) {
                    startNewCrawlers(c.getEndShape(), c.getEndShape().getChildren());
                }
                crawlers.remove(c.getIndex());
                continue;
            }
            c.move();
        }
        cr.clear();
    }

    //paintovanie vsetkych objektov
    public void paint(Graphics g){
        if(!simulApp.mainGui.getCurrentMode().equals(cManipulator.CRAWLING)){
            return;
        }
        HashMap<String, cCrawlerPoint> cr = new HashMap<>(crawlers);
        Iterator it = cr.values().iterator();
        while(it.hasNext()){
            cCrawlerPoint c = (cCrawlerPoint) it.next();
            if(c.inFinish()){
                if(!c.getEndShape().getChildren().isEmpty()){
                    startNewCrawlers(c.getEndShape(), c.getEndShape().getChildren());
                }
                crawlers.remove(c.getIndex());
                continue;
            }
            c.paint(g);
        }
        cr.clear();
    }

    public void clear() {
        crawlers.clear();
    }
}
