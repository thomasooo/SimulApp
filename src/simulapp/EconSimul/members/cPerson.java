package simulapp.EconSimul.members;

import simulapp.EconSimul.manipulation.simulManipulator;
import simulapp.graphics.canvasObjects.members.cCrawlerPoint;
import simulapp.graphics.canvasObjects.members.cShape;
import simulapp.main.simulApp;

import java.awt.*;

/**
 * Created by Tomas Ballon on 1.2.2017.
 */
public class cPerson extends cCrawlerPoint {

    //konstanty stavov k dispozicii
    public static final int ON_ROUTE_STATE = 0; //je na ceste
    public static final int IN_PROCESSING_POINT_STATE = 1; //je vo vnutri
    public static final int WAITING_STATE = 2; //caka na vstup
    public static final int OUT_OF_AVAILABLE_UNITS_STATE = 3; //nema uz peniaze alebo cas
    public static final int EXITED_STATE = 4; //opustila graf

    protected double moneyAvailable;
    protected int timeAvailable;
    protected int changedPosX;
    protected int currentTime;
    protected int currentState; //v akom stave sa nachadza
    protected int nextActionTime; //ak je toto mensie, alebo rovne ako currentTime, moze vykonat dalsiu akciu
    protected int nextActionState; //stav nasledujucej akcie
    protected int startWaitingTime;
    protected int priority;
    protected Color mainColor;
    protected cShape parentPoint;


    public cPerson(cShape start, double money, int time){
        super(simulApp.indexes.get(), start, null);
        type = simulManipulator.CPERSON;
        moneyAvailable = money;
        timeAvailable = time;
        changedPosX = 0;
        currentTime = 0;
        nextActionTime = 0;
        nextActionState = ON_ROUTE_STATE;
        currentState = -1;
        priority = 0;
        mainColor = color;
        parentPoint = null;
        setOnRouteState();
    }

    public cPerson(cShape start){
        this(start, 100, 100);
    }

    public void setMainColor(){
        super.setColor(mainColor);
    }

    public void setMainColor(Color c){
        super.setColor(c);
        mainColor = c;
    }

    public Color getMainColor(){
        return mainColor;
    }

    public void reset(){
        X = 0;
        Y = 0;
    }

    public void setPriority(int prior){
        priority = prior;
    }

    public int getPriority(){
        return priority;
    }

    public void setParentPoint(cShape parent){
        parentPoint = parent;
    }

    public cShape getParentPoint(){
        return parentPoint;
    }

    public void setOutOfAvailableUnitsState(){
        if(currentState == OUT_OF_AVAILABLE_UNITS_STATE){
            return;
        }
        currentState = OUT_OF_AVAILABLE_UNITS_STATE;
        color = Color.BLACK;
        movingEnabled = false;
        Y -= 5;
    }

    public void setInProcessingPointState(){
        if(currentState == IN_PROCESSING_POINT_STATE || currentState == OUT_OF_AVAILABLE_UNITS_STATE){
            return;
        }
        currentState = IN_PROCESSING_POINT_STATE;
        color = Color.BLUE;
        movingEnabled = false;
        X -= 5;
        changedPosX = -5;
    }

    public void setWaitingState(int waitingStart){
        if(currentState == WAITING_STATE || currentState == OUT_OF_AVAILABLE_UNITS_STATE){
            return;
        }
        startWaitingTime = waitingStart;
        currentState = WAITING_STATE;
        color = Color.RED;
        movingEnabled = false;
        X += 5;
        changedPosX = 5;
    }

    public void setOnRouteState(){
        if(currentState == ON_ROUTE_STATE || currentState == OUT_OF_AVAILABLE_UNITS_STATE){
            return;
        }
        currentState = ON_ROUTE_STATE;
        setMainColor();
        X -= changedPosX;
        changedPosX = 0;
        movingEnabled = true;
    }

    public void setExitedState(){
        if(currentState == ON_ROUTE_STATE || currentState == OUT_OF_AVAILABLE_UNITS_STATE){
            return;
        }
        currentState = EXITED_STATE;
        color = color.YELLOW;
        movingEnabled = false;
    }

    public boolean endWaiting(int waitingEnd){
        int consumed = waitingEnd - startWaitingTime;
        if(consumed < 0){
            consumed = 0;
        }
        return timeConsoumate(consumed);
    }

    public int endWaitingGetConsumedTime(int waitingEnd){
        int consumed = waitingEnd - startWaitingTime;
        if(consumed < 0){
            consumed = 0;
        }
        return consumed;
    }

    public void setMoneyAvailable(double monAvail){
        moneyAvailable = monAvail;
    }

    public double getMoneyAvailable(){
        return moneyAvailable;
    }

    public void setTimeAvailable(int timeAvail){
        timeAvailable = timeAvail;
    }

    public int getTimeAvailable(){
        return timeAvailable;
    }

    public boolean hasTimeAndMoney(){
        if(timeAvailable > 0 && moneyAvailable > 0){
            return true;
        }
        return false;
    }

    public boolean timeConsoumate(int time){
        if(timeAvailable >= time){
            timeAvailable -= time;
            return true;
        }
        setOutOfAvailableUnitsState();
        return false;
    }

    public boolean moneyConsoumate(double money){
        if(moneyAvailable >= money){
            moneyAvailable -= money;
            return true;
        }
        setOutOfAvailableUnitsState();
        return false;
    }

    public int getCurrentState(){
        return currentState;
    }

    //moze ist na personsOut v processing pointe
    public boolean isProcessed(){
        if(!hasTimeAndMoney()){
            setOutOfAvailableUnitsState();
            return true;
        }
        if(currentState == OUT_OF_AVAILABLE_UNITS_STATE){
            return true;
        }
        if(currentState == ON_ROUTE_STATE){
            return true;
        }
        if(nextActionTime <= currentTime && nextActionState == ON_ROUTE_STATE){
            return true;
        }
        return false;
    }

    public void setNextAction(int time, int state){
        nextActionState = state;
        nextActionTime = time;
    }

    public void tick(int curTime){
        currentTime = curTime;
    }

}
