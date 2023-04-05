public class FileProcessor implements Runnable{

    private MonitorBufferResult bagOfResult;
    private String nameFile;

    private Controller controller;

    public FileProcessor(MonitorBufferResult bagOfResult, String nameFile, Controller controller) {
        this.bagOfResult = bagOfResult;
        this.nameFile = nameFile;
        this.controller = controller;
    }

    @Override
    public void run() {



        //bagOfResult.putProcessed(new Pair<>(nameFile,100.0));
        this.controller.addNumThread();
        System.out.println(nameFile);




    }
}
