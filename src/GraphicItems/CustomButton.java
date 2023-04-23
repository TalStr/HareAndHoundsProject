package GraphicItems;

import javax.swing.*;
import java.awt.*;

public class CustomButton extends JButton {
    public CustomButton(String text)
    {
        super(text);
        this.setForeground(Color.WHITE);
        this.setBackground(Color.GRAY);
        this.setFocusPainted(false);
        this.setBorderPainted(false);
        this.setFont(new Font("Arial", Font.BOLD, 14));
        this.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
    }
}
