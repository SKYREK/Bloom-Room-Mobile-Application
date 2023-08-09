package com.example.bloomroom;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bloomroom.Adaptors.AdminFlowerAdapter;
import com.example.bloomroom.Adaptors.FlowerAdapter;
import com.example.bloomroom.Models.Flower;
import com.example.bloomroom.R;
import com.example.bloomroom.databinding.FragmentHomeBinding;

import java.util.List;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

//    public View onCreateView(@NonNull LayoutInflater inflater,
//                             ViewGroup container, Bundle savedInstanceState) {
//
//
//        binding = FragmentHomeBinding.inflate(inflater, container, false);
//        View root = binding.getRoot();
//        return root;
//    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Find the TextView from the activity layout
        TextView title = requireActivity().findViewById(R.id.toolbar_title);
        if (title != null) {
            title.setText("BLOOMROOM");
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        RecyclerView flowerRecyclerView = rootView.findViewById(R.id.flower_list);

        Flower.getAllFlowers(new Flower.FlowerCallback() {
            @Override
            public void onFlowersLoaded(List<Flower> flowerList) {
                flowerRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
                FlowerAdapter categoryAdapter = new FlowerAdapter(flowerList, getContext());
                flowerRecyclerView.setAdapter(categoryAdapter);
            }

            @Override
            public void onFailure(String errorMessage) {
                // Handle the failure case if needed
            }
        });

        return rootView;
    }





    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}