import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;

public class FileSearcher implements Runnable{
    private final File folder;
    private MonitorBufferTask monitor;
    private CountDownLatch latch;
    private Flag stopThread;

    public FileSearcher(String folder, MonitorBufferTask monitor, CountDownLatch latch,Flag stopThread) {
        this.folder = new File(folder);
        this.monitor = monitor;
        this.latch = latch;
        this.stopThread=stopThread;
    }

    public void run(){
        listFiles(this.folder);
        monitor.stopSearcher();
        latch.countDown();
    }


    private void listFiles(final File folder){
        if(stopThread.getFlag()){
            for (File entry : folder.listFiles()){
                if(stopThread.getFlag()){
                    if(entry.isDirectory()){
                        listFiles(entry);
                    }else{
                        if(isFileJava(entry.getPath())) monitor.putFile(entry.getPath());
                    }
                }

            }
        }
    }

    private boolean isFileJava(String path){
        return path.toLowerCase().endsWith(".java");
    }

}
