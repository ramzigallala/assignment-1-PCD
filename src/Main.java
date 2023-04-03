public class Main {
    public static void main(String[] args) {
        FileSeacher list = new FileSearcherImpl("D:\\Desktop\\test");
        Thread th = new Thread(list);
        th.start();
        try {
            th.join();
            if(!th.isAlive()){
                for (String file : list.getListF()){
                    System.out.println(file);
                }
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }



    }


}
