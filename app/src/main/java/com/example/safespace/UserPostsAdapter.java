package com.example.safespace;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class UserPostsAdapter extends RecyclerView.Adapter<UserPostsAdapter.PostViewHolder> {

    private List<UserPost> posts;
    private final OnPostDeleteClickListener deleteListener;
    private final OnPostEditClickListener editListener;

    public interface OnPostDeleteClickListener {
        void onDeleteClick(UserPost post);
    }

    public interface OnPostEditClickListener {
        void onEditClick(UserPost post);
    }

    public UserPostsAdapter(List<UserPost> posts,
                            OnPostDeleteClickListener deleteListener,
                            OnPostEditClickListener editListener) {
        this.posts = posts;
        this.deleteListener = deleteListener;
        this.editListener = editListener;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_user_post, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        UserPost post = posts.get(position);
        holder.bind(post);

    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public void updatePosts(List<UserPost> newPosts) {
        this.posts = newPosts;
        notifyDataSetChanged();
    }

    class PostViewHolder extends RecyclerView.ViewHolder {
        private final ImageView userAvatar;
        private final TextView userName;
        private final TextView postTime;
        private final TextView postContent;
        private final Button deleteButton;
        private final Button editButton;
        private final TextView likeCount;
        private final TextView commentCount;
        private final TextView shareCount;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            userAvatar = itemView.findViewById(R.id.userAvatar);
            userName = itemView.findViewById(R.id.userName);
            postTime = itemView.findViewById(R.id.postTime);
            postContent = itemView.findViewById(R.id.postContent);
            deleteButton = itemView.findViewById(R.id.deleteButton);
            editButton = itemView.findViewById(R.id.editButton);
            likeCount = itemView.findViewById(R.id.likeCount);
            commentCount = itemView.findViewById(R.id.commentCount);
            shareCount = itemView.findViewById(R.id.shareCount);
        }

        public void bind(UserPost post) {
            // Set user info
            userName.setText(post.getUserName());
            postTime.setText(post.getPostTime());

            // Set post content
            postContent.setText(post.getContent());

            // Set interaction counts
            likeCount.setText(String.valueOf(post.getLikeCount()));
            commentCount.setText(String.valueOf(post.getCommentCount()));
            shareCount.setText(String.valueOf(post.getShareCount()));

            // Set button click listeners
            deleteButton.setOnClickListener(v -> {
                if (deleteListener != null) {
                    deleteListener.onDeleteClick(post);
                }
            });

            editButton.setOnClickListener(v -> {
                if (editListener != null) {
                    editListener.onEditClick(post);
                }
            });
        }
    }
}