package com.example.antisoch;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility class to handle Firebase operations for forum posts
 */
public class FirebaseUtil {

    private static final String POSTS_COLLECTION = "forum_posts";
    private static FirebaseFirestore db;

    /**
     * Get FirebaseFirestore instance
     * @return FirebaseFirestore instance
     */
    public static FirebaseFirestore getFirestore() {
        if (db == null) {
            db = FirebaseFirestore.getInstance();
        }
        return db;
    }

    /**
     * Get forum posts collection reference
     * @return CollectionReference for forum posts
     */
    public static CollectionReference getPostsCollection() {
        return getFirestore().collection(POSTS_COLLECTION);
    }

    /**
     * Save a forum post to Firestore
     * @param post ForumPost object to save
     * @param listener PostUploadListener to handle success/failure
     */
    public static void savePost(ForumPost post, PostUploadListener listener) {
        // Create a new document with auto-generated ID
        DocumentReference newPostRef = getPostsCollection().document();
        
        // Set the post ID to the document ID
        post.setId(newPostRef.getId());
        
        // Save the post to Firestore
        newPostRef.set(post)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        if (listener != null) {
                            listener.onSuccess(post);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if (listener != null) {
                            listener.onFailure(e.getMessage());
                        }
                    }
                });
    }

    /**
     * Get all forum posts
     * @param listener PostsLoadListener to handle results
     */
    public static void getAllPosts(PostsLoadListener listener) {
        getPostsCollection()
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<ForumPost> posts = new ArrayList<>();
                        
                        if (!queryDocumentSnapshots.isEmpty()) {
                            posts = queryDocumentSnapshots.toObjects(ForumPost.class);
                        }
                        
                        if (listener != null) {
                            listener.onPostsLoaded(posts);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if (listener != null) {
                            listener.onFailure(e.getMessage());
                        }
                    }
                });
    }

    /**
     * Get posts filtered by tag/category
     * @param tag Tag/category to filter by
     * @param listener PostsLoadListener to handle results
     */
    public static void getPostsByTag(String tag, PostsLoadListener listener) {
        getPostsCollection()
                .whereEqualTo("tag", tag)
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<ForumPost> posts = new ArrayList<>();
                        
                        if (!queryDocumentSnapshots.isEmpty()) {
                            posts = queryDocumentSnapshots.toObjects(ForumPost.class);
                        }
                        
                        if (listener != null) {
                            listener.onPostsLoaded(posts);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if (listener != null) {
                            listener.onFailure(e.getMessage());
                        }
                    }
                });
    }

    /**
     * Interface for post upload callbacks
     */
    public interface PostUploadListener {
        void onSuccess(ForumPost post);
        void onFailure(String errorMessage);
    }

    /**
     * Interface for posts loading callbacks
     */
    public interface PostsLoadListener {
        void onPostsLoaded(List<ForumPost> posts);
        void onFailure(String errorMessage);
    }
} 