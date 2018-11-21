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

    /**********************************************************************
     Function Name: getValue
     Purpose: To calculate the value of the build
     Return Value: The value of the build, an integer value
     **********************************************************************/
    public int getValue() {
        int value = 0;

        if (!mSets.isEmpty()) {
            for (Card card : mSets.get(0).getCards()) {
                value += card.getValue();
            }
        }

        return value;
    }

    /**********************************************************************
     Function Name: getWeight
     Purpose: To calculate the weight of the build
     Return Value: The weight of the build, an integer value
     **********************************************************************/
    public int getWeight() {
        int weight = 0;

        for (Set set : mSets) {
            weight += set.getWeight();
        }

        return weight;
    }

    /**********************************************************************
     Function Name: increase
     Purpose: To increase the build
     Parameters:
     aCard, a Card instance passed by value
     **********************************************************************/
    public void increase(Card aCard) {
        mSets.get(0).addCard(aCard);
    }

    /**********************************************************************
     Function Name: extend
     Purpose: To extend the build
     Parameters:
     aSet, a Set instance passed by value
     **********************************************************************/
    public void extend(Set aSet) {
        mSets.add(aSet);
    }

    /**********************************************************************
     Function Name: contains
     Purpose: To determine whether the Build contains another build
     Parameters:
     aBuild, a Build instance, passed by value
     Return Value: Whether the build contains another build, a boolean value
     **********************************************************************/
    public boolean contains(Build aBuild) {
        return mSets.containsAll(aBuild.getSets());
    }

    /**********************************************************************
     Function Name: equals
     Purpose: To determine whether the build equals another build
     Parameters:
     aObject, an Object instance passed by value
     Return Value: Whether the build equals another build, a boolean value
     **********************************************************************/
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
