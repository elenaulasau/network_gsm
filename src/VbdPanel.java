import jdk.security.jarsigner.JarSigner;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.util.EventObject;

public class VbdPanel extends JPanel implements BaseListener<AddVbdEvent> {
    private SmsSystem system;

    public VbdPanel(SmsSystem system){
        this.system = system;
    }
    @Override
    public void invoke(AddVbdEvent event) {
        JPanel vbdStation = new JPanel(new GridLayout(5, 1, 5, 5));

        JTextField label = new JTextField("VBD " + event.getVbd().getNumber());
      label.setEditable(false);
        JLabel label1 = new JLabel("Message " + event.getVbd().getMessage());
        JSlider slider = new JSlider(1,100);
        slider.setValue(event.getVbd().getFrequency());
        slider.addChangeListener(e -> {
            event.getVbd().setFrequency(10000/slider.getValue());
        });

        JComboBox<STATE> comboBox = new JComboBox<>();
        comboBox.addItem(STATE.ACTIVE);
        comboBox.addItem(STATE.WAITING);
        comboBox.addActionListener(e -> {
          event.getVbd().setActive((STATE) comboBox.getSelectedItem());
      });

        JButton terminate = new JButton("Terminate");
        terminate.addActionListener(e -> {
            event.getVbd().terminate();
            this.remove(vbdStation);
            this.revalidate();
            this.repaint();
        });
        terminate.setPreferredSize(new Dimension(100, 30));

        vbdStation.add(label);
        vbdStation.add(label1);
        vbdStation.add(slider);
        vbdStation.add(comboBox);
        vbdStation.add(terminate);

        vbdStation.setBorder(SmsSystem.border);

        this.add(vbdStation);
        this.revalidate();
        this.repaint();
    }
}

class AddVbdEvent extends EventObject{

   private VBD vbd;
    public AddVbdEvent(Object source, VBD vbd) {
        super(source);
        this.vbd = vbd;
    }

    public VBD getVbd() {
        return vbd;
    }
}
