package com.example.antisoch;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.antisoch.database.Contact;
import com.example.antisoch.database.ContactRepository;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MyContactsActivity extends AppCompatActivity {

    private RecyclerView contactsRecyclerView;
    private ContactsAdapter contactsAdapter;
    private List<Contact> contactsList;
    private ContactRepository contactRepository;
    private EditText searchEditText;
    private FloatingActionButton addContactButton;
    
    private static final int PICK_CONTACT_REQUEST = 1001;
    private static final int READ_CONTACTS_PERMISSION_CODE = 1002;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_contacts_page);

        // Initialize repository
        contactRepository = ContactRepository.getInstance(this);

        // Initialize views
        contactsRecyclerView = findViewById(R.id.contactsRecyclerView);
        searchEditText = findViewById(R.id.searchEditText);
        addContactButton = findViewById(R.id.addContactButton);

        // Set up RecyclerView
        contactsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Load contacts from database
        loadContacts();

        // Initialize adapter
        contactsAdapter = new ContactsAdapter(this, contactsList);
        contactsRecyclerView.setAdapter(contactsAdapter);

        // Set up swipe functionality
        setupSwipeToDeleteAndFavorite();

        // Set up search functionality
        setupSearch();

        // Set click listener for add contact button
        addContactButton.setOnClickListener(v -> {
            showAddContactOptions();
        });
    }
    
    private void showAddContactOptions() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Contact");
        String[] options = {"Add New Contact", "Import from Phone"};
        
        builder.setItems(options, (dialog, which) -> {
            switch (which) {
                case 0: // Add new contact
                    Toast.makeText(MyContactsActivity.this, "Add new contact feature coming soon", Toast.LENGTH_SHORT).show();
                    break;
                case 1: // Import from phone
                    importContactFromPhone();
                    break;
            }
        });
        
        builder.show();
    }
    
    private void importContactFromPhone() {
        // Check permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) 
                != PackageManager.PERMISSION_GRANTED) {
            // Request permission
            ActivityCompat.requestPermissions(this, 
                    new String[]{Manifest.permission.READ_CONTACTS}, 
                    READ_CONTACTS_PERMISSION_CODE);
        } else {
            // Permission already granted, open contacts picker
            openContactsPicker();
        }
    }
    
    private void openContactsPicker() {
        Intent pickContactIntent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(pickContactIntent, PICK_CONTACT_REQUEST);
    }
    
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, 
                                          @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == READ_CONTACTS_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted
                openContactsPicker();
            } else {
                // Permission denied
                Toast.makeText(this, "Permission required to access contacts", 
                        Toast.LENGTH_SHORT).show();
            }
        }
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_CONTACT_REQUEST && resultCode == RESULT_OK) {
            if (data != null) {
                Uri contactUri = data.getData();
                if (contactUri != null) {
                    retrieveContactInfo(contactUri);
                }
            }
        }
    }
    
    private void retrieveContactInfo(Uri contactUri) {
        String displayName = "";
        String phoneNumber = "";
        String company = "";
        
        // Get the contact's display name
        try (Cursor cursor = getContentResolver().query(contactUri, null, null, null, null)) {
            if (cursor != null && cursor.moveToFirst()) {
                int nameIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
                if (nameIndex != -1) {
                    displayName = cursor.getString(nameIndex);
                }
                
                // Get the contact's ID
                String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                
                // Get phone number
                try (Cursor phoneCursor = getContentResolver().query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                        new String[]{contactId},
                        null)) {
                    if (phoneCursor != null && phoneCursor.moveToFirst()) {
                        phoneNumber = phoneCursor.getString(
                                phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    }
                }
                
                // Get company/organization
                try (Cursor orgCursor = getContentResolver().query(
                        ContactsContract.Data.CONTENT_URI,
                        null,
                        ContactsContract.Data.CONTACT_ID + " = ? AND " +
                                ContactsContract.Data.MIMETYPE + " = ?",
                        new String[]{contactId, ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE},
                        null)) {
                    if (orgCursor != null && orgCursor.moveToFirst()) {
                        company = orgCursor.getString(
                                orgCursor.getColumnIndex(ContactsContract.CommonDataKinds.Organization.COMPANY));
                        
                        // Get job title if available
                        String title = orgCursor.getString(
                                orgCursor.getColumnIndex(ContactsContract.CommonDataKinds.Organization.TITLE));
                        
                        if (title != null && !title.isEmpty()) {
                            company = company + ", " + title;
                        }
                    }
                }
            }
        } catch (Exception e) {
            Toast.makeText(this, "Error retrieving contact: " + e.getMessage(), 
                    Toast.LENGTH_SHORT).show();
            return;
        }
        
        // Create and save the contact
        if (!displayName.isEmpty()) {
            Contact newContact = new Contact(
                    displayName, 
                    phoneNumber, 
                    company.isEmpty() ? "Personal Contact" : company, 
                    "", // We'll leave title empty as we combined it with company
                    false, 
                    null);
            
            contactRepository.insertContact(newContact);
            
            // Refresh contacts list
            loadContacts();
            contactsAdapter.updateContacts(contactsList);
            
            Toast.makeText(this, "Contact imported: " + displayName, 
                    Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Could not retrieve contact information", 
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh contacts list in case any changes happened in another activity
        loadContacts();
        if (contactsAdapter != null) {
            contactsAdapter.notifyDataSetChanged();
        }
    }

    private void loadContacts() {
        // Populate database with initial data if it's empty
        contactRepository.populateInitialData();
        
        // Load all contacts from database
        contactsList = contactRepository.getAllContacts();
        
        // Log the contact count
        android.util.Log.d("MyContactsActivity", "Loaded " + contactsList.size() + " contacts from database");
    }

    private void setupSearch() {
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    List<Contact> searchResults = contactRepository.searchContacts(s.toString());
                    contactsAdapter.updateContacts(searchResults);
                } else {
                    contactsAdapter.updateContacts(contactRepository.getAllContacts());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void setupSwipeToDeleteAndFavorite() {
        ItemTouchHelper.SimpleCallback swipeCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                
                if (direction == ItemTouchHelper.RIGHT) {
                    // Swipe right to delete
                    showDeleteConfirmationDialog(position);
                } else {
                    // Swipe left to favorite
                    toggleFavoriteStatus(position);
                }
            }
            
            @Override
            public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                super.clearView(recyclerView, viewHolder);
                
                // Reset the view when swiping is done
                View itemView = viewHolder.itemView;
                View foreground = itemView.findViewById(R.id.contactCardView);
                View deleteBackground = itemView.findViewById(R.id.deleteLayout);
                View favoriteBackground = itemView.findViewById(R.id.starLayout);
                
                // Reset both backgrounds to be invisible
                deleteBackground.setVisibility(View.GONE);
                favoriteBackground.setVisibility(View.GONE);
                
                // Reset foreground position
                foreground.setTranslationX(0);
            }

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, 
                                   @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, 
                                   int actionState, boolean isCurrentlyActive) {
                
                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                    View itemView = viewHolder.itemView;
                    View foreground = itemView.findViewById(R.id.contactCardView);
                    View deleteBackground = itemView.findViewById(R.id.deleteLayout);
                    View favoriteBackground = itemView.findViewById(R.id.starLayout);
                    
                    // Reset backgrounds initially
                    deleteBackground.setVisibility(View.GONE);
                    favoriteBackground.setVisibility(View.GONE);
                    
                    // Handle right swipe (delete)
                    if (dX > 0) {
                        deleteBackground.setVisibility(View.VISIBLE);
                        foreground.setTranslationX(dX);
                    } 
                    // Handle left swipe (favorite)
                    else if (dX < 0) {
                        favoriteBackground.setVisibility(View.VISIBLE);
                        foreground.setTranslationX(dX);
                    }
                    return; // Skip default implementation
                }
                
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };

        new ItemTouchHelper(swipeCallback).attachToRecyclerView(contactsRecyclerView);
    }
    
    private void showDeleteConfirmationDialog(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Contact");
        builder.setMessage("Are you sure you want to delete this contact?");
        
        builder.setPositiveButton("Delete", (dialog, which) -> {
            // Delete the contact
            deleteContact(position);
        });
        
        builder.setNegativeButton("Cancel", (dialog, which) -> {
            // Reset the item view
            contactsAdapter.notifyItemChanged(position);
        });
        
        builder.setOnCancelListener(dialog -> {
            // Reset the item view if dialog is dismissed
            contactsAdapter.notifyItemChanged(position);
        });
        
        builder.show();
    }
    
    private void deleteContact(int position) {
        if (position >= 0 && position < contactsList.size()) {
            Contact deletedContact = contactsList.get(position);
            
            // Delete from database first
            contactRepository.deleteContact(deletedContact);
            
            // Then update UI
            contactsList.remove(position);
            contactsAdapter.notifyItemRemoved(position);
            
            // Show confirmation toast
            Toast.makeText(this, "Contact deleted: " + deletedContact.getName(), Toast.LENGTH_SHORT).show();
            
            // Log the new count
            android.util.Log.d("MyContactsActivity", "Contact deleted. New count: " + contactRepository.getContactCount());
        }
    }
    
    private void toggleFavoriteStatus(int position) {
        if (position >= 0 && position < contactsList.size()) {
            Contact contact = contactsList.get(position);
            contact.setFavorite(!contact.isFavorite());
            
            // Update database first
            contactRepository.updateContact(contact);
            
            // Then update UI
            contactsAdapter.notifyItemChanged(position);
            
            // Show confirmation toast
            String message = contact.isFavorite() ? 
                    "Contact added to favorites: " + contact.getName() :
                    "Contact removed from favorites: " + contact.getName();
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            
            // Log the updated state
            android.util.Log.d("MyContactsActivity", "Contact favorite status updated. Total favorites: " + 
                    contactRepository.getFavoriteContactCount());
        }
    }
    
    private void notifyContactsChanged() {
        // This helps ensure the ProfileActivity gets updated
        // by forcing an update to the SharedPreferences
        contactRepository.getAllContacts(); // This forces a refresh
        
        // Log the current count
        android.util.Log.d("MyContactsActivity", "Contact list changed - current count: " + 
                contactRepository.getContactCount());
    }

    public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ContactViewHolder> {
        private Context context;
        private List<Contact> contacts;
        
        public ContactsAdapter(Context context, List<Contact> contacts) {
            this.context = context;
            this.contacts = contacts;
        }
        
        public void updateContacts(List<Contact> newContacts) {
            this.contacts = newContacts;
            notifyDataSetChanged();
        }
        
        @NonNull
        @Override
        public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.contact_item, parent, false);
            return new ContactViewHolder(view);
        }
        
        @Override
        public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
            Contact contact = contacts.get(position);
            
            holder.contactName.setText(contact.getName());
            holder.contactCompany.setText(contact.getCompany());
            holder.contactTitle.setText(contact.getTitle());
            
            // Show star icon for favorite contacts
            if (contact.isFavorite()) {
                holder.favoriteIcon.setVisibility(View.VISIBLE);
            } else {
                holder.favoriteIcon.setVisibility(View.GONE);
            }
        }
        
        @Override
        public int getItemCount() {
            return contacts.size();
        }
        
        public class ContactViewHolder extends RecyclerView.ViewHolder {
            ImageView contactImage;
            TextView contactName;
            TextView contactCompany;
            TextView contactTitle;
            ImageView favoriteIcon;
            
            public ContactViewHolder(@NonNull View itemView) {
                super(itemView);
                contactImage = itemView.findViewById(R.id.contactImage);
                contactName = itemView.findViewById(R.id.contactName);
                contactCompany = itemView.findViewById(R.id.contactCompany);
                contactTitle = itemView.findViewById(R.id.contactTitle);
                favoriteIcon = itemView.findViewById(R.id.favoriteIcon);
            }
        }
    }
}