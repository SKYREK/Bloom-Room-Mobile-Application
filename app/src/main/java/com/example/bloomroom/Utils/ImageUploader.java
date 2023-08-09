package com.example.bloomroom.Utils;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import java.util.UUID;

public class ImageUploader {

    private static final int REQUEST_CODE_IMAGE_PICKER = 101;

    // Call this function to prompt the user to pick an image from the gallery
    public void pickImageFromGallery(Activity activity) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        activity.startActivityForResult(intent, REQUEST_CODE_IMAGE_PICKER);
    }

    // This function should be called in the onActivityResult() of the Activity where you called pickImageFromGallery()
    public void handleImagePickerResult(int requestCode, int resultCode, Intent data, final OnImageUploadCompleteListener listener) {
        if (requestCode == REQUEST_CODE_IMAGE_PICKER && resultCode == Activity.RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            uploadImageToFirebase(selectedImageUri, listener);
        }
    }
    public void handleImagePickerResult( Intent data, OnImageUploadCompleteListener listener) {
            Uri selectedImageUri = data.getData();
            uploadImageToFirebase(selectedImageUri, listener);

    }

    // Function to upload image to Firebase Storage
    private void uploadImageToFirebase(Uri imageUri, final OnImageUploadCompleteListener listener) {
        String currentUserUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String imageName = UUID.randomUUID().toString();

        StorageReference storageReference = FirebaseStorage.getInstance().getReference()
                .child("images")
                .child(currentUserUid)
                .child(imageName);

        storageReference.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<com.google.firebase.storage.UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(com.google.firebase.storage.UploadTask.TaskSnapshot taskSnapshot) {
                        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String imageUrl = uri.toString();
                                listener.onImageUploadComplete(imageUrl);
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        listener.onImageUploadFailed(e.getMessage());
                    }
                });
    }

    // Interface to handle image upload events
    public interface OnImageUploadCompleteListener {
        void onImageUploadComplete(String imageUrl);
        void onImageUploadFailed(String errorMessage);
    }
}
