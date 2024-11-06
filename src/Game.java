//Define the abstract methods play()
// (to be implemented in subclasses for playing
// a single game) and
// playNext() (to check if another game should be played).

abstract class Game{

    //need to work on this one
    //Create playAll() to play multiple games and return an AllGamesRecord.
    //so, start with playAll() --> calls play
    //when game is over, should:
    //store game Record
    // ask to play new game --> playNext()
    //ask if new user
    public abstract AllGameRecord playAll();

    public abstract void play();

    public abstract boolean playNext();

}
