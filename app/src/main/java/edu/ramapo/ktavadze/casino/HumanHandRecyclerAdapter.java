package edu.ramapo.ktavadze.casino;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import java.util.ArrayList;

/**
 * HumanHandRecyclerAdapter Class to facilitate human hand recycler functionality.
 */

public class HumanHandRecyclerAdapter extends RecyclerView.Adapter<HumanHandRecyclerAdapter.ViewHolder> {
    private static final String TAG = "HumanHandRecycler";


    private Context mContext;
    private ArrayList<Card> mCards;
    private Card mSelectedCard;
    private View mSelectedView;

    public HumanHandRecyclerAdapter(Context context, ArrayList<Card> cards, Card selectedCard, View selectedView) {
        mContext = context;
        mCards = cards;
        mSelectedCard = selectedCard;
        mSelectedView = selectedView;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageButton imageButtonPlayerCard;

        public ViewHolder(View itemView) {
            super(itemView);

            imageButtonPlayerCard = itemView.findViewById(R.id.imageButton_player_card);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_player_card, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Get card
        final Card card = mCards.get(position);

        // Set card image
        final String fileName = "card_" + card.getName().toLowerCase();
        holder.imageButtonPlayerCard.setImageResource(mContext.getResources()
                .getIdentifier(fileName, "drawable", mContext.getPackageName()));

        holder.imageButtonPlayerCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (card.equals(mSelectedCard)) {
                    mSelectedCard = null;
                    mSelectedView = null;

                    v.setBackgroundColor(0);
                }
                else {
                    if (mSelectedView != null) {
                        mSelectedView.setBackgroundColor(0);
                    }

                    mSelectedCard = card;
                    mSelectedView = v;

                    v.setBackgroundColor(mContext.getResources().getColor(R.color.colorAccent));
                }

                Log.d(TAG, "onClick: Clicked on: " + card.getName());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mCards.size();
    }
}