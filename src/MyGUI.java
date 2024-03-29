import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.concurrent.CountDownLatch;

public class MyGUI extends JFrame {
    public static final int NUM_THREAD = Runtime.getRuntime().availableProcessors() + 1;
    private Button start;
    private Button stop;
    private TextArea rank;
    private TextArea interval;
    private TextField maxL, NL, D, numRank;
    private CountDownLatch latch;
    private Flag stopThread;

    public MyGUI () {
        setTitle("Assigment 1");
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        maxL = new TextField();
        NL = new TextField();
        D = new TextField("D:\\Desktop\\PCD\\TestFolder2");
        numRank = new TextField();
        inputPanel("maxL");
        inputPanel("NL");
        inputPanel("Directory");
        inputPanel("numRank");
        startStopPanel();
        viewResultPanel();

        setVisible(true);
    }

    private void inputPanel(String name){
        JPanel panel = new JPanel(new GridLayout(0,4));
        Label label = new Label(name);
        panel.add(label);
        switch (name) {
            case "maxL" -> panel.add(maxL);
            case "NL" -> panel.add(NL);
            case "Directory" -> panel.add(D);
            case "numRank" -> panel.add(numRank);
        }
        add(panel);
    }

    private void startStopPanel(){
        JPanel panel = new JPanel(new GridLayout(2,1, 20, 20));
        start = new Button("start");
        start.addActionListener(startListener());
        stop = new Button("stop");
        stop.addActionListener(stopListener());
        stop.setEnabled(false);
        panel.add(start);
        panel.add(stop);
        add(panel);
    }



    private ActionListener stopListener() {
        return e -> {
            stopThread.stopThread();
            start.setEnabled(true);
            stop.setEnabled(false);
        };
    }



    private ActionListener startListener() {
        return e -> {
            try {
                stopThread = new Flag();
                latch = new CountDownLatch(NUM_THREAD);
                MonitorBufferResult monitorResult = new MonitorBufferResult(Integer.parseInt(maxL.getText()), Integer.parseInt(NL.getText()));
                ControllerGUI controllerGUI = new ControllerGUI(this, monitorResult, latch, Integer.parseInt(numRank.getText()));
                Thread threadControllerGUI = new Thread(controllerGUI);
                threadControllerGUI.start();
                Controller controller = new Controller(monitorResult,latch, D.getText(), stopThread);
                Thread threadController = new Thread(controller);
                threadController.start();

            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
            start.setEnabled(false);
            stop.setEnabled(true);
        };
    }

    private void viewResultPanel() {
        JPanel panel = new JPanel(new GridLayout(1,2, 10,10));
        rank = new TextArea();
        rank.setEditable(false);
        interval = new TextArea();
        interval.setEditable(false);
        panel.add(rank);
        panel.add(interval);
        add(panel);
    }

    public TextArea getRank() {
        return this.rank;
    }

    public TextArea getInterval() {
        return this.interval;
    }

    public Button getStart() {
        return this.start;
    }
}
