package com.example.safespace;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {

    private Context context;
    private List<Comment> commentList;

    public CommentAdapter(Context context, List<Comment> commentList) {
        this.context = context;
        this.commentList = commentList;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_comment, parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        Comment comment = commentList.get(position);

        holder.commentUsernameText.setText(comment.getUsername());
        holder.commentText.setText(comment.getContent());
        holder.commentTimeText.setText(comment.getTimestamp());
        holder.commentLikeCount.setText(String.valueOf(comment.getLikes()));
        holder.commentDislikeCount.setText(String.valueOf(comment.getDislikes()));

        // Set click listeners
        holder.replyText.setOnClickListener(view -> {
            Toast.makeText(context, "Reply to " + comment.getUsername(), Toast.LENGTH_SHORT).show();
            // Implement reply functionality
        });

        holder.hideAllRepliesText.setOnClickListener(view -> {
            Toast.makeText(context, "Show/Hide replies", Toast.LENGTH_SHORT).show();
            // Implement show/hide replies functionality
        });

        // Update visibility of hide replies text based on whether the comment has replies
        if (comment.isHasReplies()) {
            holder.hideAllRepliesText.setVisibility(View.VISIBLE);
        } else {
            holder.hideAllRepliesText.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    public static class CommentViewHolder extends RecyclerView.ViewHolder {
        TextView commentUsernameText, commentText, commentTimeText;
        TextView commentLikeCount, commentDislikeCount, hideAllRepliesText, replyText;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            commentUsernameText = itemView.findViewById(R.id.commentUsernameText);
            commentText = itemView.findViewById(R.id.commentText);
            commentTimeText = itemView.findViewById(R.id.commentTimeText);
            commentLikeCount = itemView.findViewById(R.id.commentLikeCount);
            commentDislikeCount = itemView.findViewById(R.id.commentDislikeCount);
            hideAllRepliesText = itemView.findViewById(R.id.hideAllRepliesText);
            replyText = itemView.findViewById(R.id.replyText);
        }
    }
}