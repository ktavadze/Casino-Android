package edu.ramapo.ktavadze.casino;

public class Round {
    private int mNumber;
    private Table mTable;
    private Deck mDeck;

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

    public void startTurn(Player aComputer, Player aHuman) {
        if (aComputer.getHand().isEmpty() && aHuman.getHand().isEmpty()) {
            // TODO: Seed deck
//                if (aComputer.getPile().isEmpty() && aHuman.getPile().isEmpty()) {
//                    if (mTable.getLooseSet().isEmpty() && mTable.getBuilds().isEmpty()) {
//                        if (Console::processDeckSeed()) {
//                            Serialization::seedDeck(mDeck);
//                        }
//                    }
//                }

            // Deal players
            aHuman.setHand(mDeck.drawSet());
            aComputer.setHand(mDeck.drawSet());

            if (aComputer.getPile().isEmpty() && aHuman.getPile().isEmpty()) {
                if (mTable.getLooseSet().isEmpty() && mTable.getBuilds().isEmpty()) {
                    // Deal table
                    mTable.setLooseSet(mDeck.drawSet());
                }
            }

            // Populate piles for testing
            aHuman.mPile.addSet(mDeck.drawSet());
            aHuman.mPile.addSet(mDeck.drawSet());
            aHuman.mPile.addSet(mDeck.drawSet());
            aComputer.mPile.addSet(mDeck.drawSet());
            aComputer.mPile.addSet(mDeck.drawSet());
            aComputer.mPile.addSet(mDeck.drawSet());
        }
    }

    /**********************************************************************
     Function Name: start
     Purpose: To start the round
     Parameters:
     aComputer, a Player instance passed by reference
     aHuman, a Player instance passed by reference
     **********************************************************************/
//    public void start(Player aComputer, Player aHuman) {
//        while (!isOver(aComputer, aHuman)) {
//            if (aComputer.getHand().isEmpty() && aHuman.getHand().isEmpty()) {
//                // Seed deck
//                if (aComputer.getPile().isEmpty() && aHuman.getPile().isEmpty()) {
//                    if (mTable.getLooseSet().isEmpty() && mTable.getBuilds().isEmpty()) {
//                        if (Console::processDeckSeed()) {
//                            Serialization::seedDeck(mDeck);
//                        }
//                    }
//                }
//
//                // Deal players
//                aHuman.setHand(mDeck.drawSet());
//                aComputer.setHand(mDeck.drawSet());
//
//                if (aComputer.getPile().isEmpty() && aHuman.getPile().isEmpty()) {
//                    if (mTable.getLooseSet().isEmpty() && mTable.getBuilds().isEmpty()) {
//                        // Deal table
//                        mTable.setLooseSet(mDeck.drawSet());
//                    }
//                }
//            }
//
//            startTurn(aComputer, aHuman);
//        }
//
//        // Clear table
//        if (mTable.getLooseSet().getSize() > 0) {
//            for (Card card : mTable.getLooseSet().getCards()) {
//                // Check capture status
//                if (aHuman.capturedLast()) {
//                    // Add loose card to human pile
//                    aHuman.addToPile(card);
//                }
//                else {
//                    // Add loose card to computer pile
//                    aComputer.addToPile(card);
//                }
//
//                // Remove loose card from table
//                mTable.removeLooseCard(card);
//            }
//        }
//
//        updateScores(aComputer, aHuman);
//
//        // Clear piles
//        aHuman.clearPile();
//        aComputer.clearPile();
//    }

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

    /**********************************************************************
     Function Name: isOver
     Purpose: To determine whether the round is over
     Parameters:
     aComputer, a Player instance passed by value
     aHuman, a Player instance passed by value
     Return Value: Whether the round is over, a boolean value
     **********************************************************************/
    private boolean isOver(Player aComputer, Player aHuman) {
        if (mDeck.isEmpty()) {
            if (aComputer.getHand().isEmpty() && aHuman.getHand().isEmpty()) {
                return true;
            }
        }

        return false;
    }

    /**********************************************************************
     Function Name: startTurn
     Purpose: To start a turn
     Parameters:
     aComputer, a Player instance passed by reference
     aHuman, a Player instance passed by reference
     **********************************************************************/
//    private void startTurn(Player aComputer, Player aHuman) {
//        for (;;) {
//            Console::displayMessage(stringify(aComputer, aHuman));
//
//            int choice = Console::processTurnMenu(aHuman.isNext());
//
//            if (aHuman.isNext()) {
//                switch (choice) {
//                    case 1: {
//                        String name = Console::processFileMenu();
//
//                        String state = stringify(aComputer, aHuman);
//
//                        if (Serialization::saveGame(name, state)) {
//                            exit(0);
//                        }
//
//                        break;
//                    }
//                    case 2:
//                        if (makeMove(aComputer, aHuman)) {
//                            aHuman.isNext(false);
//                            aComputer.isNext(true);
//
//                            return;
//                        }
//                        break;
//                    case 3:
//                        aHuman.askForHelp(mTable);
//                        break;
//                    case 4:
//                        exit(0);
//                }
//            }
//            else {
//                switch (choice) {
//                    case 1: {
//                        String name = Console::processFileMenu();
//
//                        String state = stringify(aComputer, aHuman);
//
//                        if (Serialization::saveGame(name, state)) {
//                            exit(0);
//                        }
//
//                        break;
//                    }
//                    case 2:
//                        if (makeMove(aComputer, aHuman)) {
//                            aHuman.isNext(true);
//                            aComputer.isNext(false);
//
//                            return;
//                        }
//                        break;
//                    case 3:
//                        exit(0);
//                }
//            }
//        }
//    }

    /**********************************************************************
     Function Name: makeMove
     Purpose: To allow players to make a move
     Parameters:
     aComputer, a Player instance passed by reference
     aHuman, a Player instance passed by reference
     Return Value: Whether a legal move was made, a boolean value
     **********************************************************************/
    private boolean makeMove(Player aComputer, Player aHuman) {
        if (aHuman.isNext()) {
            int moveCode = aHuman.makeMove(mTable);

            switch (moveCode) {
                case 1:
                    aHuman.capturedLast(true);
                    aComputer.capturedLast(false);
                case 0:
                    return true;
                default:
                    return false;
            }
        }
        else {
            int moveCode = aComputer.makeMove(mTable);

            switch (moveCode) {
                case 1:
                    aHuman.capturedLast(false);
                    aComputer.capturedLast(true);
                case 0:
                    return true;
                default:
                    return false;
            }
        }
    }

    /**********************************************************************
     Function Name: updateScores
     Purpose: To update player scores
     Parameters:
     aComputer, a Player instance passed by reference
     aHuman, a Player instance passed by reference
     **********************************************************************/
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

        // Console::displayRoundResults(computerPile, humanPile, computerSpades, humanSpades, computerScore, humanScore);
    }
}
