import java.util.ArrayList;
import java.util.List;

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

        final List<Pair<String,Long>> list;
        try {
            list = monitorResult.getListProcessed().stream().toList();
            for(int i = 0; i<5; i++) System.out.println(list.get(i).getNameFile()+" "+ list.get(i).getLineFile());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        int maxLine= 100;
        int interval = 4;
        
        
        
        int range = maxLine/interval;
        List<Long> listNumberInInterval = new ArrayList<>();
        for (int i=0;i<=interval;i++)listNumberInInterval.add(0L);
        try {
            for (Pair<String,Long> entry: monitorResult.getListProcessed()) {
                int min=0;
                int max =0;
                for(int i=0; i<interval; i++){
                    max= (range+min)-1;
                    if(entry.getLineFile()>=min && entry.getLineFile()<max) listNumberInInterval.set(i, listNumberInInterval.get(i)+1);

                    if(entry.getLineFile()>=maxLine) {
                        listNumberInInterval.set(interval,listNumberInInterval.get(interval)+1);
                        i = interval;
                    }

                    min = max+1;
                }
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        for (Long entry: listNumberInInterval) {
            System.out.println(entry);
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
