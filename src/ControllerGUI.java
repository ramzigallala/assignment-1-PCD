import java.io.File;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class ControllerGUI implements Runnable{
    private MyGUI myGUI;
    private MonitorBufferResult bagOfResults;
    private CountDownLatch latch;
    private int numRank;


    public ControllerGUI(MyGUI myGUI, MonitorBufferResult bagOfResults, CountDownLatch latch, int N) {
        this.myGUI = myGUI;
        this.bagOfResults = bagOfResults;
        this.latch = latch;
        this.numRank = N;
    }

    @Override
    public void run() {
        while(latch.getCount()>1 ){
            updateRank();
            updateInterval();
        }
        updateInterval();
        latch.countDown();
        myGUI.getStart().setEnabled(true);
    }

    private void updateInterval() {
        String text = "";
        try {
            for (Long entry: bagOfResults.getListNumberInInterval()) {
                text = text + entry + "\n";
            }
            myGUI.getInterval().setText(text);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    private void updateRank(){
        if(bagOfResults.getNameSubList(numRank).isPresent()){
            List<String> list = bagOfResults.getNameSubList(numRank).get();
            String mom = "";
            for (String entry: list) {
                File file= new File(entry);
                mom = mom + file.getAbsolutePath() + "\n";
            }
            myGUI.getRank().setText(mom);
        }
    }

}
