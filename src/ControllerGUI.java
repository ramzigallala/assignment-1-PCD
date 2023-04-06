import java.io.File;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.TreeSet;

public class ControllerGUI implements Runnable{
    private MyGUI myGUI;
    private MonitorBufferResult bagOfResults;
    private boolean flagAvailable;
    private int last = 5;
    private MyLatch phaser;

    public ControllerGUI(Optional<MyGUI> myGUI, MonitorBufferResult bagOfResults, MyLatch phaser) {
        if(myGUI.isPresent()){
            this.myGUI = myGUI.get();
            this.bagOfResults = bagOfResults;
            this.phaser = phaser;
        }


    }

    @Override
    public void run() {
        while(phaser.getNWorkersOnline()>1){
            updateRank();
        }

    }

    private void updateRank(){
        if(bagOfResults.getNameSubList(5).isPresent()){
            List<String> list = bagOfResults.getNameSubList(5).get();
            String mom = "";
            for (String entry: list) {
                File file= new File(entry);
                mom = mom + file.getName() + "\n";
            }
            myGUI.getRank().setText(mom);
        }
    }
}
