import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by jeffreyng on 2014-09-27.
 */
public class CLHLock implements Lock {

    private final AtomicReference<Node> tail;
    private final ThreadLocal<Node> prev;
    private final ThreadLocal<Node> myNode;
    AtomicInteger acquiredCount;


    public CLHLock()
    {
        tail = new AtomicReference<Node>(new Node());
        myNode = new ThreadLocal<Node>(){
            protected Node initialValue(){
                return new Node();
            }
        };
        prev = new ThreadLocal<Node>();
        acquiredCount = new AtomicInteger(0);
    }

    public void lock()
    {
        Node n = myNode.get();
        n.locked.set(true);
        Node p = tail.getAndSet(n);
        prev.set(p);
        while (p.locked.get()) {}
    }

    public int unlock()
    {
        int t = acquiredCount.getAndIncrement();
        Node n = myNode.get();
        n.locked.set(false);
        myNode.set(prev.get());
        return t;
    }

    public void resetDelay()
    {
        acquiredCount.getAndSet(0);
    }


}
class Node {
   AtomicBoolean locked = new AtomicBoolean(false);
}
