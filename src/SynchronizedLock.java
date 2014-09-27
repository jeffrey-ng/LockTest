import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by jeffreyng on 2014-09-27.
 */
public class SynchronizedLock implements Lock {

    AtomicBoolean locked;
    private Object lockObj = new Object();
    int acquiredLock;
    public SynchronizedLock()
    {
        locked = new AtomicBoolean(false);
        acquiredLock = 0;
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
            acquiredLock++;
            locked.set(false);
            return acquiredLock;
        }

    }

}
