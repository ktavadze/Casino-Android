package edu.ramapo.ktavadze.casino;

import java.util.HashMap;

public class Round {
    private int mNumber;
    private Table mTable;
    private Deck mDeck;
    private HashMap<String, Integer> mResults = new HashMap<>();

    public Round(int aNumber) {
        mNumber = aNumber;
        mTable = new Table();
        mDeck = new Deck();
    }

    public Round(int aNumber, Table aTable, Deck aDeck) {
        mNumber = aNumber;
        mTable = aTable;
        mDeck = aDeck;
    }

    public int getNumber() {
        return mNumber;
    }

    public Table getTable() {
        return mTable;
    }

    public Deck getDeck() {
        return mDeck;
    }

    public void setDeck(Deck aDeck) {
        mDeck = aDeck;
    }

    public HashMap<String, Integer> getResults() {
        return mResults;
    }

    /**
     Starts the round.
     @param aComputer - Player (computer) instance to reference.
     @param aHuman - Player (human) instance to reference.
     */
    public void start(Player aComputer, Player aHuman) {
        if (aComputer.getHand().isEmpty() && aHuman.getHand().isEmpty()) {
            // Deal players
            aHuman.setHand(mDeck.drawSet());
            aComputer.setHand(mDeck.drawSet());

            // Deal table
            if (aComputer.getPile().isEmpty() && aHuman.getPile().isEmpty()) {
                if (mTable.getLooseSet().isEmpty() && mTable.getBuilds().isEmpty()) {
                    mTable.setLooseSet(mDeck.drawSet());
                }
            }
        }
    }

    /**
     Allows the players to make a move.
     @param aComputer - Player (computer) instance to reference.
     @param aHuman - Player (human) instance to reference.
     @param aMove - Move instance to reference.
     @return Boolean value representing whether a legal move was made.
     */
    public boolean processMove(Player aComputer, Player aHuman, Move aMove) {
        if (aHuman.isNext()) {
            int moveCode = aHuman.makeMove(mTable, aMove);

            switch (moveCode) {
                case 1:
                    aHuman.capturedLast(true);
                    aComputer.capturedLast(false);
                case 0:
                    aHuman.isNext(false);
                    aComputer.isNext(true);

                    update(aComputer, aHuman);

                    return true;
                default:
                    return false;
            }
        }
        else {
            int moveCode = aComputer.makeMove(mTable, aMove);

            switch (moveCode) {
                case 1:
                    aHuman.capturedLast(false);
                    aComputer.capturedLast(true);
                case 0:
                    aHuman.isNext(true);
                    aComputer.isNext(false);

                    update(aComputer, aHuman);

                    return true;
                default:
                    return false;
            }
        }
    }

    /**
     Checks whether the round is over.
     @param aComputer - Player (computer) instance to reference.
     @param aHuman - Player (human) instance to reference.
     @return Boolean value representing the result of the check.
     */
    public boolean isOver(Player aComputer, Player aHuman) {
        if (mDeck.isEmpty()) {
            if (aComputer.getHand().isEmpty() && aHuman.getHand().isEmpty()) {
                return true;
            }
        }

        return false;
    }

    /**
     Generates a string representation of the deck.
     @param aComputer - Player (computer) instance to reference.
     @param aHuman - Player (human) instance to reference.
     @return String value representing the round.
     */
    public String stringify(Player aComputer, Player aHuman) {
        String data = "";

        data += "Round: " + mNumber + "\n";

        data += "\nComputer:" + aComputer.stringify() + "\n";

        data += "\nHuman:" + aHuman.stringify() + "\n";

        data += "\nTable: " + mTable.stringify() + "\n";

        for (Build build : mTable.getBuilds()) {
            data += "\nBuild Owner: " + build.stringify();
            if (build.isHuman()) {
                data += " Human\n";
            }
            else {
                data += " Computer\n";
            }
        }

        data += "\nDeck: " + mDeck.stringify() + "\n";

        data += "\nNext Player: ";
        if (aHuman.isNext()) {
            data += "Human";
        }
        else {
            data += "Computer";
        }

        return data;
    }

    /**
     Updates the round between turns.
     @param aComputer - Player (computer) instance to reference.
     @param aHuman - Player (human) instance to reference.
     */
    private void update(Player aComputer, Player aHuman) {
        if (isOver(aComputer, aHuman)) {
            // Clear loose set
            if (!mTable.getLooseSet().isEmpty()) {
                if (aHuman.capturedLast()) {
                    aHuman.getPile().addSet(mTable.getLooseSet());
                }
                else {
                    aComputer.getPile().addSet(mTable.getLooseSet());
                }

                mTable.getLooseSet().clear();
            }

            // Update scores
            updateScores(aComputer, aHuman);
        }
        else if (aComputer.getHand().isEmpty() && aHuman.getHand().isEmpty()) {
            // Deal players
            aHuman.getHand().addSet(mDeck.drawSet());
            aComputer.getHand().addSet(mDeck.drawSet());
        }
    }

    /**
     Updates the scores between rounds.
     @param aComputer - Player (computer) instance to reference.
     @param aHuman - Player (human) instance to reference.
     */
    private void updateScores(Player aComputer, Player aHuman) {
        int computerScore = 0;
        int humanScore = 0;

        Set computerPile = aComputer.getPile();
        Set humanPile = aHuman.getPile();

        // Check pile sizes
        if (computerPile.getSize() > humanPile.getSize()) {
            computerScore += 3;
        }
        else if (computerPile.getSize() < humanPile.getSize()) {
            humanScore += 3;
        }

        // Count spades
        int computerSpades = 0;
        int humanSpades = 0;

        for (Card card : computerPile.getCards()) {
            if (card.isSpade()) {
                computerSpades++;
            }
        }

        for (Card card : humanPile.getCards()) {
            if (card.isSpade()) {
                humanSpades++;
            }
        }

        // Check spade counts
        if (computerSpades > humanSpades) {
            computerScore += 1;
        }
        else if (computerSpades < humanSpades) {
            humanScore += 1;
        }

        // Check DX
        Card tenOfDiamonds = new Card("DX");

        if (computerPile.contains(tenOfDiamonds)) {
            computerScore += 2;
        }
        else if (humanPile.contains(tenOfDiamonds)) {
            humanScore += 2;
        }

        // Check S2
        Card twoOfSpades = new Card("S2");

        if (computerPile.contains(twoOfSpades)) {
            computerScore += 1;
        }
        else if (humanPile.contains(twoOfSpades)) {
            humanScore += 1;
        }

        // Check aces
        for (Card card : computerPile.getCards()) {
            if (card.isAce()) {
                computerScore += 1;
            }
        }

        for (Card card : humanPile.getCards()) {
            if (card.isAce()) {
                humanScore += 1;
            }
        }

        // Update scores
        aComputer.setScore(aComputer.getScore() + computerScore);
        aHuman.setScore(aHuman.getScore() + humanScore);

        // Update results
        mResults.put("computerPile", computerPile.getSize());
        mResults.put("computerSpades", computerSpades);
        mResults.put("computerScore", computerScore);
        mResults.put("humanPile", humanPile.getSize());
        mResults.put("humanSpades", humanSpades);
        mResults.put("humanScore", humanScore);
    }
}
