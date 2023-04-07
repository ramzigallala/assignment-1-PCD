import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        int numThread = Runtime.getRuntime().availableProcessors() +1;
        MyLatch phaser = new MyLatch(numThread);
        MonitorBufferResult monitorResult = new MonitorBufferResult(100, 4);

        try {
            Controller controller = new Controller(monitorResult,phaser, "D:\\Desktop\\PCD\\TestFolder2");
            Thread th2 = new Thread(controller);
            phaser.takeThread(th2);
            th2.start();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        phaser.join();

        final List<Pair<String,Long>> list;
        try {
            list = monitorResult.getListProcessed().stream().toList();
            for(int i = 0; i<5; i++) System.out.println(list.get(i).getFirst()+" "+ list.get(i).getSecond());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        int maxLine= 100;
        int interval = 4;
        
        
        
        int range = maxLine/interval;
        List<Long> listNumberInInterval = new ArrayList<>();
        boolean flag = true;
        for (int i=0;i<=interval;i++)listNumberInInterval.add(0L);
        try {
            for (Pair<String,Long> entry: monitorResult.getListProcessed()) {
                int min=0;
                int max =0;
                for(int i=0; i<interval; i++){
                    max= (range+min);
                    if(entry.getSecond()>=min && entry.getSecond()<max) {
                        listNumberInInterval.set(i, listNumberInInterval.get(i)+1);
                        i=interval;
                        flag = false;
                    } else if(entry.getSecond()>=maxLine) {
                        listNumberInInterval.set(interval,listNumberInInterval.get(interval)+1);
                        i = interval;
                        flag = false;
                    }
                    min = max;
                }

                if(flag){

                    System.out.println(entry.getSecond());
                    flag = true;
                }
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        int total = 0;
        for (Long entry: listNumberInInterval) {
            System.out.println(entry);
            total = total + entry.intValue();
        }
        System.out.println("total: "+total);
        try {
            System.out.println(monitorResult.getListProcessed().size());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }


        //System.out.println("numdfSFFds: "+numThread);
        /*
        try {
            for (Pair<String, Long> entry: monitorResult.getListProcessed()) {
                System.out.println(entry.getNameFile()+ " "+ entry.getLineFile());
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
*/


    }


}
