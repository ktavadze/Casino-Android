package edu.ramapo.ktavadze.casino;

import android.view.View;

import java.util.ArrayList;

public class Move {
    private String mType;

    private Card mPlayerCard;
    private Set mLooseSet;
    private ArrayList<Build> mBuilds;

    private View mPlayerCardView;
    private ArrayList<View> mLooseSetViews;
    private ArrayList<View> mBuildViews;

    public Move() {
        mType = "";

        mPlayerCard = new Card();
        mLooseSet = new Set();
        mBuilds = new ArrayList<>();

        mPlayerCardView = null;
        mLooseSetViews = new ArrayList<>();
        mBuildViews = new ArrayList<>();
    }

    public String getType() {
        return mType;
    }

    public void setType(String aType) {
        mType = aType;
    }

    public Card getPlayerCard() {
        return mPlayerCard;
    }

    public void setPlayerCard(Card aPlayerCard) {
        mPlayerCard = aPlayerCard;
    }

    public Set getLooseSet() {
        return mLooseSet;
    }

    public ArrayList<Build> getBuilds() {
        return mBuilds;
    }

    public View getPlayerCardView() {
        return mPlayerCardView;
    }

    public void setPlayerCardView(View aPlayerCardView) {
        mPlayerCardView = aPlayerCardView;
    }

    public ArrayList<View> getLooseSetViews() {
        return mLooseSetViews;
    }

    public ArrayList<View> getBuildViews() {
        return mBuildViews;
    }

    public void clear() {
        mType = "";

        mPlayerCard = new Card();
        mLooseSet = new Set();
        mBuilds = new ArrayList<>();

        mPlayerCardView = null;
        mLooseSetViews = new ArrayList<>();
        mBuildViews = new ArrayList<>();
    }
}
