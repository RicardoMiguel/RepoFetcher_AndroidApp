package com.controller;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.model.Repo;
import com.repofetcher.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RepoListAdapter extends RecyclerView.Adapter<RepoListAdapter.RepoListViewHolder> {

    private List<? extends Repo> repoList;
    private Context context;

    public RepoListAdapter(@NonNull List<? extends Repo> repoList, @NonNull Context context) {
        super();
        this.repoList = repoList;
        this.context = context;
    }

    @Override
    public RepoListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.repo_simple_info, parent, false);

        return new RepoListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RepoListViewHolder holder, int position) {
        Repo repo = repoList.get(position);
        holder.name.setText(repo.getName());
        holder.ownerName.setText(repo.getOwner().getLogin());
        Picasso.with(context).load(repo.getOwner().getAvatarUrl()).error(R.drawable.ic_face_black_48dp).into(holder.ownerAvatar);
    }

    @Override
    public int getItemCount() {
        return repoList.size();
    }

    class RepoListViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        ImageView ownerAvatar;
        TextView ownerName;

        RepoListViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.repo_name);
            ownerAvatar = (ImageView)view.findViewById(R.id.owner_avatar_image_view);
            ownerName = (TextView)view.findViewById(R.id.owner_name);
        }
    }
}
