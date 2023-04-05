public class Main {
    ;
    public static void main(String[] args) {
        int numThread=5;
        /*
        try {
            th.join();
            if(!th.isAlive()){
                for (String file : monitor.getListF()){
                    System.out.println(file);
                }
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
*/
        MonitorBufferResult monitorResult = new MonitorBufferResult();
        numThread = 4;
        Controller controller = new Controller(monitorResult,numThread);
        Thread th2 = new Thread(controller);
        th2.start();






    }


}
