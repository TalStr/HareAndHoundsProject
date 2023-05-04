import GameBots.*;
import GameEnums.GameType;
import GraphicItems.*;
import LogicClasses.GameLogic;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class GameBoard extends JFrame implements BotListener {
    private final int WINDOW_WIDTH = 800;
    private final int WINDOW_HEIGHT = 650;
    GameLogic logic = new GameLogic();
    VertexButton selectedButton;
    GameBot bot;
    GameMenu menu;
    boolean saveGame;
    Board board;
    public GameBoard(GameMenu menu, boolean saveGame) {
        super("Hare And Hounds");
        createJFrame();
        this.menu = menu;
        this.saveGame = saveGame;
        this.bot = null;
    }

    public GameBoard(GameMenu menu, boolean saveGame, int difficulty, GameType playerPlaying)
    {
        super("Hare And Hounds");
        createJFrame();
        makeBot(difficulty,playerPlaying);
        this.menu = menu;
        this.saveGame = saveGame;
    }

    private void createJFrame() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        this.setLayout(new BorderLayout());
        this.setLocationRelativeTo(null); // Center the window on the screen

        Board board = new Board(logic.getState());
        this.add(board, BorderLayout.CENTER);
        this.board = board;
        setActionListener();
        // Create south component
        JPanel b = new JPanel(new BorderLayout());
        b.setBackground(Color.DARK_GRAY);
        b.setPreferredSize(new Dimension(800, 50));

        // Add left button
        CustomButton button1 = new CustomButton("Back");
        button1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(bot == null)
                    logic.undoMove();
                else
                    logic.undoTwoMoves();
                board.setState(logic.getState());
            }
        });
        JPanel leftButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
        leftButtonPanel.setBackground(Color.DARK_GRAY);
        leftButtonPanel.add(button1);
        b.add(leftButtonPanel, BorderLayout.WEST);

        // Add right buttons
        CustomButton button2 = new CustomButton("Restart");
        button2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                board.setState(logic.resetState());
            }
        });
        CustomButton button3 = new CustomButton("End Game");
        button3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               new ReviewBoard(logic.getLog());
            }
        });
        JPanel rightButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 10));
        rightButtonPanel.setBackground(Color.DARK_GRAY);
        rightButtonPanel.add(button2);
        rightButtonPanel.add(button3);
        b.add(rightButtonPanel, BorderLayout.EAST);

        this.add(b, BorderLayout.SOUTH);
        this.pack();
    }
    private void setActionListener(){
        for (int i=0; i<11;i++){
            board.playableCells[i].addActionListener(createGridButtonActionListener());
        }
    }
    private ActionListener createGridButtonActionListener() {
        // Create an action listener for the grid buttons
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                VertexButton sourceButton = (VertexButton) e.getSource();

                // If the clicked button has the current player's animal, handle the selection
                if (logic.getVertexInfo(sourceButton.getVertexID()) == logic.getCurrentPlayer()) {
                    handleButtonClickOnCurrentPlayer(sourceButton);
                }
                // If the clicked button is empty and a button was previously selected, handle the move
                else if (selectedButton != null && logic.getVertexInfo(sourceButton.getVertexID()) == GameType.EMPTY) {
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
        if (logic.canMove(selectedButton.getVertexID(), sourceButton.getVertexID())) {
            hidePossibleMoves(selectedButton);
            move(selectedButton, sourceButton);
            selectedButton.setBorder(null);
            selectedButton = null;
            if (logic.checkGameOver()) {
                displayWinner();
                if(saveGame)
                {
                    logic.saveGameLog("ID-" + Integer.toString(GameCounter.getCountStat()));
                    GameCounter.incrementCounter();
                }
                dispose();
                menu.setVisible(true);
            }
            else
                nextTurn();
        }
    }
    public void move(VertexButton from, VertexButton to)
    {
        board.makeAnimal(to.getVertexID(), logic.getVertexInfo(from.getVertexID()));
        board.removeAnimal(from.getVertexID());
        logic.moveAnimal(from.getVertexID(), to.getVertexID(), false);
    }
    private void nextTurn() {
        logic.nextTurn();
        if (bot != null && logic.getCurrentPlayer() == bot.getPlaying()) {
            bot.makeMove();
        }
    }
    private void highlightPossibleMoves(VertexButton b)
    {
        ArrayList<Integer> p = logic.getPossibleMoves(b.getVertexID());
        for (Integer integer : p) {
            board.playableCells[integer].setBackground(Color.GREEN);
        }
    }
    private void hidePossibleMoves(VertexButton b)
    {
        ArrayList<Integer> p = logic.getPossibleMoves(b.getVertexID());
        for (Integer integer : p) {
            board.playableCells[integer].setBackground(Color.LIGHT_GRAY);
        }
    }
    private void displayWinner()
    {
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
        VertexButton fromButton = board.playableCells[from];
        VertexButton toButton = board.playableCells[to];
        simulateBotClick(fromButton);
        simulateBotClick(toButton);
    }
    public void makeBot(int difficulty, GameType playerPlaying)
    {
        switch (difficulty) {
            case 0 -> bot = new EasyBot(this, (playerPlaying == GameType.WOLF) ? GameType.RABBIT : GameType.WOLF, logic);
            case 1 -> bot = new MediumBot(this, (playerPlaying == GameType.WOLF) ? GameType.RABBIT : GameType.WOLF, logic);
            case 2 -> bot = new HardBot(this, (playerPlaying == GameType.WOLF) ? GameType.RABBIT : GameType.WOLF, logic);
        }
        if (logic.getCurrentPlayer() == bot.getPlaying()) {
            bot.makeMove();
        }
    }
}
