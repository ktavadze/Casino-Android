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
 * ComputerHandRecyclerAdapter Class to facilitate computer hand recycler functionality.
 */

public class ComputerHandRecyclerAdapter extends RecyclerView.Adapter<ComputerHandRecyclerAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<Card> mCards;

    public ComputerHandRecyclerAdapter(Context context, ArrayList<Card> cards) {
        mContext = context;
        mCards = cards;
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
        View view = LayoutInflater.from(mContext)
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
    }

    @Override
    public int getItemCount() {
        return mCards.size();
    }
}