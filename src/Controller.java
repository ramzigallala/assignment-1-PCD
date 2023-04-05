import com.sun.source.tree.WhileLoopTree;

public class Controller implements Runnable{
    //TODO numThread trasformare in un phaser
    private int numThread;
    private MonitorBufferResult bagOfResult;
    private MonitorBufferTask bagOfTask;

    private Thread th;

    public Controller(MonitorBufferResult bagOfResult, int numThread) {
        this.numThread = numThread;
        this.bagOfResult = bagOfResult;

        bagOfTask = new MonitorBufferTask();
        FileSeacher list = new FileSearcherImpl("D:\\Desktop\\prova", bagOfTask);
        th = new Thread(list);
        th.start();
        decNumThread();

    }

    @Override
    public void run() {

        while(th.isAlive() || !bagOfTask.isEmpty()){
            System.out.println(bagOfTask.isEmpty());

            addThread();

        }
        System.out.println("fuori");

    }
    private synchronized void addThread() {
        if(!bagOfTask.isEmpty()){
            if(getNumThread()!=0){
                try {
                    new Thread(new FileProcessor(bagOfResult, bagOfTask.getFile(), this)).start();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                decNumThread();
            }

        }
    }
    public synchronized int getNumThread() {
        return this.numThread;
    }

    public synchronized void decNumThread() {
        this.numThread--;
    }

    public synchronized void addNumThread() {
        this.numThread++;

    }

}
