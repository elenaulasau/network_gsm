import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class MainFrame extends JFrame {

    private SmsSystem system;

    public MainFrame(String title, SmsSystem system){
        super(title);
        this.system = system;
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        addVbdPanel();

        addVrdPanel();


        JPanel stationsPanel = new JPanel(new BorderLayout()); //box layout
        addSenderPanel(stationsPanel);
        addRecipientPanel(stationsPanel);



        JPanel bscAllLayersPanel = new JPanel(new BorderLayout());
        JScrollPane bscLayers = new JScrollPane(bscAllLayersPanel);
        bscLayers.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        BscLayersPanel bscLayersPanel = new BscLayersPanel(this);
        system.getChangeLayerListenerDispatcher().addListener(bscLayersPanel);
        bscLayersPanel.setLayout(new BoxLayout(bscLayersPanel, BoxLayout.X_AXIS));

        JButton addLayer = new JButton("Add BSC layer");
        addLayer.addActionListener(e -> {
            system.addLayer();
        });

        bscAllLayersPanel.add(bscLayersPanel, BorderLayout.CENTER);
        bscAllLayersPanel.add(addLayer, BorderLayout.SOUTH);


        stationsPanel.add(bscLayers, BorderLayout.CENTER);

        add(stationsPanel, BorderLayout.CENTER);



        this.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {

            }

            @Override
            public void windowClosing(WindowEvent e) {
                BinaryWriter bw = new BinaryWriter(system.getVbds());
                bw.write("src\\binary.bin");
            }

            @Override
            public void windowClosed(WindowEvent e) {

            }

            @Override
            public void windowIconified(WindowEvent e) {

            }

            @Override
            public void windowDeiconified(WindowEvent e) {

            }

            @Override
            public void windowActivated(WindowEvent e) {

            }

            @Override
            public void windowDeactivated(WindowEvent e) {

            }
        });

        pack();
        setLocationRelativeTo(null);
        setExtendedState(getExtendedState() | JFrame.MAXIMIZED_BOTH);
        setVisible(true);
    }
    private void addVbdPanel() {
        JPanel vbdPanel = new JPanel(new BorderLayout());
        vbdPanel.setPreferredSize(new Dimension(300, 300));

        JButton addVbd = new JButton("Add VBD");
        addVbd.setPreferredSize(new Dimension(250, 30));
        VbdPanel inside = new VbdPanel(system);
        system.getAddVbdEventListenerDispatcher().addListener(inside);
        AddScrollablePanel(this, BorderLayout.WEST,  vbdPanel, inside, addVbd);
        addVbd.addActionListener(e -> {
            String message = JOptionPane.showInputDialog(null, "Input message: ");
                if(message != null)
            system.addVbd(message);
        });
    }

    private void addVrdPanel(){
        JPanel vrdPanel = new JPanel(new BorderLayout());
        vrdPanel.setPreferredSize(new Dimension(300, 300));

        JButton addVrd = new JButton("Add VRD");
        addVrd.setPreferredSize(new Dimension(250, 30));
        VrdPanel inside = new VrdPanel(system);
        system.getAddVrdEventListeners().addListener(inside);
        AddScrollablePanel(this, BorderLayout.EAST,  vrdPanel, inside, addVrd);
        addVrd.addActionListener(e -> {
            system.addVrd();
        });
    }

    public JPanel addBscLayerPanel(JPanel bscLayersPanel, BscLayer layer){
        JPanel bscLayerPanel = new JPanel(new BorderLayout());
        bscLayerPanel.setPreferredSize(new Dimension(300, 300));

        JButton deleteButton = new JButton("Delete Layer");
        deleteButton.setPreferredSize(new Dimension(30,30));

        BscPanel inside = new BscPanel(layer);
        system.getAddBscListenerDispatcher().addListener(inside);
        AddScrollablePanel(bscLayersPanel, "",  bscLayerPanel, inside, deleteButton);
        //btsButton.addActionListener(e -> inside.addBts(new AddBtsEvent(this, null)));
        deleteButton.addActionListener(e -> system.removeLayer(layer));

        return bscLayerPanel;
    }
    private void addSenderPanel(JPanel stationsPanel) {
        JPanel btsSenderPanel = new JPanel(new BorderLayout());
        btsSenderPanel.setPreferredSize(new Dimension(150, 300));

        //JButton btsButton = new JButton("BTS");

        BtsPanel inside = new BtsPanel(true,system);
        system.getAddBtsListenerDispatcher().addListener(inside);
        AddScrollablePanel(stationsPanel, BorderLayout.WEST,  btsSenderPanel, inside, null);
        //btsButton.addActionListener(e -> inside.addBts(new AddBtsEvent(this, null)));
        //btsButton.addActionListener(e -> system.addBts(true));
    }

    private void addRecipientPanel(JPanel stationsPanel) {
        JPanel btsRecipientPanel = new JPanel(new BorderLayout());
        btsRecipientPanel.setPreferredSize(new Dimension(150, 300));

        //JButton btsButton = new JButton("BTS");

        BtsPanel inside = new BtsPanel(false,system);
        system.getAddBtsListenerDispatcher().addListener(inside);
        AddScrollablePanel(stationsPanel, BorderLayout.EAST,  btsRecipientPanel, inside, null);
        //btsButton.addActionListener(e -> inside.addBts(new AddBtsEvent(this, null)));
        //btsButton.addActionListener(e -> system.addBts(false));
    }

    private void AddScrollablePanel(Container parent, String borderLayout, JPanel panel, JPanel inside, JButton addButton)
    {
        //чтобы не растягивало
        JPanel fill = new JPanel();
        //интернал лежит внутри скролабл
        //интернал состоит из инсайд (сверху) и филл (снизу)
        JPanel internal = new JPanel(new BorderLayout());
        JScrollPane scrollPane = new JScrollPane(internal);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);


        internal.add(inside, BorderLayout.NORTH);
        internal.add(fill, BorderLayout.CENTER);

        inside.setBackground(Color.YELLOW);
        inside.setLayout(new BoxLayout(inside, BoxLayout.Y_AXIS));


        panel.add(scrollPane, BorderLayout.CENTER);

        if (addButton != null)
            panel.add(addButton, BorderLayout.SOUTH);

        parent.add(panel, borderLayout);
    }
}
