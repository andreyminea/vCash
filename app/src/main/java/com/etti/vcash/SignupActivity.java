package com.etti.vcash;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.etti.managers.FirebaseSingleton;
import com.etti.managers.SharedPrefsSingleton;
import com.etti.models.Card;
import com.etti.models.User;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.UploadTask;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class SignupActivity extends AppCompatActivity {

    private final String TAG = "DEBUGG";
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private final int RC_SIGN_IN = 2453;
    private MaterialButton btnOpenCamera;
    private MaterialButton btnComplete;
    private ImageView selfieView;
    private TextInputEditText firstname;
    private TextInputEditText lastname;
    private TextInputEditText country;
    private TextInputEditText city;
    private TextInputEditText address;
    private User user;

    private static final boolean DEBUG = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        user = new User();

        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.PhoneBuilder().build());

        // Create and launch sign-in intent
        if(!DEBUG)
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .setIsSmartLockEnabled(false)
                        .build(),
                RC_SIGN_IN);

    }

    @Override
    protected void onStart() {
        super.onStart();

        firstname = (TextInputEditText) findViewById(R.id.firstname);
        lastname = (TextInputEditText) findViewById(R.id.lastname);
        country = (TextInputEditText) findViewById(R.id.country);
        city = (TextInputEditText) findViewById(R.id.city);
        address = (TextInputEditText) findViewById(R.id.city);
        selfieView = (ImageView) findViewById(R.id.selfieView);
        btnOpenCamera = (MaterialButton) findViewById(R.id.btnOpenCamera);
        btnComplete = (MaterialButton) findViewById(R.id.btnComplete);

        btnOpenCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                try {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                } catch (ActivityNotFoundException e) {
                    // display error state to the user
                }
            }
        });

        btnComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "On complete button pressed");

                try {

                    if(checkField(firstname.getText().toString())
                     && checkField(lastname.getText().toString())
                     && checkField(country.getText().toString())
                     && checkField(city.getText().toString())
                     && checkField(address.getText().toString()) )
                    {
                        user.setFirstname(firstname.getText().toString());
                        user.setLastname(lastname.getText().toString());
                        user.setCountry(country.getText().toString());
                        user.setCity(city.getText().toString());
                        user.setAddress(address.getText().toString());

                        if(selfieView.getDrawable() != null)
                        {
                            Bitmap bitmap = ((BitmapDrawable) selfieView.getDrawable()).getBitmap();
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                            byte[] data = baos.toByteArray();

                            String phoneNr;
                            if(!DEBUG)
                                phoneNr = FirebaseSingleton.getUser().getPhoneNumber().toString();
                            else
                            {
                                phoneNr = "+40762211258";
                                user.setPhoneNumber(phoneNr);
                            }

                            UploadTask uploadTask = FirebaseSingleton
                                                        .getFirebaseStorage()
                                                        .getReference().child("users/" + phoneNr)
                                                        .putBytes(data);
                            String imgLink = FirebaseSingleton.getFirebaseStorage()
                                    .getReference().child("users/" + phoneNr).toString();

                            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    FirebaseSingleton.getFirebaseStorage()
                                            .getReference().child("users/" + phoneNr).getDownloadUrl()
                                            .addOnSuccessListener(new OnSuccessListener<Uri>()
                                            {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            Log.d(TAG, uri.toString());
                                            user.setImageLink(uri.toString());

                                            FirebaseSingleton.getUsersCollection().add(user)
                                                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                        @Override
                                                        public void onSuccess(DocumentReference documentReference) {
                                                            SharedPrefsSingleton.saveUserData(user, getApplicationContext());
                                                            SharedPrefsSingleton.saveUserID(documentReference.getId(), getApplicationContext());
                                                            String userFullName = user.getLastname() + " " + user.getFirstname();
                                                            createUserCard(userFullName, documentReference.getId());
                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull @NotNull Exception e) {
                                                            Log.d(TAG, "Failed to add user: " + e.toString());
                                                        }
                                                    });
                                        }
                                    });
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull @NotNull Exception e) {
                                    Log.d(TAG, "On failure: " + e.toString());
                                }
                            });
                        }
                        else
                        {
                            Log.d(TAG, "Drawable is not null");
                        }

                    }
                    else
                    {
                        Log.d(TAG, "One of the fields is empty");

                    }
                }catch (Exception e)
                {
                    Toast.makeText(SignupActivity.this,
                                "There is something wrong in your form",
                                    Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "Exception: " + e.toString());

                }
            }

        });


        // Refreshes here
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            selfieView.setImageBitmap(imageBitmap);
            if(imageBitmap.getWidth() > imageBitmap.getHeight())
                selfieView.setRotation(-90);
            Glide.with(getApplicationContext())
                    .load(imageBitmap)
                    .apply(new RequestOptions().override(800, 600))
                    .into(selfieView);
        }

        if (requestCode == RC_SIGN_IN)
        {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                // Successfully signed in
                FirebaseUser user = FirebaseSingleton.getAuthInstance().getCurrentUser();
                FirebaseSingleton.setUser(user);
                verifyUser();
            } else {
                goBack();
            }
        }
    }

    boolean checkField(String field)
    {
        if(field.length() > 0)
            return true;

        Toast.makeText(getApplicationContext(),
                    "Please fill all the fields",
                        Toast.LENGTH_SHORT).show();
        return false;
    }

    private void verifyUser()
    {
        FirebaseSingleton.getUsersCollection()
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult())
                    {
                        if(FirebaseSingleton.getUser().getPhoneNumber().equals(document.get("phoneNumber")))
                        {
                            Log.d(TAG, "is equal, user found, throw user to login screen");
                            User existingUser = document.toObject(User.class);
                            Log.d(TAG, existingUser.toString());
                            SharedPrefsSingleton.saveUserData(existingUser, getApplicationContext());
                            FirebaseSingleton.getUserCardCollectionReference(document.getId()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                    for (QueryDocumentSnapshot document : queryDocumentSnapshots)
                                    {
                                        Log.d(TAG, document.getId());
                                        Card userCard = document.toObject(Card.class);
                                        SharedPrefsSingleton.saveUserCardData(userCard, getApplicationContext());
                                        SharedPrefsSingleton.setHasAccount(getApplicationContext(), true);
                                        Log.d(TAG, userCard.toString());
                                        Toast.makeText(SignupActivity.this,
                                                "An user with this phone number is already registered",
                                                Toast.LENGTH_SHORT).show();
                                        goBack();
                                        break;
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull @NotNull Exception e) {
                                    goBack();
                                }
                            });

                        }
                    }
                    user.setPhoneNumber(FirebaseSingleton.getUser().getPhoneNumber());
                } else {

                    Log.d(TAG, "Error getting documents: ", task.getException());
                    goBack();
                }
            }
        });
    }

    private void createUserCard(String userFullName, String userID)
    {
        Card card = new Card();
        card.generateNewCard();
        card.setCardholder(userFullName);

        FirebaseSingleton.getUserCardCollectionReference(userID).add(card)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                SharedPrefsSingleton.saveUserCardID(documentReference.getId(), getApplicationContext());
                SharedPrefsSingleton.saveUserCardData(card, getApplicationContext());
                Log.d(TAG, card.toString());
                Toast.makeText(getApplicationContext(), "CVV: " + card.getCvv(), Toast.LENGTH_LONG).show();
                goBack();
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Cannot create card", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void goBack()
    {
        Intent intent = new Intent(getApplicationContext(), ReminderActivity.class);
        startActivity(intent);
    }
}