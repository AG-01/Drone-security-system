package com.example.aeroalert;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class UserRequestsAdapter extends RecyclerView.Adapter<UserRequestsAdapter.ViewHolder> {

    private Context context;
    private List<UserRequests> userRequestsList;

    public UserRequestsAdapter(Context context, List<UserRequests> userRequestsList) {
        this.context = context;
        this.userRequestsList = userRequestsList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_user_request, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        UserRequests userRequest = userRequestsList.get(position);
        holder.tvRequestTitle.setText("Tittle - " + userRequest.request_title);
        holder.tvUserName.setText("User - " + userRequest.user_name);
        holder.tvRequestType.setText("Req Type - " + userRequest.request_type);
        holder.tvRequestDetails.setText("Details - " + userRequest.request_details);
        holder.tvRequestDate.setText("Date - " + userRequest.request_date);
    }

    @Override
    public int getItemCount() {
        return userRequestsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvRequestTitle, tvUserName, tvRequestType, tvRequestDetails, tvRequestDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvRequestTitle = itemView.findViewById(R.id.tvRequestTitle);
            tvUserName = itemView.findViewById(R.id.tvUserName);
            tvRequestType = itemView.findViewById(R.id.tvRequestType);
            tvRequestDetails = itemView.findViewById(R.id.tvRequestDetails);
            tvRequestDate = itemView.findViewById(R.id.tvRequestDate);
        }
    }
}
