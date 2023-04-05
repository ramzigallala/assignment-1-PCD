import java.util.Collection;
import java.util.HashSet;

public class Main2 {

    public static void main(String[] args) {

        Collection<String> listF = new HashSet<>();
        listF.add("ciao");
        listF.add("prova");
        System.out.println(listF);

        if(listF.iterator().hasNext()){
            listF.remove(listF.iterator().next());
        }
        System.out.println(listF);






    }





}
