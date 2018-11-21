package edu.ramapo.ktavadze.casino;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import java.util.ArrayList;

/**
 * LooseSetRecyclerAdapter Class to facilitate loose set recycler functionality.
 */

public class LooseSetRecyclerAdapter extends RecyclerView.Adapter<LooseSetRecyclerAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<Card> mCards;
    private Move mMove;

    public LooseSetRecyclerAdapter(Context context, ArrayList<Card> cards, Move move) {
        mContext = context;
        mCards = cards;
        mMove = move;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageButton imageButtonLooseCard;

        public ViewHolder(View itemView) {
            super(itemView);

            imageButtonLooseCard = itemView.findViewById(R.id.imageButton_loose_card);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.recycler_loose_card, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Get card
        final Card card = mCards.get(position);

        // Set card image
        final String fileName = "card_" + card.getName().toLowerCase();
        holder.imageButtonLooseCard.setImageResource(mContext.getResources()
                .getIdentifier(fileName, "drawable", mContext.getPackageName()));

        // Set on click listener
        holder.imageButtonLooseCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMove.getLooseSet().contains(card)) {
                    mMove.getLooseSet().removeCard(card);
                    mMove.getLooseSetViews().remove(v);

                    v.setBackgroundColor(0);
                }
                else {
                    mMove.getLooseSet().addCard(card);
                    mMove.getLooseSetViews().add(v);

                    v.setBackgroundColor(mContext.getResources().getColor(R.color.colorAccent));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mCards.size();
    }
}