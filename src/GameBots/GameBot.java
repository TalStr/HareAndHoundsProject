package GameBots;

import GameEnums.GameType;
import LogicClasses.GameLogic;

public class GameBot {
    BotListener listener;
    GameType playing;
    GameLogic logic;
    public GameBot(BotListener listener, GameType playing, GameLogic logic) {
        this.listener = listener;
        this.playing = playing;
        this.logic = logic;
    }
    public void makeMove(){

    }
    public GameType getPlaying(){
        return(this.playing);
    }
}
