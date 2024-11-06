//if there are m players and n phrases, Game’s playAll() should play m*n games.
//WheelOfFortuneAIGame should have three constructors.
// -One should set the WheelOfFortunePlayer to a default player,
// -one should allow the client to specify a single concrete WheelOfFortunePlayer (**Not sure what this means)
// -and one should accept a list of WheellOfFortunePlayers (**Not sure what this means)

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.*;

public class WOFAI extends WheelofFortuneA{
    private StringBuilder phrase;
    private StringBuilder hiddenPhrase;
    private int wrongAnswers;
    private static Scanner scanner = new Scanner(System.in);
    private StringBuilder previousGuesses;

    //list of answers
    private List<String> phrases;
    private List<String> commonLetters;

    private AllGameRecord allGameRecords;
    private int playerId;

    //constructor
    //will need to evolve to take in 3 concrete implementations
    //Constructors:
    //Default Player: Initializes with a default AI player.
    //Single Specified Player: Allows the client to provide a single specific AI player for the game.
    //List of Players: Accepts a list of multiple AI players, enabling you to set up a multi-player game.

    //default
    public WOFAI(AllGameRecord allGameRecords) {
        reset();
        this.playerId = playerId();
        this.allGameRecords = allGameRecords;
        this.commonLetters = new ArrayList<>(Arrays.asList( // Initialize commonLetters in constructor
                "e", "a", "r", "i", "o", "t", "n", "s", "l", "c", "u", "d", "p", "m", "h", "g", "b", "f", "y", "w", "k", "v", "x", "z", "j", "q"
        ));
    }

