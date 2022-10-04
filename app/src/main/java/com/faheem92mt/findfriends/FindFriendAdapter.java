package com.faheem92mt.findfriends;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.faheem92mt.R;
import com.faheem92mt.common.Constants;
import com.faheem92mt.common.NodeNames;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

// we are creating an adapter class
// to which we pass an object of "View Holder" class inside the <>
// Aaaanddd we create the View Holder class inside this class

// basically an adapter for a recycler view
// thus extends from RecyclerView.Adapter
public class FindFriendAdapter extends RecyclerView.Adapter<FindFriendAdapter.FindFriendViewHolder> {

    private Context context;
    // an array which will hold the deets of all users
    private List<FindFriendModel> findFriendModelList;

    // object of database reference
    private DatabaseReference friendRequestDatabase;
    private FirebaseUser currentUser;

    // userId of the user to whom request is sent
    private String userId;

    // we will create constructor for this "Adapter Class"
    //cuz we want values like "context" and "list of users" from the fragment

    // constructor
    public FindFriendAdapter(Context context, List<FindFriendModel> findFriendModelList) {

        this.context = context;
        // we will send this list from our fragment
        // On FindFriendsFragment, we're gonna query the DB, we will get all user deets,
        // and we will send the array of all deets to this Adapter Class
        this.findFriendModelList = findFriendModelList;
    }

    //method 1 - onCreateViewHolder
    @NonNull
    @Override
    public FindFriendAdapter.FindFriendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        // the variable "view" holds the value of our xml file
        View view = LayoutInflater.from(context).inflate(R.layout.find_friends_layout, parent, false);

        // which gets returned as a ViewHolder
        return new FindFriendViewHolder(view);
    }

    // 2 paremeters - 1 for the reference to the xml and 2 for which user in question i.e.
    // position of the user in the array
    @Override
    public void onBindViewHolder(@NonNull final FindFriendAdapter.FindFriendViewHolder holder, int position) {

        // this is the particular user in question
        final FindFriendModel friendModel = findFriendModelList.get(position);

        // NAME
        // the userName is set for this particular user
        holder.tvFullName.setText(friendModel.getUserName());

        // PROFILE PICTURE
        // manish method ,,, not Master Kolhe's method
        FirebaseStorage storage = FirebaseStorage.getInstance();

        // fileRef refers to the photo id of user
        StorageReference storageRef = storage.getInstance().getReferenceFromUrl("gs://chat-app-7d78e.appspot.com");
        // we use that photo id to access profile picture

        StorageReference mountainsRef = storageRef.child(friendModel.getPhotoName());



        mountainsRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Log.d("TAG",String.valueOf(uri));
                Glide.with(context)
                .load(uri)
                .placeholder(R.drawable.default_profile)
                .error(R.drawable.default_profile)
                .into(holder.ivProfile2);
            }
        });
        // END OF PROFILE PICTURE SETUP OF EACH USER

        // FRIEND REQUEST
        friendRequestDatabase = FirebaseDatabase.getInstance().getReference().child(NodeNames.FRIEND_REQUESTS);
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        // to check if friend request is sent
        if (friendModel.isRequestSent()) {
            holder.btnSendRequest.setVisibility(View.GONE);
            holder.btnCancelRequest.setVisibility(View.VISIBLE);
        }
        else {
            holder.btnSendRequest.setVisibility(View.VISIBLE);
            holder.btnCancelRequest.setVisibility(View.GONE);
        }
        //
        holder.btnSendRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.btnSendRequest.setVisibility(View.GONE);
                holder.pbRequest.setVisibility(View.VISIBLE);

                // we need userID of the user to whom the friend request is sent
                userId = friendModel.getUserID();

                //setting the  "friend request status" as "sent" for the user to whom friend request is sent
                friendRequestDatabase.child(currentUser.getUid()).child(userId).child(NodeNames.REQUEST_TYPE)
                        .setValue(Constants.REQUEST_STATUS_SENT).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()) {
                            friendRequestDatabase.child(userId).child(currentUser.getUid()).child(NodeNames.REQUEST_TYPE)
                                    .setValue(Constants.REQUEST_STATUS_RECEIVED).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if (task.isSuccessful()) {
                                        Toast.makeText(context, "Alhamdulillah! Request Sent!", Toast.LENGTH_SHORT).show();
                                        holder.btnSendRequest.setVisibility(View.GONE);
                                        holder.pbRequest.setVisibility(View.GONE);
                                        holder.btnCancelRequest.setVisibility(View.VISIBLE);
                                    }
                                    else {
                                        Toast.makeText(context, "Failed to send friend request : %1$s", Toast.LENGTH_SHORT).show();
                                        holder.btnSendRequest.setVisibility(View.VISIBLE);
                                        holder.pbRequest.setVisibility(View.GONE);
                                        holder.btnCancelRequest.setVisibility(View.GONE);
                                    }
                                }
                            });
                        }
                        else {
                            Toast.makeText(context, "Failed to send friend request : %1$s", Toast.LENGTH_SHORT).show();
                            holder.btnSendRequest.setVisibility(View.VISIBLE);
                            holder.pbRequest.setVisibility(View.GONE);
                            holder.btnCancelRequest.setVisibility(View.GONE);
                        }


                    }
                });
            }
        });

    }

    @Override
    public int getItemCount() {
        // size of array
        return findFriendModelList.size();
    }

    // we use this adapter on our FindFriendFragment to fetch the list
    // and show it on the recycler view

    // this is the "View Holder" class
    public class FindFriendViewHolder extends RecyclerView.ViewHolder {

        private ImageView ivProfile2;
        private TextView tvFullName;
        private Button btnSendRequest, btnCancelRequest;
        private ProgressBar pbRequest;

        public FindFriendViewHolder(@NonNull View itemView) {
            super(itemView);

            ivProfile2 = itemView.findViewById(R.id.ivProfile2);
            tvFullName = itemView.findViewById(R.id.tvFullName);
            btnSendRequest = itemView.findViewById(R.id.btnSendRequest);
            btnCancelRequest = itemView.findViewById(R.id.btnCancelRequest);
            pbRequest = itemView.findViewById(R.id.pbRequest);
        }
    }
    // end of "View Holder" Class

}
