import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

enum STATE{
    ACTIVE, WAITING
}
public class VBD extends Device{
    private byte[] encodedMessage;
    private String message;
    private int frequency;
    private STATE isActive = STATE.ACTIVE;
    private long sentMes = 0L;
    private VbdThread thread;

    private boolean encoded = false;


    public VBD(int number, String message, SmsSystem system){
       super(number, system);
        this.message = message;
        setFrequency(1000);
        thread = new VbdThread(number, this);
        thread.start();
    }

    public long getSentMes() {
        return sentMes;
    }

    public String getMessage() {
        return message;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public STATE isActive() {
        return isActive;
    }

    public void setActive(STATE active){
        isActive = active;
        if(isActive == STATE.ACTIVE){
            thread.resumeThread();
        }
    }

    public void increaseSent(){
        sentMes++;
    }


    public void terminate(){
        thread.finish();
    }

    public SmsMessage createMessage(){
        encode();
        SmsMessage sms = new SmsMessage(this.encodedMessage, this.number, system.getRecipientNumber());
        return sms;
    }

    public void encode(){
        String senderNum = this.number + "";
        while (senderNum.length() < 11){
            senderNum = "0" + senderNum;
        }


        String recipientNum = system.getRecipientNumber() + "";
        while (recipientNum.length() < 11){
            recipientNum = "0" + recipientNum;
        }


        String res = PDU.castNumberToHex(senderNum, true);
        res = res + "04";
        res = res + PDU.castNumberToHex(recipientNum, false);
        res = res + "00" + "00";
        res = res + getTime();
        int len = message.length();
        String length = Integer.toHexString(len);
        if(length.length() < 2) length = "0" + length;
        res = res + length;


        String[] split = new String[message.length()];
        for(int i = 0; i < message.length(); i++){
            split[i] = message.substring(i, i+1);
        }


        String encodedString = PDU.codeMessage(split);
        res = res + encodedString;




        encodedString = res;

        String[] forBytes = encodedString.split("");
        int arrLen = forBytes.length/2;
        if(forBytes.length % 2 != 0)  arrLen = (forBytes.length+1)/2;
        encodedMessage = new byte[arrLen];
        int arrCounter = 0;
        for(int i = 0; i < forBytes.length; i+=2){
            String oneByte = forBytes[i] + forBytes[i+1];
            byte element = (byte) ((Integer.parseInt(oneByte, 16)) & 0xFF) ;
            encodedMessage[arrCounter] = element;
            arrCounter++;
        }

        System.out.println(encodedString);

    }

    private static String getTime() {
        LocalDateTime now = LocalDateTime.now();
        int year = now.getYear() -2000;
        String month ="" + now.getMonthValue();
        String day = ""+now.getDayOfMonth();
        String hour =""+ now.getHour();
        String minute = ""+now.getMinute();
        String second =""+ now.getSecond();


        if(month.length()<2) month = "0" + month;
        if(day.length()<2) day = "0" + day;
        if(hour.length()<2) hour = "0" + hour;
        if(minute.length()<2) minute = "0" + minute;
        if(second.length()<2) second = "0" + second;


        String date = "" + year + month+day+hour+minute+second;
        System.out.println(date);
        String[] date1 = date.split("");
        String res1 = "";
        for(int i = 1; i < date.length(); i+=2){
            res1 = res1 + date1[i] + date1[i-1];
        }
        return res1+"80";
    }


    public void sendMessage(SmsMessage message){
        system.vbdToBts(message);
    }
}



class VbdThread extends Thread{
    private boolean running = true;
    private VBD vbd;
    public VbdThread(int number, VBD vbd ){
        this.setName("VBD" + number);
        this.vbd = vbd;
    }
    public void resumeThread(){
        synchronized (this){
            notify();
        }
    }

    @Override
    public void run() {
        while (running) {
            while (vbd.isActive() == STATE.WAITING){
                try {
                    synchronized (this){
                        wait();
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
                if(!running) return;
            SmsMessage message = vbd.createMessage();
            vbd.sendMessage(message);
            vbd.increaseSent();
            try {
                Thread.sleep(vbd.getFrequency());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void finish(){
        running = false;
    vbd.setActive(STATE.WAITING);
    }
}
