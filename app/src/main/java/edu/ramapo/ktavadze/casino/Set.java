package edu.ramapo.ktavadze.casino;

import java.util.ArrayList;

public class Set {
    private ArrayList<Card> mCards;

    public Set() {
        mCards = new ArrayList<>();
    }

    public Set(ArrayList<Card> aCards) {
        mCards = aCards;
    }

    public ArrayList<Card> getCards() {
        return mCards;
    }

    /**
     Produces the card at the specified index in the set.
     @param aIndex - Integer value representing the index.
     @return Card instance at the specified index.
     */
    public Card getCardAt(int aIndex) {
        return mCards.get(aIndex);
    }

    /**
     Produces the first card in the set.
     @return Card instance at the front of the set.
     */
    public Card getFirstCard() {
        return mCards.get(0);
    }

    /**
     Produces the last card in the set.
     @return Card instance at the back of the set.
     */
    public Card getLastCard() {
        return mCards.get(mCards.size() - 1);
    }

    /**
     Adds the specified card to the set.
     @param aCard - Card instance to be added.
     */
    public void addCard(Card aCard) {
        mCards.add(aCard);
    }

    /**
     Removes the specified card from the set.
     @param aCard - Card instance to be removed.
     */
    public void removeCard(Card aCard) {
        mCards.remove(aCard);
    }

    /**
     Adds the specified set to the set.
     @param aSet - Set instance to be added.
     */
    public void addSet(Set aSet) {
        mCards.addAll(aSet.getCards());
    }

    /**
     Removes the specified set to the set.
     @param aSet - Set instance to be removed.
     */
    public void removeSet(Set aSet) {
        mCards.removeAll(aSet.getCards());
    }

    /**
     Clears the set.
     */
    public void clear() {
        mCards.clear();
    }

    /**
     Checks whether the set is empty.
     @return Boolean value representing the result of the check.
     */
    public boolean isEmpty() {
        return mCards.isEmpty();
    }

    /**
     Produces the set size.
     @return Integer value representing the size of the set.
     */
    public int getSize() {
        return mCards.size();
    }

    /**
     Calculates the value of the set.
     @return Integer value representing the value of the set.
     */
    public int getValue() {
        int value = 0;

        for (Card card : mCards) {
            value += card.getValue();
        }

        return value;
    }

    /**
     Calculates the weight of the set.
     @return Integer value representing the weight of the set.
     */
    public int getWeight() {
        int weight = 0;

        for (Card card : mCards) {
            weight += card.getWeight();
        }

        return weight;
    }

    /**
     Checks whether the set contains the specified card.
     @param aCard - Card instance to check for.
     @return Boolean value representing the result of the check.
     */
    public boolean contains(Card aCard) {
        return mCards.contains(aCard);
    }

    /**
     Checks whether the set contains the specified set.
     @param aSet - Set instance to check for.
     @return Boolean value representing the result of the check.
     */
    public boolean contains(Set aSet) {
        return mCards.containsAll(aSet.getCards());
    }

    /**
     Checks whether the set contains the specified build.
     @param aBuild - Build instance to check for.
     @return Boolean value representing the result of the check.
     */
    public boolean contains(Build aBuild) {
        for (Set set : aBuild.getSets()) {
            if (!mCards.containsAll(set.getCards())) {
                return false;
            }
        }

        return true;
    }

    /**
     Checks whether the set equals the specified set.
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

    /**
     Generates a string representation of the set.
     @return String value representing the set.
     */
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
