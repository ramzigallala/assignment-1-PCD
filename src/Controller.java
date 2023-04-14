import java.util.Optional;
import java.util.concurrent.CountDownLatch;

public class Controller implements Runnable{
    private final CountDownLatch latch;
    private final MonitorBufferResult bagOfResult;
    private final MonitorBufferTask bagOfTask;
    private final Thread threadSearcher;
    private long threadCount;
    private Flag stopThread;

    public Controller(MonitorBufferResult bagOfResult, CountDownLatch latch, String D) throws InterruptedException {
        this.latch = latch;
        this.bagOfResult = bagOfResult;
        bagOfTask = new MonitorBufferTask();
        this.stopThread = new Flag();
        this.threadCount = latch.getCount()-2;//controller, searcher
        FileSearcher list = new FileSearcher(D, bagOfTask, latch, stopThread);
        threadSearcher = new Thread(list);
        bagOfTask.runningSearcher();
        threadSearcher.start();



    }
    public Controller(MonitorBufferResult bagOfResult, CountDownLatch latch, String D, Flag stopThread) throws InterruptedException {
        this.latch = latch;
        this.bagOfResult = bagOfResult;
        bagOfTask = new MonitorBufferTask();
        this.stopThread = new Flag();
        this.threadCount = latch.getCount()-3; //controller, searcher, controllerGUI
        FileSearcher list = new FileSearcher(D, bagOfTask, latch, stopThread);
        threadSearcher = new Thread(list);
        bagOfTask.runningSearcher();
        threadSearcher.start();
        this.stopThread = stopThread;
    }

    @Override
    public void run() {
        while(threadCount>0 && stopThread.getFlag()){
            FileProcessor fileProcessor = new FileProcessor(bagOfResult, bagOfTask, latch, stopThread);
            Thread th = new Thread(fileProcessor);
            th.start();
            threadCount--;
        }
        latch.countDown();
    }
}
