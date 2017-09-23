package simulapp.main;

import java.util.TimerTask;

/**
 * Created by Tomas Ballon on 22.1.2017.
 */
public class tick extends TimerTask { //pomocna classa pre tick

    private simulApp app;
    protected boolean canTick;

    public tick(simulApp app){
        this.app = app;
        canTick = true;
    }

    public void run() {
        if(canTick) {
            canTick = false;
            app.tick();
        }

        canTick = true;
    }
}
