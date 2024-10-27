import javax.swing.*;
import java.time.Instant;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SmsSystem system = new SmsSystem();


            new MainFrame("Frame", system);

            system.addLayer();
            system.addLayer();

            system.addBts(true);
            system.addBts(false);


            system.addBscTo(0);
            system.addBscTo(1);
            system.addVrd();


        });

    }
}