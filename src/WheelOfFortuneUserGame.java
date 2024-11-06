//Extend WheelOfFortuneA and implement getGuess() using Scanner to get user input.

//Override play() to handle game logic for a user.
//Implement playNext() to ask if the user wants to play another game.

//***Once a particular phrase is used, it should be discarded from the phrase list so it isn’t chosen again

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class WheelOfFortuneUserGame extends WheelofFortuneA {
    private StringBuilder phrase;
    private StringBuilder hiddenPhrase;
    private int wrongAnswers;
    private static Scanner scanner = new Scanner(System.in);
    private StringBuilder previousGuesses;

    private List<String> phrases;
    //attempting to store GameRecord Objects:
    //*this part was not intuitive for me- need more practice, make sense of
    private AllGameRecord allGameRecords;
    private int playerId;

    public WheelOfFortuneUserGame(AllGameRecord allGameRecords) {
        //how do we get playerID?
        //when is it generated?
        //dont we need to implement it from WOF I?
        this.playerId = playerId(); //call the method
        //this.playerId = playerId;

        //*this part was not intuitive for me- need more practice, make sense of
        this.allGameRecords = allGameRecords;
        //make a list of the phrases
        this.phrases = readPhrases();
        //choose one phrase
        this.phrase = randomPhrase();
        this.hiddenPhrase = generateHiddenPhrase(phrase);
        this.wrongAnswers = 5;
        this.previousGuesses = new StringBuilder("");
    }

    //@Override
    public int playerId(){
        //constructor should be set to playerID method
        //should return playerID from AllGameRecord
        return AllGameRecord.getPlayerId();
    }

    //@Override
    public void reset(){
        // Reset fields for a new game
        this.phrase = randomPhrase();
        this.hiddenPhrase = generateHiddenPhrase(phrase);
        this.wrongAnswers = 5;
        this.previousGuesses.setLength(0); // Clear previous guesses
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
                recordGame(false);
                break;
            }
        }
    }

    private void recordGame(boolean won) {
        int score = calculateScore(won);
        GameRecord gameRecord = new GameRecord(score, playerId);
        allGameRecords.add(gameRecord);
        System.out.println("Game recorded with score: " + score);
    }

    @Override
    //  ask if new user
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
    public boolean playNext() {
        System.out.print("Would you like to play another game? (y/n): ");
        String response = scanner.nextLine().trim().toLowerCase();
        return response.equals("y");
    }

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

    @Override
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

    @Override
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

    @Override
    public String getGuess(StringBuilder previousGuesses){
        while(true) {
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

    private int calculateScore(boolean won) {
        return won ? wrongAnswers * 10 : 0;
        // scoring logic break down:
        // if won true, return wrongAnswers * 10, else 0;
    }

    public Boolean checkWin(){
        return (phrase.toString().equals(hiddenPhrase.toString()));
    }

    public static void main(String[] args){
        //refactor so it just calls AllGameRecord.playAll()
        AllGameRecord allGames = new AllGameRecord();
        WheelOfFortuneUserGame game = new WheelOfFortuneUserGame(allGames);
        game.playAll();

        // Print all game records for verification
        System.out.println("All games played:");
        for(GameRecord record : allGames.listOfGameRecords) {
            System.out.println("Player ID: " + record.playerId + ", Score: " + record.score);
        }
    }
}



