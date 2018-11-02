package edu.ramapo.ktavadze.casino;

public class Card {
    private String mName;
    private int mValue;
    private int mWeight;

    public Card(String aName) {
        mName = aName;

        // Assign value
        switch (aName.charAt(1)) {
            case 'A':
                mValue = 1;
                break;
            case '2':
                mValue = 2;
                break;
            case '3':
                mValue = 3;
                break;
            case '4':
                mValue = 4;
                break;
            case '5':
                mValue = 5;
                break;
            case '6':
                mValue = 6;
                break;
            case '7':
                mValue = 7;
                break;
            case '8':
                mValue = 8;
                break;
            case '9':
                mValue = 9;
                break;
            case 'X':
                mValue = 10;
                break;
            case 'J':
                mValue = 11;
                break;
            case 'Q':
                mValue = 12;
                break;
            case 'K':
                mValue = 13;
                break;
        }

        // Assign weight
        mWeight = 1;

        if (aName.equals("DX"))
        {
            mWeight += 8;
        }
        else if (aName.equals("S2") || aName.equals("SA"))
        {
            mWeight += 6;
        }
        else if (aName.charAt(1) == 'A')
        {
            mWeight += 4;
        }
        else if (aName.charAt(0) == 'S')
        {
            mWeight += 2;
        }
    }

    public String getName() {
        return mName;
    }

    public int getValue() {
        return mValue;
    }

    public int getWeight() {
        return mWeight;
    }

    public boolean isAce()
    {
        return mName.charAt(1) == 'A';
    }

    public boolean isSpade()
    {
        return mName.charAt(0) == 'S';
    }

    /**********************************************************************
     Function Name: equals
     Purpose: To determine whether the card equals another card
     Parameters:
     aObject, an Object instance passed by value
     Return Value: Whether the card equals another card, a boolean value
     **********************************************************************/
    @Override
    public boolean equals(Object aObject)
    {
        // Check instance
        if (aObject == this) {
            return true;
        }

        // Check type
        if (!(aObject instanceof Card)) {
            return false;
        }

        Card aCard = (Card) aObject;

        return aCard.getName().equals(mName);
    }
}
