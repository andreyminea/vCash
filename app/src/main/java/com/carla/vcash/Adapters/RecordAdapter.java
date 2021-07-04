package com.carla.vcash.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.carla.managers.SharedPrefsSingleton;
import com.carla.models.Record;
import com.carla.vcash.R;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class RecordAdapter  extends RecyclerView.Adapter<RecordAdapter.ViewHolder>
{
    private List<Record> records;
    private Context context;

    public RecordAdapter(List<Record> recordList, Context context)
    {
        this.records = recordList;
        this.context = context;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.record_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull RecordAdapter.ViewHolder holder, int position) {
        Record tempRecord = records.get(position);
        Glide.with(context)
                .load(SharedPrefsSingleton.getUser(context).getImageLink())
                .circleCrop()
                .into(holder.senderImg);
        holder.operationAmount.setText(String.valueOf(tempRecord.getAmount()));

        switch (tempRecord.getTypeOfOperation())
        {
            case TOP_UP:
                Glide.with(context)
                        .load(R.drawable.ic_baseline_credit_card_24)
                        .circleCrop()
                        .into(holder.receiverImg);
                holder.operationType.setText("TOP UP");
                break;
            case WITHDRAW:
                Glide.with(context)
                        .load(R.drawable.ic_round_local_atm_24)
                        .circleCrop()
                        .into(holder.receiverImg);
                holder.operationType.setText("WITHDRAW");
                break;
            case SEND:
                Glide.with(context)
                        .load(tempRecord.getReceiver().getImageLink())
                        .circleCrop()
                        .into(holder.receiverImg);
                holder.operationType.setText("SENT");
                break;
            case RECEIVE:
                Glide.with(context)
                        .load(tempRecord.getSender().getImageLink())
                        .circleCrop()
                        .into(holder.receiverImg);
                holder.operationType.setText("RECEIVED");
                break;
        }
    }

    @Override
    public int getItemCount() {
        return records.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        private ImageView senderImg;
        private ImageView receiverImg;
        private TextView operationType;
        private TextView operationAmount;

        public ViewHolder(@NonNull @NotNull View view) {
            super(view);
            senderImg = (ImageView) view.findViewById(R.id.senderImg);
            receiverImg = (ImageView) view.findViewById(R.id.receiverImg);
            operationType = (TextView) view.findViewById(R.id.operationType);
            operationAmount = (TextView) view.findViewById(R.id.operationSum);
        }
    }
}
