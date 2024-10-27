import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;



public class SmsSystem {

public static Border border = BorderFactory.createCompoundBorder(new EmptyBorder(5, 5, 5, 5), new EtchedBorder());

    private List<VBD> vbds = new ArrayList<>();
    private List<VRD> vrds = new ArrayList<>();
    private List<BscLayer> bscLayers = new ArrayList<>();
    private int vbdCounter = 0;
    private int btsCounter = 0;
    private int bscCounter = 0;
    private List<BTS> senderBts = new ArrayList<>();
    private List<BTS> recipientBts = new ArrayList<>();

    private ListenerDispatcher<AddBtsEvent> addBtsListenerDispatcher = new ListenerDispatcher<>();
    private ListenerDispatcher<AddBscEvent> addBscListenerDispatcher = new ListenerDispatcher<>();

    private ListenerDispatcher<ChangeLayerEvent> changeLayerListenerDispatcher = new ListenerDispatcher<>();
    private ListenerDispatcher<AddVrdEvent> addVrdEventListeners = new ListenerDispatcher<>();

    private ListenerDispatcher<VrdReceiveEvent> vrdReceiveEventListener = new ListenerDispatcher<>();

    private ListenerDispatcher<AddVbdEvent> addVbdEventListenerDispatcher = new ListenerDispatcher<>();

    public ListenerDispatcher<AddVbdEvent> getAddVbdEventListenerDispatcher() {
        return addVbdEventListenerDispatcher;
    }

    public ListenerDispatcher<VrdReceiveEvent> getVrdReceiveEventListener() {
        return vrdReceiveEventListener;
    }

    public ListenerDispatcher<AddVrdEvent> getAddVrdEventListeners() {
        return addVrdEventListeners;
    }

    public ListenerDispatcher<ChangeLayerEvent> getChangeLayerListenerDispatcher() {
        return changeLayerListenerDispatcher;
    }

    public ListenerDispatcher<AddBtsEvent> getAddBtsListenerDispatcher() {
        return addBtsListenerDispatcher;
    }

    public ListenerDispatcher<AddBscEvent> getAddBscListenerDispatcher() {
        return addBscListenerDispatcher;
    }

    public List<BscLayer> getBscLayers() {
        return bscLayers;
    }

    public List<VBD> getVbds() {
        return vbds;
    }

    public void addBts(boolean isSender){
        BTS newBts = new BTS(btsCounter++, this, !isSender);
        if (isSender) {
            senderBts.add(newBts);
        }
        else {
            recipientBts.add(newBts);
        }
        addBtsListenerDispatcher.fire(new AddBtsEvent(this, newBts, isSender));
    }
    public void addVbd(String message){
        VBD newVbd = new VBD(vbdCounter++, message, this);
        vbds.add(newVbd);
        addVbdEventListenerDispatcher.fire(new AddVbdEvent(this, newVbd));
    }

    public void addVrd(){
        System.out.println("VrD added");
        VRD newVrd = new VRD(vbdCounter++, this);
        vrds.add(newVrd);
        addVrdEventListeners.fire(new AddVrdEvent(this, newVrd));
    }


    public synchronized void addLayer(){
        BscLayer bscLayer = new BscLayer(this);
        changeLayerListenerDispatcher.fire(new ChangeLayerEvent(this, bscLayer,true));
        bscLayer.add(new BSC(bscCounter++, this, bscLayer));
        bscLayers.add(bscLayer);

    }

    public synchronized void addBscTo(int layer){
        BscLayer bscLayer = bscLayers.get(layer);
        bscLayer.add(new BSC(bscCounter++, this, bscLayer));
    }
    public synchronized void addBscTo(BscLayer layer){
        int indL = bscLayers.indexOf(layer);
        layer.add(new BSC(bscCounter++, this, layer));
    }
    //TODO
    public int getRecipientNumber(){
        int ind =(int) (Math.random()*vrds.size());
        return vrds.get(ind).getNumber();
    }

    public synchronized void  vbdToBts(SmsMessage smsMessage) {
        System.out.println("Sended " + smsMessage.getMessage() + " from vbd to bts");
        int ind = findSmallestBts(senderBts, false);
        senderBts.get(ind).receiveMessage(smsMessage);
    }

