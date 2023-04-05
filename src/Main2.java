import java.util.Collection;
import java.util.HashSet;
import java.util.concurrent.Phaser;

public class Main2 {

    public static void main(String[] args) {

        Phaser phaser = new Phaser(2);
        phaser.arriveAndDeregister();

        phaser.arriveAndAwaitAdvance();
        System.out.println(phaser);









    }





}
