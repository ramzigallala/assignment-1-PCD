import java.util.TreeSet;

public class MonitorBufferResult {

    private TreeSet<Pair<String, Double>> listProcessed;
    private boolean flagFirst;

    public MonitorBufferResult() {
        flagFirst = true;
        listProcessed = new TreeSet<>();
    }

    public synchronized void putProcessed (Pair<String, Double> obj) throws InterruptedException{
        flagFirst=false;
        listProcessed.add(obj);
        notifyAll();
    }

    public synchronized TreeSet<Pair<String, Double>> getListProcessed() throws InterruptedException {
        while (flagFirst){
            wait();
        }
        notifyAll();
        return listProcessed;
    }
}
