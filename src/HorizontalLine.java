import javax.swing.*;
import java.awt.*;

public class HorizontalLine extends JPanel
{
    public HorizontalLine(){
        this.setBackground(Color.GRAY);
    }
    @Override public void paint(Graphics g)
    {
        Dimension d = this.getSize();
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(5));
        g2.setColor(Color.BLACK);
        g2.drawLine(0,d.height/2,d.width,d.height/2);
    }
}
