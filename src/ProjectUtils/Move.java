package ProjectUtils;

import java.io.Serializable;

public class Move implements Serializable {
    public int from;
    public int to;
    public Move(int from, int to){
        this.from = from;
        this.to = to;
    }
}
