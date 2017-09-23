package simulapp.graphics.canvasObjects.members;

import simulapp.EconSimul.savers.cShapeSaver;
import simulapp.metrics.Measure;
import simulapp.util.geometry;
import simulapp.util.math;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import static simulapp.util.math.randomUniform;

/**
 * Created by Tomas Ballon on 22.1.2017.
 */
public class cShape {
    //konstanty pre ctypes
    public static final String CTEXT = "cText";
    public static final String CRECTANGLE = "cRectangle";
    public static final String CELLIPSE = "cEllipse";
    public static final String CCRAWLERPOINT = "cCrawlerPoint";
    //dalsie konstanty su definovane v manipulatoroch

    public static final String CRAWLERSTART = "yes";
    public static final String NOT_CRAWLERSTART = "no";

    public static final String UNIFORM_DISTRIBUTION = "Uniforn"; //nepouzivane - zle
    public static final String NORMAL_DISTRIBUTION = "Normal"; //nepouzivane - zle
    public static final String EXPONENTIAL_DISTRIBUTION = "Exponential"; //nepouzivane - zle
    public static final String PERCENTUAL_DISTRIBUTION = "Percentual";
    public static final String CIRCULAR_DISTRIBUTION = "Circular";
    public static final String SHORTEST_QUEUE_DISTRIBUTION = "Shortest Queue";

    protected String index;
    protected String value;
    protected String type;
    protected int X;
    protected int Y;
    protected int width;
    protected int height;
    protected boolean crawlerStart;
    protected boolean selected;
    protected Color color;
    protected Color fontColor;
    protected Color connectionColor;
    protected String currentDistribution;
    protected double paramDistribution;

    protected String imgSrc;
    protected BufferedImage image;
    protected BufferedImage resizedImage;

    protected Measure meter; //moznost merania a zapisovania v kazdom objekte

    protected int currentCircular;

    protected boolean destroyed;

    protected LinkedHashMap<String, cShape> children;
    protected LinkedHashMap<String, Double> visitProbabilities;

    public cShape(String index, String val, int x, int y, String type){
        meter = new Measure();

        this.X = x;
        this.Y = y;
        this.index = index;
        this.type = type;
        width = 30;
        height = 30;
        value = val;
        crawlerStart = false;
        selected = false;
        color = Color.LIGHT_GRAY;
        fontColor = Color.BLACK;
        connectionColor = Color.LIGHT_GRAY;
        imgSrc = null;
        image = null;
        resizedImage = null;

        destroyed = false;

        children = new LinkedHashMap<>();
        visitProbabilities = new LinkedHashMap<>();

        currentDistribution = CIRCULAR_DISTRIBUTION;
        paramDistribution = 0;

        currentCircular = 0;
    }

    public String getType(){
        return type;
    }

    public void setType(String t){
        type = t;
    }

    public int getX(){
        return X;
    }

    public int getY(){
        return Y;
    }

    public Measure getMeter(){
        return meter;
    }

    public void setChildDistribution(String distribution){
        currentDistribution = distribution;
    }

    public String getChildDistribution(){
        return currentDistribution;
    }

    public void setParamDistribution(double param){
        paramDistribution = param;
    }

    public double getParamDistribution(){
        return paramDistribution;
    }

    public String getImgSrc(){
        return imgSrc;
    }

    public void setImgSrc(String i){
        imgSrc = i;
        if(i == null || i == ""){
            imgSrc = null;
            image = null;
            resizedImage = null;
            return;
        }
        try {
            image = ImageIO.read(new File(i));
            resizeImage();
        } catch (IOException ex) {
            imgSrc = null;
            image = null;
            resizedImage = null;
            return;
        }
    }

    public void setDimensions(int w, int h){
        width = w;
        height = h;
        resizeImage();
    }

    public int getWidth(){
        return width;
    }

    public int getHeight(){
        return height;
    }

    public Color getColor(){
        return color;
    }

    public Color getFontColor(){
        return fontColor;
    }

    public Color getConnectionColor(){
        return connectionColor;
    }

    public String getCurrnetDistribution(){
        return currentDistribution;
    }

    public void setText(String txt) {
        value = txt;
    }

    public void setPosition(int x, int y) {
        this.X = x;
        this.Y = y;
    }

    public void setSelected(boolean selected){
        this.selected = selected;
    }

    public void setCrawlerStart(boolean start){
        crawlerStart = start;
    }

    public boolean isCrawlerStart(){
        return crawlerStart;
    }

    public String getCrawlerstartString(){
        if(crawlerStart){
            return CRAWLERSTART;
        }
        return NOT_CRAWLERSTART;
    }

    public boolean isSelected(){
        return selected;
    }

    public void setColor(Color c){
        color = c;
    }

    public void setFontColor(Color c){
        fontColor = c;
    }

    public void setConnectionColor(Color c){
        connectionColor = c;
    }

