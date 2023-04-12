import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class SimulatedGame implements BotListener{
    GameLogic logic = new GameLogic();
    GameBot wolfBot;
    GameBot rabbitBot;
    public SimulatedGame(int wolfDifficulty, int rabbitDifficulty)
    {
        makeBots(wolfDifficulty,rabbitDifficulty);
        simulate();
        logic.saveGameLog("Sim1");
        new ReviewBoard(logic.getLog());
    }
    private void simulate()
    {
        while(!logic.checkGameOver())
        {
            if(logic.getCurrentPlayer() == GameType.WOLF)
                wolfBot.makeMove();
            else
                rabbitBot.makeMove();
            logic.nextTurn();
        }
    }
    public void move(int from, int to)
    {
        logic.moveAnimal(from, to, false);
    }
    @Override
    public void onBotMove(int from, int to) {
        move(from, to);
    }
    public void makeBots(int wolfDiff, int rabbitDiff)
    {
        switch (wolfDiff) {
            case 1 -> this.wolfBot = new EasyBot(this, GameType.WOLF, logic);
            case 2 -> this.wolfBot = new MediumBot(this, GameType.WOLF, logic);
            case 3 -> this.wolfBot = new HardBot(this, GameType.WOLF, logic);
        }
        switch (rabbitDiff) {
            case 1 -> this.rabbitBot = new EasyBot(this, GameType.RABBIT, logic);
            case 2 -> this.rabbitBot = new MediumBot(this, GameType.RABBIT, logic);
            case 3 -> this.rabbitBot = new HardBot(this, GameType.RABBIT, logic);
        }
    }
}
