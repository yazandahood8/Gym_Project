package com.example.gym.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gym.R;
import com.example.gym.Data.Coach;

import java.util.List;

public class CoachAdapter extends RecyclerView.Adapter<CoachAdapter.CoachViewHolder> {

    private List<Coach> coachList;
    private Context context;

    public CoachAdapter(List<Coach> coachList) {
        this.coachList = coachList;
    }

    @NonNull
    @Override
    public CoachViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_coach_card, parent, false);
        return new CoachViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CoachViewHolder holder, int position) {
        Coach coach = coachList.get(position);
        holder.coachName.setText(coach.getName());
        holder.coachPhoneNumber.setText(coach.getPhoneNumber());
        holder.coachExperience.setText(String.format("%d years of experience", coach.getYearOfExperience()));
        holder.coachImage.setImageResource(R.drawable.coach_placeholder);  // Placeholder image for now

        // Call button click listener
        holder.buttonCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create intent to open the phone dialer with the coach's phone number
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:" + coach.getPhoneNumber()));
                context.startActivity(callIntent);
            }
        });

        // Message button click listener
        holder.buttonMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create intent to send a message to the coach's phone number
                Intent messageIntent = new Intent(Intent.ACTION_SENDTO);
                messageIntent.setData(Uri.parse("smsto:" + coach.getPhoneNumber()));
                messageIntent.putExtra("sms_body", "Hello Coach " + coach.getName() + ", I would like to...");
                context.startActivity(messageIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return coachList.size();
    }

    public static class CoachViewHolder extends RecyclerView.ViewHolder {
        ImageView coachImage;
        TextView coachName;
        TextView coachPhoneNumber;
        TextView coachExperience;
        Button buttonCall;
        Button buttonMessage;

        public CoachViewHolder(@NonNull View itemView) {
            super(itemView);
            coachImage = itemView.findViewById(R.id.coach_image);
            coachName = itemView.findViewById(R.id.coach_name);
            coachPhoneNumber = itemView.findViewById(R.id.coach_phone_number);
            coachExperience = itemView.findViewById(R.id.coach_experience);
            buttonCall = itemView.findViewById(R.id.button_call);
            buttonMessage = itemView.findViewById(R.id.button_message);
        }
    }
}
