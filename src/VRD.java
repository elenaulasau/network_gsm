public class VRD extends Device{
    private int received = 0;
    private boolean clean = false;
    private Thread thread;
    private boolean running = true;

    public VRD(int number, SmsSystem system) {
        super(number, system);
        thread = new Thread(() -> {
            try {
                while (running) {
                    Thread.sleep(1000 * 10);
                    if (clean){
                        received = 0;
                        system.getVrdReceiveEventListener().fire(new VrdReceiveEvent(system, this));
                    }

                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        thread.start();
    }



    public int getReceived() {
        return received;
    }

    public void terminate(){
        running = false;
    }

    public void setClean(boolean clean) {
        this.clean = clean;
    }

    public int getNumber() {
        return number;
    }
    public synchronized void receiveMessage(){
        System.out.println();
        System.out.println("Received");
        received++;



        System.out.println("VRD" + getNumber() + " received" + received + "messages");
        System.out.println();
    }
}
