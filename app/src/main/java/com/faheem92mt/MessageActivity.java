package com.faheem92mt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.faheem92mt.common.Util;

public class MessageActivity extends AppCompatActivity {

    private TextView tvMessage;
    private ProgressBar pbMessage;

    private ConnectivityManager.NetworkCallback networkCallback;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        tvMessage = findViewById(R.id.tvMessage2);
        pbMessage = findViewById(R.id.pbMessage);

        // if android version is greater than or same as lollipop
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            networkCallback = new ConnectivityManager.NetworkCallback() {

                @Override
                public void onAvailable(@NonNull Network network) {
                    super.onAvailable(network);
                    finish();
                }

                @Override
                public void onLost(@NonNull Network network) {
                    super.onLost(network);
                    tvMessage.setText("Sorry! No internet available.");
                }

            };

            ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);

            connectivityManager.registerNetworkCallback(new NetworkRequest.Builder()
                    .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                    .build(),networkCallback);
        }


    }

    public void btnRetryClick(View view) {
        pbMessage.setVisibility(View.VISIBLE);

        if (Util.connectionAvailable(this)) {
            finish();
        }
        else {
            new android.os.Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    pbMessage.setVisibility(View.GONE);
                }
            }, 1000);
        }

    }

    public void btnCloseClick(View view) {
        finishAffinity();
    }


}