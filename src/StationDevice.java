import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public abstract class StationDevice extends Device {
    protected List<SmsMessage> messageList = new ArrayList<>();
    protected StationThread thread;

    public StationDevice(String name, int number, SmsSystem system) {
        super(number, system);
        thread = new StationThread(name + number, this);
        thread.start();
    }



    protected abstract void sendOneMessageToSystem(SmsMessage message);

    public synchronized void receiveMessage(SmsMessage smsMessage) {
        smsMessage.timeToSend(getDelay());
        messageList.add(smsMessage);
    }

    protected abstract int getDelay();

    public int getNumberOfMessages() {
        return messageList.size();
    }

    public List<SmsMessage> getMessageList() {
        return messageList;
    }

    public void send() {
        if (messageList.size() == 0)
            return;
        List<SmsMessage> toSend = new ArrayList<>();
        synchronized (this) {
            for (int i = 0; i < messageList.size(); i++) {
                if (messageList.get(i).getTimeToSend().isBefore(Instant.now())) {
                    toSend.add(messageList.get(i));
                }
            }
            for (SmsMessage sms : toSend) {
                messageList.remove(sms);
            }
        }

        for (SmsMessage smsMessage : toSend) {
            sendOneMessageToSystem(smsMessage);
        }
    }
}

class StationThread extends Thread {
    private boolean running = true;
    private StationDevice stationDevice;

    public StationThread(String name, StationDevice stationDevice) {
        this.setName(name);
        this.stationDevice = stationDevice;
    }

    @Override
    public void run() {
        while (running) {
            sendMessages();
        }
    }

    private void sendMessages() {
        try {
            Thread.sleep(100);
            stationDevice.send();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void finish() {
        running = false;
    }
}