package edu.ramapo.ktavadze.casino;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * FilesRecyclerAdapter Class to facilitate files recycler functionality.
 */

public class FilesRecyclerAdapter extends RecyclerView.Adapter<FilesRecyclerAdapter.ViewHolder> {
    private FilesFragment mFilesFragment;
    private ArrayList<String> mFileNames;

    public FilesRecyclerAdapter(FilesFragment filesFragment, ArrayList<String> fileNames) {
        mFilesFragment = filesFragment;
        mFileNames = fileNames;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textFileName;

        public ViewHolder(View itemView) {
            super(itemView);

            textFileName = itemView.findViewById(R.id.text_file_name);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_file, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Get file name
        final String fileName = mFileNames.get(position);

        // Set text
        holder.textFileName.setText(fileName);

        // Set on click listener
        holder.textFileName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFilesFragment.actionLoadFile(fileName);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mFileNames.size();
    }
}