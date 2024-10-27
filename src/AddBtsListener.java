import javax.swing.*;
import java.awt.*;
import java.util.EventObject;

class AddBtsEvent extends EventObject{
    private BTS bts;
    private boolean isSender;

    public AddBtsEvent(Object source, BTS bts, boolean isSender) {
        super(source);
        this.bts = bts;
        this.isSender = isSender;
    }

    public BTS getBts(){
        return bts;
    }

    public boolean getIsSender(){
        return isSender;
    }
}
