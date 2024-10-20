package com.example.gym.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.gym.Data.Coach;
import com.example.gym.R;

import java.util.List;

public class CoachAdapter extends BaseAdapter {

    private List<Coach> coachList;
    private LayoutInflater inflater;
    private Context context;

    public CoachAdapter(Context context, List<Coach> coachList) {
        this.context = context;
        this.coachList = coachList;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return coachList.size();
    }

    @Override
    public Object getItem(int position) {
        return coachList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_coach_card, parent, false);
            holder = new ViewHolder();
            holder.coachImage = convertView.findViewById(R.id.coach_image);
            holder.coachName = convertView.findViewById(R.id.coach_name);
            holder.coachPhoneNumber = convertView.findViewById(R.id.coach_phone_number);
            holder.coachExperience = convertView.findViewById(R.id.coach_experience);
            holder.buttonCall = convertView.findViewById(R.id.button_call);
            holder.buttonMessage = convertView.findViewById(R.id.button_message);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Coach coach = coachList.get(position);
        holder.coachName.setText(coach.getName());
        holder.coachPhoneNumber.setText(coach.getPhoneNumber());
        holder.coachExperience.setText(String.format("%d years of experience", coach.getYearOfExperience()));

        // Use Glide to load the image into the ImageView
        if (coach.getImage() != null && !coach.getImage().isEmpty()) {
            Glide.with(context)
                    .load(coach.getImage()) // URL or file path
                    .placeholder(R.drawable.coach_placeholder) // Placeholder image
                    .error(R.drawable.coach_placeholder) // Error image if load fails
                    .into(holder.coachImage);
        } else {
            // If no image URL/path is provided, set the placeholder
            holder.coachImage.setImageResource(R.drawable.coach_placeholder);
        }

        // Call button click listener
        holder.buttonCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:" + coach.getPhoneNumber()));
                context.startActivity(callIntent);
            }
        });

        // Message button click listener
        holder.buttonMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent messageIntent = new Intent(Intent.ACTION_SENDTO);
                messageIntent.setData(Uri.parse("smsto:" + coach.getPhoneNumber()));
                messageIntent.putExtra("sms_body", "Hello Coach " + coach.getName() + ", I would like to...");
                context.startActivity(messageIntent);
            }
        });

        return convertView;
    }

    // ViewHolder pattern for better performance
    private static class ViewHolder {
        ImageView coachImage;
        TextView coachName;
        TextView coachPhoneNumber;
        TextView coachExperience;
        Button buttonCall;
        Button buttonMessage;
    }
}
