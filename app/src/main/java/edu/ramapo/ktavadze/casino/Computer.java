package edu.ramapo.ktavadze.casino;

public class Computer extends Player {
    public Computer() {
        mIsHuman = false;
        mScore = 0;
        mHand = new Set();
        mPile = new Set();
    }

    public Computer(boolean aIsNext, int aScore, Set aHand, Set aPile) {
        mIsHuman = false;
        mIsNext = aIsNext;
        mScore = aScore;
        mHand = aHand;
        mPile = aPile;
    }

    /**********************************************************************
     Function Name: makeMove
     Purpose: To allow the computer (virtual) to make a move
     Parameters:
     aTable, a Table instance passed by reference
     Return Value: The move code associated with made move, an integer value
     (1 = capture; 0 = build/trail)
     **********************************************************************/
    @Override
    public int makeMove(Table aTable) {
        if (canIncrease(aTable)) {
            processIncreaseBuild(aTable);
        }
        else if (canExtend(aTable)) {
            processExtendBuild(aTable);
        }
        else if (canCreate(aTable)) {
            processCreateBuild(aTable);
        }
        else if (canCapture(aTable)) {
            processCapture(aTable);

            return 1;
        }
        else {
            processTrail(aTable);
        }

        return 0;
    }

    /**********************************************************************
     Function Name: processIncreaseBuild
     Purpose: To allow the computer to increase the best existing build
     Parameters:
     aTable, a Table instance passed by reference
     **********************************************************************/
    private void processIncreaseBuild(Table aTable) {
        Build bestBuild = findBestIncrease(aTable);

        increaseBuild(aTable, bestBuild);
    }

    /**********************************************************************
     Function Name: processExtendBuild
     Purpose: To allow the computer to extend the best existing build
     Parameters:
     aTable, a Table instance passed by reference
     **********************************************************************/
    private void processExtendBuild(Table aTable) {
        Build bestBuild = findBestExtend(aTable);

        extendBuild(aTable, bestBuild);
    }

    /**********************************************************************
     Function Name: processExtendBuild
     Purpose: To allow the computer to create the best new build
     Parameters:
     aTable, a Table instance passed by reference
     **********************************************************************/
    private void processCreateBuild(Table aTable) {
        Build bestBuild = findBestCreate(aTable);

        createBuild(aTable, bestBuild);
    }

    /**********************************************************************
     Function Name: processCapture
     Purpose: To allow the computer to capture the best set
     Parameters:
     aTable, a Table instance passed by reference
     **********************************************************************/
    private void processCapture(Table aTable) {
        Set bestCaptureSet = findBestCapture(aTable);

        capture(aTable, bestCaptureSet);
    }

    /**********************************************************************
     Function Name: processTrail
     Purpose: To allow the computer to trail the best card
     Parameters:
     aTable, a Table instance passed by reference
     **********************************************************************/
    private void processTrail(Table aTable) {
        Card bestTrailCard = findBestTrail(aTable);

        trail(aTable, bestTrailCard);
    }

    /**********************************************************************
     Function Name: increaseBuild
     Purpose: To allow the computer to increase specified build
     Parameters:
     aTable, a Table instance passed by reference
     aBuild, a Build instance passed by value. Represents the increased build
     **********************************************************************/
    private void increaseBuild(Table aTable, Build aBuild) {
        Set buildSet = aBuild.getSets().get(0);

        // Remove player card from hand
        mHand.removeCard(buildSet.getLastCard());

        // Increase build
        for (int i = 0; i < aTable.getBuilds().size(); i++) {
            Build build = aTable.getBuilds().get(i);

            if (buildSet.contains(build)) {
                aTable.increaseBuild(i, buildSet.getLastCard(), mIsHuman);
            }
        }
    }

    /**********************************************************************
     Function Name: extendBuild
     Purpose: To allow the computer to extend specified build
     Parameters:
     aTable, a Table instance passed by reference
     aBuild, a Build instance passed by value. Represents the extended build
     **********************************************************************/
    private void extendBuild(Table aTable, Build aBuild) {
        Set buildSet = aBuild.getSets().get(aBuild.getSets().size() - 1);

        // Remove player card from hand
        mHand.removeCard(buildSet.getFirstCard());

        // Remove loose set from table
        for (Card card : aTable.getLooseSet().getCards()) {
            if (buildSet.contains(card)) {
                aTable.removeLooseCard(card);
            }
        }

        Set extendedSet = new Set();

        for (Build build : aTable.getBuilds()) {
            for (Set set : build.getSets()) {
                extendedSet.addSet(set);
            }
        }

        // Extend build
        for (int i = 0; i < aTable.getBuilds().size(); i++) {
            Build build = aTable.getBuilds().get(i);

            if (extendedSet.contains(build)) {
                aTable.extendBuild(i, buildSet, mIsHuman);
            }
        }
    }

    /**********************************************************************
     Function Name: createBuild
     Purpose: To allow the computer to create specified build
     Parameters:
     aTable, a Table instance passed by reference
     aBuild, a Build instance passed by value. Represents the created build
     **********************************************************************/
    private void createBuild(Table aTable, Build aBuild) {
        Set buildSet = aBuild.getSets().get(0);

        // Remove player card from hand
        mHand.removeCard(buildSet.getFirstCard());

        // Remove loose set from table
        for (Card card : aTable.getLooseSet().getCards()) {
            if (buildSet.contains(card)) {
                aTable.removeLooseCard(card);
            }
        }

        // Add build to table
        aTable.addBuild(aBuild);
    }

    /**********************************************************************
     Function Name: capture
     Purpose: To allow the computer to capture specified set
     Parameters:
     aTable, a Table instance passed by reference
     aCaptureSet, a Set instance passed by value
     **********************************************************************/
    private void capture(Table aTable, Set aCaptureSet) {
        // Remove capture card from hand
        mHand.removeCard(aCaptureSet.getFirstCard());

        // Add capture card to pile
        mPile.addCard(aCaptureSet.getFirstCard());

        // Capture loose set
        for (Card card : aTable.getLooseSet().getCards()) {
            if (aCaptureSet.contains(card)) {
                captureLooseCard(aTable, card);
            }
        }

        // Capture builds
        for (Build build : aTable.getBuilds()) {
            if (aCaptureSet.contains(build)) {
                captureBuild(aTable, build);
            }
        }
    }

    /**********************************************************************
     Function Name: trail
     Purpose: To allow the computer to trail specified card
     Parameters:
     aTable, a Table instance passed by reference
     aTrailCard, a Card instance passed by value
     **********************************************************************/
    private void trail(Table aTable, Card aTrailCard) {
        // Remove trail card from hand
        mHand.removeCard(aTrailCard);

        // Add trail card to table
        aTable.addLooseCard(aTrailCard);
    }
}
