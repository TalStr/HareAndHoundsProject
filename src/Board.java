import GameEnums.GameType;
import GraphicItems.*;
import LogicClasses.GameState;

import javax.swing.*;
import java.awt.*;

public class Board extends JPanel{
    private final int WINDOW_WIDTH = 800;
    private final int WINDOW_HEIGHT = 600;
    final int rows = 5;
    final int columns = 9;
    VertexButton[] playableCells = new VertexButton[11];
    public Board(GameState state){
        //this.setLayout(new GridBagLayout());
        this.setLayout(new BorderLayout());
        this.setBackground(Color.GRAY);
        this.setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 5));
        this.add(initGrid(state), BorderLayout.CENTER);
        this.setBackground(Color.GRAY);
    }
    private void drawLines(JComponent[][] cells){
        for (int j = 1;j < columns-1; j+=2){
            if (j >=3 && j<= columns-3){
                cells[0][j].setLayout(new BorderLayout());
                cells[0][j].add(new HorizontalLine(), BorderLayout.CENTER);
                cells[4][j].setLayout(new BorderLayout());
                cells[4][j].add(new HorizontalLine(), BorderLayout.CENTER);
            }
            cells[2][j].setLayout(new BorderLayout());
            cells[2][j].add(new HorizontalLine(), BorderLayout.CENTER);
        }
        for (int j = 2;j < columns-2; j+=2){
            cells[1][j].setLayout(new BorderLayout());
            cells[1][j].add(new VerticalLine(), BorderLayout.CENTER);
            cells[3][j].setLayout(new BorderLayout());
            cells[3][j].add(new VerticalLine(), BorderLayout.CENTER);
        }
        for (int j = 1;j < columns-1; j+=2){
            cells[1][j].setLayout(new BorderLayout());
            cells[1][j].add(new DiagonalLine((j/2)%2 != 0), BorderLayout.CENTER);
            cells[3][j].setLayout(new BorderLayout());
            cells[3][j].add(new DiagonalLine((j/2)%2 == 0), BorderLayout.CENTER);
        }
    }
    public void makeAnimal(int vertex, GameType type) {
        String path;
        if(type == GameType.WOLF)
            path ="Images/wolf.jfif";
        else
            path ="Images/rabbit.jfif";
        ImageIcon originalIcon = new ImageIcon(path);
        int scaledWidth = originalIcon.getIconWidth() / 2;
        int scaledHeight = originalIcon.getIconHeight() / 2;
        Image scaledImage = originalIcon.getImage().getScaledInstance(scaledWidth, scaledHeight, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImage);
        playableCells[vertex].setIcon(scaledIcon);
        playableCells[vertex].setHorizontalAlignment(JButton.CENTER);
        playableCells[vertex].setVerticalAlignment(JButton.CENTER);
        playableCells[vertex].setBorder(BorderFactory.createEmptyBorder());
    }

    private void placeAnimals(GameState state)
    {
        for(int i=0; i < 3; i++){
            makeAnimal(state.getWolfVertex(i), GameType.WOLF);

        }
        makeAnimal(state.getRabbitVertex(), GameType.RABBIT);

    }
    private JPanel initGrid(GameState state) {
        int counterVertices = 0;
        JPanel board = new JPanel(new GridBagLayout());
        board.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        board.setBackground(Color.GRAY);
        GridBagConstraints gbc = new GridBagConstraints();
        JComponent[][] cells = new JComponent[rows][columns];
        int cellWidth = WINDOW_WIDTH / columns;
        int cellHeight = WINDOW_HEIGHT / rows;

        // Iterate through all rows and columns of the grid
        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < columns; column++) {
                gbc.gridx = column;
                gbc.gridy = row;
                if (isVertex(row, column))
                {
                    playableCells[counterVertices] = new VertexButton(counterVertices);
                    cells[row][column] = playableCells[counterVertices];
                    counterVertices++;
                }
                else
                {
                    cells[row][column] = new GamePanel();
                }
                cells[row][column].setPreferredSize(new Dimension(cellWidth, cellHeight));
                board.add(cells[row][column], gbc);
            }
        }
        // Draw lines and place animals on the grid
        drawLines(cells);
        placeAnimals(state);
        return board;
    }
    private boolean isVertex(int row, int column) {
        // Check if the current position is a vertex (valid position for an animal)
        return (row % 2 == 0 && column % 2 == 0) && !((row == 0 || row == rows - 1) && (column == 0 || column == columns - 1));
    }
    public void removeAnimal(int vertex)
    {
        playableCells[vertex].setIcon(null);
        playableCells[vertex].setBackground(Color.LIGHT_GRAY);
    }
    public void setState(GameState state){
        for(int i=0; i<11;i++)
            removeAnimal(i);
        placeAnimals(state);
    }

}
