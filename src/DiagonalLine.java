import javax.swing.*;
import java.awt.*;

public class DiagonalLine extends JPanel{
    Boolean type;
    public DiagonalLine(Boolean type){
        this.type = type;
        this.setBackground(Color.GRAY);
    }
    @Override public void paint(Graphics g)
    {
        Dimension d = this.getSize();
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(5));
        g2.setColor(Color.BLACK);
        if(type)
            g2.drawLine(0,0,d.width,d.height);
        else
            g2.drawLine(d.width,0,0,d.height);
    }
}
