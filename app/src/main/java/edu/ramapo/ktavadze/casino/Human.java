package edu.ramapo.ktavadze.casino;

import java.util.ArrayList;

public class Human extends Player {
    public Human(boolean aIsNext) {
        mIsHuman = true;
        mIsNext = aIsNext;
        mScore = 0;
        mHand = new Set();
        mPile = new Set();
    }

    public Human(boolean aIsNext, int aScore, Set aHand, Set aPile) {
        mIsHuman = true;
        mIsNext = aIsNext;
        mScore = aScore;
        mHand = aHand;
        mPile = aPile;
    }

    /**
     Allows the human to make a move.
     @param aTable - Table instance to reference.
     @param aMove - Move instance to reference.
     @return Integer value representing the type of move made (1 for capture, 0 for legal, -1 for illegal).
     */
    @Override
    public int makeMove(Table aTable, Move aMove) {
        switch (aMove.getType()) {
            case "increase":
                if (processIncreaseBuild(aTable, aMove)) {
                    return 0;
                }
                break;
            case "extend":
                if (processExtendBuild(aTable, aMove)) {
                    return 0;
                }
                break;
            case "create":
                if (processCreateBuild(aTable, aMove)) {
                    return 0;
                }
                break;
            case "capture":
                if (processCapture(aTable, aMove)) {
                    return 1;
                }
                break;
            case "trail":
                if (processTrail(aTable, aMove)) {
                    return 0;
                }
                break;
        }

        return -1;
    }

    /**
     Allows the human to make an increase (build) move.
     @param aTable - Table instance to reference.
     @param aMove - Move instance to reference.
     @return Boolean value representing whether a legal move was made.
     */
    private boolean processIncreaseBuild(Table aTable, Move aMove) {
        // Check builds
        if (aTable.getBuilds().isEmpty()) {
            mMessage = "Error: no builds to increase!";

            return false;
        }

        // Select build
        Build selectedBuild = aMove.getBuilds().get(0);
        int selectedBuildIndex = aTable.getBuilds().indexOf(selectedBuild);

        // Select build card
        Card buildCard = aMove.getPlayerCard();

        // Increase build
        if (canIncreaseBuild(aTable, selectedBuild, buildCard)) {
            aTable.increaseBuild(selectedBuildIndex, buildCard, mIsHuman);

            // Remove build card from hand
            mHand.removeCard(buildCard);

            mMessage = "Increase " + selectedBuild.stringify();
            mMessage += " for " + selectedBuild.getWeight();

            return true;
        }

        return false;
    }

    /**
     Allows the human to make an extend (build) move.
     @param aTable - Table instance to reference.
     @param aMove - Move instance to reference.
     @return Boolean value representing whether a legal move was made.
     */
    private boolean processExtendBuild(Table aTable, Move aMove) {
        // Check builds
        if (aTable.getBuilds().isEmpty()) {
            mMessage = "Error: no builds to extend!";

            return false;
        }

        // Select build
        Build selectedBuild = aMove.getBuilds().get(0);
        int selectedBuildIndex = aTable.getBuilds().indexOf(selectedBuild);

        // Select build card
        Card buildCard = aMove.getPlayerCard();

        // Select loose set
        Set looseSet = new Set();

        if (selectedBuild.getValue() != buildCard.getValue()) {
            looseSet = aMove.getLooseSet();
        }

        if (canExtendBuild(aTable, selectedBuild, buildCard, looseSet)) {
            Set buildSet = new Set();
            buildSet.addCard(buildCard);
            buildSet.addSet(looseSet);

            aTable.extendBuild(selectedBuildIndex, buildSet);

            // Remove loose set from table
            aTable.getLooseSet().removeSet(looseSet);

            // Remove build card from hand
            mHand.removeCard(buildCard);

            mMessage = "Extend " + selectedBuild.stringify();
            mMessage += " for " + selectedBuild.getWeight();

            return true;
        }

        return false;
    }

    /**
     Allows the human to make a create (build) move.
     @param aTable - Table instance to reference.
     @param aMove - Move instance to reference.
     @return Boolean value representing whether a legal move was made.
     */
    private boolean processCreateBuild(Table aTable, Move aMove) {
        // Check loose set
        if (aTable.getLooseSet().isEmpty()) {
            mMessage = "Error: no loose cards to build with!";

            return false;
        }

        // Select build card
        Card buildCard = aMove.getPlayerCard();

        // Select loose set
        Set looseSet = aMove.getLooseSet();

        // Create build
        if (canCreateBuild(aTable, buildCard, looseSet)) {
            Set buildSet = new Set();
            buildSet.addCard(buildCard);
            buildSet.addSet(looseSet);

            Build build = new Build(mIsHuman, buildSet);

            aTable.addBuild(build);

            // Remove loose set from table
            for (Card card : looseSet.getCards()) {
                aTable.removeLooseCard(card);
            }

            // Remove build card from hand
            mHand.removeCard(buildCard);

            mMessage = "Create " + build.stringify();
            mMessage += " for " + build.getWeight();

            return true;
        }

        return false;
    }

