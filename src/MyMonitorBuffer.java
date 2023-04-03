import java.util.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MyMonitorBuffer {

    //TODO change count with a flag
    private int count, first, size;
    private boolean flagFirst;
    private TreeSet<Pair<String, Double>> listProcessed;
    private Collection<String> listF;
    private Lock mutexProcessed, mutexFileReader;
    private Condition notEmptyListProcessed, notEmptyListFile;

    public MyMonitorBuffer() {
        size = first = count = 0;
        flagFirst = true;
        listProcessed = new TreeSet<>();
        listF = new HashSet<String>();
        mutexProcessed = new ReentrantLock();
        mutexFileReader = new ReentrantLock();
        notEmptyListProcessed = mutexProcessed.newCondition();
        notEmptyListFile = mutexFileReader.newCondition();
    }

    public void putProcessed(Pair<String, Double> obj){
        try {
            mutexProcessed.lock();
            flagFirst=false;
            listProcessed.add(obj);
            notEmptyListProcessed.signal();
        }finally {
            mutexProcessed.unlock();
        }

    }

    public void putFile(String path){
        try {
            mutexFileReader.lock();
            count++;
            listF.add(path);
            notEmptyListFile.signal();
        }finally {
            mutexFileReader.unlock();
        }

    }

    public String getFile() throws InterruptedException {
        try {
            mutexFileReader.lock();
            while(count==0){
                notEmptyListFile.await();
            }
            String mom = listF.toArray()[listF.size()].toString();
            listF.remove(listF.stream().findFirst());
            return mom;
        }finally {
            mutexFileReader.unlock();
        }
    }

    public TreeSet<Pair<String, Double>> getListProcessed() throws InterruptedException {
        try {
            mutexProcessed.lock();
            while (flagFirst){
                notEmptyListProcessed.await();
            }
            return listProcessed;
        }finally {
            mutexProcessed.unlock();
        }

    }

    public Collection<String> getListF() {
        try{
            mutexFileReader.lock();
            return this.listF;
        }finally {
            mutexFileReader.unlock();
        }

    }
/*
    public boolean isEmptyListProcessed() throws InterruptedException{
        try{
            mutexProcessed.lock();
            return flagFirst;
        }finally {
            mutexProcessed.unlock();
        }
    }
*/

}
