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
    public synchronized Optional<Integer> takeThread(Thread thread) throws InterruptedException {
        if(nWorkersOnline>=nWorkers){
            wait();
        }
        Optional<Integer> index = getIndexEmpty();
        if (index.isPresent()){
            nWorkersOnline++;
            listThread.set(index.get(), Optional.of(thread));
            //System.out.println("preso "+index);
            notifyAll();
            return index;
        }
        return Optional.empty();

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
        listThread.get(2).get().interrupt();
        for (Optional<Thread> entry : listThread) {
            //entry.ifPresent(Thread::interrupt);
            if(!entry.isEmpty())
                entry.get().interrupt();
        }
        reset();
    }
}
