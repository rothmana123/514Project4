import java.util.List;
import java.util.Scanner;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.*;

public class WOFAI2 extends WheelofFortuneA{
    private StringBuilder phrase;
    public static StringBuilder hiddenPhrase;
    private int wrongAnswers;
    private static Scanner scanner = new Scanner(System.in);
    private StringBuilder previousGuesses;

    //list of answers
    private List<String> phrases;

    private AllGameRecord allGameRecords;
    private int playerId;

    //how can I pass in a default AI player?
    private WOFInterface player;
    //private WOFIVowelFirst player;
    public static int index = 0;

    public ArrayList<WOFInterface> WOFPlayers;

    //default constructor
    public WOFAI2(AllGameRecord allGameRecords) {
        System.out.println("This is the default implementation with WOFI Random");
        this.player = new WOFIRandom();
        //this.player = new WOFIVowelFirst();
        this.phrases = readPhrases();
        resetGame();
        this.playerId = player.playerId();
        this.allGameRecords = allGameRecords;
        System.out.println("The default playerId is " + playerId);
        playAll(player);
    }

    //constructor allows user to choose the player
    public WOFAI2(AllGameRecord allGameRecords, WOFInterface player) {
        System.out.println("This is implementation allows user to choose Concrete WOF Interface Implementation");
        //this.player = player;
        this.phrases = readPhrases();
        resetGame();
        this.playerId = player.playerId();
        this.allGameRecords = allGameRecords;
        System.out.println("The default playerId is " + playerId);
        playAll(player);
    }

    // and one should accept a list of WheellOfFortunePlayers.
    //constructor allows user to choose the player
    public WOFAI2(AllGameRecord allGameRecords, ArrayList<WOFInterface> WOFPlayers) {
        System.out.println("This implementation runs a list of Concrete WOF Interface Implementations");
        this.WOFPlayers = WOFPlayers;
        this.phrases = readPhrases();
        resetGame();
        //this.playerId = player.playerId();
        this.allGameRecords = allGameRecords;
        System.out.println("The default playerId is " + playerId);
        for (WOFInterface player : WOFPlayers) {
            this.playerId = player.playerId();
            playAll(player);
            this.phrases = readPhrases();// Play all games for the current player
            resetGame();
        }
    }

    public void resetGame(){
        // Reset fields for a new game
        this.phrase = randomPhrase();
        this.hiddenPhrase = generateHiddenPhrase(phrase);
        this.wrongAnswers = 10;
        //this.previousGuesses.setLength(0); // Clear previous guesses
        this.previousGuesses = new StringBuilder("");
    }
    //    @Override
    public AllGameRecord playAll(WOFInterface player) {
        while (true) {
            //need to refactor to Play on the WOFI object player passed in
            //what does playGameRandom do?
            System.out.println("Current playerId: " + playerId);
            play(player);
            if (playNext()) {
                resetGame();
            } else {
                break;
            }
        }
        //should make a diagram of how all the methods modify allGameRecords to track data flow
        return allGameRecords;
    }

    //refactor to call play() on WOFI object
    public void play(WOFInterface player) {
        WOFAI2.index = 0;
        System.out.println(hiddenPhrase.toString());
        while(true) {
            //get user guess
            System.out.println();
            String guess = player.getGuess(previousGuesses);
            System.out.println("Player guessed " + guess);

            //keep track of wrong answers and adapt hiddePhrase
            processGuess(guess);
            //System.out.println(hiddenPhrase);

            //check win condition
            if(checkWin()){
                System.out.println("You won!");
                recordGame(true);
                break;
            } else if (wrongAnswers <= 0) {
                System.out.println("You lost!");
                recordGame(false);
                break;
            }
        }
    }

    @Override
    public AllGameRecord playAll() {
        return null;
    }

    @Override
    public void play() {}

    //Play Next for Default AI
    @Override
    public boolean playNext() {
        return phrases.size() > 0;
    }

    public Boolean checkWin(){
        return (phrase.toString().equals(hiddenPhrase.toString()));
    }

    private void recordGame(boolean won) {
        int score = calculateScore(won);
        GameRecord gameRecord = new GameRecord(score, playerId);
        System.out.println(gameRecord.score);
        allGameRecords.add(gameRecord);
        System.out.println("Game recorded with score: " + score);
    }

    private int calculateScore(boolean won) {
        return won ? wrongAnswers * 10 : 0;
        // scoring logic break down:
        // if won true, return wrongAnswers * 10, else 0;
    }

    @Override
    public List<String> readPhrases(){
        List<String> phraseList = new ArrayList<>();
        try {
            phraseList = Files.readAllLines(Paths.get("phrases.txt"));
        } catch (IOException e) {
            System.out.println(e);
        }
        return phraseList;
    }

