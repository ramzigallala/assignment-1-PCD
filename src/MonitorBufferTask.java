import java.util.*;

public class MonitorBufferTask {
    private Collection<String> listF;

    public MonitorBufferTask() {
        listF = new HashSet<String>();
    }

    public synchronized void putFile(String path){
        listF.add(path);
        notifyAll();
    }

    public synchronized String getFile() throws InterruptedException {
        while(isEmpty()){
            wait();
        }

        String nameFile = listF.iterator().next();
        listF.remove(nameFile);
        notifyAll();
        return nameFile;
    }
    public synchronized boolean isEmpty(){
        return listF.isEmpty();
    }

}
