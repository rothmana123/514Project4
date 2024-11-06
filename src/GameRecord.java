import java.util.Collections;
//does this need to extend AllGameRecord?

public class GameRecord implements Comparable<GameRecord>{
    int score;
    int playerId;

    public GameRecord(int score, int playerId){
        this.score = score;
        this.playerId = playerId;
    }

//    public int getPlayerID(boolean newPlayer, int playerId){
//        if(!(newPlayer)){
//            return playerId;
//        } else {
//            return AllGameRecord.generateNewPlayerId();
//        }
//    }

    @Override
    public int compareTo(GameRecord other) {
        return Integer.compare(this.score, other.score);
    }

}
