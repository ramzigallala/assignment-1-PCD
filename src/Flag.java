public class Flag {
    private boolean stopFlag;

    public Flag() {
        stopFlag=true;
    }

    public synchronized void stopThread(){
        stopFlag=false;
    }

    public synchronized boolean getFlag() {
        return this.stopFlag;
    }
}