    private synchronized int findSmallestBts(List<BTS> btsList, boolean isRecipient) {
        int ind = 0;
        int min = 20;
        for(int i = 0; i < btsList.size(); i++){
            int num  = btsList.get(i).getNumberOfMessages();
            if(num <= min) {
                min = num;
                ind = i;
            }
        }

        if(min >= 5) {
            addBts(!isRecipient);
            ind = btsList.size()-1;
            System.out.println("New bts created");
            //System.out.println(layer.getBscs().size() + " stations now");

            return ind;
        }
        return ind;
    }


    public synchronized void BtsToBsc(SmsMessage smsMessage) {
        //System.out.println("Sended " + smsMessage.getMessage() + " from bts to bsc");
        BscLayer first = bscLayers.get(0);
        int ind = findSmallestBsc(first);

        first.getBscs().get(ind).receiveMessage(smsMessage);

    }

    private synchronized int findSmallestBsc(BscLayer layer) {
        int ind = 0;
        int min =20;
        for(int i = 0; i < layer.getBscs().size(); i++){
            int num = layer.getBscs().get(i).getMessageList().size();

            if(num < min ){
                min = num;
                ind = i;
            }
        }

        synchronized (this) {
            if (min >= 5) {
                addBscTo(layer);
                ind = layer.getBscs().size() - 1;
                //System.out.println("New bsc created");
                //System.out.println(layer.getBscs().size() + " stations now");
                return ind;
            }
        }
        return ind;
    }


    public synchronized void bscToNextLayer(SmsMessage smsMessage, BscLayer layer) {
        int ind = bscLayers.indexOf(layer);
        //System.out.println(layer.getBscs().size() + " stations before");
        if(ind == bscLayers.size()-1){

            int btsInd = findSmallestBts(recipientBts, true);

            recipientBts.get(btsInd).receiveMessage(smsMessage);
//            System.out.println("sended " + smsMessage.getMessage() + " bsc to bts");
        } else {
            ind+=1;
            BscLayer currentLayer = bscLayers.get(ind);
            int smallestInd = findSmallestBsc(currentLayer);
            currentLayer.getBscs().get(smallestInd).receiveMessage(smsMessage);
//            System.out.println("Sended " + smsMessage.getMessage() + " to the next layer");
        }
       // System.out.println(layer.getBscs().size() + " stations now");

    }
    public synchronized void BtsToVrd(SmsMessage smsMessage) {
        //System.out.println("Sended " + smsMessage.getMessage() + " bts to vrd");
        int ind = smsMessage.getRecipientId();
        System.out.println(smsMessage.getMessage());

        for(int i = 0; i < vrds.size(); i++){
            if(vrds.get(i).getNumber() == ind){
                vrds.get(i).receiveMessage();
                vrdReceiveEventListener.fire(new VrdReceiveEvent(this, vrds.get(i)));
                return;
            }
        }
        System.out.println("Recipient not found");

    }

    public synchronized void removeBts(BTS bts){
        senderBts.remove(bts);
        recipientBts.remove(bts);
    }
    public synchronized void removeBsc(BSC bsc){
        for(BscLayer layer : bscLayers){
            layer.removeBsc(bsc);
        }
    }

    public synchronized void removeLayer(BscLayer layer) {
       for (int i = 0; i < layer.getBscs().size(); i++){
           layer.getBscs().get(i).terminate();
        }
        changeLayerListenerDispatcher.fire(new ChangeLayerEvent(this, layer, false));
        bscLayers.remove(layer);
    }

}

interface BaseListener<TEvent>
{
    public void invoke(TEvent event);
}

class ListenerDispatcher<TEvent extends EventObject> {
    private List<BaseListener<TEvent>> listeners = new ArrayList<>();
    public void addListener(BaseListener<TEvent> listener){this.listeners.add(listener); }
    public void removeListener(BaseListener<TEvent> listener){
        this.listeners.remove(listener);
    }
    public void fire(TEvent event){
        for(BaseListener<TEvent> listener : listeners)
            listener.invoke(event);
    }
}