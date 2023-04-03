public class Main {
    public static void main(String[] args) {
        MyMonitorBuffer monitor = new MyMonitorBuffer();
        FileSeacher list = new FileSearcherImpl("D:\\Desktop\\test", monitor);
        Thread th = new Thread(list);
        th.start();
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



    }


}
