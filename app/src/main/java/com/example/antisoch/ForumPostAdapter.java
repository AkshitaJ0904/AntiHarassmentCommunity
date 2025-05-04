package com.example.antisoch;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ForumPostAdapter extends RecyclerView.Adapter<ForumPostAdapter.ViewHolder> {

    private Context context;
    private List<ForumPost> forumPosts;
    private FragmentManager fragmentManager;

    public ForumPostAdapter(Context context, List<ForumPost> forumPosts, FragmentManager fragmentManager) {
        this.context = context;
        this.forumPosts = forumPosts;
        this.fragmentManager = fragmentManager;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_forum_post, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ForumPost post = forumPosts.get(position);

        // Set user information
        holder.username.setText(post.getUsername());
        holder.postTime.setText(post.getTimePosted());

        // Set default avatar
        holder.userAvatar.setImageResource(R.drawable.default_avatar);

        // Set post content
        holder.postTitle.setText(post.getTitle());
        holder.postContent.setText(post.getContent());

        // Set post image if available
        if (post.getImageResource() != null) {
            holder.postImage.setVisibility(View.VISIBLE);
            holder.postImage.setImageResource(post.getImageResource());
        } else {
            holder.postImage.setVisibility(View.GONE);
        }

        // Set categories
        List<String> categories = post.getCategories();
        if (categories != null && categories.size() > 0) {
            holder.chipCategory1.setText(categories.get(0));
            holder.chipCategory1.setVisibility(View.VISIBLE);

            if (categories.size() > 1) {
                holder.chipCategory2.setText(categories.get(1));
                holder.chipCategory2.setVisibility(View.VISIBLE);
            } else {
                holder.chipCategory2.setVisibility(View.GONE);
            }
        } else {
            holder.chipCategory1.setVisibility(View.GONE);
            holder.chipCategory2.setVisibility(View.GONE);
        }

        // Set interaction counts
        holder.textLikes.setText(String.valueOf(post.getLikes()));
        holder.textComments.setText(String.valueOf(post.getComments()));
        holder.textShares.setText(String.valueOf(post.getShares()));

        // Navigate to CommentActivity on comment icon click
        holder.commentIcon.setOnClickListener(v -> {
            Intent intent = new Intent(context, CommentActivity.class);
            intent.putExtra("POST_ID", post.getId());
            intent.putExtra("POST_TITLE", post.getTitle());
            intent.putExtra("POST_CONTENT", post.getContent());
            intent.putExtra("USERNAME", post.getUsername());
            intent.putExtra("TIMESTAMP", post.getTimePosted());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return forumPosts.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView userAvatar;
        TextView username, postTime, postTitle, postContent;
        ImageView postImage;
        Chip chipCategory1, chipCategory2;
        TextView textLikes, textComments, textShares;
        ImageView commentIcon; // New field for comment icon

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            userAvatar = itemView.findViewById(R.id.user_avatar);
            username = itemView.findViewById(R.id.username);
            postTime = itemView.findViewById(R.id.post_time);
            postTitle = itemView.findViewById(R.id.post_title);
            postContent = itemView.findViewById(R.id.post_content);
            postImage = itemView.findViewById(R.id.post_image);
            chipCategory1 = itemView.findViewById(R.id.chip_post_category_1);
            chipCategory2 = itemView.findViewById(R.id.chip_post_category_2);
            textLikes = itemView.findViewById(R.id.likeCountText);
            textComments = itemView.findViewById(R.id.commentCountText);
            textShares = itemView.findViewById(R.id.shareCountText);
            commentIcon = itemView.findViewById(R.id.commentIcon); // Make sure this ID exists in your layout
        }
    }
}