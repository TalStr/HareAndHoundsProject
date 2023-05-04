package LogicClasses;

import GameEnums.GameType;
import ProjectUtils.Point;
import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class GameLogic {
    Point[] vertices = new Point[]{
            new Point(0,1),
            new Point(0,2),
            new Point(0, 3),
            new Point(1,0),
            new Point(1,1),
            new Point(1,2),
            new Point(1,3),
            new Point(1,4),
            new Point(2,1),
            new Point(2,2),
            new Point(2,3),
    };
    int[][] wolfG = new int[][] {
            {0,1,0,0,1,1,0,0,0,0,0},
            {0,0,1,0,0,1,0,0,0,0,0},
            {0,0,0,0,0,0,1,1,0,0,0},
            {1,0,0,0,1,0,0,0,1,0,0},
            {1,0,0,0,0,1,0,0,1,0,0},
            {0,1,1,0,0,0,1,0,0,1,1},
            {0,0,1,0,0,0,0,1,0,0,1},
            {0,0,0,0,0,0,0,0,0,0,0},
            {0,0,0,0,1,1,0,0,0,1,0},
            {0,0,0,0,0,1,0,0,0,0,1},
            {0,0,0,0,0,0,1,1,0,0,0}
        };
    int[][] rabbitG = new int[][] {
            {0,1,0,1,1,1,0,0,0,0,0},
            {1,0,1,0,0,1,0,0,0,0,0},
            {0,1,0,0,0,1,1,1,0,0,0},
            {1,0,0,0,1,0,0,0,1,0,0},
            {1,0,0,1,0,1,0,0,1,0,0},
            {1,1,1,0,1,0,1,0,1,1,1},
            {0,0,1,0,0,1,0,1,0,0,1},
            {0,0,1,0,0,0,1,0,0,0,1},
            {0,0,0,1,1,1,0,0,0,1,0},
            {0,0,0,0,0,1,0,0,1,0,1},
            {0,0,0,0,0,1,1,1,0,1,0}
    };
    private GameState state = new GameState();
    private GameLog log = new GameLog();
    public boolean canMove(int from, int to)
    {
        if (getVertexInfo(from) == GameType.WOLF)
            return (wolfG[from][to] == 1);
        else
            return (rabbitG[from][to] == 1);
    }
    public int[] getAdjRow(int vertexID)
    {
        return((state.getVertexInfo(vertexID)==GameType.WOLF)? wolfG[vertexID]: rabbitG[vertexID]);
    }
    public ArrayList<Integer> getAdjacentVertices(int vertexID) {
        ArrayList<Integer> adjacentVertices = new ArrayList<>();
        int[] edges = getAdjRow(vertexID);
        for (int i = 0; i < edges.length; i++) {
            if (edges[i] == 1) {
                adjacentVertices.add(i);
            }
        }
        return adjacentVertices;
    }

    public ArrayList<Integer> getPossibleMoves(int vertexID) {
        ArrayList<Integer> possibles = new ArrayList<>();
        ArrayList<Integer> adjacentVertices = getAdjacentVertices(vertexID);
        for (int adjacentVertex : adjacentVertices) {
            if (state.getVertexInfo(adjacentVertex) == GameType.EMPTY) {
                possibles.add(adjacentVertex);
            }
        }
        return possibles;
    }
    public int getMinWolfCol(){
        int min = 11;
        for (int i = 0; i <3; i++){
            if(vertices[state.getWolfVertex(i)].col < min)
                min = vertices[state.getWolfVertex(i)].col;
        }
        return min;
    }
    public boolean checkGameOver()
    {
        if(state.getRemainingMoves() == 0)
            return true;
        if(state.getCurrentPlayer() == GameType.WOLF)
        {
            return(getPossibleMoves(state.getRabbitVertex()).isEmpty());
        }
        else{
            return (this.vertices[state.getRabbitVertex()].col <= getMinWolfCol());
        }
    }
    public GameType getCurrentPlayer()
    {
        return state.getCurrentPlayer();
    }
    public void moveAnimal(int fromVertexID, int toVertexID, boolean delete)
    {
        if(!delete)
        {
            log.addMove(fromVertexID, toVertexID);
            state.useMoves(1);
        }
        else
            state.addMoves(1);
        state.moveAnimal(fromVertexID,toVertexID);
    }
    public void nextTurn()
    {
        state.setCurrentPlayer((getCurrentPlayer() == GameType.WOLF) ? GameType.RABBIT : GameType.WOLF);
    }
    public int getRabbitVertexID(){
        return state.getRabbitVertex();
    }
    public int[] getWolvesVertexIDs(){
        return state.getWolvesVertices();
    }
    public int getWolfVertexID(int index){
        return state.getWolfVertex(index);
    }
    public Point getVertexPoint(int vertexID){
        return this.vertices[vertexID];
    }
    public GameType getVertexInfo(int vertexID){
        return state.getVertexInfo(vertexID);
    }
    public int getRemainingMoves(){
        return state.getRemainingMoves();
    }
    public GameState getState(){
        return this.state;
    }
    public GameState resetState(){
        this.state = new GameState();
        return this.state;
    }
    public void undoMove()
    {
        int[] move = log.getLastMove();
        log.deleteLastMove();
        this.moveAnimal(move[1], move[0], true);
        this.nextTurn();
        if(state.getCurrentPlayer() == GameType.RABBIT)
            System.out.println("Rabbit");
        else
            System.out.println("Wolf");
    }
    public void undoTwoMoves()
    {
        undoMove();
        undoMove();
//        int[] move = log.getLastMove();
//        log.deleteLastMove();
//        this.moveAnimal(move[1], move[0], true);
//        //System.out.println(move[1] + ", " + move[0]);
//        move = log.getLastMove();
//        log.deleteLastMove();
//        this.moveAnimal(move[1], move[0], true);
//        //System.out.println(move[1] + ", " + move[0]);
    }
    public GameLog getLog(){
        return this.log;
    }
    public void saveGameLog(String fileName){
        File file = new File(new File("gameLogs"), fileName + ".bin");
        try {
            file.createNewFile();
            try (FileOutputStream fileOutputStream = new FileOutputStream(file);
                 ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream)) {
                objectOutputStream.writeObject(log);
            }
        }
        catch(IOException ex){
            ex.printStackTrace();
        }
    }
}
