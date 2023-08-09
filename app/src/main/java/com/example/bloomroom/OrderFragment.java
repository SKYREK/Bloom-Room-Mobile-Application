package com.example.bloomroom;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.bloomroom.Adaptors.CategoryAdapter;
import com.example.bloomroom.Adaptors.OrderAdapter;
import com.example.bloomroom.Models.Category;
import com.example.bloomroom.Models.Order;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OrderFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OrderFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public OrderFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OrderFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OrderFragment newInstance(String param1, String param2) {
        OrderFragment fragment = new OrderFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_order, container, false);
        RecyclerView orderRecycleView = rootView.findViewById(R.id.customer_order_view);

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Order.getOrdersByUserId(uid,new Order.OrderCallback() {
            @Override
            public void onOrdersLoaded(List<Order> orderList) {
                orderRecycleView.setLayoutManager(new LinearLayoutManager(getContext()));
                OrderAdapter orderAdapter = new OrderAdapter(orderList, getContext());
                orderRecycleView.setAdapter(orderAdapter);
            }

            @Override
            public void onFailure(String errorMessage) {
                // Handle the failure case if needed
            }
        });
        TextView title = requireActivity().findViewById(R.id.toolbar_title);
        if (title != null) {
            title.setText("My Orders");
        }
        return rootView;
    }

}