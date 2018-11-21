package edu.ramapo.ktavadze.casino;

import java.util.ArrayList;

public class Table {
    private Set mLooseSet;
    private ArrayList<Build> mBuilds;

    public Table() {
        mLooseSet = new Set();
        mBuilds = new ArrayList<>();
    }

    public Table(Set aLooseSet, ArrayList<Build> aBuilds) {
        mLooseSet = aLooseSet;
        mBuilds = aBuilds;
    }

    public Set getLooseSet() {
        return mLooseSet;
    }

    public void setLooseSet(Set aLooseSet) {
        mLooseSet = aLooseSet;
    }

    public ArrayList<Build> getBuilds() {
        return mBuilds;
    }

    /**
     Adds the specified card to the table.
     @param aCard - Card instance to be added.
     */
    public void addLooseCard(Card aCard) {
        mLooseSet.addCard(aCard);
    }

    /**
     Removes the specified card from the table.
     @param aCard - Card instance to be removed.
     */
    public void removeLooseCard(Card aCard) {
        mLooseSet.removeCard(aCard);
    }

    /**
     Adds the specified build to the table.
     @param aBuild - Build instance to be added.
     */
    public void addBuild(Build aBuild) {
        mBuilds.add(aBuild);
    }

    /**
     Removes the specified build from the table.
     @param aBuild - Build instance to be added.
     */
    public void removeBuild(Build aBuild) {
        mBuilds.remove(aBuild);
    }

    /**
     Allows the ability to increase the build at the specified index.
     @param aIndex - Integer value representing the index.
     @param aCard - Card instance to increase with.
     @param aIsHuman - Boolean value representing the new owner.
     */
    public void increaseBuild(int aIndex, Card aCard, boolean aIsHuman) {
        mBuilds.get(aIndex).increase(aCard);
        mBuilds.get(aIndex).isHuman(aIsHuman);
    }

    /**
     Allows the ability to extend the build at the specified index.
     @param aIndex - Integer value representing the index.
     @param aSet - Set instance to extend with.
     */
    public void extendBuild(int aIndex, Set aSet) {
        mBuilds.get(aIndex).extend(aSet);
    }

    /**
     Generates a string representation of the table.
     @return String value representing the table.
     */
    public String stringify() {
        StringBuilder data = new StringBuilder();

        int count = 0;

        for (Build build : mBuilds) {
            count++;

            if (count > 1) {
                data.append(" ");
            }

            data.append(build.stringify());
        }

        if (count > 0) {
            data.append(" ");
        }

        data.append(mLooseSet.stringify());

        return data.toString();
    }
}
