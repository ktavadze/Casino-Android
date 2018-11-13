package edu.ramapo.ktavadze.casino;

import android.util.Log;

import java.util.ArrayList;

public class Human extends Player {
    private static final String TAG = "Human";
    
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

    /**********************************************************************
     Function Name: makeMove
     Purpose: To allow the human (virtual) to make a move
     Parameters:
     aTable, a Table instance passed by reference
     Return Value: The move code associated with made move, an integer value
     (1 = capture; 0 = build/trail; -1 = illegal)
     **********************************************************************/
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
                    return 0;
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

    /**********************************************************************
     Function Name: processIncreaseBuild
     Purpose: To allow the human to increase a build
     Parameters:
     aTable, a Table instance passed by reference
     Return Value: Whether a legal build was increased, a boolean value
     **********************************************************************/
    private boolean processIncreaseBuild(Table aTable, Move aMove) {
        // Check builds
        if (aTable.getBuilds().isEmpty()) {
            Log.d(TAG, "processIncreaseBuild: ERROR: no builds to increase!");

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

            return true;
        }

        return false;
    }

    /**********************************************************************
     Function Name: processExtendBuild
     Purpose: To allow the human to extend a build
     Parameters:
     aTable, a Table instance passed by reference
     Return Value: Whether a legal build was extended, a boolean value
     **********************************************************************/
    private boolean processExtendBuild(Table aTable, Move aMove) {
        // Check builds
        if (aTable.getBuilds().isEmpty()) {
            Log.d(TAG, "processExtendBuild: ERROR: no builds to extend!");

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

            return true;
        }

        return false;
    }

    /**********************************************************************
     Function Name: processCreateBuild
     Purpose: To allow the human to create a build
     Parameters:
     aTable, a Table instance passed by reference
     Return Value: Whether a legal build was created, a boolean value
     **********************************************************************/
    private boolean processCreateBuild(Table aTable, Move aMove) {
        // Check loose set
        if (aTable.getLooseSet().isEmpty()) {
            Log.d(TAG, "processCreateBuild: ERROR: no loose cards to build with!");

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

            return true;
        }

        return false;
    }

    /**********************************************************************
     Function Name: processCapture
     Purpose: To allow the human to make a capture move
     Parameters:
     aTable, a Table instance passed by reference
     Return Value: Whether a legal capture move was made, a boolean value
     **********************************************************************/
    private boolean processCapture(Table aTable, Move aMove) {
        // Check table
        if (aTable.getLooseSet().isEmpty() && aTable.getBuilds().isEmpty()) {
            Log.d(TAG, "processCapture: ERROR: no cards to capture!");;

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

            return true;
        }

        return false;
    }

    /**********************************************************************
     Function Name: processTrail
     Purpose: To allow the human to make a trail move
     Parameters:
     aTable, a Table instance passed by reference
     Return Value: Whether a legal trail move was made, a boolean value
     **********************************************************************/
    private boolean processTrail(Table aTable, Move aMove) {
        // Select trail card
        Card trailCard = aMove.getPlayerCard();

        if (canTrail(aTable, trailCard)) {
            // Add trail card to table
            aTable.addLooseCard(trailCard);

            // Remove trail card from hand
            mHand.removeCard(trailCard);

            return true;
        }

        return false;
    }

    /**********************************************************************
     Function Name: canIncreaseBuild
     Purpose: To determine whether the human can increase specified build
     Parameters:
     aTable, a Table instance passed by value
     aSelectedBuild, a Build instance passed by value
     aBuildCard, a Card instance passed by value
     Return Value: Whether the human can increase specified build, a boolean value
     **********************************************************************/
    private boolean canIncreaseBuild(Table aTable, Build aSelectedBuild, Card aBuildCard) {
        // Check build card
        if (reservedForCapture(aTable, aBuildCard)) {
            Log.d(TAG, "canIncreaseBuild: ERROR: build card reserved for capture!");

            return false;
        }

        // Check build owner
        if (aSelectedBuild.isHuman() == mIsHuman) {
            Log.d(TAG, "canIncreaseBuild: ERROR: cannot increase own build!");

            return false;
        }

        // Check build size
        if (aSelectedBuild.getSets().size() > 1) {
            Log.d(TAG, "canIncreaseBuild: ERROR: cannot increase multi-builds!");

            return false;
        }

        // Check build value
        if (countCardsHeld(aBuildCard.getValue() + aSelectedBuild.getValue()) == 0) {
            Log.d(TAG, "canIncreaseBuild: ERROR: no card in hand matching build value!");

            return false;
        }

        return true;
    }

