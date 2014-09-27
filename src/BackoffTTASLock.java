import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by jeffreyng on 2014-09-27.
 */
public class BackoffTTASLock implements Lock {
    protected AtomicBoolean locked;
    int acquiredCount;
    private final long initialSleepTime;
    int attempts;
    long MAXWAIT = 1000;

    public BackoffTTASLock()
    {
        locked = new AtomicBoolean(false);
        acquiredCount = 0;
        initialSleepTime = 2;
        attempts = 0;
    }

    public void lock()
    {
        boolean acquired = false;
        long sleepTime = initialSleepTime;

        while(!acquired)
        {
            if(!locked.get())
            {
                acquired=locked.compareAndSet(false, true);
            } else {
                attempts++;
                try
                 {
                    Thread.sleep(sleepTime);
                 } catch(InterruptedException e) {
                    throw new IllegalStateException(e);
                }
                sleepTime =(long) Math.min(MAXWAIT,Math.pow(sleepTime,attempts));
               // System.out.println("Sleeptime: " +sleepTime +" Attempts: "+ attempts);
            }
        }
    }

    public int unlock()
    {
        acquiredCount++;
        locked.set(false);
        return acquiredCount;

    }
}
