public class GameState {
    private GameType[] gamePositions = new GameType[]
            {GameType.WOLF, GameType.EMPTY, GameType.EMPTY,
                    GameType.WOLF, GameType.EMPTY, GameType.EMPTY,
                    GameType.EMPTY, GameType.RABBIT, GameType.WOLF, GameType.EMPTY, GameType.EMPTY};
    private int[] wolvesLoc = new int[] {0, 3, 8};
    private int rabbitLoc =  7;
    private GameType currentPlayer = GameType.WOLF;
    private int remainingMoves = 30;
    public GameType[] getPositions()
    {
        return this.gamePositions;
    }
    public GameType getVertexInfo(int vertexID){
        return this.gamePositions[vertexID];
    }
    public void setVertexInfo(int vertexID, GameType to){
        this.gamePositions[vertexID] = to;
    }
    public int[] getWolvesVertices(){
        return this.wolvesLoc;
    }
    public int getWolfVertex(int index){
        return wolvesLoc[index];
    }
    public void setWolfVertex(int index, int vertexID){
        this.wolvesLoc[index] = vertexID;
    }
    public int getRabbitVertex(){
        return this.rabbitLoc;
    }
    public void setRabbitVertex(int vertexID){
        this.rabbitLoc = vertexID;
    }
    public GameType getCurrentPlayer(){
        return this.currentPlayer;
    }
    public void setCurrentPlayer(GameType type){
        this.currentPlayer = type;
    }
    public void useMoves(int moves){
        this.remainingMoves -= moves;
    }
    public void addMoves(int moves){
        this.remainingMoves += moves;
    }
    public int getRemainingMoves(){
        return this.remainingMoves;
    }
    public void moveAnimal(int fromVertexID, int toVertexID)
    {
        this.setVertexInfo(toVertexID, this.getVertexInfo(fromVertexID));
        this.setVertexInfo(fromVertexID,GameType.EMPTY);
        if(this.getVertexInfo(toVertexID) == GameType.RABBIT)
            this.setRabbitVertex(toVertexID);
        else{
            for (int i = 0; i < 3; i++){
                if(this.getWolfVertex(i) == fromVertexID)
                    this.setWolfVertex(i,toVertexID);
            }
        }
    }
    public void updatePositions(GameType[] newPositions)
    {
        int wolfCounter = 0;
        for (int i = 0; i < 11; i++){
            this.gamePositions[i] = newPositions[i];
            if(newPositions[i] == GameType.WOLF)
                this.wolvesLoc[wolfCounter++] = i;
            if(newPositions[i] == GameType.RABBIT)
                this.rabbitLoc = i;
        }
    }
}
