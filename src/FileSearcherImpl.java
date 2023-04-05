import java.io.File;
import java.util.concurrent.Phaser;

public class FileSearcherImpl implements FileSeacher{
    private final File folder;

    private MonitorBufferTask monitor;
    private MyLatch phaser;
    public FileSearcherImpl(String folder, MonitorBufferTask monitor, MyLatch phaser) {
        this.folder = new File(folder);
        this.monitor = monitor;
        this.phaser = phaser;

    }

    public void run(){
        listFiles(this.folder);
        phaser.releaseThread();
    }

    private void listFiles(final File folder){
        for (File entry : folder.listFiles()){
            if(entry.isDirectory()){
                listFiles(entry);
            }else{
                if(isFileJava(entry.getPath())) monitor.putFile(entry.getPath());
            }
        }
    }

    private boolean isFileJava(String path){
        return path.toLowerCase().endsWith(".txt");
    }


}
