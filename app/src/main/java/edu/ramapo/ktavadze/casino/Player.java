package edu.ramapo.ktavadze.casino;

import java.util.ArrayList;

public class Player {
    protected boolean mIsHuman;
    protected boolean mIsNext;
    protected boolean mCapturedLast;
    protected int mScore;
    protected Set mHand;
    protected Set mPile;
    protected String mMessage = "";

    public Player() {}

    public boolean isHuman() {
        return mIsHuman;
    }

    public boolean isNext() {
        return mIsNext;
    }

    public void isNext(boolean aIsNext) {
        mIsNext = aIsNext;
    }

    public boolean capturedLast() {
        return mCapturedLast;
    }

    public void capturedLast(boolean aCapturedLast) {
        mCapturedLast = aCapturedLast;
    }

    public int getScore() {
        return mScore;
    }

    public void setScore(int aScore) {
        mScore = aScore;
    }

    public Set getHand() {
        return mHand;
    }

    public void setHand(Set aHand) {
        mHand = aHand;
    }

    public Set getPile() {
        return mPile;
    }

    public String getMessage() {
        return mMessage;
    }

    public int makeMove(Table aTable, Move aMove) {
        return 0;
    }

    /**********************************************************************
     Function Name: askForHelp
     Purpose: To allow the player to ask for help
     Parameters:
     aTable, a Table instance passed by value
     **********************************************************************/
    public void askForHelp(Table aTable) {
        if (canIncrease(aTable)) {
            findBestIncrease(aTable);
        }
        else if (canExtend(aTable)) {
            findBestExtend(aTable);
        }
        else if (canCreate(aTable)) {
            findBestCreate(aTable);
        }
        else if (canCapture(aTable)) {
            findBestCapture(aTable);
        }
        else {
            findBestTrail(aTable);
        }
    }

    public String stringify() {
        String data = "";

        data += "\n   Score: " + mScore;

        data += "\n   Hand: " + mHand.stringify();

        data += "\n   Pile: " + mPile.stringify();

        return data;
    }

    /**********************************************************************
     Function Name: captureLooseCard
     Purpose: To capture a loose card
     Parameters:
     aTable, a Table instance passed by reference
     aCard, a Card instance passed by value
     **********************************************************************/
    protected void captureLooseCard(Table aTable, Card aCard) {
        // Add loose card to pile
        mPile.addCard(aCard);

        // Remove loose card from table
        aTable.removeLooseCard(aCard);
    }

    /**********************************************************************
     Function Name: captureBuild
     Purpose: To capture a build
     Parameters:
     aTable, a Table instance passed by reference
     aBuild, a Build instance passed by value
     **********************************************************************/
    protected void captureBuild(Table aTable, Build aBuild) {
        // Add build to pile
        for (Set set : aBuild.getSets()) {
            mPile.addSet(set);
        }

        // Remove build from table
        aTable.removeBuild(aBuild);
    }

