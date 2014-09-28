/**
 * Created by jeffreyng on 2014-09-27.
 */
public class TestingThread implements Runnable{

    private final Lock lock;
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

    private void count()
    {

        lock.lock();

        try {


        } finally {
            int temp2 = lock.unlock();

            if (!firstSuccess)
                {
                    firstSuccess = true;
                } else {
                    // subtract 1 cause we count ourselves obtaining
                    int temp3 = (temp2 - lastAcquiredCount) -1;
                    if (temp3 > delayx) delayx = temp3;
                }
                lastAcquiredCount = temp2;
//            System.out.println(lastAcquiredCount);
//            System.out.println("delayx" + delayx);

        }
        delays[id] = delayx;
    }
}
