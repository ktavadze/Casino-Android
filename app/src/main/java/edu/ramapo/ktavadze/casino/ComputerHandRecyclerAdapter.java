package edu.ramapo.ktavadze.casino;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * ComputerHandRecyclerAdapter Class to facilitate computer hand recycler functionality.
 */

public class ComputerHandRecyclerAdapter extends RecyclerView.Adapter<ComputerHandRecyclerAdapter.ViewHolder> {
    private static final String TAG = "ComputerHandRecyclerAdapter";

    private Context mContext;
    private ArrayList<Card> mCards;

    public ComputerHandRecyclerAdapter(Context context, ArrayList<Card> cards) {
        mContext = context;
        mCards = cards;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CardView computerCard;

        public ViewHolder(View itemView) {
            super(itemView);

            computerCard = itemView.findViewById(R.id.cardview_computer_card);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_computer_card, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Get card info
        final Card card = mCards.get(position);

        // Set card image
        final String fileName = "card_" + card.getName().toLowerCase();
        holder.computerCard.setBackgroundResource(mContext.getResources()
                .getIdentifier(fileName, "drawable", mContext.getPackageName()));
    }

    @Override
    public int getItemCount() {
        return mCards.size();
    }
}