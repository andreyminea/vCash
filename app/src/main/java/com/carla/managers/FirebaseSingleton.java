package com.carla.managers;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

public class FirebaseSingleton
{
    private static final String TAG = "DEBUGG Singleton";

    private static FirebaseAuth mAuth;
    private static FirebaseUser mUser;
    private static CollectionReference mUsersCollection;
    private static FirebaseFirestore mFirestoreDb;
    private static FirebaseStorage mFirebaseStorage;


    private FirebaseSingleton() {}

    public static FirebaseAuth getAuthInstance()
    {
        if(mAuth == null)
        {
            mAuth = FirebaseAuth.getInstance();
            mAuth.setLanguageCode("ro");
        }
        return mAuth;
    }


    public static FirebaseUser getUser() {
        return mUser;
    }

    public static CollectionReference getUsersCollection(){
        initDB();
        return mUsersCollection;
    }

    public static FirebaseStorage getFirebaseStorage()
    {
        if(mFirebaseStorage == null)
            mFirebaseStorage = FirebaseStorage.getInstance();
        return mFirebaseStorage;
    }

    public static void setUser(FirebaseUser mUser)
    {
        FirebaseSingleton.mUser = mUser;
    }

    private static void initDB()
    {
        if(mFirestoreDb == null)
            mFirestoreDb = FirebaseFirestore.getInstance();

        if(mUsersCollection == null)
            mUsersCollection = mFirestoreDb.collection("Users");
    }

    public static CollectionReference getUserCardCollectionReference(String documentID)
    {
        CollectionReference ref = getUsersCollection().document(documentID)
                                                                .collection("Cards");
        return ref;
    }

    public static DocumentReference getUserCardDocumentReference(String userID, String cardID)
    {
        DocumentReference ref = getUserCardCollectionReference(userID).document(cardID);
        return ref;
    }

}
