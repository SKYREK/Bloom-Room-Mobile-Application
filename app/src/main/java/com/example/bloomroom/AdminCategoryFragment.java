package com.example.bloomroom;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bloomroom.Adaptors.AdminCategoryAdapter;
import com.example.bloomroom.Models.Category;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AdminCategoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AdminCategoryFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AdminCategoryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AdminCategoryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AdminCategoryFragment newInstance(String param1, String param2) {
        AdminCategoryFragment fragment = new AdminCategoryFragment();
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
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_admin_category, container, false);
        RecyclerView categoryRecyclerView = rootView.findViewById(R.id.admin_category_list);

        Category.getAllCategories(new Category.CategoryCallback() {
            @Override
            public void onCategoriesLoaded(List<Category> categoryList) {
                categoryRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                AdminCategoryAdapter categoryAdapter = new AdminCategoryAdapter(categoryList, getContext());
                categoryRecyclerView.setAdapter(categoryAdapter);
            }

            @Override
            public void onFailure(String errorMessage) {
                // Handle the failure case if needed
            }
        });

        return rootView;
    }

}