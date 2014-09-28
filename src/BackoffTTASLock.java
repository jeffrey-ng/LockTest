import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by jeffreyng on 2014-09-27.
 */
public class BackoffTTASLock implements Lock {
    protected AtomicBoolean locked;
    protected AtomicInteger acquiredCount;
    private int limit;
    Random r;

    public BackoffTTASLock()
    {
        locked = new AtomicBoolean(false);
        acquiredCount = new AtomicInteger(0);
        r = new Random();
    }

    public void lock()
    {
        BackOff b = new BackOff();
        while (true)
        {
            while (locked.get()) {};
            if (!locked.getAndSet(true))
            {
                return;
            } else {
                try
                {
                    b.backoff();
                } catch(InterruptedException e) {
                   e.printStackTrace();
                }
            }
        }
    }

    public int unlock()
    {
        int t = acquiredCount.getAndIncrement();
        locked.set(false);
        return t;

    }
    public void resetDelay()
    {
        acquiredCount.getAndSet(0);
    }


}

class BackOff {
    final int min, max;
    int limit;
    final Random random;

    public BackOff() {
        min = 1;
        max = 100;
        limit = min;
        random = new Random();
    }
    public void backoff() throws InterruptedException {
        int delay = random.nextInt(limit);
        limit = Math.min(max, 2 * limit);
        Thread.sleep(delay);
    }
}
