/**
 * Created by jeffreyng on 2014-09-27.
 */
public class TestingThread implements Runnable{

    private final Lock lock;
    int count;
    int id;
    int problemSize;
    int delayx;
    int lastAcquiredCount;
    boolean firstSuccess;
    int[] delays;


    public TestingThread(Lock lock, int id, int problemSize, int[] delays)
    {
        this.lock = lock;
        this.id = id;
        this.problemSize = problemSize;
        delayx = 0;
        lastAcquiredCount = 0;
        firstSuccess = false;
        this.delays = delays;
    }

    public void run()
    {
        for (int i =0; i < problemSize;i++)
        {
            count();
            Thread.yield();
        }
    }

    private int count()
    {
        int temp = 0;
        lock.lock();
        try {
            temp = count;
            count = temp + 1;

        } finally {
            int temp2 = lock.unlock();
                if (!firstSuccess)
                {
                    delayx += temp2 - lastAcquiredCount;

                    firstSuccess = true;
                } else {
                    // subtract 1 cause we count ourselves obtaining
                    delayx += (temp2 - lastAcquiredCount) -1;
                }
                lastAcquiredCount = temp2;
        }
        delays[id] = delayx;

        return temp;
    }
}
