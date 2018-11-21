package edu.ramapo.ktavadze.casino;

import java.util.ArrayList;

public class Build {
    private boolean mIsHuman;
    private ArrayList<Set> mSets = new ArrayList<>();

    public Build() {}

    public Build(boolean aIsHuman, Set aSet) {
        mIsHuman = aIsHuman;
        mSets.add(aSet);
    }

    public Build(Build aBuild) {
        mIsHuman = aBuild.isHuman();
        mSets = new ArrayList<>();

        for (Set set : aBuild.getSets()) {
            Set newSet = new Set();

            for (Card card : set.getCards()) {
                newSet.addCard(new Card(card.getName()));
            }

            mSets.add(newSet);
        }
    }

    public boolean isHuman() {
        return mIsHuman;
    }

    public void isHuman(boolean aIsHuman) {
        mIsHuman = aIsHuman;
    }

    public ArrayList<Set> getSets() {
        return mSets;
    }

    /**
     Calculates the value of the build.
     @return Integer value representing the value of the build.
     */
    public int getValue() {
        int value = 0;

        if (!mSets.isEmpty()) {
            for (Card card : mSets.get(0).getCards()) {
                value += card.getValue();
            }
        }

        return value;
    }

    /**
     Calculates the weight of the build.
     @return Integer value representing the weight of the build.
     */
    public int getWeight() {
        int weight = 0;

        for (Set set : mSets) {
            weight += set.getWeight();
        }

        return weight;
    }

    /**
     Increases the build with the specified card.
     @param aCard - Card instance to increase with.
     */
    public void increase(Card aCard) {
        mSets.get(0).addCard(aCard);
    }

    /**
     Extends the build with the specified set.
     @param aSet - Set instance to extend the build.
     */
    public void extend(Set aSet) {
        mSets.add(aSet);
    }

    /**
     Checks whether the build contains the specified build.
     @param aBuild - Build instance to check for.
     @return Boolean value representing the result of the check.
     */
    public boolean contains(Build aBuild) {
        return mSets.containsAll(aBuild.getSets());
    }

    /**
     Checks whether the build equals the specified build.
     @param aObject - Object instance to check.
     @return Boolean value representing the result of the comparison.
     */
    @Override
    public boolean equals(Object aObject) {
        // Check instance
        if (aObject == this) {
            return true;
        }

        // Check type
        if (!(aObject instanceof Build)) {
            return false;
        }

        Build aBuild = (Build) aObject;

        // Check owner
        if (aBuild.isHuman() != mIsHuman) {
            return false;
        }

        // Check size
        if (aBuild.getSets().size() != mSets.size()) {
            return false;
        }

        return mSets.containsAll(aBuild.getSets());
    }

    /**
     Generates a string representation of the build.
     @return String value representing the build.
     */
    public String stringify() {
        StringBuilder data = new StringBuilder();

        data.append("[");

        for (Set set : mSets) {
            data.append(" [");
            data.append(set.stringify());
            data.append("]");
        }

        data.append(" ]");

        return data.toString();
    }
}
