import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class WOFIVowelFirst implements WOFInterface{
    public int playerId;
    private List<String> vowelFirstLetters;

    public WOFIVowelFirst(){
        AllGameRecord.generateNewPlayerId();
        this.playerId = AllGameRecord.getPlayerId();
        this.vowelFirstLetters = new ArrayList<>(Arrays.asList("e", "a", "i", "o", "u", "r", "t", "n", "s", "l", "c", "u", "d", "p", "m", "h", "g", "b", "f", "y", "w", "k", "v", "x", "z", "j", "q"));
    }

    @Override
    public String getGuess(StringBuilder previousGuesses) {
        while (true) {
            System.out.println("You have already guessed these letters: " + previousGuesses);
            System.out.println(WOFAI2.getHiddenPhrase());
            System.out.println("Guess a Letter");
            String letter = vowelFirstLetters.get(WOFAI2.index);
            WOFAI2.index++;
            previousGuesses.append(letter);
            return letter;
        }
    }

    @Override
    public int playerId() {
        return this.playerId;
    }

    @Override
    public void reset() {
        playerId = AllGameRecord.generateNewPlayerId();
    }
}