    public void appendText(String txt){
        value += txt;
    }

    public String getIndex(){
        return index;
    }

    public void setIndex(String i){
        index = i;
    }

    public String getText(){
        return value;
    }

    public void addChild(cShape shape){
        if(children.containsKey(shape.getIndex())){
            return;
        }
        //sam seba emoze pridat ako siblinga
        if(shape.getIndex() == index){
            return;
        }

        children.put(shape.getIndex(), shape);
        visitProbabilities.put(shape.getIndex(), 1.0);
    }

    public HashMap<String, cShape> getChildren(){
        ArrayList<cShape> destroyedSiblings = new ArrayList<>();
        for (cShape s: children.values()) {
            //vybratie destroynutych
            if (s.isDestroyed()) {
                destroyedSiblings.add(s);
            }
        }
        for (cShape s: destroyedSiblings) {
            //zmazanie destroynutych
            deleteChild(s);
        }
        return children;
    }

    public HashMap<String, Double> getVisitProbabilities(){
        return visitProbabilities;
    }

    public void setVisitProbabilities(HashMap<String, Double> vp){
        visitProbabilities = new LinkedHashMap<>(vp);
    }

    public void setVisitProbability(String index, double prop){
        if(!visitProbabilities.containsKey(index)){
            return;
        }
        visitProbabilities.put(index, prop);
    }

    public double getVisitProbability(String index){
        if(!children.containsKey(index) || !visitProbabilities.containsKey(index)){
            return 0;
        }
        return visitProbabilities.get(index);
    }

    public cShape getNextShapeToVisitByProbability(){
        if(currentDistribution.equals(CIRCULAR_DISTRIBUTION)){
            return getNextShapeToVisitByProbabilityCircular();
        }
        if(currentDistribution.equals(PERCENTUAL_DISTRIBUTION)){
            return getNextShapeToVisitByProbabilityPercentual();
        }
        if(currentDistribution.equals(SHORTEST_QUEUE_DISTRIBUTION)){
            return getNextShapeToVisitByProbabilityShortestQueue();
        }
        /*
        if(currentDistribution.equals(UNIFORM_DISTRIBUTION)){
            return getNextShapeToVisitByProbabilityUniform();
        }
        if(currentDistribution.equals(NORMAL_DISTRIBUTION)){
            return getNextShapeToVisitByProbabilityNormal();
        }
        if(currentDistribution.equals(EXPONENTIAL_DISTRIBUTION)){
            return getNextShapeToVisitByProbabilityExponential();
        }
        */
        return null;
    }

    public cShape getNextShapeToVisitByProbabilityCircular(){
        if(currentCircular >= children.values().size()){
            currentCircular = 0;
        }
        int i = 0;
        for(cShape s: children.values()){
            if(i == currentCircular){
                currentCircular++;
                return s;
            }
            i++;
        }
        return null;
    }

    public cShape getNextShapeToVisitByProbabilityShortestQueue(){
        int shortest = -1;
        cShape sh = null;
        for(cShape s: children.values()){
            if(shortest == -1 || s.getWaintingPersonsCount() < shortest){
                sh = s;
                shortest = s.getWaintingPersonsCount();
            }else if(s.getWaintingPersonsCount() == shortest){
                if(randomUniform(0, 1) == 0){
                    sh = s;
                    shortest = s.getWaintingPersonsCount();
                }
            }
        }
        return sh;
    }

    public cShape getNextShapeToVisitByProbabilityPercentual(){
        recalculatePercentualValues();
        double totalProp = 0;
        for(cShape s: children.values()){
            totalProp += getVisitProbability(s.getIndex());
        }
        double res = randomUniform(0.0, totalProp);
        totalProp = 0;
        for(cShape s: children.values()){
            totalProp += getVisitProbability(s.getIndex());
            if(res <= totalProp){
                return s;
            }
        }
        return null;
    }

    public cShape getNextShapeToVisitByProbabilityUniform(){
        int totalProp = 0;
        for(cShape s: children.values()){
            totalProp += getVisitProbability(s.getIndex());
        }
        int res = randomUniform(0, totalProp);
        totalProp = 0;
        for(cShape s: children.values()){
            totalProp += getVisitProbability(s.getIndex());
            if(res <= totalProp){
                return s;
            }
        }
        return null;
    }

    public cShape getNextShapeToVisitByProbabilityNormal(){
        double r = Math.abs(math.randomNormal(0, paramDistribution)); //absolutna hodnota

        cShape res = null;
        for(cShape s: children.values()){
            if(r >= getVisitProbability(s.getIndex())){
                if(res == null || getVisitProbability(res.getIndex()) < getVisitProbability(s.getIndex())){
                    res = s; //vyberame mensie ako random, ale to co mu je najblizsie
                }
            }
        }

        return res;
    }

