package com.example.firebase;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class FirestoreSchemaExporter {

    private FirebaseFirestore db;

    public FirestoreSchemaExporter() {
        db = FirebaseFirestore.getInstance();
    }

    public void exportFirestoreSchema() {
        // Replace with the collection you want to inspect
        CollectionReference collectionRef = db.collection("users");

        collectionRef.get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        if (querySnapshot != null) {
                            Map<String, Object> schema = new HashMap<>();
                            for (DocumentSnapshot documentSnapshot : querySnapshot) {
                                buildSchema(documentSnapshot, schema);
                            }

                            // Convert to JSON for better readability
                            JSONObject json = new JSONObject(schema);
                            try {
                                System.out.println(json.toString(2)); // Pretty-print JSON
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    } else {
                        System.err.println("Error retrieving data: " + task.getException());
                    }
                });
    }

    private void buildSchema(DocumentSnapshot documentSnapshot, Map<String, Object> schema) {
        for (Map.Entry<String, Object> entry : documentSnapshot.getData().entrySet()) {
            schema.put(entry.getKey(), entry.getValue());
        }
    }
}
