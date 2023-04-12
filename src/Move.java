import java.io.Serializable;

public class Move implements Serializable {
    int from;
    int to;
    public Move(int from, int to){
        this.from = from;
        this.to = to;
    }
}
