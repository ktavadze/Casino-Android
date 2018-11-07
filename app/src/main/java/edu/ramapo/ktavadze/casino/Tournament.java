package edu.ramapo.ktavadze.casino;

public class Tournament {
    private Computer mComputer;
    private Human mHuman;
    private Round mRound;

    public Tournament(boolean aHumanTurn) {
        mComputer = new Computer(aHumanTurn);
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

    /**********************************************************************
     Function Name: start
     Purpose: To start the tournament
     **********************************************************************/
//    public void start() {
//        while (!isOver()) {
//            mRound.start(mComputer, mHuman);
//
//            // Console::displayTournamentScores(mComputer.getScore(), mHuman.getScore());
//
//            if (!isOver()) {
//                int roundNumber = mRound.getNumber() + 1;
//
//                if (mHuman.capturedLast()) {
//                    mHuman.isNext(true);
//                    mComputer.isNext(false);
//                }
//                else {
//                    mHuman.isNext(false);
//                    mComputer.isNext(true);
//                }
//
//                mRound = new Round(roundNumber);
//            }
//        }
//
//        // Console::displayTournamentResult(mComputer.getScore(), mHuman.getScore());
//    }

    /**********************************************************************
     Function Name: isOver
     Purpose: To determine whether the tournament is over
     Return Value: Whether the tournament is over, a boolean value
     **********************************************************************/
    private boolean isOver() {
        if (mComputer.getScore() >= 21 || mHuman.getScore() >= 21) {
            return true;
        }

        return false;
    }
}
