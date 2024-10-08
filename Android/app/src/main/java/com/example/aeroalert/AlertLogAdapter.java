package com.example.aeroalert;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AlertLogAdapter extends RecyclerView.Adapter<AlertLogAdapter.AlertLogViewHolder> {

    private List<AlertLog> alertLogList;
    private Context context;

    public AlertLogAdapter(List<AlertLog> alertLogList, Context context) {
        this.alertLogList = alertLogList;
        this.context = context;
    }

    @NonNull
    @Override
    public AlertLogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.alert_log, parent, false);
        return new AlertLogViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AlertLogViewHolder holder, int position) {
        AlertLog alertLog = alertLogList.get(position);

        holder.textViewType.setText("Type: " + alertLog.getType());
        holder.textViewTimestamp.setText("Timestamp: " + alertLog.getTimestamp().toString());
        holder.textViewDescription.setText("Description: " + alertLog.getDescription());
        holder.textViewAddress.setText("Address: " + alertLog.getAddress());
        holder.textViewLatitude.setText("Latitude: " + alertLog.getLatitude());
        holder.textViewLongitude.setText("Longitude: " + alertLog.getLongitude());
        holder.textViewCamType.setText("Cam Type: " + alertLog.getCamType());

        // Set the appropriate icon for the alert type
//        switch (alertLog.getType()) {
//            case "Fire":
//                holder.imageViewType.setImageResource(R.drawable.ic_fire);
//                break;
//            case "Smoke":
//                holder.imageViewType.setImageResource(R.drawable.ic_smoke);
//                break;
//            case "Weapons":
//                holder.imageViewType.setImageResource(R.drawable.ic_weapons);
//                break;
//            case "Fighting":
//                holder.imageViewType.setImageResource(R.drawable.ic_fighting);
//                break;
//            default:
//                holder.imageViewType.setImageResource(R.drawable.ic_alert);
//                break;
//        }
    }

    @Override
    public int getItemCount() {
        return alertLogList.size();
    }

    public static class AlertLogViewHolder extends RecyclerView.ViewHolder {
        TextView textViewType, textViewTimestamp, textViewDescription, textViewAddress, textViewLatitude, textViewLongitude, textViewCamType;
//        ImageView imageViewType;

        public AlertLogViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewType = itemView.findViewById(R.id.textViewType);
            textViewTimestamp = itemView.findViewById(R.id.textViewTimestamp);
            textViewDescription = itemView.findViewById(R.id.textViewDescription);
            textViewAddress = itemView.findViewById(R.id.textViewAddress);
            textViewLatitude = itemView.findViewById(R.id.textViewLatitude);
            textViewLongitude = itemView.findViewById(R.id.textViewLongitude);
            textViewCamType = itemView.findViewById(R.id.textViewCamType);
//            imageViewType = itemView.findViewById(R.id.imageViewType);
        }
    }
}