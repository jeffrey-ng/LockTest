import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by jeffreyng on 2014-09-21.
 */
public class SimpleTTASLock implements Lock{

    protected AtomicBoolean locked;
    int acquiredCount;

    public SimpleTTASLock()
    {
        locked = new AtomicBoolean(false);
        acquiredCount = 0;
    }

    public void lock()
    {
        boolean acquired = false;
        while(!acquired)
        {
            if(!locked.get())
            {
                acquired=locked.compareAndSet(false, true);

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
