package GraphicItems;

import javax.swing.*;
import java.awt.*;

public class VerticalLine extends JPanel {
    public VerticalLine(){
        this.setBackground(Color.GRAY);
    }
    @Override public void paint(Graphics g)
    {
        Dimension d = this.getSize();
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(5));
        g2.setColor(Color.BLACK);
        g2.drawLine(d.width/2,0,d.width/2,d.height);
    }
}
