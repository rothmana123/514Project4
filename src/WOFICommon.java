import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class WOFICommon implements WOFInterface{
    public int playerId;
    private List<String> commonLetters;

    public WOFICommon() {
        AllGameRecord.generateNewPlayerId();
        this.playerId = AllGameRecord.getPlayerId();
        this.commonLetters = new ArrayList<>(Arrays.asList("e", "a", "r", "i", "o", "t", "n", "s", "l", "c", "u", "d", "p", "m", "h", "g", "b", "f", "y", "w", "k", "v", "x", "z", "j", "q"));
    }

    @Override
    public String getGuess(StringBuilder previousGuesses) {
        while (true) {
            System.out.println("You have already guessed these letters: " + previousGuesses);
            System.out.println(WOFAI2.getHiddenPhrase());
            System.out.println("Guess a Letter");
            String letter = commonLetters.get(WOFAI2.index);
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
