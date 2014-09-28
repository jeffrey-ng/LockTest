/**
 * Created by jeffreyng on 2014-09-26.
 */
public interface Lock {


    void lock();

    int unlock();

    void resetDelay();
}