    /**
     Allows the human to make a capture move.
     @param aTable - Table instance to reference.
     @param aMove - Move instance to reference.
     @return Boolean value representing whether a legal move was made.
     */
    private boolean processCapture(Table aTable, Move aMove) {
        // Check table
        if (aTable.getLooseSet().isEmpty() && aTable.getBuilds().isEmpty()) {
            mMessage = "Error: no cards to capture!";

            return false;
        }

        // Select capture card
        Card captureCard = aMove.getPlayerCard();

        // Select loose set
        Set looseSet = aMove.getLooseSet();

        // Select firm set
        Set firmSet = new Set();

        for (Build build : aMove.getBuilds()) {
            for (Set set : build.getSets()) {
                firmSet.addSet(set);
            }
        }

        // Capture
        if (canCaptureSelection(aTable, captureCard, looseSet, firmSet)) {
            capture(aTable, captureCard, looseSet, firmSet);

            mMessage = "Capture " + captureCard.getName();

            if (!looseSet.isEmpty()) {
                mMessage += " " + looseSet.stringify();
            }

            if (!firmSet.isEmpty()) {
                mMessage += " " + firmSet.stringify();
            }

            mMessage += " for " + (captureCard.getWeight() + looseSet.getWeight() + firmSet.getWeight());

            return true;
        }

        return false;
    }

    /**
     Allows the human to make a trail move.
     @param aTable - Table instance to reference.
     @param aMove - Move instance to reference.
     @return Boolean value representing whether a legal move was made.
     */
    private boolean processTrail(Table aTable, Move aMove) {
        // Select trail card
        Card trailCard = aMove.getPlayerCard();

        if (canTrail(aTable, trailCard)) {
            // Add trail card to table
            aTable.addLooseCard(trailCard);

            // Remove trail card from hand
            mHand.removeCard(trailCard);

            mMessage = "Trail " + trailCard.getName();
            mMessage += " for " + trailCard.getWeight();

            return true;
        }

        return false;
    }

    /**
     Checks whether the human can make the specified increase (build) move.
     @param aTable - Table instance to reference.
     @param aSelectedBuild - Build instance to increase.
     @param aBuildCard - Card instance to increase with.
     @return Boolean value representing whether the move is legal and can be made.
     */
    private boolean canIncreaseBuild(Table aTable, Build aSelectedBuild, Card aBuildCard) {
        // Check build card
        if (reservedForCapture(aTable, aBuildCard)) {
            mMessage = "Error: build card reserved for capture!";

            return false;
        }

        // Check build owner
        if (aSelectedBuild.isHuman() == mIsHuman) {
            mMessage = "Error: cannot increase own build!";

            return false;
        }

        // Check build size
        if (aSelectedBuild.getSets().size() > 1) {
            mMessage = "Error: cannot increase multi-builds!";

            return false;
        }

        // Check build value
        if (countCardsHeld(aBuildCard.getValue() + aSelectedBuild.getValue()) == 0) {
            mMessage = "Error: no card in hand matching build value!";

            return false;
        }

        return true;
    }

    /**
     Checks whether the human can make the specified extend (build) move.
     @param aTable - Table instance to reference.
     @param aSelectedBuild - Build instance to extend.
     @param aBuildCard - Card instance to extend with.
     @param aLooseSet - Set instance to extend with.
     @return Boolean value representing whether the move is legal and can be made.
     */
    private boolean canExtendBuild(Table aTable, Build aSelectedBuild, Card aBuildCard, Set aLooseSet) {
        // Check build owner
        if (aSelectedBuild.isHuman() != mIsHuman) {
            mMessage = "Error: cannot extend opponent's build!";

            return false;
        }

        // Check build card
        if (reservedForCapture(aTable, aBuildCard)) {
            mMessage = "Error: build card reserved for capture!";

            return false;
        }

        // Check build value
        if (aBuildCard.getValue() + aLooseSet.getValue() != aSelectedBuild.getValue()) {
            mMessage = "Error: build sum mismatch!";

            return false;
        }

        return true;
    }

