package com.carla.vcash.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.carla.models.SimpleUser;
import com.carla.vcash.R;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder>
{
    private List<SimpleUser> userList;
    private static ItemClickListener mClickListener;
    private Context context;
    private View lastSelectedView;

    public UserAdapter(List<SimpleUser> userList, ItemClickListener mClickListener, Context context) {
        this.userList = userList;
        this.mClickListener = mClickListener;
        this.context = context;
        lastSelectedView = null;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull UserAdapter.ViewHolder holder, int position) {
        holder.firstName.setText(userList.get(position).getFirstname());
        holder.lastName.setText(userList.get(position).getLastname());
        Glide.with(context)
                .load(userList.get(position).getImageLink())
                .circleCrop()
                .into(holder.profilePicture);
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        private ImageView profilePicture;
        public ImageView selectIcon;
        private TextView firstName;
        private TextView lastName;
        private View view;

        public ViewHolder(View view) {
            super(view);
            this.view = view;
            profilePicture = (ImageView) view.findViewById(R.id.userCardProfile);
            firstName = (TextView) view.findViewById(R.id.userCardLastName);
            lastName = (TextView) view.findViewById(R.id.userCardFirstName);
            selectIcon = (ImageView) view.findViewById(R.id.selectCheck);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mClickListener.onItemClick(view, getAdapterPosition());
        }
    }
}
