import java.util.*;

public class MonitorBufferTask {


    private int count;
    private Collection<String> listF;

    public MonitorBufferTask() {

        listF = new HashSet<String>();
    }

    public synchronized void putFile(String path){

        count++;
        listF.add(path);
        notifyAll();
    }

    public synchronized String getFile() throws InterruptedException {
        while(isEmpty()){
            wait();
        }

        String nameFile = listF.iterator().next();
        listF.remove(nameFile);
        count--;
        notifyAll();
        return nameFile;

    }



    public synchronized Collection<String> getListF() throws InterruptedException {
        if(isEmpty()) wait();
        return listF;

    }

    public synchronized boolean isEmpty(){
        return count == 0;
    }



}