    @Override
    public StringBuilder randomPhrase() {
        if (phrases.isEmpty()) {
            System.out.println("No more phrases available!");
            return null;
        }

        Random random = new Random();
        int index = random.nextInt(phrases.size());
        String selectedPhrase = phrases.remove(index); // Select and remove the phrase
        return new StringBuilder(selectedPhrase);
    }

    public StringBuilder generateHiddenPhrase(StringBuilder phrase){
        StringBuilder hiddenPhrase = new StringBuilder();
        char letter;
        for (int i = 0; i < phrase.length(); i++){
            letter = phrase.charAt(i);
            if(letter == ' '){
                hiddenPhrase.append(' ');
            }
            else {
                hiddenPhrase.append("*");
            }
        }
        return hiddenPhrase;
    }

    public void processGuess(String guess){
        String checkLetter = guess.substring(0, 1);
        if(phrase.indexOf(checkLetter) != -1) {
            for (int i = 0; i < phrase.length(); i++) {
                if (checkLetter.charAt(0) == phrase.charAt(i)) {
                    hiddenPhrase.replace(i, i + 1, checkLetter);
                }
            }
        }
        else {
            wrongAnswers--;
            System.out.println("Nope.  Wrong Answers left: " + wrongAnswers);
        }
    }

    @Override
    public String getGuess(StringBuilder previousGuesses) {
        while (true) {
            System.out.println("You have already guessed these letters: " + previousGuesses);
            System.out.println(hiddenPhrase);
            System.out.println("Guess a Letter");
            String guessString = scanner.nextLine().toLowerCase();

            // Check to make sure input is 1 character
            if (!(guessString.length() == 1)) {
                System.out.println("Please enter only 1 letter");
                continue;
            }

            //make sure letter has not been guessed already
            if (previousGuesses.indexOf(guessString) != -1) {
                System.out.println("You already guess that letter");
                System.out.println();
                continue;
            } else {
                previousGuesses.append(guessString);
            }

            //change String to Char
            char guessLetter = guessString.charAt(0);

            //Check to make sure entry is a letter
            if (!Character.isLetter(guessLetter)) {
                System.out.println("Only guess a letter");
                continue;
            }

            //convert back to String
            return Character.toString(guessLetter);
            ///next move is to process guessLetter through Process Guess
        }
    }

    public static StringBuilder getHiddenPhrase(){
        return hiddenPhrase;
    }

    public static void main(String[] args) {
        AllGameRecord allGames = new AllGameRecord();
        WOFAI2 game = new WOFAI2(allGames);

        System.out.println("All games played:");
        for(GameRecord record : allGames.listOfGameRecords) {
            System.out.println("Player ID: " + record.playerId + ", Score: " + record.score);
        }

        while(true) {
            System.out.println("Choose a Player: 1. Random 2. Vowel First 3. Common Letters");
            int choice = scanner.nextInt();
            if (choice == 1) {
                WOFIRandom random = new WOFIRandom();
                WOFAI2 game1 = new WOFAI2(allGames, random);
                break;
            } else if (choice == 2) {
                WOFIVowelFirst vowel = new WOFIVowelFirst();
                WOFAI2 game1 = new WOFAI2(allGames, vowel);
                break;
            } else if (choice == 3) {
                WOFICommon common = new WOFICommon();
                WOFAI2 game1 = new WOFAI2(allGames, common);
                break;
            } else {
                System.out.println("Answer must be 1, 2 or 3");
                System.out.println("Choose a Player: 1. Random 2. Vowel First 3. Common Letters");
            }
        }
        System.out.println("All games played:");
        for(GameRecord record : allGames.listOfGameRecords) {
            System.out.println("Player ID: " + record.playerId + ", Score: " + record.score);
        }
        System.out.println(AllGameRecord.getPlayerId());

        ArrayList<WOFInterface> wofIArray = new ArrayList<>();
        WOFIRandom random = new WOFIRandom();
        WOFICommon common = new WOFICommon();
        WOFIVowelFirst vowel = new WOFIVowelFirst();
        wofIArray.add(random);
        System.out.println(random.playerId());
        wofIArray.add(common);
        System.out.println(common.playerId());
        wofIArray.add(vowel);
        System.out.println(vowel.playerId());
        System.out.println(AllGameRecord.getPlayerId());
        WOFAI2 game2 = new WOFAI2(allGames, wofIArray);
        System.out.println("All games played:");
        for(GameRecord record : allGames.listOfGameRecords) {
            System.out.println("Player ID: " + record.playerId + ", Score: " + record.score);
        }
        System.out.println("The average of all scores is " + AllGameRecord.average(allGames.listOfGameRecords));
        System.out.println("The average of all scores for Player 4 is " + AllGameRecord.playerAverage(4));
        System.out.println("The first 5 high scores are " + AllGameRecord.highGameList(allGames.listOfGameRecords, 5));
        System.out.println("The 2 highest scores for player 5 are " + AllGameRecord.highGameListPlayer(5, allGames.listOfGameRecords, 2));

    }
}

