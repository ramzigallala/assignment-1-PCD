import java.util.ArrayList;
import java.util.List;

public class Main3 {
    public static void main(String[] args) {
        List<Integer> list = new ArrayList<>();

        list.add(5);
        list.add(6);
        System.out.println(list.size()+" "+ list);
        list.remove(list.size()-1);
        System.out.println(list.size()+" "+ list);
        list.add(8);
        System.out.println(list.size()+" "+ list);
    }
}
