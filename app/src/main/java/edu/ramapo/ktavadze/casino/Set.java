package edu.ramapo.ktavadze.casino;

import java.util.ArrayList;

public class Set {
    ArrayList<Card> mCards = new ArrayList<>();

    public Set() {}

    public Set(ArrayList<Card> aCards) {
        mCards = aCards;
    }

    public ArrayList<Card> getCards() {
        return mCards;
    }

    public Card getCardAt(int aIndex) {
        return mCards.get(aIndex);
    }

    public Card getFirstCard() {
        return mCards.get(0);
    }

    public Card getLastCard() {
        return mCards.get(mCards.size() - 1);
    }

    public void addCard(Card aCard) {
        mCards.add(aCard);
    }

    public void removeCard(Card aCard) {
        mCards.remove(aCard);
    }

    public void addSet(Set aSet) {
        mCards.addAll(aSet.getCards());
    }

    public void removeSet(Set aSet) {
        mCards.removeAll(aSet.getCards());
    }

    public void clear() {
        mCards.clear();
    }

    public boolean isEmpty() {
        return mCards.isEmpty();
    }

    public int getSize() {
        return mCards.size();
    }

    /**********************************************************************
     Function Name: getValue
     Purpose: To calculate the value of a set
     Return Value: The value of a set, an integer value
     **********************************************************************/
    public int getValue() {
        int value = 0;

        for (Card card : mCards) {
            value += card.getValue();
        }

        return value;
    }

    /**********************************************************************
     Function Name: getWeight
     Purpose: To calculate the weight of a set
     Return Value: The weight of a set, an integer value
     **********************************************************************/
    public int getWeight() {
        int weight = 0;

        for (Card card : mCards) {
            weight += card.getWeight();
        }

        return weight;
    }

    /**********************************************************************
     Function Name: contains
     Purpose: To determine whether the set contains a card
     Parameters:
     aCard, a Card instance, passed by value
     Return Value: Whether the set contains a card, a boolean value
     **********************************************************************/
    public boolean contains(Card aCard) {
        return mCards.contains(aCard);
    }

    /**********************************************************************
     Function Name: contains
     Purpose: To determine whether the set contains another set
     Parameters:
     aSet, a Set instance, passed by value
     Return Value: Whether the set contains another set, a boolean value
     **********************************************************************/
    public boolean contains(Set aSet) {
        return mCards.containsAll(aSet.getCards());
    }

    /**********************************************************************
     Function Name: contains
     Purpose: To determine whether the set contains a build
     Parameters:
     aBuild, a Build instance, passed by value
     Return Value: Whether the set contains a build, a boolean value
     **********************************************************************/
    public boolean contains(Build aBuild) {
        for (Set set : aBuild.getSets()) {
            if (!mCards.containsAll(set.getCards())) {
                return false;
            }
        }

        return true;
    }

    /**********************************************************************
     Function Name: equals
     Purpose: To determine whether the set equals another set
     Parameters:
     aObject, an Object instance passed by value
     Return Value: Whether the set equals another set, a boolean value
     **********************************************************************/
    @Override
    public boolean equals(Object aObject) {
        // Check instance
        if (aObject == this) {
            return true;
        }

        // Check type
        if (!(aObject instanceof Set)) {
            return false;
        }

        Set aSet = (Set) aObject;

        // Check size
        if (aSet.getSize() != mCards.size()) {
            return false;
        }

        return mCards.containsAll(aSet.getCards());
    }

    public String stringify() {
        StringBuilder data = new StringBuilder();

        int count = 0;

        for (Card card : mCards) {
            count++;

            if (count > 1) {
                data.append(" ");
            }

            data.append(card.getName());
        }

        return data.toString();
    }
}
