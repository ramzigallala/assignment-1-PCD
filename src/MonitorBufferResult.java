import java.util.Comparator;
import java.util.TreeSet;

public class MonitorBufferResult {

    private final TreeSet<Pair<String, Long>> listProcessed;
    private boolean flagFirst;

    public MonitorBufferResult() {
        flagFirst = true;
        listProcessed = new TreeSet<>((o1, o2) -> {
            if (o1.getLineFile() > o2.getLineFile()) return 1;
            return -1;
        });
    }

    public synchronized void putProcessed (Pair<String, Long> obj) throws InterruptedException{
        flagFirst=false;
        listProcessed.add(obj);
        notifyAll();
    }

    public synchronized TreeSet<Pair<String, Long>> getListProcessed() throws InterruptedException {
        while (flagFirst){
            wait();
        }
        notifyAll();
        return listProcessed;
    }
}
