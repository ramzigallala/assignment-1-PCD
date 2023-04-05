public class Main {

    public static void main(String[] args) {
        int numThread = Runtime.getRuntime().availableProcessors() +1;
        System.out.println("num: "+numThread);
        MyLatch phaser = new MyLatch(numThread);
        MonitorBufferResult monitorResult = new MonitorBufferResult();
        Controller controller = new Controller(monitorResult,phaser);
        Thread th2 = new Thread(controller);
        phaser.takeThread();
        th2.start();
        phaser.join();
        System.out.println("numdfSFFds: "+numThread);
        try {
            for (Pair<String, Long> entry: monitorResult.getListProcessed()) {
                System.out.println(entry.getNameFile()+ " "+ entry.getLineFile());
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }



    }


}
