package edu.ramapo.ktavadze.casino;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;

/**
 * BuildsRecyclerAdapter Class to facilitate builds recycler functionality.
 */

public class BuildsRecyclerAdapter extends RecyclerView.Adapter<BuildsRecyclerAdapter.ViewHolder> {
    private static final String TAG = "BuildsRecycler";

    private Context mContext;
    private ArrayList<Build> mBuilds;
    private Move mMove;

    public BuildsRecyclerAdapter(Context context, ArrayList<Build> builds, Move move) {
        mContext = context;
        mBuilds = builds;
        mMove = move;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout linearBuildContainer;

        public ViewHolder(View itemView) {
            super(itemView);

            linearBuildContainer = itemView.findViewById(R.id.linear_build_container);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_build, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.linearBuildContainer.removeAllViews();

        // Get build
        final Build build = mBuilds.get(position);

        for (Set set : build.getSets()) {
            LinearLayout linearSet = new LinearLayout(mContext);

            LinearLayout.LayoutParams linearSetParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    1
            );
            linearSet.setLayoutParams(linearSetParams);

            linearSet.setPadding(12, 0, 12, 0);

            for (Card card : set.getCards()) {
                ImageView imageCard = new ImageView(mContext);

                imageCard.setAdjustViewBounds(true);
                imageCard.setScaleType(ImageView.ScaleType.FIT_CENTER);
                imageCard.setMaxHeight(128);
                imageCard.setPadding(12, 12, 12, 12);

                final String fileName = "card_" + card.getName().toLowerCase();
                imageCard.setImageResource(mContext.getResources()
                        .getIdentifier(fileName, "drawable", mContext.getPackageName()));

                linearSet.addView(imageCard);
            }
            holder.linearBuildContainer.addView(linearSet);
        }

        if (build.isHuman()) {
            holder.linearBuildContainer.setBackgroundColor(mContext.getResources().getColor(R.color.colorGreen));
        }
        else {
            holder.linearBuildContainer.setBackgroundColor(mContext.getResources().getColor(R.color.colorRed));
        }

        holder.linearBuildContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMove.getBuilds().contains(build)) {
                    mMove.getBuilds().remove(build);
                    mMove.getBuildViews().remove(v);

                    if (build.isHuman()) {
                        v.setBackgroundColor(mContext.getResources().getColor(R.color.colorGreen));
                    }
                    else {
                        v.setBackgroundColor(mContext.getResources().getColor(R.color.colorRed));
                    }
                }
                else {
                    mMove.getBuilds().add(build);
                    mMove.getBuildViews().add(v);

                    v.setBackgroundColor(mContext.getResources().getColor(R.color.colorAccent));
                }

                Log.d(TAG, "Clicked on: " + build.toString());
            }
        });

    }

    @Override
    public int getItemCount() {
        return mBuilds.size();
    }
}