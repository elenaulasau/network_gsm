import javax.swing.*;
import java.awt.*;
import java.util.EventObject;
import java.util.HashMap;
import java.util.Map;

public class BscLayersPanel extends JPanel implements BaseListener<ChangeLayerEvent>{
    private MainFrame frame;
    private Map<BscLayer, JPanel> panels = new HashMap<>();

    public BscLayersPanel(MainFrame frame) {
        this.frame = frame;
    }

    @Override
    public void invoke(ChangeLayerEvent changeLayerEvent) {

        if (changeLayerEvent.isAdded()) {
            JPanel layerPanel = frame.addBscLayerPanel(this, changeLayerEvent.getLayer());
            //layerPanel.setLayout(new GridLayout(0,1,0,0));
            panels.put(changeLayerEvent.getLayer(), layerPanel);
        }
        else {
            JPanel panel = panels.get(changeLayerEvent.getLayer());
            this.remove(panel);
            panels.remove(changeLayerEvent.getLayer());
        }

        this.revalidate();
        this.repaint();
    }

}

class ChangeLayerEvent extends EventObject {
    private BscLayer layer;
    private boolean isAdded;

    public ChangeLayerEvent(Object source, BscLayer layer, boolean isAdded) {
        super(source);
        this.layer = layer;
        this.isAdded = isAdded;
    }

    public BscLayer getLayer() {
        return layer;
    }

    public boolean isAdded() {
        return isAdded;
    }
}


