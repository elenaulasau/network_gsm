import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class BSC extends StationDevice {
    private BscLayer layer;


    public BSC(int number, SmsSystem system, BscLayer layer) {
        super("BSC", number, system);
        this.layer = layer;
    }

    public synchronized void terminate() {
        system.removeBsc(this);
        thread.finish();
        for (SmsMessage sms : messageList) {
            system.bscToNextLayer(sms, layer);
        }
    }

    public List<SmsMessage> getMessageList() {
        return messageList;
    }


    public int getName() {
        return number;
    }


    @Override
    protected int getDelay() {
        int timer = (int) (Math.random() * 5) + 10;
        return timer;
    }

    @Override
    protected void sendOneMessageToSystem(SmsMessage smsMessage) {
        system.bscToNextLayer(smsMessage, layer);
    }
}
