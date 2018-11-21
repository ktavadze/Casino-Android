package edu.ramapo.ktavadze.casino;

public class Tournament {
    private Computer mComputer;
    private Human mHuman;
    private Round mRound;

    public Tournament(boolean aHumanTurn) {
        mComputer = new Computer(!aHumanTurn);
        mHuman = new Human(aHumanTurn);
        mRound = new Round(1);
    }

    public Tournament(Computer aComputer, Human aHuman, Round aRound) {
        mComputer = aComputer;
        mHuman = aHuman;
        mRound = aRound;
    }

    public Computer getComputer() {
        return mComputer;
    }

    public Human getHuman() {
        return mHuman;
    }

    public Round getRound() {
        return mRound;
    }

    /**
     Updates the tournament between rounds.
     */
    public void update() {
        // Clear piles
        mHuman.getPile().clear();
        mComputer.getPile().clear();

        // Set turn
        if (mHuman.capturedLast()) {
            mHuman.isNext(true);
            mComputer.isNext(false);
        }
        else {
            mHuman.isNext(false);
            mComputer.isNext(true);
        }

        mRound = new Round(mRound.getNumber() + 1);
    }

    /**
     Checks whether the tournament is over.
     @return Boolean value representing the result of the check.
     */
    public boolean isOver() {
        if (mComputer.getScore() >= 21 || mHuman.getScore() >= 21) {
            return true;
        }

        return false;
    }
}
