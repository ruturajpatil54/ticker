package com.rrd.ticker.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.rrd.ticker.R;
import com.rrd.ticker.notifier.NotifierService;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    final static String logTag = HomeFragment.class.getCanonicalName();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        Button registerAlarmButton = root.findViewById(R.id.registerAlarm);
        registerAlarmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // schedule alarms
                try {
                    NotifierService notifierService = new NotifierService();
                    notifierService.scheduleAlarms(10, getActivity().getApplicationContext());
                } catch(Exception e) {
                    Log.d(logTag, e.getLocalizedMessage());
                    e.printStackTrace();
                }
            }
        });

        return root;
    }
}