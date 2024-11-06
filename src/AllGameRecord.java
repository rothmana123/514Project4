//Implement add(GameRecord) to add game results.
//Implement average() and average(playerId) to compute average scores.
//Define highGameList(n) and highGameList(playerId, n) to retrieve sorted lists of top scores, using Collections.sort().

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;

public class AllGameRecord {
    ArrayList<GameRecord> listOfGameRecords;
    public static int playerId = 0;

    //constructor
    public AllGameRecord(){
        listOfGameRecords = new ArrayList<GameRecord>();
    }

    //return current playerId
    //wait, is this necessary?
    //also in GameRecord...
    public static int getPlayerId(){
        return playerId;
    }

    //generate unique player Id
    public static int generateNewPlayerId() {
        playerId++;
        return playerId;
    }

    //add game to list of records
    public void add(GameRecord game){
        listOfGameRecords.add(game);
    }

    //
    public float average(ArrayList<GameRecord> games){
        int sum = 0;
        int length = games.size();
        for(GameRecord game : games){
            sum+=game.score;
        }
        return (float) sum /length;
    }

    public float playerAverage(int playerId){
        ArrayList<GameRecord> gamesByPlayerID = new ArrayList<>();
        for(GameRecord game : listOfGameRecords){
            if(game.playerId == playerId){
                gamesByPlayerID.add(game);
            }
        }
        return this.average(gamesByPlayerID);
    }

    public ArrayList<GameRecord> highGameList(ArrayList<GameRecord> arrayOfGames, int n){
        Collections.sort(arrayOfGames);

        // Ensures we don't exceed the list size if n is greater than arrayOfGames size
       // n = Math.min(n, arrayOfGames.size());
        return new ArrayList<>(arrayOfGames.subList(0, n));
        //return arrayOfGames;
    }

    public ArrayList<GameRecord> highGameList(int playerId, ArrayList<GameRecord> arrayOfGames, int n){
        ArrayList<GameRecord> gamesByPlayerID = new ArrayList<>();
        for(GameRecord game : arrayOfGames){
            if(game.playerId == playerId){
                gamesByPlayerID.add(game);
            }
        }
        return highGameList(gamesByPlayerID, n);
    }



}
