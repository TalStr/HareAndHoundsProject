package ProjectUtils;

import GraphicItems.CustomButton;

public class Point {
    public int row, col;
    public Point(int row, int col){
        this.row = row;
        this.col = col;
    }
    public boolean isSame(Point p){
        return(this.row == p.row && this.col == p.col);
    }
    public CustomButton getBtn(CustomButton mat[][])
    {
        return mat[this.row][this.col];
    }
}