    public cShape getNextShapeToVisitByProbabilityExponential(){
        double r = math.randomExponential(paramDistribution);

        cShape res = null;
        for(cShape s: children.values()){
            if(r <= getVisitProbability(s.getIndex())){
                if(res == null || getVisitProbability(res.getIndex()) > getVisitProbability(s.getIndex())){
                    res = s; //vyberame vacsie ako radom, ale to co mu je najblizsie
                }
            }
        }

        return res;
    }

    public void recalculatePercentualValues(){
        double totalProp = 0;
        for(cShape s: children.values()){
            totalProp += getVisitProbability(s.getIndex());
        }
        for(cShape s: children.values()){
           if(getVisitProbability(s.getIndex()) == 0 || totalProp == 0){
               setVisitProbability(s.getIndex(), 0);
               continue;
           }
           double onePercent = totalProp / 100;
           double currentPercent = getVisitProbability(s.getIndex()) / onePercent;
           setVisitProbability(s.getIndex(), currentPercent);
        }
    }

    public void deleteChild(cShape shape){
        children.remove(shape.getIndex());
        visitProbabilities.remove(shape.getIndex());
    }

    public boolean isClicked(int x, int y){
        int minX = X - (width / 2);
        int minY = Y - (height / 2);
        int maxX = minX + width;
        int maxY = minY + height;

        if(x >= minX && x <= maxX && y >= minY && y <= maxY){
            return true;
        }
        return false;
    }

    public void paintChildrenConnections(Graphics g){
        g.setColor(connectionColor);
        for (cShape s: getChildren().values()) {
            Point p1 = new Point(X, Y);
            Point p2 = new Point(s.getX(), s.getY());
            Point midpoint = geometry.midPoint(p1, p2);

            g.drawLine(X, Y, s.getX(), s.getY());
            geometry.drawArrow(g, X, Y, midpoint.x, midpoint.y);
        }
    }

    public void paint(Graphics g) {
        if(resizedImage != null){
            paintImage(g);
        }else{
            paintShape(g);
        }
    }

    public void paintImage(Graphics g){
        g.setColor(color);

        Point startDraw = geometry.getStartXY(X, Y, width, height);

        g.drawImage(resizedImage, startDraw.x, startDraw.y, null);

        if(selected){
            g.setColor(Color.RED);
            g.drawRect(startDraw.x, startDraw.y, width, height);
        }

        if(crawlerStart) {
            Point startDravC = geometry.getStartXY(X, Y, (width + 10), (height + 10));
            g.setColor(Color.GREEN);
            Graphics2D g2 = (Graphics2D) g;
            g2.setStroke(new BasicStroke(2));
            g2.drawRect(startDravC.x, startDravC.y, (width + 10), (height + 10));
        }

        g.setColor(fontColor);

        Font font = new Font("Arial", Font.BOLD, (height / 2));
        FontMetrics metrics = g.getFontMetrics(font);

        int textX = (X - (metrics.stringWidth(value) / 2));
        int textY = startDraw.y + height + metrics.getHeight();

        g.setFont(font);
        g.drawString(value, textX, textY);
    }

    public void paintShape(Graphics g){
        if(crawlerStart){
            g.setColor(Color.GREEN);
        }else {
            g.setColor(color);
        }
        g.drawLine(X, Y, X, Y);

        g.setColor(fontColor);

        Font font = new Font("Arial", Font.BOLD, (height / 2));
        FontMetrics metrics = g.getFontMetrics(font);

        int textX = (X - (metrics.stringWidth(value) / 2));
        int textY = (Y + (metrics.getHeight() / 2));

        g.setFont(font);
        g.drawString(value, textX, textY);
    }

    public void destroy(){
        destroyed = true;
    }

    public boolean isDestroyed(){
        return destroyed;
    }

    public void tick(int time){

    }

    protected void resizeImage(){
        if(image == null){
            return;
        }

        ColorModel cm = image.getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = image.copyData(null);
        resizedImage = new BufferedImage(cm, raster, isAlphaPremultiplied, null);

        int scaledWidth = width;
        int scaledHeight = (int) (image.getHeight() * ( (double) scaledWidth / image.getWidth() ));

        if (scaledHeight > height) {
            scaledHeight = height;
            scaledWidth = (int) (image.getWidth() * ( (double) scaledHeight / image.getHeight() ));

            if (scaledWidth > width) {
                scaledWidth = width;
                scaledHeight = height;
            }
        }

        Image resized =  image.getScaledInstance(scaledWidth, scaledHeight, Image.SCALE_SMOOTH);
        resizedImage = new BufferedImage(scaledWidth, scaledHeight, Image.SCALE_REPLICATE);
        resizedImage.getGraphics().drawImage(resized, 0, 0 , null);

    }

    public cShapeSaver save(){
        return new cShapeSaver(this);
    }

    public int getWaintingPersonsCount() {
        return 0;
    }
}
