package com.example.soraaihelp;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MyContactsActivity extends AppCompatActivity {

    private RecyclerView contactsRecyclerView;
    private ContactsAdapter contactsAdapter;
    private List<Contact> contactsList;
    private ImageView btnBackContacts;
    private ImageView btnAddContact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_contacts_page);

        // Initialize views
        contactsRecyclerView = findViewById(R.id.contactsRecyclerView);
        btnBackContacts = findViewById(R.id.btnBackContacts);
        btnAddContact = findViewById(R.id.btnAddContact);

        // Set up RecyclerView
        contactsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize contacts list
        contactsList = new ArrayList<>();
        loadDummyContacts();

        // Initialize adapter
        contactsAdapter = new ContactsAdapter(this, contactsList);
        contactsRecyclerView.setAdapter(contactsAdapter);

        // Set up swipe functionality
        setupSwipeToDeleteAndStar();

        // Set click listener for back button
        btnBackContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Set click listener for add contact button
        btnAddContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MyContactsActivity.this, R.string.add_contact_soon, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadDummyContacts() {
        // Using ic_launcher instead of ic_launcher_round to fix resource issues
        contactsList.add(new Contact("Ayush", "(480) 555-0103", R.drawable.ic_launcher));
        contactsList.add(new Contact("Bhusham Shetty Dhanus", "(270) 555-0117", R.drawable.ic_launcher, true));
        contactsList.add(new Contact("Aditya", "(684) 555-0102", R.drawable.ic_launcher));
        contactsList.add(new Contact("Sivin", "(704) 555-0127", R.drawable.ic_launcher));
        contactsList.add(new Contact("Palak", "(505) 555-0125", R.drawable.ic_launcher));
    }

    private void setupSwipeToDeleteAndStar() {
        SwipeHelper swipeHelper = new SwipeHelper(this, contactsRecyclerView) {
            @Override
            public void instantiateUnderlayButton(RecyclerView.ViewHolder viewHolder, List<UnderlayButton> underlayButtons) {
                // Right swipe (Delete)
                underlayButtons.add(new UnderlayButton(
                        "Delete",
                        0,
                        getResources().getColor(android.R.color.holo_red_light),
                        new UnderlayButtonClickListener() {
                            @Override
                            public void onClick(int position) {
                                // Remove contact after delay
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        contactsList.remove(position);
                                        contactsAdapter.notifyItemRemoved(position);
                                        Toast.makeText(MyContactsActivity.this, "Contact Deleted", Toast.LENGTH_SHORT).show();
                                    }
                                }, 2500); // 2.5 seconds delay
                            }
                        }
                ));

                // Left swipe (Star/Priority)
                underlayButtons.add(new UnderlayButton(
                        "Star",
                        0,
                        getResources().getColor(android.R.color.holo_green_light),
                        new UnderlayButtonClickListener() {
                            @Override
                            public void onClick(int position) {
                                // Mark contact as priority after delay
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        Contact contact = contactsList.get(position);
                                        contact.setPriority(!contact.isPriority());
                                        contactsAdapter.notifyItemChanged(position);
                                        Toast.makeText(MyContactsActivity.this,
                                                contact.isPriority() ? "Priority Added" : "Priority Removed",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }, 2500); // 2.5 seconds delay
                            }
                        }
                ));
            }
        };
    }

    // Contact model class
    public static class Contact {
        private String name;
        private String phoneNumber;
        private int imageResource;
        private boolean priority;

        public Contact(String name, String phoneNumber, int imageResource) {
            this.name = name;
            this.phoneNumber = phoneNumber;
            this.imageResource = imageResource;
            this.priority = false;
        }

        public Contact(String name, String phoneNumber, int imageResource, boolean priority) {
            this.name = name;
            this.phoneNumber = phoneNumber;
            this.imageResource = imageResource;
            this.priority = priority;
        }

        public String getName() {
            return name;
        }

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public int getImageResource() {
            return imageResource;
        }

        public boolean isPriority() {
            return priority;
        }

        public void setPriority(boolean priority) {
            this.priority = priority;
        }
    }

    // Adapter for contacts RecyclerView
    public static class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ContactViewHolder> {
        private Context context;
        private List<Contact> contacts;

        public ContactsAdapter(Context context, List<Contact> contacts) {
            this.context = context;
            this.contacts = contacts;
        }

        @NonNull
        @Override
        public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            // Use contact_item instead of item_contact to match the actual layout file name
            View view = LayoutInflater.from(context).inflate(R.layout.contact_item, parent, false);
            return new ContactViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
            Contact contact = contacts.get(position);
            holder.contactName.setText(contact.getName());
            holder.contactNumber.setText(contact.getPhoneNumber());
            holder.contactImage.setImageResource(contact.getImageResource());

            // Show star icon if contact is priority
            holder.starIcon.setVisibility(contact.isPriority() ? View.VISIBLE : View.GONE);
        }

        @Override
        public int getItemCount() {
            return contacts.size();
        }

        public static class ContactViewHolder extends RecyclerView.ViewHolder {
            ImageView contactImage;
            TextView contactName;
            TextView contactNumber;
            ImageView starIcon;

            public ContactViewHolder(@NonNull View itemView) {
                super(itemView);
                contactImage = itemView.findViewById(R.id.contactImage);
                contactName = itemView.findViewById(R.id.contactName);
                contactNumber = itemView.findViewById(R.id.contactNumber);
                starIcon = itemView.findViewById(R.id.starIcon);
            }
        }
    }

    // Abstract class for swipe functionality
    public abstract static class SwipeHelper extends ItemTouchHelper.SimpleCallback {
        private RecyclerView recyclerView;
        private List<UnderlayButton> buttons;
        private float swipeThreshold = 0.5f;
        private Context context;
        private boolean isDeleting = false;
        private boolean isStarring = false;
        private long swipeStartTime = 0;

        public abstract void instantiateUnderlayButton(RecyclerView.ViewHolder viewHolder, List<UnderlayButton> underlayButtons);

        public SwipeHelper(Context context, RecyclerView recyclerView) {
            super(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
            this.recyclerView = recyclerView;
            this.context = context;
            this.buttons = new ArrayList<>();
            new ItemTouchHelper(this).attachToRecyclerView(recyclerView);
        }

        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getAdapterPosition();

            if (direction == ItemTouchHelper.LEFT) {
                // Star/Priority action
                if (!isStarring) {
                    isStarring = true;
                    swipeStartTime = System.currentTimeMillis();

                    // Check if held for more than 2 seconds
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            long elapsedTime = System.currentTimeMillis() - swipeStartTime;
                            if (isStarring && elapsedTime >= 2000) {
                                UnderlayButton button = buttons.get(1); // Star button
                                button.listener.onClick(position);
                            }
                            isStarring = false;
                        }
                    }, 2000);
                }
            } else if (direction == ItemTouchHelper.RIGHT) {
                // Delete action
                if (!isDeleting) {
                    isDeleting = true;
                    swipeStartTime = System.currentTimeMillis();

                    // Check if held for more than 2 seconds
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            long elapsedTime = System.currentTimeMillis() - swipeStartTime;
                            if (isDeleting && elapsedTime >= 2000) {
                                UnderlayButton button = buttons.get(0); // Delete button
                                button.listener.onClick(position);
                            }
                            isDeleting = false;
                        }
                    }, 2000);
                }
            }

            // Reset the view after swipe
            recyclerView.getAdapter().notifyItemChanged(viewHolder.getAdapterPosition());
        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView,
                                @NonNull RecyclerView.ViewHolder viewHolder,
                                float dX, float dY, int actionState, boolean isCurrentlyActive) {

            int position = viewHolder.getAdapterPosition();
            float translationX = dX;
            View itemView = viewHolder.itemView;

            if (position < 0) {
                return;
            }

            buttons = new ArrayList<>();
            instantiateUnderlayButton(viewHolder, buttons);

            // Draw background
            if (dX > 0) { // Swiping right (Delete)
                View deleteView = viewHolder.itemView.findViewById(R.id.deleteLayout);
                if (deleteView != null) {
                    deleteView.setVisibility(View.VISIBLE);
                }
            } else if (dX < 0) { // Swiping left (Star)
                View starView = viewHolder.itemView.findViewById(R.id.starLayout);
                if (starView != null) {
                    starView.setVisibility(View.VISIBLE);
                }
            }

            // Reset views when not swiping
            if (dX == 0) {
                View deleteView = viewHolder.itemView.findViewById(R.id.deleteLayout);
                View starView = viewHolder.itemView.findViewById(R.id.starLayout);
                if (deleteView != null) deleteView.setVisibility(View.GONE);
                if (starView != null) starView.setVisibility(View.GONE);
            }

            super.onChildDraw(c, recyclerView, viewHolder, translationX, dY, actionState, isCurrentlyActive);
        }

        public class UnderlayButton {
            private String text;
            private int imageResId;
            private int color;
            private UnderlayButtonClickListener listener;

            public UnderlayButton(String text, int imageResId, int color, UnderlayButtonClickListener listener) {
                this.text = text;
                this.imageResId = imageResId;
                this.color = color;
                this.listener = listener;
            }
        }

        public interface UnderlayButtonClickListener {
            void onClick(int position);
        }
    }
}