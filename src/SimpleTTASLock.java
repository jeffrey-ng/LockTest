import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by jeffreyng on 2014-09-21.
 */
public class SimpleTTASLock implements Lock{

    protected AtomicBoolean locked;
    AtomicInteger acquiredCount;

    public SimpleTTASLock()
    {
        locked = new AtomicBoolean(false);
        acquiredCount = new AtomicInteger(0);
    }

    public void lock()
    {
        while (true) {
            while (locked.get()){};
            if (!locked.getAndSet(true)) { return;}
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
