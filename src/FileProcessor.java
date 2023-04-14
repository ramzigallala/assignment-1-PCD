import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Phaser;

public class FileProcessor implements Runnable{

    private final MonitorBufferResult bagOfResult;
    private final MonitorBufferTask bagOfTask;
    private final CountDownLatch latch;
    private Flag stopFlag;

    public FileProcessor(MonitorBufferResult bagOfResult, MonitorBufferTask bagOfTask, CountDownLatch latch, Flag stopThread) {
        this.bagOfResult = bagOfResult;
        this.bagOfTask = bagOfTask;
        this.latch = latch;
        this.stopFlag=stopThread;
    }

    @Override
    public void run() {

        while(bagOfTask.isAvailable() && stopFlag.getFlag()){
            try {
                updateBagOfResult();
            } catch (InterruptedException | IOException e) {
                throw new RuntimeException(e);
            }
        }
        latch.countDown();
    }
    private void updateBagOfResult() throws InterruptedException, IOException {

        Optional<String> name = bagOfTask.getFile();
        if(name.isPresent()){
            Long numLines= getNumLines(Path.of(name.get()));
            bagOfResult.putProcessed(new Pair<>(name.get(), numLines));
        }

    }

    private Long getNumLines(Path path) throws IOException {
        return Files.lines(path).count();
    }

}
