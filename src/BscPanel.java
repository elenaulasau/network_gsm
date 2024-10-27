import javax.swing.*;
import java.awt.*;
import java.util.EventObject;

public class BscPanel extends JPanel implements BaseListener<AddBscEvent> {
    private BscLayer layer;

    public BscPanel( BscLayer layer) {

        this.layer = layer;
    }

    @Override
    public void invoke(AddBscEvent event) {
        if(event.getLayer() != layer) return;

        JPanel bscStation = new JPanel(new GridLayout(2, 1, 5, 5));


        JLabel label = new JLabel("BSC " + event.getBsc().getNumber());
        JButton terminate = new JButton("Terminate");
        terminate.addActionListener(e -> {
            event.getBsc().terminate();
            this.remove(bscStation);
            this.revalidate();
            this.repaint();
        });
        terminate.setPreferredSize(new Dimension(30, 30));

        bscStation.add(label);

        bscStation.add(terminate);
        bscStation.setBorder(SmsSystem.border);

        this.add(bscStation);
        this.revalidate();
        this.repaint();


    }
}

class AddBscEvent extends EventObject{
    private BSC bsc;
    private BscLayer layer;
    public AddBscEvent(Object source, BSC bsc, BscLayer layer) {
        super(source);
        this.bsc = bsc;
        this.layer = layer;
    }

    public BSC getBsc() {
        return bsc;
    }

    public BscLayer getLayer() {
        return layer;
    }
}
