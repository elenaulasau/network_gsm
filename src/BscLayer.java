import java.util.ArrayList;
import java.util.List;

public class BscLayer {
    private List<BSC> bscs = new ArrayList<>();
    private SmsSystem system;

    public BscLayer(SmsSystem system){
        this.system = system;
    }
    public synchronized void add(BSC bsc) {
        bscs.add(bsc);
        system.getAddBscListenerDispatcher().fire(new AddBscEvent(system, bsc, this));
    }

    public synchronized List<BSC> getBscs() {
        return bscs;
    }


    public synchronized void removeBsc(BSC bsc){
        bscs.remove(bsc);
    }
}
