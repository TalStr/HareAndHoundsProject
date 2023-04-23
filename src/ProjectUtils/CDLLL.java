package ProjectUtils;

import java.io.Serializable;

public class CDLLL implements Serializable {
    public CDLLL prev;
    public CDLLL next;
    public Move move;
    public CDLLL(int from, int to){
        prev = this;
        next = this;
        move = new Move(from,to);
    }
    public CDLLL(){
        prev = this;
        next = this;
        move = null;
    }
    public void add(int from, int to)
    {
        CDLLL temp = new CDLLL(from, to);
        temp.next = this;
        temp.prev = this.prev;
        temp.prev.next = temp;
        this.prev = temp;
    }
    public void deleteLast()
    {
        if(this.next == this){
            this.move = null;
        }
        CDLLL temp = this.prev;
        this.prev = temp.prev;
        this.prev.next = this;
    }
}
