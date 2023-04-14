import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;

public class MainConsole {

    public static final String FOLDER = "D:\\Desktop\\PCD\\TestFolder2";
    public static final int NUM_THREAD = Runtime.getRuntime().availableProcessors() + 1;

    public static void main(String[] args) {
        int maxLine;
        int interval;
        int rank;

        Scanner scanner = new Scanner(System.in);
        System.out.print("MAXL: ");
        maxLine = scanner.nextInt();
        System.out.println();
        System.out.print("Interval: ");
        interval = scanner.nextInt();
        System.out.println();
        System.out.print("Rank: ");
        rank = scanner.nextInt();
        System.out.println();


        Chrono chrono = new Chrono();
        CountDownLatch latch = new CountDownLatch(NUM_THREAD);
        MonitorBufferResult monitorResult = new MonitorBufferResult(maxLine, interval);

        try {
            Controller controller = new Controller(monitorResult,latch, FOLDER);
            Thread th2 = new Thread(controller);
            chrono.start();
            th2.start();


        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }


        try {
            latch.await();
            chrono.stop();

            final List<Pair<String,Long>> list;
            try {
                list = monitorResult.getListProcessed().stream().toList();
                for(int i = 0; i< rank; i++) System.out.println(list.get(i).getFirst()+" "+ list.get(i).getSecond());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            int range = maxLine/interval;
            List<Long> listNumberInInterval = new ArrayList<>();
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
                        } else if(entry.getSecond()>=maxLine) {
                            listNumberInInterval.set(interval,listNumberInInterval.get(interval)+1);
                            i = interval;
                        }
                        min = max;
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
            System.out.println("time: " + chrono.getTime());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
