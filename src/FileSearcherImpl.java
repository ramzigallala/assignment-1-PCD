import java.io.File;

public class FileSearcherImpl implements FileSeacher{
    private final File folder;

    private MonitorBufferTask monitor;

    public FileSearcherImpl(String folder, MonitorBufferTask monitor) {
        this.folder = new File(folder);
        this.monitor = monitor;

    }

    public void run(){
        listFiles(this.folder);
    }

    private void listFiles(final File folder){
        for (File entry : folder.listFiles()){
            if(entry.isDirectory()){
                listFiles(entry);
            }else{
                monitor.putFile(entry.getPath());
            }
        }
    }


}
