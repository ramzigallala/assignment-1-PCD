import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MyLatch {
    private int nWorkers;
    private int nWorkersOnline;

    private List<Optional<Thread>> listThread;


    public MyLatch(int nWorkers) {
        this.nWorkers = nWorkers;
        this.nWorkersOnline = 0;
        listThread = new ArrayList<>();
        for(int i=0; i<nWorkers; i++) listThread.add(Optional.empty());

    }
    public synchronized void reset() {
        nWorkersOnline = 0;
        listThread.replaceAll(entry -> Optional.empty());
    }
    public synchronized void join() {
        while (nWorkersOnline !=0) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public synchronized int getNWorkersOnline(){
        return this.nWorkersOnline;
    }
    public synchronized int getNWorkers(){
        return this.nWorkers;
    }
    public synchronized int takeThread(Thread thread) throws InterruptedException {
        if(!(nWorkersOnline<(nWorkers-1))){
            wait();
        }
        nWorkersOnline++;
        int index = getIndexEmpty().get();
        listThread.set(index, Optional.of(thread));
        notifyAll();
        return index;

        /*
        try {
            throw new Exception("No index for thread");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
*/
    }

    private synchronized Optional<Integer> getIndexEmpty() {
        for (Optional<Thread> entry: listThread) {
            if(entry.isEmpty()){
                return Optional.of(listThread.indexOf(entry));
            }
        }
        return Optional.empty();
    }

    public synchronized void releaseThread(int index) {
        nWorkersOnline--;
        listThread.set(index,Optional.empty());
        notifyAll();
    }

    public synchronized void stopThread() {
        for (Optional<Thread> entry : listThread) {
            entry.ifPresent(Thread::interrupt);
        }
        reset();
    }
}
