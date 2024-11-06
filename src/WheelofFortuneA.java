//Create the Abstract WheelOfFortune Class

import java.util.ArrayList;
import java.util.List;

abstract class WheelofFortuneA extends Game {
    //doesnt need to implement WOFI
    public abstract List readPhrases();

    public abstract StringBuilder randomPhrase();
    public abstract StringBuilder generateHiddenPhrase(StringBuilder phrase);
    public abstract void processGuess(String guess);

    public abstract String getGuess(StringBuilder previousGuesses);

    //public AllGameRecord playAll(); -> from Game class
    //public abstract int playerId();  -> implements in WOF User

    //public abstract void reset(); -> implements in WOF User
}