    /**********************************************************************
     Function Name: canIncrease
     Purpose: To determine whether the player can increase any builds
     Parameters:
     aTable, a Table instance passed by value
     Return Value: Whether the player can increase any builds, a boolean value
     **********************************************************************/
    protected boolean canIncrease(Table aTable) {
        for (Card playerCard : mHand.getCards()) {
            if (!reservedForCapture(aTable, playerCard)) {
                // Check for possible increased builds
                for (Build build : aTable.getBuilds()) {
                    if (build.isHuman() != mIsHuman && build.getSets().size() == 1) {
                        if (countCardsHeld(build.getValue() + playerCard.getValue()) > 0) {
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    /**********************************************************************
     Function Name: canExtend
     Purpose: To determine whether the player can extend any builds
     Parameters:
     aTable, a Table instance passed by value
     Return Value: Whether the player can extend any builds, a boolean value
     **********************************************************************/
    protected boolean canExtend(Table aTable) {
        Set tableLooseSet = aTable.getLooseSet();
        ArrayList<Set> tableLooseSets = generateSetCombinations(tableLooseSet);

        for (Card playerCard : mHand.getCards()) {
            if (!reservedForCapture(aTable, playerCard)) {
                // Find all simple builds
                for (Card looseCard : tableLooseSet.getCards()) {
                    if (countCardsHeld(looseCard.getValue() + playerCard.getValue()) > 0) {
                        Set buildSet = new Set();
                        buildSet.addCard(playerCard);
                        buildSet.addCard(looseCard);

                        // Check for possible extended builds
                        for (Build tableBuild : aTable.getBuilds()) {
                            if (tableBuild.isHuman() == mIsHuman) {
                                if (tableBuild.getValue() == buildSet.getValue()) {
                                    return true;
                                }
                            }
                        }
                    }
                }

                // Find all compound builds
                for (Set looseSet : tableLooseSets) {
                    if (countCardsHeld(looseSet.getValue() + playerCard.getValue()) > 0) {
                        Set buildSet = new Set();
                        buildSet.addCard(playerCard);
                        buildSet.addSet(looseSet);

                        // Check for possible extended builds
                        for (Build tableBuild : aTable.getBuilds()) {
                            if (tableBuild.isHuman() == mIsHuman) {
                                if (tableBuild.getValue() == buildSet.getValue()) {
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
        }

        return false;
    }

    /**********************************************************************
     Function Name: canCreate
     Purpose: To determine whether the player create any builds
     Parameters:
     aTable, a Table instance passed by value
     Return Value: Whether the player can create any builds, a boolean value
     **********************************************************************/
    protected boolean canCreate(Table aTable) {
        Set tableLooseSet = aTable.getLooseSet();
        ArrayList<Set> tableLooseSets = generateSetCombinations(tableLooseSet);

        for (Card playerCard : mHand.getCards()) {
            if (!reservedForCapture(aTable, playerCard)) {
                // Check for possible simple builds
                for (Card looseCard : tableLooseSet.getCards()) {
                    if (countCardsHeld(looseCard.getValue() + playerCard.getValue()) > 0) {
                        return true;
                    }
                }

                // Check for possible compound builds
                for (Set looseSet : tableLooseSets) {
                    if (countCardsHeld(looseSet.getValue() + playerCard.getValue()) > 0) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    /**********************************************************************
     Function Name: canCapture
     Purpose: To determine whether the player can capture anything
     Parameters:
     aTable, a Table instance passed by value
     Return Value: Whether the player can capture anything, a boolean value
     **********************************************************************/
    protected boolean canCapture(Table aTable) {
        // Check loose set
        if (aTable.getLooseSet().getSize() > 0) {
            Set tableLooseSet = aTable.getLooseSet();

            // Check for matching loose cards
            for (Card card : tableLooseSet.getCards()) {
                if (countCardsHeld(card.getValue()) > 0) {
                    return true;
                }
            }

            // Check for matching loose sets
            if (tableLooseSet.getSize() > 1) {
                ArrayList<Set> tableLooseSets = generateSetCombinations(tableLooseSet);

                for (Set set : tableLooseSets) {
                    if (countCardsHeld(set.getValue()) > 0) {
                        return true;
                    }
                }
            }
        }

        // Check builds
        if (!aTable.getBuilds().isEmpty()) {
            // Check for matching builds
            for (Build build : aTable.getBuilds()) {
                if (countCardsHeld(build.getValue()) > 0) {
                    return true;
                }
            }
        }

        return false;
    }

    /**********************************************************************
     Function Name: reservedForCapture
     Purpose: To determine whether a card is reserved for capture
     Parameters:
     aTable, a Table instance passed by value
     aCard, a Card instance passed by value
     Return Value: Whether a card is reserved for capture, a boolean value
     **********************************************************************/
    protected boolean reservedForCapture(Table aTable, Card aCard) {
        for (Build build : aTable.getBuilds()) {
            if (build.getValue() == aCard.getValue() && build.isHuman() == mIsHuman) {
                if (countCardsHeld(aCard.getValue()) < 2) {
                    return true;
                }
            }
        }

        return false;
    }

    /**********************************************************************
     Function Name: ownsBuild
     Purpose: To determine whether the player owns any builds
     Parameters:
     aTable, a Table instance passed by value
     Return Value: Whether the player owns any builds, a boolean value
     **********************************************************************/
    protected boolean ownsBuild(Table aTable) {
        for (Build build : aTable.getBuilds()) {
            if (build.isHuman() == mIsHuman) {
                return true;
            }
        }

        return false;
    }

    /**********************************************************************
     Function Name: countCardsHeld
     Purpose: To calculate the number of cards of specified value in hand
     Parameters:
     aValue, an integer
     Return Value: The number of cards of specified value in held, an integer value
     **********************************************************************/
    protected int countCardsHeld(int aValue) {
        int count = 0;

        for (Card card : mHand.getCards()) {
            if (card.getValue() == aValue) {
                count++;
            }
        }

        return count;
    }

    /**********************************************************************
     Function Name: findBestIncrease
     Purpose: To find the best increased build
     Parameters:
     aTable, a Table instance passed by value
     Return Value: The best increased build, a Build instance
     **********************************************************************/
    protected Build findBestIncrease(Table aTable) {
        // Find possible builds
        ArrayList<Build> possibleBuilds = new ArrayList<>();

        for (Card playerCard : mHand.getCards()) {
            if (!reservedForCapture(aTable, playerCard)) {
                // Find possible increased builds
                for (Build tableBuild : aTable.getBuilds()) {
                    if (tableBuild.isHuman() != mIsHuman && tableBuild.getSets().size() == 1) {
                        if (countCardsHeld(tableBuild.getValue() + playerCard.getValue()) > 0) {
                            Build build = new Build(tableBuild);
                            build.increase(playerCard);

                            possibleBuilds.add(build);
                        }
                    }
                }
            }
        }

        // Find best build
        Build bestBuild = new Build();

        for (Build build : possibleBuilds) {
            if (build.getWeight() > bestBuild.getWeight()) {
                bestBuild = build;
            }
        }

        mMessage = "Increase: " + bestBuild.stringify();
        mMessage += "\nHeuristic: " + bestBuild.getWeight();

        return bestBuild;
    }

    /**********************************************************************
     Function Name: findBestExtend
     Purpose: To find the best extended build
     Parameters:
     aTable, a Table instance passed by value
     Return Value: The best extended build, a Build instance
     **********************************************************************/
    protected Build findBestExtend(Table aTable) {
        // Find possible builds
        ArrayList<Build> possibleBuilds = new ArrayList<>();

        Set tableLooseSet = aTable.getLooseSet();
        ArrayList<Set> tableLooseSets = generateSetCombinations(tableLooseSet);

        for (Card playerCard : mHand.getCards()) {
            if (!reservedForCapture(aTable, playerCard)) {
                // Find all simple builds
                for (Card looseCard : tableLooseSet.getCards()) {
                    if (countCardsHeld(looseCard.getValue() + playerCard.getValue()) > 0) {
                        Set buildSet = new Set();
                        buildSet.addCard(playerCard);
                        buildSet.addCard(looseCard);

                        // Find possible extended builds
                        for (Build tableBuild : aTable.getBuilds()) {
                            if (tableBuild.isHuman() == mIsHuman) {
                                if (tableBuild.getValue() == buildSet.getValue()) {
                                    Build build = new Build(tableBuild);
                                    build.extend(buildSet);

                                    possibleBuilds.add(build);
                                }
                            }
                        }
                    }
                }

                // Find all compound builds
                for (Set looseSet : tableLooseSets) {
                    if (countCardsHeld(looseSet.getValue() + playerCard.getValue()) > 0) {
                        Set buildSet = new Set();
                        buildSet.addCard(playerCard);
                        buildSet.addSet(looseSet);

                        // Find possible extended builds
                        for (Build tableBuild : aTable.getBuilds()) {
                            if (tableBuild.isHuman() == mIsHuman) {
                                if (tableBuild.getValue() == buildSet.getValue()) {
                                    Build build = new Build(tableBuild);
                                    build.extend(buildSet);

                                    possibleBuilds.add(build);
                                }
                            }
                        }
                    }
                }
            }
        }

        // Find best build
        Build bestBuild = new Build();

        for (Build build : possibleBuilds) {
            if (build.getWeight() > bestBuild.getWeight()) {
                bestBuild = build;
            }
        }

        mMessage = "Extend: " + bestBuild.stringify();
        mMessage += "\nHeuristic: " + bestBuild.getWeight();

        return bestBuild;
    }

    /**********************************************************************
     Function Name: findBestCreate
     Purpose: To find the best new build
     Parameters:
     aTable, a Table instance passed by value
     Return Value: The best new build, a Build instance
     **********************************************************************/
    protected Build findBestCreate(Table aTable) {
        // Find possible builds
        ArrayList<Build> possibleBuilds = new ArrayList<>();

        Set tableLooseSet = aTable.getLooseSet();
        ArrayList<Set> tableLooseSets = generateSetCombinations(tableLooseSet);

        for (Card playerCard : mHand.getCards()) {
            if (!reservedForCapture(aTable, playerCard)) {
                // Find all simple builds
                for (Card looseCard : tableLooseSet.getCards()) {
                    if (countCardsHeld(looseCard.getValue() + playerCard.getValue()) > 0) {
                        Set buildSet = new Set();
                        buildSet.addCard(playerCard);
                        buildSet.addCard(looseCard);

                        Build build = new Build(mIsHuman, buildSet);

                        possibleBuilds.add(build);
                    }
                }

                // Find all compound builds
                for (Set looseSet : tableLooseSets) {
                    if (countCardsHeld(looseSet.getValue() + playerCard.getValue()) > 0) {
                        Set buildSet = new Set();
                        buildSet.addCard(playerCard);
                        buildSet.addSet(looseSet);

                        Build build = new Build(mIsHuman, buildSet);

                        possibleBuilds.add(build);
                    }
                }
            }
        }

        // Find best build
        Build bestBuild = new Build();

        for (Build build : possibleBuilds) {
            if (build.getWeight() > bestBuild.getWeight()) {
                bestBuild = build;
            }
        }

        mMessage = "Create: " + bestBuild.stringify();
        mMessage += "\nHeuristic: " + bestBuild.getWeight();

        return bestBuild;
    }

    /**********************************************************************
     Function Name: findBestCapture
     Purpose: To find the best capture set
     Parameters:
     aTable, a Table instance passed by value
     Return Value: The best capture set, a Set instance
     **********************************************************************/
    protected Set findBestCapture(Table aTable) {
        // Find possible capture sets
        ArrayList<Set> possibleCaptureSets = new ArrayList<>();

        Set tableLooseSet = aTable.getLooseSet();
        ArrayList<Set> tableLooseSets = generateSetCombinations(tableLooseSet);

        for (Card playerCard : mHand.getCards()) {
            Set matchingLooseCards = new Set();
            Set bestMatchingLooseSet = new Set();
            ArrayList<Build> matchingBuilds = new ArrayList<>();

            // Check loose set
            if (tableLooseSet.getSize() > 0) {
                // Find all matching loose cards
                for (Card card : tableLooseSet.getCards()) {
                    if (card.getValue() == playerCard.getValue()) {
                        matchingLooseCards.addCard(card);
                    }
                }

                // Find best matching loose set
                if (tableLooseSet.getSize() > 1) {
                    for (Set set : tableLooseSets) {
                        if (set.getValue() == playerCard.getValue()) {
                            if (set.getWeight() > bestMatchingLooseSet.getWeight()) {
                                bestMatchingLooseSet = set;
                            }
                        }
                    }
                }
            }

            // Check builds
            if (!aTable.getBuilds().isEmpty()) {
                // Find all matching builds
                for (Build build : aTable.getBuilds()) {
                    if (build.getValue() == playerCard.getValue()) {
                        matchingBuilds.add(build);
                    }
                }
            }

            // Construct capture set
            Set captureSet = new Set();
            captureSet.addCard(playerCard);
            captureSet.addSet(matchingLooseCards);
            captureSet.addSet(bestMatchingLooseSet);

            for (Build build : matchingBuilds) {
                for (Set set : build.getSets()) {
                    captureSet.addSet(set);
                }
            }

            if (captureSet.getSize() > 1) {
                possibleCaptureSets.add(captureSet);
            }
        }

        // Find best capture set
        Set bestCaptureSet = new Set();

        for (Set set : possibleCaptureSets) {
            if (set.getWeight() > bestCaptureSet.getWeight()) {
                bestCaptureSet = set;
            }
        }

        mMessage = "Capture: " + bestCaptureSet.stringify();
        mMessage += "\nHeuristic: " + bestCaptureSet.getWeight();

        return bestCaptureSet;
    }

    /**********************************************************************
     Function Name: findBestTrail
     Purpose: To find the best trail card
     Parameters:
     aTable, a Table instance passed by value
     Return Value: The best trail card, a Card instance
     **********************************************************************/
    protected Card findBestTrail(Table aTable) {
        // Find best trail card
        Card bestTrailCard = new Card();

        for (Card card : mHand.getCards()) {
            if (bestTrailCard.getWeight() == 0) {
                bestTrailCard = card;
            }
            else if (card.getWeight() < bestTrailCard.getWeight()) {
                bestTrailCard = card;
            }
        }

        mMessage = "Trail: " + bestTrailCard.getName();
        mMessage += "\nHeuristic: " + bestTrailCard.getWeight();

        return bestTrailCard;
    }

    /**********************************************************************
     Function Name: generateSetCombinations
     Purpose: To generate set combinations
     Parameters:
     aLooseSet, a Set instance passed by value
     Return Value: The possible set combinations, a ArrayList of Set instances
     **********************************************************************/
    private ArrayList<Set> generateSetCombinations(Set aLooseSet) {
        ArrayList<Set> looseSets = new ArrayList<>();

        // Generate doubles
        if (aLooseSet.getSize() > 1) {
            for (int i = 0; i < aLooseSet.getSize(); i++) {
                for (int j = 0; j < aLooseSet.getSize(); j++) {
                    if (i != j) {
                        Set set = new Set();
                        set.addCard(aLooseSet.getCardAt(i));
                        set.addCard(aLooseSet.getCardAt(j));

                        if (set.getValue() < 14) {
                            if (!looseSets.contains(set)) {
                                looseSets.add(set);
                            }
                        }
                    }
                }
            }
        }

        // Generate triples
        if (aLooseSet.getSize() > 2) {
            for (int i = 0; i < aLooseSet.getSize(); i++) {
                for (int j = 0; j < aLooseSet.getSize(); j++) {
                    for (int k = 0; k < aLooseSet.getSize(); k++) {
                        if (i != j && i != k && j != k) {
                            Set set = new Set();
                            set.addCard(aLooseSet.getCardAt(i));
                            set.addCard(aLooseSet.getCardAt(j));
                            set.addCard(aLooseSet.getCardAt(k));

                            if (set.getValue() < 14) {
                                if (!looseSets.contains(set)) {
                                    looseSets.add(set);
                                }
                            }
                        }
                    }
                }
            }
        }

        return looseSets;
    }
}
