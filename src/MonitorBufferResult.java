import java.util.List;
import java.util.Optional;
import java.util.TreeSet;

public class MonitorBufferResult {

    private final TreeSet<Pair<String, Long>> listProcessed;
    private boolean flagFirst;

    public MonitorBufferResult() {
        flagFirst = true;
        listProcessed = new TreeSet<>((o1, o2) -> {
            if (o1.getLineFile() < o2.getLineFile()) return 1;
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


    public synchronized Optional<List<String>> getNameSubList(int i) {
        if(listProcessed.size()>=5){
            return Optional.of(listProcessed.stream().map(Pair::getNameFile).toList().subList(0,i));
        }
        return Optional.empty();
    }
}
