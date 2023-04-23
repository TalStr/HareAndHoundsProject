package GameBots;

import GameEnums.GameType;
import LogicClasses.GameLogic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class EasyBot extends GameBot{
    public EasyBot(BotListener listener, GameType playing, GameLogic logic){
        super(listener, playing, logic);
    }
    @Override
    public void makeMove() {
        int from;
        int to;
        int rnd;
        ArrayList<Integer> possibleMoves;
        if(playing == GameType.RABBIT) {
            from = logic.getRabbitVertexID();
            possibleMoves = logic.getPossibleMoves(from);
            rnd = new Random().nextInt(possibleMoves.size());
            to = possibleMoves.get(rnd);
        }
        else
        {
            from = getRandWolf();
            possibleMoves = logic.getPossibleMoves(from);
            rnd = new Random().nextInt(possibleMoves.size());
            to = possibleMoves.get(rnd);
        }
        if (super.listener != null)
            super.listener.onBotMove(from, to);

    }
    private int getRandWolf(){
        ArrayList<Integer> options = new ArrayList<>(Arrays.stream(logic.getWolvesVertexIDs()).boxed().toList());
        int count = 3;
        int rnd = new Random().nextInt(count);
        while(options.size() != 0 && logic.getPossibleMoves(options.get(rnd)).size() == 0)
        {
            options.remove(rnd);
            count--;
            rnd = new Random().nextInt(count);
        }
        return (options.size() == 0)? -1: options.get(rnd);
    }

}
