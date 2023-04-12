import java.util.ArrayList;

public class HardBot extends GameBot{
    ColorPattern[] pattern = new ColorPattern[]
            {ColorPattern.WHITE, ColorPattern.PURPLE, ColorPattern.WHITE
                    ,ColorPattern.ORANGE, ColorPattern.PURPLE, ColorPattern.ORANGE, ColorPattern.PURPLE, ColorPattern.ORANGE
                    ,ColorPattern.WHITE, ColorPattern.PURPLE, ColorPattern.WHITE};
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
        int fromVertex = logic.getRabbitVertexID();
        ArrayList<Integer> possibles = logic.getPossibleMoves(fromVertex);
        int toVertex;
        if (possibles.size() == 1)
            toVertex = possibles.get(0);
        else
        {
            toVertex = bestMoveRabbit(possibles,fromVertex);
        }
        if (super.listener != null)
            super.listener.onBotMove(fromVertex, toVertex);
    }
    private int bestMoveRabbit(ArrayList<Integer> options, int from)
    {
        int toVertex = -1;
        int mPattern = -1;
        //int pPattern = -1;
        int column1 = -1;
        int column2 = -1;
        int maxPossibles = -1;
        for (int i = 0; i < options.size();i++){
            if(moveMakesPattern(from,options.get(i)) && logic.getVertexPoint(options.get(i)).col > column1)
            {
                mPattern = options.get(i);
                toVertex = options.get(i);
                column1 = logic.getVertexPoint(toVertex).col;
            }
            if(mPattern == -1 &&
                    movePreventsPattern(from,options.get(i)) && logic.getVertexPoint(options.get(i)).col > column2)
            {
                //pPattern = options.get(i);
                toVertex = options.get(i);
                column2 = logic.getVertexPoint(toVertex).col;
            }
        }
        if(toVertex == -1)
        {
            for(int i = 0; i < options.size();i++){
                if(logic.getPossibleMoves(options.get(i)).size() > maxPossibles)
                {
                    toVertex = options.get(i);
                    maxPossibles = logic.getPossibleMoves(options.get(i)).size();
                }
            }
        }
        return toVertex;
    }
    private boolean moveMakesPattern(int from, int to)
    {
        int[] colors = countColors();
        colors[pattern[from].index]--;
        colors[pattern[to].index]++;
        return (colors[0] == 2 && colors[1] == 1 && colors[2] == 1);
    }
    private boolean movePreventsPattern(int from, int to)
    {
        int[] colors = countColors();
        colors[pattern[from].index]--;
        colors[pattern[to].index]++;
        //return (colors[0] == 2 && colors[1] == 1 && colors[2] == 1);
        return (preventsIdealPattern(colors));
    }
    private boolean preventsIdealPattern(int[] colors)
    {
        return ((Math.abs(colors[0]-idealPattern[0]) > 1)
                && (Math.abs(colors[1]-idealPattern[1]) > 1)
                && (Math.abs(colors[2]-idealPattern[2]) > 1));
    }
    private void makeWolfMove(){
        int colors[] = countColors();
        //System.out.println(colors[0] + ", " + colors[1] + ", " + colors[2]);
        ColorPattern from = fromColor(colors);
        ColorPattern to = toColor(colors);
        int fromVertex;
        int toVertex = -1;
        if(from == ColorPattern.WHITE)
            fromVertex = chooseWolfOnWhite(to);
        else
            fromVertex = getWolfOnPurpleOrange(from);
        switch (to){
            case ORANGE -> toVertex=5;
            case PURPLE -> toVertex=choosePurpleDestination(fromVertex);
            case WHITE -> toVertex=chooseWhiteDestination(fromVertex);
        }
        //System.out.println(fromVertex + ", " + toVertex);
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
        switch (pattern[from]){
            case ORANGE:
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
        switch (pattern[from]){
            case ORANGE:
                to = (logic.getVertexInfo(2) == GameType.WOLF || (logic.getVertexInfo(10) == GameType.EMPTY))? 10: 2;
                break;
            case PURPLE:
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
