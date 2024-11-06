import java.util.*;

public class WOFIRandom implements WOFInterface {
    //private static Scanner scanner = new Scanner(System.in);
    public int playerId;

    public WOFIRandom(){
        AllGameRecord.generateNewPlayerId();
        this.playerId = AllGameRecord.getPlayerId();
    }

    @Override
    public String getGuess(StringBuilder previousGuesses) {
        while (true) {
            System.out.println("You have already guessed these letters: " + previousGuesses);
            System.out.println(WOFAI2.getHiddenPhrase());
            System.out.println("Guess a Letter");
            Random random = new Random();
            char randomLowercaseLetter = (char) (random.nextInt(26) + 97);
            //char randomLowercaseLetter = (char) (random.nextInt(97, 124));
            String letter = Character.toString(randomLowercaseLetter);
            if (previousGuesses.indexOf(letter) != -1) {
                System.out.println("You already guess that letter");
                System.out.println();
                continue;
            }
            previousGuesses.append(letter);
            return letter;
        }
    }

    @Override
    public int playerId() {
        return this.playerId;
    }

    //should this simply reset player id?
    //do I need to change reset in WOFAI2 to resetGame?
    @Override
    public void reset() {
        playerId = AllGameRecord.generateNewPlayerId();
    }
}


