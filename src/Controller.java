public class Controller implements Runnable{
    private final MyLatch phaser;
    private final MonitorBufferResult bagOfResult;
    private final MonitorBufferTask bagOfTask;

    private final Thread threadSearcher;

    public Controller(MonitorBufferResult bagOfResult, MyLatch phaser) {
        this.phaser = phaser;
        this.bagOfResult = bagOfResult;

        bagOfTask = new MonitorBufferTask();
        FileSeacher list = new FileSearcherImpl("D:\\Desktop\\prova", bagOfTask, phaser);
        threadSearcher = new Thread(list);
        phaser.takeThread();
        threadSearcher.start();



    }

    @Override
    public void run() {
        while(threadSearcher.isAlive() || !bagOfTask.isEmpty()){
            addThread();

        }
        phaser.releaseThread();

    }
    private synchronized void addThread() {
        if(!bagOfTask.isEmpty()){

            if(threadAvailable()){
                try {
                    new Thread(new FileProcessor(bagOfResult, bagOfTask.getFile(), phaser)).start();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                phaser.takeThread();
            }
        }
    }
    public synchronized boolean threadAvailable() {
        return phaser.getNWorkersOnline()<(phaser.getNWorkers()-1);
    }

}
