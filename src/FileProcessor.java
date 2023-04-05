import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.concurrent.Phaser;

public class FileProcessor implements Runnable{

    private final MonitorBufferResult bagOfResult;
    private final Path nameFile;

    private final MyLatch phaser;

    public FileProcessor(MonitorBufferResult bagOfResult, String nameFile, MyLatch phaser) {
        this.bagOfResult = bagOfResult;
        this.nameFile = Path.of(nameFile);
        this.phaser = phaser;
    }

    @Override
    public void run() {


        try {
            bagOfResult.putProcessed(new Pair<>(nameFile.toString(), getNumLines()));
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        phaser.releaseThread();


    }
    private Long getNumLines() throws IOException {
        return Files.lines(nameFile).count();
    }
}
