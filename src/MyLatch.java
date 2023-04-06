public class MyLatch {
    private int nWorkers;
    private int nWorkersOnline;

    public MyLatch(int nWorkers) {
        this.nWorkers = nWorkers;
        this.nWorkersOnline = 0;
    }
    public synchronized void reset() {
        nWorkersOnline = 0;
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
    public synchronized void takeThread() {
        nWorkersOnline++;
        notifyAll();
    }
    public synchronized void releaseThread() {
        nWorkersOnline--;
        notifyAll();
    }
}
