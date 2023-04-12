import javax.swing.JButton;
import javax.swing.JPanel;
import java.awt.*;

public class Switch extends JPanel {
    private boolean on;
    private JButton button1;
    private JButton button2;
    private String text1;
    private String text2;

    public Switch(String text1, String text2) {
        this.text1 = text1;
        this.text2 = text2;

        // Use a FlowLayout with horizontal alignment and no gaps between components
        setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));

        button1 = new JButton(text1);
        button1.setBackground(Color.LIGHT_GRAY);
        button1.addActionListener(e -> toggle());
        add(button1);

        button2 = new JButton(text2);
        button2.setBackground(Color.LIGHT_GRAY);
        button2.addActionListener(e -> toggle());
        add(button2);

        button2.setBackground(new Color(173, 216, 230));
        on = false;
    }

    private void toggle() {
        on = !on;
        if (on) {
            button1.setBackground(new Color(173, 216, 230));
            button2.setBackground(Color.LIGHT_GRAY);
        } else {
            button1.setBackground(Color.LIGHT_GRAY);
            button2.setBackground(new Color(173, 216, 230));
        }
    }

    public boolean isOn() {
        return on;
    }
}
