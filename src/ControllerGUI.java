import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.TreeSet;

public class ControllerGUI implements Runnable{
    private MyGUI myGUI;
    private MonitorBufferResult bagOfResults;
    private int last = 5;
    private MyLatch phaser;
    private int numRank;
    private int indexThread;



    public ControllerGUI(Optional<MyGUI> myGUI, MonitorBufferResult bagOfResults, MyLatch phaser, int N) {
        if(myGUI.isPresent()){
            this.myGUI = myGUI.get();
            this.bagOfResults = bagOfResults;
            this.phaser = phaser;
            this.numRank=N;
        }
        this.indexThread=indexThread;


    }

    @Override
    public void run() {
        while(phaser.getNWorkersOnline()>1){
            updateRank();
            updateInterval();
        }
        updateInterval();
        phaser.releaseThread(indexThread);
        myGUI.getStart().setEnabled(true);

    }

    private void updateInterval() {
        String text = "";
        try {
            for (Long entry: bagOfResults.getlistNumberInInterval()) {
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
                mom = mom + file.getName() + "\n";
            }
            myGUI.getRank().setText(mom);
        }
    }

    public void setIndexThread(int indexThread) {
        this.indexThread = indexThread;
    }
}
