import java.util.*;

public class MonitorBufferTask {
    private List<String> listF;
    private boolean searcher= false;

    public MonitorBufferTask() {
        listF = new LinkedList<>();
    }

    public synchronized void putFile(String path){
        listF.add(path);
        notifyAll();
    }

    public synchronized Optional<String> getFile() throws InterruptedException {
        while(listF.isEmpty() && searcher){
            wait();
        }

        if(!listF.isEmpty()){
            String nameFile = listF.get(0);
            listF.remove(0);
            notifyAll();
            return Optional.of(nameFile);
        }
        notifyAll();
        return Optional.empty();
    }
    public synchronized boolean isAvailable(){
        notifyAll();
        return searcher || !listF.isEmpty();
    }
    public synchronized void runningSearcher(){
        notifyAll();
        searcher=true;
    }
    public synchronized void stopSearcher(){
        notifyAll();
        searcher=false;
    }

}
