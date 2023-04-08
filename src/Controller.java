import java.util.Optional;

public class Controller implements Runnable{
    private final MyLatch phaser;
    private final MonitorBufferResult bagOfResult;
    private final MonitorBufferTask bagOfTask;
    private final Thread threadSearcher;

    private int indexThread;

    public Controller(MonitorBufferResult bagOfResult, MyLatch phaser, String D) throws InterruptedException {
        this.phaser = phaser;
        this.bagOfResult = bagOfResult;

        bagOfTask = new MonitorBufferTask();
        FileSeacher list = new FileSearcherImpl(D, bagOfTask, phaser);
        threadSearcher = new Thread(list);
        phaser.takeThread(threadSearcher);
        threadSearcher.start();
    }
    public Controller(MonitorBufferResult bagOfResult, MyLatch phaser, Optional<MyGUI> myGUI, String D, int N) throws InterruptedException {
        this.phaser = phaser;
        this.bagOfResult = bagOfResult;

        bagOfTask = new MonitorBufferTask();
        FileSeacher list = new FileSearcherImpl(D, bagOfTask, phaser);
        threadSearcher = new Thread(list);
        int indexS = phaser.takeThread(threadSearcher).get();
        list.setIndexThread(indexS);
        threadSearcher.start();
        ControllerGUI controllerGUI= new ControllerGUI(myGUI, bagOfResult, phaser, N);
        Thread threadControllerGUI = new Thread(controllerGUI);
        int indexC = phaser.takeThread(threadControllerGUI).get();
        controllerGUI.setIndexThread(indexC);
        threadControllerGUI.start();
    }

    @Override
    public void run() {
        while(threadSearcher.isAlive() || !bagOfTask.isEmpty()){
            addThread();
        }
        phaser.releaseThread(indexThread);
    }
    private synchronized void addThread() {
        if(!bagOfTask.isEmpty()){
            //if(threadAvailable()){
                try {

                    FileProcessor fileProcessor = new FileProcessor(bagOfResult, bagOfTask.getFile(), phaser);
                    Thread th = new Thread(fileProcessor);
                    Optional<Integer> index = phaser.takeThread(th);
                    if(index.isPresent()){
                        fileProcessor.setIndexThread(index.get());
                        th.start();
                    }

                } catch (InterruptedException e) {

                }

            //}
        }
    }
    public synchronized boolean threadAvailable() {
        return phaser.getNWorkersOnline()<(phaser.getNWorkers()-1);
    }

    public void setIndexThread(int indexThread) {
        this.indexThread = indexThread;
    }
}
