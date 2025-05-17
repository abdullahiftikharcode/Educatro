package com.example.educatro.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.educatro.R;
import com.example.educatro.models.Author;

import java.util.List;

public class AuthorAdapter extends RecyclerView.Adapter<AuthorAdapter.AuthorViewHolder> {

    private List<Author> authors;
    private OnAuthorClickListener listener;

    public interface OnAuthorClickListener {
        void onAuthorClick(Author author);
        void onFollowClick(Author author);
    }

    public AuthorAdapter(List<Author> authors) {
        this.authors = authors;
    }

    public AuthorAdapter(List<Author> authors, OnAuthorClickListener listener) {
        this.authors = authors;
        this.listener = listener;
    }

    @NonNull
    @Override
    public AuthorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_author, parent, false);
        return new AuthorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AuthorViewHolder holder, int position) {
        Author author = authors.get(position);
        
        holder.nameTextView.setText(author.getName());
        holder.categoryTextView.setText(author.getCategory());
        
        // Load profile image with Glide
        if (author.getProfileImageUrl() != null && !author.getProfileImageUrl().isEmpty()) {
            Glide.with(holder.itemView.getContext())
                    .load(author.getProfileImageUrl())
                    .placeholder(R.drawable.placeholder_profile)
                    .error(R.drawable.placeholder_profile)
                    .circleCrop()
                    .into(holder.profileImageView);
        } else {
            holder.profileImageView.setImageResource(R.drawable.placeholder_profile);
        }
        
        // Set follow button text based on follow status
        holder.followButton.setText(author.isFollowing() ? 
                holder.itemView.getContext().getString(R.string.following) : 
                holder.itemView.getContext().getString(R.string.follow));
        
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onAuthorClick(author);
                }
            }
        });
        
        holder.followButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toggle follow status
                author.setFollowing(!author.isFollowing());
                notifyItemChanged(holder.getAdapterPosition());
                
                if (listener != null) {
                    listener.onFollowClick(author);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return authors.size();
    }

    public void setAuthors(List<Author> authors) {
        this.authors = authors;
        notifyDataSetChanged();
    }

    static class AuthorViewHolder extends RecyclerView.ViewHolder {
        ImageView profileImageView;
        TextView nameTextView;
        TextView categoryTextView;
        Button followButton;

        AuthorViewHolder(@NonNull View itemView) {
            super(itemView);
            profileImageView = itemView.findViewById(R.id.profileImageView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            categoryTextView = itemView.findViewById(R.id.categoryTextView);
            followButton = itemView.findViewById(R.id.followButton);
        }
    }
} 