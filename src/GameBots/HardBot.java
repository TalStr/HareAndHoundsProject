package GameBots;

import GameEnums.ColorPattern;
import GameEnums.GameType;
import LogicClasses.GameLogic;
import ProjectUtils.Point;

import java.util.ArrayList;

public class HardBot extends GameBot{
    ColorPattern[] pattern = new ColorPattern[]
            {ColorPattern.WHITE, ColorPattern.PURPLE, ColorPattern.WHITE
                    , ColorPattern.ORANGE, ColorPattern.PURPLE, ColorPattern.ORANGE, ColorPattern.PURPLE, ColorPattern.ORANGE
                    , ColorPattern.WHITE, ColorPattern.PURPLE, ColorPattern.WHITE};
    int[] idealPattern = new int[]{2,1,1}; // 2 White, 1 Orange, 1 Purple
    public HardBot(BotListener listener, GameType playing, GameLogic logic){
        super(listener, playing, logic);
    }
    @Override
    public void makeMove(){
        if(playing == GameType.WOLF)
            makeWolfMove();
        else
            makeRabbitMove();
    }
    private void makeRabbitMove()
    {
        //Get the Current Vertex The Rabbit Is On
        int fromVertex = logic.getRabbitVertexID();
        //Get an ArrayList Of the Possible Vertices the Rabbit can move to
        ArrayList<Integer> possibles = logic.getPossibleMoves(fromVertex);
        int toVertex;
        //If there is only 1 possible move do it, else find the best move
        if (possibles.size() == 1)
            toVertex = possibles.get(0);
        else
        {
            toVertex = bestMoveRabbit(possibles,fromVertex);
        }
        if (super.listener != null)
            super.listener.onBotMove(fromVertex, toVertex);
    }
    private int bestMoveRabbit(ArrayList<Integer> options, int from) {
        int toVertex = -1;
        //Vertex that creates pattern
        int matchPattern = -1;
        int matchColumn = -1;
        int preventColumn = -1;
        for (int i = 0; i < options.size();i++){
            //If the option Makes The Pattern And The column of the vertex is bigger than the current matchColumn
            if(moveMakesPattern(from,options.get(i)) && logic.getVertexPoint(options.get(i)).col > matchColumn) {
                //Update to be best Move
                matchPattern = options.get(i);
                toVertex = options.get(i);
                matchColumn = logic.getVertexPoint(toVertex).col;
            }
            //If no Possibility matches the pattern And Current possibility prevents the pattern on the next turn
            // And current column bigger than previous option
            if(matchPattern == -1 && movePreventsPattern(from,options.get(i)) && logic.getVertexPoint(options.get(i)).col > preventColumn) {
                toVertex = options.get(i);
                preventColumn = logic.getVertexPoint(toVertex).col;
            }
        }
        //If No Option Matches Or Prevents The Pattern
        if(toVertex == -1) {
            int maxPossibles = -1;
            //Find the Option With The Most Possible Moves
            for(int i = 0; i < options.size();i++){
                if(logic.getPossibleMoves(options.get(i)).size() > maxPossibles) {
                    toVertex = options.get(i);
                    maxPossibles = logic.getPossibleMoves(options.get(i)).size();
                }
            }
        }
        return toVertex;
    }
    private boolean moveMakesPattern(int from, int to)
    {
        //find the pattern after the move
        int[] colors = countColors();
        colors[pattern[from].index]--;
        colors[pattern[to].index]++;
        //check if pattern is same as required
        return (colors[0] == 2 && colors[1] == 1 && colors[2] == 1);
    }
    private boolean movePreventsPattern(int from, int to)
    {
        //Find Pattern After Move
        int[] colors = countColors();
        colors[pattern[from].index]--;
        colors[pattern[to].index]++;
        //Check If That Pattern Prevents Matching The Pattern In The Next Turn
        return (preventsIdealPattern(colors));
    }
    private boolean preventsIdealPattern(int[] colors)
    {
        //Checks That The Ideal Pattern is More Than 1 Move Away
        return ((Math.abs(colors[0]-idealPattern[0]) > 1)
                && (Math.abs(colors[1]-idealPattern[1]) > 1)
                && (Math.abs(colors[2]-idealPattern[2]) > 1));
    }
    private void makeWolfMove(){
        //Check The Color Pattern Of The Current Board
        int colors[] = countColors();
        //Find What Color You Need To Move From
        ColorPattern from = fromColor(colors);
        //Find What Color You Need To Move To
        ColorPattern to = toColor(colors);
        int fromVertex;
        int toVertex = -1;
        //Find what Vertex You Need To Move From
        //White is The Only Color That Could Have More Than 1 Wolves On it
        //If Move From White Choose What Wolf To Move
        if(from == ColorPattern.WHITE)
            fromVertex = chooseWolfOnWhite(to);
        //Else Get The Purple Or Orange Vertex That Has a Wolf
        else
            fromVertex = getWolfOnPurpleOrange(from);
        //Switch Statement Based On What Color You need To Go To
        switch (to){
            //If You Need To Go To Orange Go To 5
            case ORANGE -> toVertex=5;
            //If You Need To Go To Purple Choose Purple Vertex
            case PURPLE -> toVertex=choosePurpleDestination(fromVertex);
            //If You Need To Go To White Choose White Vertex
            case WHITE -> toVertex=chooseWhiteDestination(fromVertex);
        }
        //Move The Character On Screen
        if (super.listener != null)
            super.listener.onBotMove(fromVertex, toVertex);
    }

