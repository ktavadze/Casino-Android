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
 * HumanHandRecyclerAdapter Class to facilitate computer hand recycler functionality.
 */

public class HumanHandRecyclerAdapter extends RecyclerView.Adapter<HumanHandRecyclerAdapter.ViewHolder> {
    private static final String TAG = "HumanHandRecycler";

    private Context mContext;
    private ArrayList<Card> mCards;

    public HumanHandRecyclerAdapter(Context context, ArrayList<Card> cards) {
        mContext = context;
        mCards = cards;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageButton imageButtonHumanCard;

        public ViewHolder(View itemView) {
            super(itemView);

            imageButtonHumanCard = itemView.findViewById(R.id.imageButton_human_card);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_human_card, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Get card
        final Card card = mCards.get(position);

        // Set card image
        final String fileName = "card_" + card.getName().toLowerCase();
        holder.imageButtonHumanCard.setImageResource(mContext.getResources()
                .getIdentifier(fileName, "drawable", mContext.getPackageName()));

        holder.imageButtonHumanCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setBackgroundColor(mContext.getResources().getColor(R.color.colorGreen));

                Log.d(TAG, "onClick: Clicked on: " + card.getName());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mCards.size();
    }
}