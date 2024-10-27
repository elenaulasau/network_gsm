import javax.swing.*;
import java.awt.*;


public class BtsPanel extends JPanel implements BaseListener<AddBtsEvent> {

    private boolean isSender;
    private SmsSystem system;

    public BtsPanel(boolean isSender, SmsSystem system)
    {
        this.isSender = isSender;
        this.system = system;
    }
    @Override
    public void invoke(AddBtsEvent event) {
        if (event.getIsSender() != isSender)
            return;

        JPanel btsStation = new JPanel(new GridLayout(2, 1, 5, 5));

        JLabel label = new JLabel("BTS " + event.getBts().getNumber());
        JButton terminate = new JButton("Terminate");
        terminate.addActionListener(e -> {
            event.getBts().terminate();
            this.remove(btsStation);
            this.revalidate();
            this.repaint();
        });
        terminate.setPreferredSize(new Dimension(100, 30));

        btsStation.add(label);

        btsStation.add(terminate);
        btsStation.setBorder(SmsSystem.border);

        this.add(btsStation);
        this.revalidate();
        this.repaint();
    }
}
