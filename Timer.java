package minesweeper;

import java.lang.reflect.InvocationTargetException;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import static minesweeper.StartGame.INTERFACE;

public class Timer extends JFrame implements Runnable {
           
        private Thread counter;
        private boolean isRunning = false;
        private int count = 0;
        public static Timer TIMER = new Timer();
        
        Runnable timerUpdater = new Runnable() {
            public void run() {
                INTERFACE.displayElapsedTime(count);
                count++;
            }
        };

        public void stop() {
            int elapsed = count;
            isRunning = false;
            try {
                counter.join();
            } catch (InterruptedException ie) {
            }
            INTERFACE.displayElapsedTime(elapsed);
            count = 0;
        }

        public void run() {
            try {
                while (isRunning) {
                    SwingUtilities.invokeAndWait(timerUpdater);
                    Thread.sleep(1000);
                }
            } catch (InterruptedException ite) {
                ite.printStackTrace(System.err);
            } catch (InvocationTargetException ex) {
                ex.getMessage();
            } 
        }

        public void Start() {
            isRunning = true;
            counter = new Thread(this);
            counter.start();
        }
    }
