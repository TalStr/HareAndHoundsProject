package LogicClasses;

import GameEnums.GameType;
import ProjectUtils.CDLLL;
import ProjectUtils.Move;

import java.io.*;

public class GameLog implements Serializable {
    private static final long serialVersionUID = 1L; // Add this line
    private GameType[] startingPositions = new GameType[]
            {GameType.WOLF, GameType.EMPTY, GameType.EMPTY,
                    GameType.WOLF, GameType.EMPTY, GameType.EMPTY,
                    GameType.EMPTY, GameType.RABBIT, GameType.WOLF, GameType.EMPTY, GameType.EMPTY};
    GameType[] currentPositions = new GameType[]
            {GameType.WOLF, GameType.EMPTY, GameType.EMPTY,
                    GameType.WOLF, GameType.EMPTY, GameType.EMPTY,
                    GameType.EMPTY, GameType.RABBIT, GameType.WOLF, GameType.EMPTY, GameType.EMPTY};
    public CDLLL moves;
    CDLLL start;
    int numOfMoves = 0;
    public GameLog()
    {
        moves = new CDLLL();
        start = moves;
    }
    public void addMove(int from, int to){
        moves.add(from, to);
        currentPositions[to] = currentPositions[from];
        currentPositions[from] = GameType.EMPTY;
        numOfMoves++;
    }
    public void printMoves()
    {
        CDLLL inc = moves.next;
        System.out.print("(" + moves.move.from + "->" + moves.move.to + ")");
        while(inc != moves){
            System.out.print(",(" + inc.move.from + "->" + inc.move.to + ")");
            inc = inc.next;
        }
        System.out.println();
    }
    public Move getLastMove()
    {
        return moves.prev.move;
    }
    public void deleteLastMove(){
        moves.deleteLast();
        numOfMoves--;
    }
    public static GameLog loadLog(String fileName){
        GameLog loaded;
        File file = new File(new File("gameLogs"), fileName + ".bin");
        try{
            ObjectInputStream logStream = new ObjectInputStream(new FileInputStream(file));
            loaded = (GameLog) logStream.readObject();
            logStream.close();
            return loaded;
        }
        catch (IOException | ClassNotFoundException ex) {
            ex.printStackTrace();
            return null;
        }
    }
    public CDLLL getMoves(){
        return this.moves;
    }

}
