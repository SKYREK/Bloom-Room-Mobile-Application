package com.example.bloomroom;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bloomroom.Adaptors.OrderAdapter;
import com.example.bloomroom.Models.Order;
import com.example.bloomroom.Models.User;
import com.example.bloomroom.Utils.ImageUploader;
import com.example.bloomroom.Utils.ImageUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AdminAccountFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AdminAccountFragment extends Fragment {

    private ImageUploader imageUploader;
    private String imgLink = "";
    private View rootView; // Declare rootView as a class-level variable

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public AdminAccountFragment() {
        // Required empty public constructor
    }

    public static AccountFragment newInstance(String param1, String param2) {
        AccountFragment fragment = new AccountFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_admin_account, container, false);
        imageUploader = new ImageUploader(); // Initialize the ImageUploader
        ImageButton imageButton = rootView.findViewById(R.id.imageButton);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImage();
            }
        });
        String uid = FirebaseAuth.getInstance().getUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").document(uid).get().addOnSuccessListener(

                documentSnapshot -> {
                    User user = documentSnapshot.toObject(User.class);
                    if (user != null) {
                        ImageUtils.loadImageFromUrl(AdminAccountFragment.this.getActivity(), user.getImageLink(), rootView.findViewById(R.id.imageView));
                        TextView name = rootView.findViewById(R.id.nameField);
                        name.setText(user.getName());
                        TextView email = rootView.findViewById(R.id.emailField);
                        email.setText(
                                FirebaseAuth.getInstance().getCurrentUser().getEmail().toString()
                        );
                    }
                }
        );
        Button logoutButton = rootView.findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent i = new Intent(AdminAccountFragment.this.getActivity(), LoginActivity.class);
                startActivity(i);
                AdminAccountFragment.this.getActivity().finish();
            }
        });
        TextView title = requireActivity().findViewById(R.id.toolbar_title);
        if (title != null) {
            title.setText("Categories");
        }


        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView title = requireActivity().findViewById(R.id.toolbar_title);
        if (title != null) {
            title.setText("My Account");
        }
    }

    private void pickImage() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, 101);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);

        imageUploader.handleImagePickerResult(requestCode, resultCode, data, new ImageUploader.OnImageUploadCompleteListener() {
            @Override
            public void onImageUploadComplete(String imageUrl) {
                imgLink = imageUrl;
                ImageUtils.loadImageFromUrl(AdminAccountFragment.this.getActivity(), imageUrl, rootView.findViewById(R.id.imageView));
                String uid = FirebaseAuth.getInstance().getUid();
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collection("users").document(uid).update("imageLink", imgLink);
                Toast.makeText(AdminAccountFragment.this.getActivity(), "Image uploaded successfully: " + imageUrl, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onImageUploadFailed(String errorMessage) {
                Toast.makeText(AdminAccountFragment.this.getActivity(), "Image upload failed: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }
}