    /**
     Checks whether the human can make the specified create (build) move.
     @param aTable - Table instance to reference.
     @param aBuildCard - Card instance to create with.
     @param aLooseSet - Set instance to create with.
     @return Boolean value representing whether the move is legal and can be made.
     */
    private boolean canCreateBuild(Table aTable, Card aBuildCard, Set aLooseSet) {
        // Check build card
        if (reservedForCapture(aTable, aBuildCard)) {
            mMessage = "Error: build card reserved for capture!";

            return false;
        }

        // Check build value
        if (countCardsHeld(aBuildCard.getValue() + aLooseSet.getValue()) == 0) {
            mMessage = "Error: no card in hand matching build value!";

            return false;
        }

        return true;
    }

    /**
     Checks whether the human can make the specified capture move.
     @param aTable - Table instance to reference.
     @param aCaptureCard - Card instance to capture with.
     @param aLooseSet - Set instance representing the loose set to capture.
     @param aFirmSet - Set instance representing the firm set to capture.
     @return Boolean value representing whether the move is legal and can be made.
     */
    private boolean canCaptureSelection(Table aTable, Card aCaptureCard, Set aLooseSet, Set aFirmSet) {
        // Check loose set
        if (aLooseSet.getSize() > 0) {
            int sum = 0;

            for (Card card : aLooseSet.getCards()) {
                if (card.getValue() != aCaptureCard.getValue()) {
                    sum += card.getValue();
                }
            }

            // Check loose sum
            if (sum != 0 && sum != aCaptureCard.getValue()) {
                mMessage = "Error: cannot capture selected loose card(s)!";

                return false;
            }
        }

        // Check for matching loose cards
        for (Card card : aTable.getLooseSet().getCards()) {
            if (card.getValue() == aCaptureCard.getValue()) {
                if (!aLooseSet.contains(card)) {
                    mMessage = "Error: must capture matching loose card(s)!";

                    return false;
                }
            }
        }

        // Check firm set
        if (aFirmSet.getSize() > 0) {
            int cardsFound = 0;

            for (Build build : aTable.getBuilds()) {
                if (build.getValue() == aCaptureCard.getValue()) {
                    // Check for matching owned builds
                    if (build.isHuman() == mIsHuman && !aFirmSet.contains(build)) {
                        if (countCardsHeld(aCaptureCard.getValue()) < 2) {
                            mMessage = "Error: must capture matching owned build(s)!";

                            return false;
                        }
                    }

                    if (aFirmSet.contains(build)) {
                        for (Set set : build.getSets()) {
                            cardsFound += set.getSize();
                        }
                    }
                }
            }

            if (cardsFound != aFirmSet.getSize()) {
                mMessage = "Error: cannot capture selected build card(s)!";

                return false;
            }
        }
        else {
            for (Build build : aTable.getBuilds()) {
                if (build.getValue() == aCaptureCard.getValue() && build.isHuman() == mIsHuman) {
                    if (countCardsHeld(aCaptureCard.getValue()) < 2) {
                        mMessage = "Error: must capture matching owned build(s)!";

                        return false;
                    }
                }
            }
        }

        return true;
    }

    /**
     Checks whether the human can make the specified trail move.
     @param aTable - Table instance to reference.
     @param aTrailCard - Card instance to trail with.
     @return Boolean value representing whether the move is legal and can be made.
     */
    private boolean canTrail(Table aTable, Card aTrailCard) {
        // Check trail card
        if (reservedForCapture(aTable, aTrailCard)) {
            mMessage = "Error: trail card reserved for capture!";

            return false;
        }

        // Check builds
        if (ownsBuild(aTable)) {
            mMessage = "Error: cannot trail while owner of build(s)!";

            return false;
        }

        // Check loose set
        for (Card card : aTable.getLooseSet().getCards()) {
            if (card.getValue() == aTrailCard.getValue()) {
                mMessage = "Error: must capture matching loose card(s)!";

                return false;
            }
        }

        return true;
    }

    /**
     Allows the human to make the specified capture move.
     @param aTable - Table instance to reference.
     @param aCaptureCard - Card instance to capture with.
     @param aLooseSet - Set instance representing the loose set to capture.
     @param aFirmSet - Set instance representing the firm set to capture.
     */
    private void capture(Table aTable, Card aCaptureCard, Set aLooseSet, Set aFirmSet) {
        // Add capture card to pile
        mPile.addCard(aCaptureCard);

        // Remove capture card from hand
        mHand.removeCard(aCaptureCard);

        // Capture loose set
        for (Card card : aLooseSet.getCards()) {
            captureLooseCard(aTable, card);
        }

        // Capture firm set
        ArrayList<Build> capturedBuilds = new ArrayList<>();

        for (Build build : aTable.getBuilds()) {
            if (aFirmSet.contains(build)) {
                capturedBuilds.add(build);
            }
        }

        for (Build build : capturedBuilds) {
            captureBuild(aTable, build);
        }
    }
}
