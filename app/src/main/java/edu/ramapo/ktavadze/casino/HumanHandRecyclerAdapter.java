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
    private Move mMove;

    public HumanHandRecyclerAdapter(Context context, ArrayList<Card> cards, Move move) {
        mContext = context;
        mCards = cards;
        mMove = move;
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
                if (card.equals(mMove.getPlayerCard())) {
                    mMove.setPlayerCard(null);
                    mMove.setPlayerCardView(null);

                    v.setBackgroundColor(0);
                }
                else {
                    if (mMove.getPlayerCardView() != null) {
                        mMove.getPlayerCardView().setBackgroundColor(0);
                    }

                    mMove.setPlayerCard(card);
                    mMove.setPlayerCardView(v);

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