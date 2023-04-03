import java.io.File;
import java.util.HashSet;
import java.util.Set;

public class FileSearcherImpl implements FileSeacher{
    private final File folder;

    private MyMonitorBuffer monitor;

    public FileSearcherImpl(String folder, MyMonitorBuffer monitor) {
        this.folder = new File(folder);
        this.monitor = monitor;
        listFiles(this.folder);
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
