package com.faheem92mt.findfriends;

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
import android.widget.Toast;

import com.faheem92mt.R;
import com.faheem92mt.common.NodeNames;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;


public class FindFriendsFragment extends Fragment {


    private RecyclerView rvFindFriends;

    private FindFriendAdapter findFriendAdapter;

    // we will add list of users after DB query
    private List<FindFriendModel> findFriendModelList;

    // we will make this as "gone" after getting users (if any)
    private TextView tvEmptyFriendsList;

    private DatabaseReference databaseReference;
    private FirebaseUser currentUser;

    private View progressBar;



    public FindFriendsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_find_friends, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvFindFriends = view.findViewById(R.id.rvFindFriends);
        progressBar = view.findViewById(R.id.progressBar);
        tvEmptyFriendsList = view.findViewById(R.id.tvEmptyFriendsList);

        // on the recycler view, we need to set a layout manager
        // we can use linearLayout or GridLayout
        // we are showing as List, so LL Manager
        rvFindFriends.setLayoutManager(new LinearLayoutManager(getActivity()));

        findFriendModelList = new ArrayList<>();

        findFriendAdapter = new FindFriendAdapter(getActivity(), findFriendModelList);
        rvFindFriends.setAdapter(findFriendAdapter);

        databaseReference = FirebaseDatabase.getInstance().getReference().child(NodeNames.USERS);
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        tvEmptyFriendsList.setVisibility(View.VISIBLE);
//        progressBar.setVisibility(View.VISIBLE);

//        int a = 2;
//
//        int[] b = {1};





        Query query = databaseReference.orderByChild(NodeNames.NAME);



            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {



                    findFriendModelList.clear();

                    for (DataSnapshot ds : snapshot.getChildren()) {

//                        Toast.makeText(getContext(), "Hi!", Toast.LENGTH_SHORT).show();

                        String userId = ds.getKey();

                        if (userId.equals(currentUser.getUid())) {
                            continue;
                        }

                        if (ds.child(NodeNames.NAME).getValue() != null) {
                            String fullName = ds.child(NodeNames.NAME).getValue().toString();
                            String photoName = "images/" + userId + ".jpg";



                            findFriendModelList.add(new FindFriendModel(fullName, photoName, userId, false));
                            findFriendAdapter.notifyDataSetChanged();

                            tvEmptyFriendsList.setVisibility(View.GONE);
                            progressBar.setVisibility(View.GONE);

                        }

                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                    progressBar.setVisibility(View.GONE);
//                    Toast.makeText(getContext(), "Failed!", Toast.LENGTH_SHORT).show();

                }
            });





    }




}