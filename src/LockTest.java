import java.util.ArrayList;
import java.util.List;

/**
 * Created by jeffreyng on 2014-09-21.
 */
public class LockTest {

    public static void main(String[] args) {

        //4 Locks
        //1: synchronized
        //2: TTAS no delay/back off
        //3: TTAS exponential back off
        //4: CLH queue

        int questionNumber = Integer.parseInt(args[0]);
        int problemSize = Integer.parseInt(args[1]);
        int threadCount = Integer.parseInt(args[2]);

        switch (questionNumber) {
            case 1:
                System.out.println("Running Synchronized Lock");
                Lock synch = new SynchronizedLock();
                run(synch,threadCount,problemSize);

                break;
            case 2:
                System.out.println("Running Simple TTASLock");
                Lock simpleTtas = new SimpleTTASLock();
                run(simpleTtas,threadCount,problemSize);

                break;
            case 3:
                System.out.println("Running Backoff TTASLock");

                Lock backoffTTAS = new BackoffTTASLock();
                run(backoffTTAS,threadCount,problemSize);

                break;
            case 4:
                System.out.println("Running CLHLock");
                Lock clh = new CLHLock();
                run(clh,threadCount,problemSize);
                break;

        }

    }


    public static void run(Lock l, int threadCount, int problemSize)
    {
        final long startTime = System.currentTimeMillis();
        List<Thread> threads = new ArrayList<Thread>();
        int[] delays = new int[threadCount];
        for (int i=0;i<threadCount;i++)
        {
            threads.add(new Thread(new TestingThread(l,i,problemSize,delays)));
        }
        for (Thread t: threads)
        {
            t.start();
        }

        for (Thread t: threads)
        {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        final long totalTime = System.currentTimeMillis() - startTime;
        System.out.println("Time: "+ totalTime+"ms");
        int totalDelay = 0;
        for (int i=0;i<threadCount;i++)
        {
            totalDelay += delays[i];
        }
        System.out.println("Total Delays: "+ totalDelay);

    }




}