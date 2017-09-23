import simulapp.main.simulApp;
import simulapp.main.tick;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Tomas Ballon on 21.1.2017.
 */
public class Run {

    public static void main(String[] args) {
        int tick = 50;

        simulApp app = new simulApp(600, 600);

        TimerTask task = new tick(app);
        Timer timer = new Timer();
        timer.schedule(task, tick, tick);
    }

}