    private ColorPattern fromColor(int[] colors)
    {
        if(colors[0] > 2)
            return ColorPattern.WHITE;
        else if(colors[1] > 1)
            return ColorPattern.ORANGE;
        else
            return ColorPattern.PURPLE;
    }
    private ColorPattern toColor(int[] colors)
    {
        if(colors[0] < 2)
            return ColorPattern.WHITE;
        else if(colors[1] < 1)
            return ColorPattern.ORANGE;
        else
            return ColorPattern.PURPLE;
    }
    private int[] countColors()
    {
        int[] count = new int[]{0,0,0};
        switch(pattern[logic.getRabbitVertexID()]){
            case WHITE -> count[0]++;
            case ORANGE -> count[1]++;
            case PURPLE -> count[2]++;
        }
        for(int i=0; i<3; i++)
        {
            switch(pattern[logic.getWolfVertexID(i)]){
                case WHITE -> count[0]++;
                case ORANGE -> count[1]++;
                case PURPLE -> count[2]++;
            }
        }
        return count;
    }
    private int chooseWolfOnWhite(ColorPattern toColor)
    {
        int[] wolves = new int[2];
        int count = 0;
        int rabbitRow = logic.getVertexPoint(logic.getRabbitVertexID()).row;
        for (int i=0; i < 3; i++)
        {
            if(pattern[logic.getWolfVertexID(i)] == ColorPattern.WHITE)
                wolves[count++] = logic.getWolfVertexID(i);
        }
        if(logic.getVertexPoint(wolves[0]).col == logic.getVertexPoint(wolves[1]).col)
        {
            return (logic.getVertexPoint(wolves[0]).row == rabbitRow && toColor == ColorPattern.ORANGE)? wolves[1]: wolves[0];
        }
        else
            return (logic.getVertexPoint(wolves[0]).col < logic.getVertexPoint(wolves[1]).col)? wolves[0]: wolves[1];
    }
    private int getWolfOnPurpleOrange(ColorPattern color)
    {
        int vertex = -1;
        for(int i=0; i<3; i++)
        {
            if(pattern[logic.getWolfVertexID(i)] == color)
                vertex = logic.getWolfVertexID(i);
        }
        return vertex;
    }
    private int choosePurpleDestination(int from)
    {
        int to = -1;
        //Switch Based on Color Of the From Vertex
        switch (pattern[from]){
            case ORANGE:
                //Choose what Purple Vertex To Go To From Orange Vertex
                //Strategy Implementation...
                if(from == 3)
                    to = 4;
                else{
                    if((logic.getVertexInfo(2) == GameType.WOLF && logic.getVertexInfo(10) == GameType.WOLF)
                            || (logic.getVertexInfo(1) == GameType.WOLF && logic.getVertexInfo(9) == GameType.WOLF))
                        to = 6;
                    else{
                        if(logic.getVertexInfo(1) != GameType.EMPTY ||
                                (logic.getVertexInfo(0) == GameType.WOLF && logic.getVertexInfo(2) == GameType.WOLF))
                            to = 9;
                        else
                            to = 1;
                    }
                }
                break;
            case WHITE:
                //Choose What Purple Vertex To Go To From White Vertex
                //Strategy Implementation...
                if(logic.getVertexPoint(logic.getRabbitVertexID()).row != logic.getVertexPoint(from).row)
                {
                    to = from + ((logic.getVertexPoint(logic.getRabbitVertexID()).row > logic.getVertexPoint(from).row)? 4: -4);
                }
                else
                    to = from+1;
                break;
        }
        return to;
    }
    private int chooseWhiteDestination(int from){
        int to = -1;
        //Switch Based On From Color
        switch (pattern[from]){
            case ORANGE:
                //Choose White Destination From Orange Vertex
                //Strategy Implementation...
                to = (logic.getVertexInfo(2) == GameType.WOLF || (logic.getVertexInfo(10) == GameType.EMPTY))? 10: 2;
                break;
            case PURPLE:
                //Choose White Destination From Purple Vertex
                //Strategy Implementation...
                if(pattern[from+1] == ColorPattern.WHITE)
                    to = from+1;
                else
                {
                    if(from == 4)
                        to = (logic.getVertexInfo(0) == GameType.WOLF)? 8: 0;
                    else
                        to = (logic.getVertexInfo(2) == GameType.WOLF)? 10: 2;
                }
                break;
        }
        return to;
    }

}
