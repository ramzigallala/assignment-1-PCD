import java.io.File;

public class FileSearcher implements Runnable{
    private final File folder;

    private MonitorBufferTask monitor;
    private MyLatch phaser;
    private int indexThread;

    public FileSearcher(String folder, MonitorBufferTask monitor, MyLatch phaser) {
        this.folder = new File(folder);
        this.monitor = monitor;
        this.phaser = phaser;

    }

    public void run(){
        listFiles(this.folder);
        phaser.releaseThread(indexThread);
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
        return path.toLowerCase().endsWith(".java");
    }
    public void setIndexThread(int indexThread) {
        this.indexThread = indexThread;
    }

}
