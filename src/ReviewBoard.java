import GraphicItems.CustomButton;
import LogicClasses.GameLog;
import LogicClasses.GameState;
import ProjectUtils.CDLLL;
import ProjectUtils.Move;
import GraphicItems.MoveList;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ReviewBoard extends JFrame{
    final int rows = 5;
    final int columns = 9;
    private static final int WINDOW_WIDTH = 1000;
    private static final int WINDOW_HEIGHT = 650;
    GameState state = new GameState();
    GameLog log;
    Board gameBoard;
    MoveList movesPanel;
    CDLLL startMove;
    int curMove = 1;
    public ReviewBoard(GameLog log) {
        super("Review");
        createJFrame();
        this.log = log;
        this.startMove = log.getMoves();
        this.setVisible(true);
    }
    private void createJFrame() {
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        this.setLayout(new BorderLayout());
        this.setLocationRelativeTo(null); // Center the window on the screen

        Board board = new Board(this.state);
        this.add(board, BorderLayout.CENTER);
        this.gameBoard = board;

        MoveList rightPanel = new MoveList();
        rightPanel.setPreferredSize(new Dimension(200, WINDOW_HEIGHT)); // Set the preferred size of the panel
        rightPanel.setBackground(Color.LIGHT_GRAY); // Set the background color
        this.add(rightPanel, BorderLayout.EAST); // Add the panel to the right of the board
        this.movesPanel = rightPanel;

        JPanel b = new JPanel(new BorderLayout());
        b.setBackground(Color.DARK_GRAY);
        b.setPreferredSize(new Dimension(800, 50));
        JPanel centerButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        centerButtonPanel.setBackground(Color.DARK_GRAY);
        // Add left button
        CustomButton button1 = new CustomButton("Back");
        button1.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e) {
                if(log.getMoves().move != null)
                {
                    Move cur = log.getMoves().move;
                    board.makeAnimal(cur.from, state.getVertexInfo(cur.to));
                    board.removeAnimal(cur.to);
                    state.moveAnimal(cur.to,cur.from);
                    movesPanel.deleteTextRow();
                    curMove--;
                    log.moves = log.getMoves().prev;
                }
            }
        });
        centerButtonPanel.add(button1);
        CustomButton button2 = new CustomButton("Restart");
        button2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e)
            {
                state = new GameState();
                board.setState(state);
                log.moves = startMove;
                rightPanel.clearText();
                curMove=1;
            }
        });
        centerButtonPanel.add(button2);

        CustomButton button3 = new CustomButton("Forward");
        button3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e)
            {
                if(log.moves.next.move != null)
                {
                    log.moves = log.moves.next;
                    Move cur = log.moves.move;
                    board.makeAnimal(cur.to, state.getVertexInfo(cur.from));
                    board.removeAnimal(cur.from);
                    state.moveAnimal(cur.from,cur.to);
                    movesPanel.addTextRow(curMove + ". " + cur.from + "->" + cur.to);
                    curMove++;
                }
            }
        });
        centerButtonPanel.add(button3);
        b.add(centerButtonPanel, BorderLayout.CENTER);

        this.add(b, BorderLayout.SOUTH);
        this.pack();
    }
}
