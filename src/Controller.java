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
        FileSearcher list = new FileSearcher(D, bagOfTask, phaser);
        threadSearcher = new Thread(list);
        Optional<Integer> indexS = phaser.takeThread(threadSearcher);
        if (indexS.isPresent()){
            list.setIndexThread(indexS.get());
            threadSearcher.start();
        }

    }
    public Controller(MonitorBufferResult bagOfResult, MyLatch phaser, MyGUI myGUI, String D, int N) throws InterruptedException {
        this.phaser = phaser;
        this.bagOfResult = bagOfResult;

        bagOfTask = new MonitorBufferTask();
        FileSearcher list = new FileSearcher(D, bagOfTask, phaser);
        threadSearcher = new Thread(list);
        Optional<Integer> indexS = phaser.takeThread(threadSearcher);
        if (indexS.isPresent()){
            list.setIndexThread(indexS.get());
            threadSearcher.start();
        }
        ControllerGUI controllerGUI= new ControllerGUI(myGUI, bagOfResult, phaser, N);
        Thread threadControllerGUI = new Thread(controllerGUI);
        Optional<Integer> indexC = phaser.takeThread(threadControllerGUI);
        if(indexC.isPresent()){
            controllerGUI.setIndexThread(indexC.get());
            threadControllerGUI.start();
        }

    }

    @Override
    public void run() {
        while(threadSearcher.isAlive() || !bagOfTask.isEmpty()){
            addThread();
        }
        phaser.releaseThread(indexThread);
    }
    private void addThread() {
        try {
            FileProcessor fileProcessor = new FileProcessor(bagOfResult, bagOfTask.getFile(), phaser);
            Thread th = new Thread(fileProcessor);
            Optional<Integer> index = phaser.takeThread(th);
            if (index.isPresent()) {
                fileProcessor.setIndexThread(index.get());
                th.start();
            }
        } catch (InterruptedException e) {

        }
    }

    public void setIndexThread(int indexThread) {
        this.indexThread = indexThread;
    }
}
