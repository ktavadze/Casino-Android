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

    public void addLooseCard(Card aCard) {
        mLooseSet.addCard(aCard);
    }

    public void removeLooseCard(Card aCard) {
        mLooseSet.removeCard(aCard);
    }

    public void addBuild(Build aBuild) {
        mBuilds.add(aBuild);
    }

    public void removeBuild(Build aBuild) {
        mBuilds.remove(aBuild);
    }

    /**********************************************************************
     Function Name: increaseBuild
     Purpose: To increase a build on the table
     Parameters:
     aIndex, an integer. Represents the index of the build being increased
     aCard, a Card instance, passed by value. Represents the card with which
     the build will be increased
     aIsHuman, a boolean. Represents the owner of the increased build
     **********************************************************************/
    public void increaseBuild(int aIndex, Card aCard, boolean aIsHuman) {
        mBuilds.get(aIndex).increase(aCard);
        mBuilds.get(aIndex).isHuman(aIsHuman);
    }

    /**********************************************************************
     Function Name: extendBuild
     Purpose: To extend a build on the table
     Parameters:
     aIndex, an integer. Represents the index of the build being extended
     aSet, a Set instance, passed by value. Represents the set with which
     the build will be extended
     aIsHuman, a boolean. Represents the owner of the extended build
     **********************************************************************/
    public void extendBuild(int aIndex, Set aSet, boolean aIsHuman) {
        mBuilds.get(aIndex).extend(aSet);
        mBuilds.get(aIndex).isHuman(aIsHuman);
    }

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
