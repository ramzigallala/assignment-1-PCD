import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.EventListener;
import java.util.Optional;

public class MyGUI extends JFrame {
    public static final int NUM_THREAD = Runtime.getRuntime().availableProcessors() + 1;
    private Button start;
    private Button stop;
    private TextArea rank;
    private TextArea interval;

    public MyGUI () {
        setTitle("Assigment 1");
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        inputPanel("maxL");
        inputPanel("NL");
        inputPanel("D");
        startStopPanel();
        viewResultPanel();

        setVisible(true);         // "super" Frame shows
    }

    private void inputPanel(String name){
        JPanel panel = new JPanel(new GridLayout(0,4));
        Label label = new Label(name);
        TextField textField = new TextField();
        panel.add(label);
        panel.add(textField);
        add(panel);
    }
    private void startStopPanel(){
        JPanel panel = new JPanel(new GridLayout(2,1, 20, 20));
        start = new Button("start");
        start.addActionListener(startListener());
        stop = new Button("stop");
        panel.add(start);
        panel.add(stop);
        add(panel);
    }

    private ActionListener startListener() {
        return e -> {
            MyLatch phaser = new MyLatch(NUM_THREAD);
            MonitorBufferResult monitorResult = new MonitorBufferResult();
            Controller controller = new Controller(monitorResult,phaser, Optional.of(this));
            Thread th = new Thread(controller);
            phaser.takeThread();
            th.start();
            stop.setEnabled(false);
        };
    }

    private void viewResultPanel() {
        JPanel panel = new JPanel(new GridLayout(1,2, 10,10));
        rank = new TextArea();
        rank.setEnabled(false);
        interval = new TextArea();
        interval.setEnabled(false);
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
}