    public int playerId(){
        //constructor should be set to playerID method
        //should return playerID from AllGameRecord
        return AllGameRecord.getPlayerId();
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

    public void reset(){
        // Reset fields for a new game
        this.phrase = randomPhrase();
        this.hiddenPhrase = generateHiddenPhrase(phrase);
        this.wrongAnswers = 5;
        //this.previousGuesses.setLength(0); // Clear previous guesses
        this.previousGuesses = new StringBuilder("");
    }

    //*rework this so that it shows the inhereted methods in order
    //Start with Game
    //-playAll
    //-play
    //-playNext
    //-***How to refactor PlayAll to account for a list
    @Override
    public AllGameRecord playAll() {
        while (true) {
            play();
            if (playNext()) {
                System.out.print("Continue as same Player? (y/n): ");
                String response = scanner.nextLine().trim().toLowerCase();
                if(response.equals("n")){
                    this.playerId = AllGameRecord.generateNewPlayerId();
                    System.out.println("checking to see if playerId changed " + this.playerId);
                }
                reset();
            } else {
                break;
            }
        }
        //should make a diagram of how all the methods modify allGameRecords to track data flow
        return allGameRecords;
    }

    @Override
    public void play() {
        while (true) {
            System.out.println(hiddenPhrase.toString());
            //get user guess
            System.out.println();
            String guess = this.getGuess(previousGuesses);
            System.out.println("Player guessed " + guess);

            //keep track of wrong answers and adapt hiddePhrase
            processGuess(guess);
            //System.out.println(hiddenPhrase);

            //check win condition
            if (checkWin()) {
                System.out.println("You won!");
                recordGame(true);
                break;
            } else if (wrongAnswers <= 0) {
                System.out.println("You lost!");
                System.out.println("This is where Record Game should be called");
                //
                recordGame(false);
                break;
            }
        }
    }

    @Override
    public boolean playNext() {
        System.out.print("Would you like to play another game? (y/n): ");
        String response = scanner.nextLine().trim().toLowerCase();
        return response.equals("y");
    }

    //Inhereted Methods from WOFA
    //readPhrases, randomPhrase, generateHiddenPhrase, processGuess, getGuess

    //readPhrases
    @Override
    public List<String> readPhrases(){
        //List<String> phraseList = null;
        //you could initialize phraseList as null in option 1, but there's a better approach that avoids potential
        // NullPointerException issues. Instead, you can initialize phraseList as an empty ArrayList to ensure it’s never null.
        // This way, even if reading the file fails, you’ll still return an empty list instead of null, which is safer for the caller.
        List<String> phraseList = new ArrayList<>();
        try {
            phraseList = Files.readAllLines(Paths.get("phrases.txt"));
        } catch (IOException e) {
            System.out.println(e);
        }
        return phraseList;
    }
    //randomPhrase
    @Override
    public StringBuilder randomPhrase() {
        if (phrases.isEmpty()) {
            System.out.println("No more phrases available!");
            return null;
        }

        //String[] phrases = readPhrases().toArray(new String[0]);
        Random random = new Random();
        int index = random.nextInt(phrases.size());
        String selectedPhrase = phrases.remove(index); // Select and remove the phrase
        return new StringBuilder(selectedPhrase);
//        int index = random.nextInt(phrases.length);
//        return new StringBuilder(phrases[index]);
    }

    //generateHiddenPhrase` -- returns the initial hidden phrase.
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
    //processGuess
    public void processGuess(String guess){

        //get the character to check against phrase
        String checkLetter = guess.substring(0, 1);

        //check if char in string first.  If yes, run the for loop to modify hiddenPhrase
        //else update wrongAnswers and return
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

    //getGuess
    @Override
    public String getGuess(StringBuilder previousGuesses){
        while(true) {
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
            if(previousGuesses.indexOf(guessString) != -1){
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


    //getGuessBot
    //return a random letter from a-z
    //use ascii value...
    //use Random class, create a variable from a range, set to a letter
    //check if its already been guess
    //return letter
    public String getGuessBotRandom(){
        while(true) {
            System.out.println("You have already guessed these letters: " + previousGuesses);
            System.out.println(hiddenPhrase);
            System.out.println("Guess a Letter");
            Random random = new Random();
            char randomLowercaseLetter = (char) (random.nextInt(26) + 97);
            //char randomLowercaseLetter = (char) (random.nextInt(97, 124));
            String letter = Character.toString(randomLowercaseLetter);
            if(previousGuesses.indexOf(letter) != -1){
                System.out.println("You already guess that letter");
                System.out.println();
                continue;
            }
            previousGuesses.append(letter);
            return letter;
        }
    }

    public String getGuessBotSmart(){
        System.out.println("You have already guessed these letters: " + previousGuesses);
        System.out.println(hiddenPhrase);
        System.out.println("Guess a Letter");

        String letter = commonLetters.get(0);
        commonLetters.remove(0);  // Remove the guessed letter
        previousGuesses.append(letter);
        return letter;
    }



    //turn this into a class
    public void playGameRandom(){
        System.out.println(hiddenPhrase.toString());

        while(true) {
            //get user guess
            System.out.println();
            String guess = this.getGuessBotRandom();
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
                System.out.println("This is where Record Game should be called");
                recordGame(false);

                break;
            }
        }
    }

    //turn this into a class
    public void playGameSmart(){
        System.out.println(hiddenPhrase.toString());

        while(true) {
            //get user guess
            System.out.println();
            String guess = this.getGuessBotSmart();
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

    public static void main(String[] args){
        AllGameRecord allGames = new AllGameRecord();
        WOFAI game = new WOFAI(allGames);
        //System.out.println(game.playerId);  //checking to see player id was assigned
        game.playAll();
//        while(true) {
//            System.out.println("Enter 1 for Random Bot or 2 for Smart Bot");
//            int gameType = scanner.nextInt();
//            if (gameType == 1) {
//                game.playGameRandom();
//                break;
//            } else if (gameType == 2) {
//                game.playGameSmart();
//                break;
//            } else {
//                continue;
//            }
//        }

        System.out.println("All games played:");
        for(GameRecord record : allGames.listOfGameRecords) {
            System.out.println("Player ID: " + record.playerId + ", Score: " + record.score);
        }
    }
}


//        this.phrase = randomPhrase();
//        this.hiddenPhrase = generateHiddenPhrase(phrase);
//        this.wrongAnswers = 5;
//        this.previousGuesses = new StringBuilder("");


