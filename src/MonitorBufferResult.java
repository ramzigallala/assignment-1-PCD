import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.TreeSet;

public class MonitorBufferResult {

    private final TreeSet<Pair<String, Long>> listProcessed;
    private final int maxLine;
    private final int interval;
    private final int range;
    private ArrayList<Long> listNumberInInterval;
    private boolean flagFirst;

    public MonitorBufferResult(int maxL, int NL) {
        flagFirst = true;
        listProcessed = new TreeSet<>((o1, o2) -> {
            if (o1.getSecond() < o2.getSecond()) return 1;
            return -1;
        });
        maxLine= maxL;
        interval = NL;
        range = maxLine/interval;
        listNumberInInterval = new ArrayList<>();

        for (int i=0;i<=interval;i++)listNumberInInterval.add(0L);
    }

    public synchronized void putProcessed (Pair<String, Long> obj) throws InterruptedException{
        flagFirst=false;
        listProcessed.add(obj);
        int index =0;
        if(getIndex(obj.getSecond()).isPresent()){
            index = getIndex(obj.getSecond()).get();
            listNumberInInterval.set(index, listNumberInInterval.get(index)+1);
        }
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
        if(listProcessed.size()>=i){
            return Optional.of(listProcessed.stream().map(Pair::getFirst).toList().subList(0,i));
        }
        return Optional.empty();
    }

    private synchronized Optional<Integer> getIndex(Long num){
        int min=0;
        int max =0;
        for(int i=0; i<interval; i++){
            max= (range+min);
            if(num>=min && num<max) {
                return Optional.of(i);
            } else if(num>=maxLine) {
                return Optional.of(interval);
            }
            min = max;
        }
        return Optional.empty();

    }

    public synchronized List<Long> getlistNumberInInterval() throws InterruptedException {
        while (flagFirst){
            wait();
        }
        return this.listNumberInInterval;
    }
}
