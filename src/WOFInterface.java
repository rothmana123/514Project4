public interface WOFInterface {
    String getGuess(StringBuilder previousGuesses);  // get the next guess from the player
    int playerId();  // an id for the player
    void reset(); // reset the player to start a new game
}