    /**********************************************************************
     Function Name: canExtendBuild
     Purpose: To determine whether the human can extend specified build
     Parameters:
     aTable, a Table instance passed by value
     aSelectedBuild, a Build instance passed by value
     aBuildCard, a Card instance passed by value
     aLooseSet, a Set instance passed by value
     Return Value: Whether the human can extend specified build, a boolean value
     **********************************************************************/
    private boolean canExtendBuild(Table aTable, Build aSelectedBuild, Card aBuildCard, Set aLooseSet) {
        // Check build owner
        if (aSelectedBuild.isHuman() != mIsHuman) {
            Log.d(TAG, "canExtendBuild: cannot extend opponent's build!");

            return false;
        }

        // Check build card
        if (reservedForCapture(aTable, aBuildCard)) {
            Log.d(TAG, "canExtendBuild: ERROR: build card reserved for capture!");

            return false;
        }

        // Check build value
        if (aBuildCard.getValue() + aLooseSet.getValue() != aSelectedBuild.getValue()) {
            Log.d(TAG, "canExtendBuild: ERROR: build sum mismatch!");

            return false;
        }

        return true;
    }

    /**********************************************************************
     Function Name: canCreateBuild
     Purpose: To determine whether the human can create specified build
     Parameters:
     aTable, a Table instance passed by value
     aBuildCard, a Card instance passed by value
     aLooseSet, a Set instance passed by value
     Return Value: Whether the human can create specified build, a boolean value
     **********************************************************************/
    private boolean canCreateBuild(Table aTable, Card aBuildCard, Set aLooseSet) {
        // Check build card
        if (reservedForCapture(aTable, aBuildCard)) {
            Log.d(TAG, "canCreateBuild: ERROR: build card reserved for capture!");

            return false;
        }

        // Check build value
        if (countCardsHeld(aBuildCard.getValue() + aLooseSet.getValue()) == 0) {
            Log.d(TAG, "canCreateBuild: ERROR: no card in hand matching build value!");

            return false;
        }

        return true;
    }

    /**********************************************************************
     Function Name: canCaptureSelection
     Purpose: To determine whether the human can capture specified selection
     Parameters:
     aTable, a Table instance passed by value
     aCaptureCard, a Card instance passed by value
     aLooseSet, a Set instance passed by value
     aFirmSet, a Set instance passed by value
     Return Value: Whether the human can extend specified build, a boolean value
     **********************************************************************/
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
                Log.d(TAG, "canCaptureSelection: ERROR: cannot capture selected loose card(s)!");

                return false;
            }
        }

        // Check for matching loose cards
        for (Card card : aTable.getLooseSet().getCards()) {
            if (card.getValue() == aCaptureCard.getValue()) {
                if (!aLooseSet.contains(card)) {
                    Log.d(TAG, "canCaptureSelection: ERROR: must capture matching loose card(s)!");

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
                            Log.d(TAG, "canCaptureSelection: ERROR: must capture matching owned build(s)!");

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
                Log.d(TAG, "canCaptureSelection: ERROR: cannot capture selected build card(s)!");

                return false;
            }
        }
        else {
            for (Build build : aTable.getBuilds()) {
                if (build.getValue() == aCaptureCard.getValue() && build.isHuman() == mIsHuman) {
                    if (countCardsHeld(aCaptureCard.getValue()) < 2) {
                        Log.d(TAG, "canCaptureSelection: ERROR: must capture matching owned build(s)!");

                        return false;
                    }
                }
            }
        }

        return true;
    }

    /**********************************************************************
     Function Name: canTrail
     Purpose: To determine whether the human can trail specified card
     Parameters:
     aTable, a Table instance passed by value
     aTrailCard, a Card instance passed by value
     Return Value: Whether the human can extend specified build, a boolean value
     **********************************************************************/
    private boolean canTrail(Table aTable, Card aTrailCard) {
        // Check trail card
        if (reservedForCapture(aTable, aTrailCard)) {
            Log.d(TAG, "canTrail: ERROR: trail card reserved for capture!");

            return false;
        }

        // Check builds
        if (ownsBuild(aTable)) {
            Log.d(TAG, "canTrail: ERROR: cannot trail while owner of build(s)!");

            return false;
        }

        // Check loose set
        for (Card card : aTable.getLooseSet().getCards()) {
            if (card.getValue() == aTrailCard.getValue()) {
                Log.d(TAG, "canTrail: ERROR: must capture matching loose card(s)!");

                return false;
            }
        }

        return true;
    }

    /**********************************************************************
     Function Name: capture
     Purpose: To allow the human to capture specified selection
     Parameters:
     aTable, a Table instance passed by reference
     aCaptureCard, a Card instance passed by value
     aLooseSet, a Set instance passed by value
     aFirmSet, a Set instance passed by value
     **********************************************************************/
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
