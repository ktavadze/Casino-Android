package edu.ramapo.ktavadze.casino;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Deck {
    private ArrayList<Card> mCards;

    public Deck() {
        // Generate names
        List<String> suits = Arrays.asList("C", "D", "H", "S");
        List<String> values = Arrays.asList("2", "3", "4", "5", "6", "7", "8","9", "X", "J", "Q", "K", "A");
        ArrayList<String> names = new ArrayList<>();

        for (String suit : suits) {
            for (String value : values) {
                names.add(suit + value);
            }
        }

        // Shuffle names
        Collections.shuffle(names);

        // Generate deck
        mCards = new ArrayList<>();

        for (String name : names) {
            mCards.add(new Card(name));
        }
    }

    public Deck(ArrayList aCards) {
        mCards = aCards;
    }

    public boolean isEmpty() {
        return mCards.isEmpty();
    }

    /**********************************************************************
     Function Name: drawSet
     Purpose: To draw a set of four cards from the deck
     Return Value: The set of cards, a Set instance
     **********************************************************************/
    public Set drawSet() {
        Set set = new Set();

        for (int i = 0; i < 4; i++) {
            if (!mCards.isEmpty()) {
                set.addCard(mCards.get(0));
                mCards.remove(0);
            }
        }

        return set;
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
