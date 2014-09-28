import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jeffreyng on 2014-09-21.
 */
public class LockTest {
    public static int questionNumber;
    public static int problemSize;
    public static int threadCount;
    public static boolean shouldPrint;

    public static void main(String[] args) {

        //4 Locks
        //1: synchronized
        //2: TTAS no delay/back off
        //3: TTAS exponential back off
        //4: CLH queue

        questionNumber = Integer.parseInt(args[0]);
        problemSize = Integer.parseInt(args[1]);
        threadCount = Integer.parseInt(args[2]);

        int iterations =1;


        switch (questionNumber) {
            case 1:
                System.out.println("Running Synchronized Lock");
                Lock synch = new SynchronizedLock();
                run(synch,threadCount,problemSize,iterations);

                break;
            case 2:
                System.out.println("Running Simple TTASLock");
                Lock simpleTtas = new SimpleTTASLock();
                run(simpleTtas,threadCount,problemSize,iterations);

                break;
            case 3:
                System.out.println("Running Backoff TTASLock");

                Lock backoffTTAS = new BackoffTTASLock();
                run(backoffTTAS,threadCount,problemSize,iterations);

                break;
            case 4:
                System.out.println("Running CLHLock");
                Lock clh = new CLHLock();
                run(clh,threadCount,problemSize,iterations);
                break;

        }

    }


    public static void run(Lock l, int threadCount, int problemSize, int iterations)
    {

        long[] times = new long[iterations];
        int[] runDelays = new int[iterations];

        for (int j=0;j<iterations;j++){
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
            float seconds = (totalTime/(float)1000)%(float)60.0;
            System.out.println("Time: "+ totalTime+"ms (" + seconds + "s)" );

            int delayx = 0;
            for (int i=0;i<threadCount;i++)
            {
                if (delays[i] > delayx)
                {
                    delayx = delays[i];
                }
            }
            System.out.println("Total Delays: "+ delayx);
            times[j] = totalTime;
            runDelays[j] = delayx;
            l.resetDelay();
        }

        long finaltime = 0;
        int finalDelay = 0;
        for (int k =0;k<iterations;k++)
        {
            finaltime+=times[k];
            finalDelay+=runDelays[k];
        }
        float avgTime = finaltime/(float)iterations;
        double avgDelay = finalDelay/(double)iterations;
       // System.out.println("Number of Iterations: " + iterations);
       // System.out.println("AverageTime: "+ avgTime);
       // System.out.println("AverageDelay: "+ avgDelay);


    }


}