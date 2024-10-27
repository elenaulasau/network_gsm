import java.time.Instant;

public class SmsMessage {
    private byte[] message;
    private int sender, recipient;

    private Instant timeToSend;



    public SmsMessage(byte[] message, int sender, int recipient){
        this.message = message;
        this.sender = sender;
        this.recipient = recipient;
        timeToSend(0);
    }
    public int getRecipientId(){
       int senderLen = (message[0] & 0xFF);
       //+2 because  after the number 04
        int recipientLen =  (message[senderLen+2] & 0xFF);
        int digitsLen  = recipientLen;
         if (recipientLen % 2 != 0) digitsLen+=1;
         String number = "";
            for(int i = 0; i < digitsLen/2; i++){
            String dig = Integer.toHexString((message[senderLen+4 + i] & 0xFF));
            if(dig.length() < 2) dig = "0" + dig;
            number = number + dig.charAt(1) +  dig.charAt(0);
            }

        if(recipientLen % 2 != 0) number = number.substring(0,number.length()-1);

        return Integer.parseInt(number);
    }


    public byte[] getMessage() {
        return message;
    }

    public void timeToSend(int second) {
        timeToSend = Instant.now().plusSeconds(second);
    }

    public Instant getTimeToSend() {
        return timeToSend;
    }
}
