import javax.swing.*;
import java.awt.*;
import java.util.EventObject;


class VrdReceived extends JLabel implements BaseListener<VrdReceiveEvent>{

    private VRD vrd;

    public VrdReceived(VRD vrd) {
        this.vrd = vrd;
    }

    @Override
    public void invoke(VrdReceiveEvent event) {
       if(event.getVrd() != vrd)
           return;

       this.setText("Received "+vrd.getReceived());
    }
}

class VrdReceiveEvent extends EventObject{
    private VRD vrd;
    public VrdReceiveEvent(Object source, VRD vrd) {
        super(source);
        this.vrd = vrd;
    }

    public VRD getVrd() {
        return vrd;
    }
}

public class VrdPanel extends JPanel implements BaseListener<AddVrdEvent> {
    private SmsSystem system;
    public VrdPanel(SmsSystem system){
        this.system = system;
    }
    @Override
    public void invoke(AddVrdEvent event) {
        JPanel vrdStation = new JPanel(new GridLayout(4, 1, 5, 5));

        JLabel label = new JLabel("VRD " + event.getVrd().getNumber());
        VrdReceived recived = new VrdReceived(event.getVrd());
        system.getVrdReceiveEventListener().addListener(recived);
        JCheckBox checkBox = new JCheckBox("Removing messages");
        checkBox.addActionListener(e -> {
            event.getVrd().setClean(checkBox.isSelected());
        });
        JButton terminate = new JButton("Terminate");
        terminate.addActionListener(e -> {
            event.getVrd().terminate();
            system.getVrdReceiveEventListener().removeListener(recived);
            this.remove(vrdStation);
            this.revalidate();
            this.repaint();
        });
        terminate.setPreferredSize(new Dimension(100, 30));

        vrdStation.add(label);
        vrdStation.add(recived);
        vrdStation.add(checkBox);
        vrdStation.add(terminate);
        vrdStation.setBorder(SmsSystem.border);

        this.add(vrdStation);
        this.revalidate();
        this.repaint();
    }
}

class AddVrdEvent extends EventObject{
    private VRD vrd;

    public AddVrdEvent(Object source, VRD vrd) {
        super(source);
        this.vrd = vrd;
    }

    public VRD getVrd() {
        return vrd;
    }
}
