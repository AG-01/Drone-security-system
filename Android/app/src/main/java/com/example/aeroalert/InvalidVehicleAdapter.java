package com.example.aeroalert;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class InvalidVehicleAdapter extends RecyclerView.Adapter<InvalidVehicleAdapter.ItemViewHolder> {

    private List<InvalidVehicle> mItemList;
    private Context mcontext;
    private String user;

    public InvalidVehicleAdapter(@NonNull List<InvalidVehicle> ItemList, Context context, String user) {
        this.mItemList = ItemList;
        this.user = user;
        this.mcontext = context;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.invalid_vehicle_item, parent, false);
        return new ItemViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull final ItemViewHolder holder, int position) {

        final InvalidVehicle request = mItemList.get(position);
        holder.vehicleNo.setText("Vehicle No : " + request.getVehicleNo());
        holder.vehicleDetails.setText("Date : " + request.getCurrentTime().toString());
        if (request.getOrderNumber() != null) {
            holder.extraDetails.setText("Details : " + request.getOrderNumber());
        } else {
            holder.extraDetails.setVisibility(View.GONE);
        }

        holder.itemView.findViewById(R.id.update).setOnClickListener(v -> {
            InvalidVehicle selectedVehicle = mItemList.get(holder.getAdapterPosition());
            selectedVehicle.setIsValid("Yes");

            ProgressDialog progressDialog = new ProgressDialog(mcontext);
            progressDialog.setMessage("Updating...");
            progressDialog.setCancelable(false);
            progressDialog.show();

//            NetworkUtils.updateInvalidVehicle1(selectedVehicle.getVehicleNo(), selectedVehicle, new Callback() {
//                @Override
//                public void onFailure(Call call, IOException e) {
//                    progressDialog.dismiss();
//                    e.printStackTrace();
//                    ((GeneratedFormListActivity) mcontext).runOnUiThread(() ->
//                            Toast.makeText(mcontext, "Error Occurred " + e.toString(), Toast.LENGTH_SHORT).show());
//                }
//
//                @Override
//                public void onResponse(Call call, Response response) throws IOException {
//                    progressDialog.dismiss();
//                    if (response.isSuccessful()) {
//                        String responseBody = response.body().string();
//                        System.out.println("Response: " + responseBody);
//                        ((GeneratedFormListActivity) mcontext).runOnUiThread(() -> {
//                            Toast.makeText(mcontext, "Vehicle approved successfully", Toast.LENGTH_SHORT).show();
//                            // Optionally refresh the data or update the UI
//                        });
//                    } else {
//                        System.err.println("Request failed with code: " + response.code());
//                    }
//                }
//            });
        });

        holder.itemView.findViewById(R.id.reject).setOnClickListener(v -> {
            InvalidVehicle selectedVehicle = mItemList.get(holder.getAdapterPosition());

            ProgressDialog progressDialog = new ProgressDialog(mcontext);
            progressDialog.setMessage("Rejecting...");
            progressDialog.setCancelable(false);
            progressDialog.show();

//            NetworkUtils.deleteInvalidVehicle(selectedVehicle.getId(), new Callback() {
//                @Override
//                public void onFailure(Call call, IOException e) {
//                    progressDialog.dismiss();
//                    e.printStackTrace();
//                    ((GeneratedFormListActivity) mcontext).runOnUiThread(() ->
//                            Toast.makeText(mcontext, "Error Occurred " + e.toString(), Toast.LENGTH_SHORT).show());
//                }
//
//                @Override
//                public void onResponse(Call call, Response response) throws IOException {
//                    progressDialog.dismiss();
//                    if (response.isSuccessful()) {
//                        String responseBody = response.body().string();
//                        System.out.println("Response: " + responseBody);
//                        ((GeneratedFormListActivity) mcontext).runOnUiThread(() -> {
//                            Toast.makeText(mcontext, "Vehicle rejected successfully", Toast.LENGTH_SHORT).show();
//                            mItemList.remove(holder.getAdapterPosition());
//                            notifyItemRemoved(holder.getAdapterPosition());
//                        });
//                    } else {
//                        System.err.println("Request failed with code: " + response.code());
//                    }
//                }
//            });
        });
    }

    @Override
    public int getItemCount() {
        return mItemList.size();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        private TextView vehicleNo, vehicleDetails, extraDetails;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            vehicleNo = itemView.findViewById(R.id.vehicleNo);
            vehicleDetails = itemView.findViewById(R.id.vehicleDetails);
            extraDetails = itemView.findViewById(R.id.extraDetails);
        }
    }
}
