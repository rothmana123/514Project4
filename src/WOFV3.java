//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
import java.util.Random;
import java.util.Scanner;

public class WOFV3 {
    //initialize instance variables - what will we use over and over in program?
    //data members
    private StringBuilder phrase;
    private StringBuilder hiddenPhrase;
    private int wrongAnswers;
    private static Scanner scanner = new Scanner(System.in);
    private StringBuilder previousGuesses;

    //constructor
    public WOFV3() {
        this.phrase = randomPhrase();
        this.hiddenPhrase = generateHiddenPhrase(phrase);
        this.wrongAnswers = 5;
        this.previousGuesses = new StringBuilder("");
    }

    public StringBuilder randomPhrase() {
        String[] phrases = {
                "have a nice day",
                "hello world",
                "java programming",
                "wheel of fortune"
        };
        Random random = new Random();
        int index = random.nextInt(phrases.length);
        return new StringBuilder(phrases[index]);
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

    //getGuess
    public String getGuess(){
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

    public Boolean checkWin(){
        return (phrase.toString().equals(hiddenPhrase.toString()));
    }

    public void playGame(){
        System.out.println(hiddenPhrase.toString());

        while(true) {
            //get user guess
            System.out.println();
            String guess = this.getGuess();
            System.out.println("Player guessed " + guess);

            //keep track of wrong answers and adapt hiddePhrase
            processGuess(guess);
            System.out.println(hiddenPhrase);

            //check win condition
            if(checkWin()){
                System.out.println("You won!");
                break;
            } else if (wrongAnswers <= 0) {
                System.out.println("You lost!");
                break;
            }
        }
    }

    public static void main(String[] args){
        WOFV3 game = new WOFV3();
        game.playGame();
    }
}
