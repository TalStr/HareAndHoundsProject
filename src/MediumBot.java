import java.util.ArrayList;
import java.util.Random;

public class MediumBot extends GameBot{
    EasyBot dumb = new EasyBot(super.listener, super.playing, super.logic);
    HardBot smart = new HardBot(super.listener, super.playing, super.logic);
    public MediumBot(BotListener listener, GameType playing, GameLogic logic){
        super(listener, playing, logic);
    }
    @Override
    public void makeMove()
    {
        Random rand = new Random();
        if(rand.nextInt(10) < 2){
            System.out.println("dumb");
            dumb.makeMove();
        }
        else{
            System.out.println("smart");
            smart.makeMove();
        }
    }

}
