package com.rsf.innometrics;

import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class MainFragment extends Fragment {

    private UsageStatsManager manager;
    private ViewAdapter viewAdapter;
    private RecyclerView recView;
    private Button mOpenUsageSettingButton;

    static MainFragment newInstance() {
        return new MainFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        manager = (UsageStatsManager) Objects.requireNonNull(getActivity())
                .getSystemService("usagestats");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_app_usage_statistics, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View rootView, Bundle savedInstanceState) {
        super.onViewCreated(rootView, savedInstanceState);

        viewAdapter = new ViewAdapter();
        recView = rootView.findViewById(R.id.recyclerview_app_usage);
        recView.scrollToPosition(0);
        recView.setAdapter(viewAdapter);
        mOpenUsageSettingButton = rootView.findViewById(R.id.button_open_usage_setting);

        List<UsageStats> usageStatsList = getUsageStatistics();
        Collections.sort(usageStatsList, new LastTimeLaunchedComparatorDesc());
        updateAppsList(Collections.singletonList(usageStatsList.get(0)));
    }

    private List<UsageStats> getUsageStatistics() {
        long time = System.currentTimeMillis();
        List<UsageStats> appList = manager.queryUsageStats(
                UsageStatsManager.INTERVAL_DAILY, time - 10000 * 10000, time);

        if (appList != null && appList.size() == 0) {
            Log.i("TAG", "The user may not allow the access to apps usage. ");
            Toast.makeText(getActivity(),
                    getString(R.string.explanation_access_to_appusage_is_not_enabled),
                    Toast.LENGTH_LONG).show();
            mOpenUsageSettingButton.setVisibility(View.VISIBLE);
            mOpenUsageSettingButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));
                }
            });
        }
        return appList;
    }

    private void updateAppsList(List<UsageStats> usageStatsList) {
        List<MappingApp> mappingAppList = new ArrayList<>();
        for (int i = 0; i < usageStatsList.size(); i++) {
            MappingApp mappingApp = new MappingApp();
            mappingApp.usageStats = usageStatsList.get(i);
            try {
                mappingApp.appIcon = Objects.requireNonNull(getActivity()).getPackageManager()
                        .getApplicationIcon(mappingApp.usageStats.getPackageName());
            } catch (PackageManager.NameNotFoundException e) {
                Log.w("TAG", String.format("App Icon is not found for %s",
                        mappingApp.usageStats.getPackageName()));
                mappingApp.appIcon = getActivity()
                        .getDrawable(R.drawable.ic_android_black_24dp);
            }
            mappingAppList.add(mappingApp);
        }
        viewAdapter.setCustomUsageStatsList(mappingAppList);
        viewAdapter.notifyDataSetChanged();
        recView.scrollToPosition(0);
    }

    private static class LastTimeLaunchedComparatorDesc implements Comparator<UsageStats> {

        @Override
        public int compare(UsageStats left, UsageStats right) {
            return Long.compare(right.getLastTimeUsed(), left.getLastTimeUsed());
        }
    }

}
