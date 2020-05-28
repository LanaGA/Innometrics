package com.rsf.innometrics;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ViewAdapter extends RecyclerView.Adapter<ViewAdapter.ViewHolder> {

    private List<MappingApp> mMappingAppList = new ArrayList<>();
    @SuppressLint("SimpleDateFormat")
    private DateFormat mDateFormat = new SimpleDateFormat();

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView mPackageName;
        private final TextView mLastTimeUsed;
        private final ImageView mAppIcon;

        ViewHolder(View v) {
            super(v);
            mPackageName = v.findViewById(R.id.textview_package_name);
            mLastTimeUsed = v.findViewById(R.id.textview_last_time_used);
            mAppIcon = v.findViewById(R.id.app_icon);
        }

        TextView getLastTimeUsed() {
            return mLastTimeUsed;
        }

        TextView getPackageName() {
            return mPackageName;
        }

        ImageView getAppIcon() {
            return mAppIcon;
        }
    }

    ViewAdapter() {
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.usage_row, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        viewHolder.getPackageName().setText(
                mMappingAppList.get(position).usageStats.getPackageName());
        long lastTimeUsed = mMappingAppList.get(position).usageStats.getLastTimeUsed();
        viewHolder.getLastTimeUsed().setText(mDateFormat.format(new Date(lastTimeUsed)));
        viewHolder.getAppIcon().setImageDrawable(mMappingAppList.get(position).appIcon);
    }

    @Override
    public int getItemCount() {
        return mMappingAppList.size();
    }

    void setCustomUsageStatsList(List<MappingApp> customUsageStats) {
        mMappingAppList = customUsageStats;
    }
}