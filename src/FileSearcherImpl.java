import java.io.File;
import java.util.HashSet;
import java.util.Set;

public class FileSearcherImpl implements FileSeacher{
    private final File folder;
    private Set<String> listF;

    public FileSearcherImpl(String folder) {
        this.folder = new File(folder);
        listF = new HashSet<>();
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

                listF.add(entry.getPath());
            }
        }
    }

    public Set<String> getListF() {
        return this.listF;
    }
}
