import GameBots.*;
import GameEnums.GameType;
import LogicClasses.GameLogic;

import java.awt.event.*;

public class SimulatedGame implements BotListener {
    GameLogic logic;
    GameBot wolfBot;
    GameBot rabbitBot;
    public SimulatedGame(GameMenu menu, boolean saveGame, boolean visible, int wolfDifficulty, int rabbitDifficulty)
    {
        logic = new GameLogic();
        makeBots(wolfDifficulty,rabbitDifficulty);
        simulate();
        if(saveGame)
        {
            logic.saveGameLog("ID-" + Integer.toString(GameCounter.getCountStat()));
            GameCounter.incrementCounter();
        }
        if(visible)
        {
            ReviewBoard reviewBoard = new ReviewBoard(logic.getLog());
            reviewBoard.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    menu.setVisible(true);
                }
            });
        }
    }
    private void simulate()
    {
        wolfBot.makeMove();
        while(!logic.checkGameOver())
        {
            logic.nextTurn();
            if(logic.getCurrentPlayer() == GameType.WOLF)
                wolfBot.makeMove();
            else
                rabbitBot.makeMove();
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
            case 0 -> this.wolfBot = new EasyBot(this, GameType.WOLF, logic);
            case 1 -> this.wolfBot = new MediumBot(this, GameType.WOLF, logic);
            case 2 -> this.wolfBot = new HardBot(this, GameType.WOLF, logic);
        }
        switch (rabbitDiff) {
            case 0 -> this.rabbitBot = new EasyBot(this, GameType.RABBIT, logic);
            case 1 -> this.rabbitBot = new MediumBot(this, GameType.RABBIT, logic);
            case 2 -> this.rabbitBot = new HardBot(this, GameType.RABBIT, logic);
        }
    }
}
