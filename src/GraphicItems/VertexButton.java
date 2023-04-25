package GraphicItems;

import javax.swing.*;
import java.awt.*;

public class VertexButton extends JButton {
    int vertexID;
    public VertexButton(int vertexID)
    {
        this.vertexID = vertexID;
        this.setLayout(null);
        this.setMargin(new Insets(0, 0, 0, 0));
        this.setBorder(null);
        //this.addActionListener();
        this.setBackground(Color.LIGHT_GRAY);
    }
    public int getVertexID(){
        return this.vertexID;
    }
}
