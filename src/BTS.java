import java.time.Instant;
import java.util.ArrayList;
import java.util.List;


public class BTS extends StationDevice {
    private boolean isRecipient;


    public BTS(int number, SmsSystem system, boolean isRecipient) {
        super("BTS", number, system);
        this.isRecipient = isRecipient;

    }

    public boolean isRecipient() {
        return isRecipient;
    }

    public synchronized void terminate() {
        thread.finish();
        for (SmsMessage sms : messageList) {
            sendOneMessageToSystem(sms);
        }

        system.removeBts(this);
    }

    @Override
    protected int getDelay() {
        return 3;
    }

    @Override
    protected void sendOneMessageToSystem(SmsMessage smsMessage) {
        if (isRecipient)
            system.BtsToVrd(smsMessage);
        else
            system.BtsToBsc(smsMessage);
    }
}


