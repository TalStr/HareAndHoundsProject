import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class GameBoard extends JFrame implements BotListener{
    final int rows = 5;
    final int columns = 9;
    private static final int WINDOW_WIDTH = 800;
    private static final int WINDOW_HEIGHT = 600;
    GameLogic logic = new GameLogic();
    VertexButton[] playableCells = new VertexButton[11];
    VertexButton selectedButton;
    GameBot bot;
    StartMenu menu;

    public GameBoard(StartMenu menu) {
        super("Hare And Hounds");
        createJFrame();
        initGrid();
        this.menu = menu;
    }

    public GameBoard(StartMenu menu, int difficulty, GameType playerPlaying)
    {
        super("Hare And Hounds");
        createJFrame();
        initGrid();
        makeBot(difficulty,playerPlaying);
        this.menu = menu;
    }

    private void createJFrame() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new GridLayout(rows, columns, 0, 0));
        this.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        this.setLocationRelativeTo(null); // Center the window on the screen
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
    private void makeAnimal(VertexButton p, GameType type) {
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
        p.setIcon(scaledIcon);
        p.setHorizontalAlignment(JButton.CENTER);
        p.setVerticalAlignment(JButton.CENTER);
        p.setBorder(BorderFactory.createEmptyBorder());
    }

    private void placeAnimals()
    {
        makeAnimal(playableCells[0], GameType.WOLF);
        makeAnimal(playableCells[3], GameType.WOLF);
        makeAnimal(playableCells[8], GameType.WOLF);
        makeAnimal(playableCells[7], GameType.RABBIT);

    }
    private void initGrid() {
        int counterVertices = 0;
        JComponent[][] cells = new JComponent[rows][columns];
        // Iterate through all rows and columns of the grid
        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < columns; column++) {
                if (isVertex(row, column))
                {
                    playableCells[counterVertices] = new VertexButton(counterVertices);
                    playableCells[counterVertices].addActionListener(createGridButtonActionListener());
                    cells[row][column] = playableCells[counterVertices];
                    counterVertices++;
                }
                else
                {
                    cells[row][column] = new GamePanel();
                }
                this.add(cells[row][column]);
            }
        }
        // Draw lines and place animals on the grid
        drawLines(cells);
        placeAnimals();
    }
    private boolean isVertex(int row, int column) {
        // Check if the current position is a vertex (valid position for an animal)
        return (row % 2 == 0 && column % 2 == 0) && !((row == 0 || row == rows - 1) && (column == 0 || column == columns - 1));
    }

    private ActionListener createGridButtonActionListener() {
        // Create an action listener for the grid buttons
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                VertexButton sourceButton = (VertexButton) e.getSource();

                // If the clicked button has the current player's animal, handle the selection
                if (logic.getVertexInfo(sourceButton.vertexID) == logic.getCurrentPlayer()) {
                    handleButtonClickOnCurrentPlayer(sourceButton);
                }
                // If the clicked button is empty and a button was previously selected, handle the move
                else if (selectedButton != null && logic.getVertexInfo(sourceButton.vertexID) == GameType.EMPTY) {
                    handleButtonClickOnEmptySpace(sourceButton);
                }
            }
        };
    }

    private void handleButtonClickOnCurrentPlayer(VertexButton sourceButton) {
        // If another button was selected previously, deselect it and hide possible moves
        if (selectedButton != null) {
            selectedButton.setBorder(null);
            hidePossibleMoves(selectedButton);
        }
        // Select the clicked button and highlight possible moves
        sourceButton.setBorder(new LineBorder(Color.GREEN, 2));
        selectedButton = sourceButton;
        highlightPossibleMoves(selectedButton);
    }

    private void handleButtonClickOnEmptySpace(VertexButton sourceButton) {
        // If the move is valid, perform the move and update the game state
        if (logic.canMove(selectedButton.vertexID, sourceButton.vertexID, logic.getVertexInfo(sourceButton.vertexID))) {
            hidePossibleMoves(selectedButton);
            move(selectedButton, sourceButton);
            selectedButton.setBorder(null);
            selectedButton = null;
            if (logic.checkGameOver()) {
                displayWinner();
                dispose();
                menu.setVisible(true);
            } else {
                // If the game is not over, proceed to the next turn
                nextTurn();
            }
        }
    }
    public void removeAnimal(VertexButton b)
    {
        b.setIcon(null);
        b.setBackground(Color.LIGHT_GRAY);
    }
    public void move(VertexButton from, VertexButton to)
    {
        makeAnimal(to, logic.getVertexInfo(from.vertexID));
        logic.moveAnimal(from.vertexID, to.vertexID, false);
        removeAnimal(from);
    }
    private void nextTurn() {
        logic.nextTurn();
        if (bot != null && logic.getCurrentPlayer() == bot.playing) { // The bot only controls wolves
            bot.makeMove();
        }
    }
    private void highlightPossibleMoves(VertexButton b)
    {
        ArrayList<Integer> p = logic.getPossibleMoves(b.vertexID);
        for (Integer integer : p) {
            playableCells[integer].setBackground(Color.GREEN);
        }
    }
    private void hidePossibleMoves(VertexButton b)
    {
        ArrayList<Integer> p = logic.getPossibleMoves(b.vertexID);
        for (Integer integer : p) {
            playableCells[integer].setBackground(Color.LIGHT_GRAY);
        }
    }
    private void displayWinner(){
        if(logic.getRemainingMoves() == 0)
            JOptionPane.showMessageDialog(null, "Rabbit won by repetition!");
        else{
            if(logic.getCurrentPlayer() == GameType.WOLF)
                JOptionPane.showMessageDialog(null, "Wolves won!");
            else
                JOptionPane.showMessageDialog(null, "Rabbit won!");
        }
    }
    private void simulateBotClick(VertexButton button) {
        ActionListener[] listeners = button.getActionListeners();

        if (listeners.length > 0) {
            listeners[0].actionPerformed(new ActionEvent(button, ActionEvent.ACTION_PERFORMED, ""));
        }
    }
    @Override
    public void onBotMove(int from, int to) {
        VertexButton fromButton = playableCells[from];
        VertexButton toButton = playableCells[to];
        simulateBotClick(fromButton);
        simulateBotClick(toButton);
    }
    public void makeBot(int difficulty, GameType playerPlaying)
    {
        switch (difficulty) {
            case 1 -> bot = new EasyBot(this, (playerPlaying == GameType.WOLF) ? GameType.RABBIT : GameType.WOLF, logic);
            case 2 -> bot = new MediumBot(this, (playerPlaying == GameType.WOLF) ? GameType.RABBIT : GameType.WOLF, logic);
            case 3 -> bot = new HardBot(this, (playerPlaying == GameType.WOLF) ? GameType.RABBIT : GameType.WOLF, logic);
        }
        if (logic.getCurrentPlayer() == bot.playing) {
            bot.makeMove();
        }
    }
}
