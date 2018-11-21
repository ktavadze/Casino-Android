package edu.ramapo.ktavadze.casino;

import java.util.ArrayList;

public class Computer extends Player {
    public Computer(boolean aIsNext) {
        mIsHuman = false;
        mIsNext = aIsNext;
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

    /**
     Allows the computer to make a move.
     @param aTable - Table instance to reference.
     @param aMove - Move instance to reference.
     @return Integer value representing the type of move made (1 for capture, 0 for legal).
     */
    @Override
    public int makeMove(Table aTable, Move aMove) {
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

    /**
     Allows the computer to make best increase (build) move.
     @param aTable - Table instance to reference.
     */
    private void processIncreaseBuild(Table aTable) {
        Build bestBuild = findBestIncrease(aTable);

        increaseBuild(aTable, bestBuild);
    }

    /**
     Allows the computer to make best extend (build) move.
     @param aTable - Table instance to reference.
     */
    private void processExtendBuild(Table aTable) {
        Build bestBuild = findBestExtend(aTable);

        extendBuild(aTable, bestBuild);
    }

    /**
     Allows the computer to make best create (build) move.
     @param aTable - Table instance to reference.
     */
    private void processCreateBuild(Table aTable) {
        Build bestBuild = findBestCreate(aTable);

        createBuild(aTable, bestBuild);
    }

    /**
     Allows the computer to make best capture move.
     @param aTable - Table instance to reference.
     */
    private void processCapture(Table aTable) {
        Set bestCaptureSet = findBestCapture(aTable);

        capture(aTable, bestCaptureSet);
    }

    /**
     Allows the computer to make best trail move.
     @param aTable - Table instance to reference.
     */
    private void processTrail(Table aTable) {
        Card bestTrailCard = findBestTrail();

        trail(aTable, bestTrailCard);
    }

    /**
     Allows the computer to increase the specified build.
     @param aTable - Table instance to reference.
     @param aBuild - Build instance representing the increased build.
     */
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

    /**
     Allows the computer to extend the specified build.
     @param aTable - Table instance to reference.
     @param aBuild - Build instance representing the extended build.
     */
    private void extendBuild(Table aTable, Build aBuild) {
        Set buildSet = aBuild.getSets().get(aBuild.getSets().size() - 1);

        // Remove player card from hand
        mHand.removeCard(buildSet.getFirstCard());

        // Remove loose set from table
        aTable.getLooseSet().removeSet(buildSet);

        // Extend build
        for (int i = 0; i < aTable.getBuilds().size(); i++) {
            Build build = aTable.getBuilds().get(i);

            if (aBuild.contains(build)) {
                aTable.extendBuild(i, buildSet);
            }
        }
    }

    /**
     Allows the computer to create the specified build.
     @param aTable - Table instance to reference.
     @param aBuild - Build instance representing the created build.
     */
    private void createBuild(Table aTable, Build aBuild) {
        Set buildSet = aBuild.getSets().get(0);

        // Remove player card from hand
        mHand.removeCard(buildSet.getFirstCard());

        // Remove loose set from table
        aTable.getLooseSet().removeSet(buildSet);

        // Add build to table
        aTable.addBuild(aBuild);
    }

    /**
     Allows the computer to capture the specified set.
     @param aTable - Table instance to reference.
     @param aCaptureSet - Set instance representing the captured set.
     */
    private void capture(Table aTable, Set aCaptureSet) {
        // Add capture card to pile
        mPile.addCard(aCaptureSet.getFirstCard());

        // Remove capture card from hand
        mHand.removeCard(aCaptureSet.getFirstCard());

        // Capture loose set
        Set capturedLooseSet = new Set();

        for (Card card : aTable.getLooseSet().getCards()) {
            if (aCaptureSet.contains(card)) {
                capturedLooseSet.addCard(card);
            }
        }

        for (Card card : capturedLooseSet.getCards()) {
            captureLooseCard(aTable, card);
        }

        // Capture builds
        ArrayList<Build> capturedBuilds = new ArrayList<>();

        for (Build build : aTable.getBuilds()) {
            if (aCaptureSet.contains(build)) {
                capturedBuilds.add(build);
            }
        }

        for (Build build : capturedBuilds) {
            captureBuild(aTable, build);
        }
    }

    /**
     Allows the computer to trail the specified card.
     @param aTable - Table instance to reference.
     @param aTrailCard - Card instance representing the trailed card.
     */
    private void trail(Table aTable, Card aTrailCard) {
        // Remove trail card from hand
        mHand.removeCard(aTrailCard);

        // Add trail card to table
        aTable.addLooseCard(aTrailCard);
    }
}
