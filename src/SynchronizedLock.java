import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by jeffreyng on 2014-09-27.
 */
public class SynchronizedLock implements Lock {

    AtomicBoolean locked;
    private Object lockObj = new Object();
    AtomicInteger acquiredCount;
    public SynchronizedLock()
    {
        locked = new AtomicBoolean(false);
        acquiredCount = new AtomicInteger(0);
    }

    public void lock()
    {
        synchronized (lockObj)
        {
            locked.set(true);
        }
    }

    public int unlock()
    {
        synchronized (lockObj)
        {
            int t = acquiredCount.getAndIncrement();
            locked.set(false);
            return t;
        }

    }
    public void resetDelay()
    {
        acquiredCount.getAndSet(0);
    }

}
