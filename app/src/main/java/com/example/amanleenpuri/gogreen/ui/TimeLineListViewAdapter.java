package com.example.amanleenpuri.gogreen.ui;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.example.amanleenpuri.gogreen.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;

import model.GreenEntry;
import util.ImageHelper;
import util.VolleyAppController;

/**
 * Created by amanleenpuri on 4/3/16.
 */
public class TimeLineListViewAdapter extends ArrayAdapter<GreenEntry> {

    ImageLoader imageLoader = VolleyAppController.getInstance().getImageLoader();

    TimeLineListViewAdapter(Context context, GreenEntry[] list){
        super(context, android.R.layout.simple_list_item_1,list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final GreenEntry ge = getItem(position);
        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.timeline_list_item, parent, false);
        }
        TextView userNameOnTimeLinetv = (TextView)convertView.findViewById(R.id.tv_user_name_on_timeline);
        TextView timeAgoOnTimeLinetv = (TextView)convertView.findViewById(R.id.tv_time_ago_on_timeline);
        final TextView numberOfStarstv = (TextView)convertView.findViewById(R.id.tv_number_of_stars_on_timeline);
        ImageView userProfilePicOnTimeLineiv = (ImageView)convertView.findViewById(R.id.iv_timeline_image);
        ImageView articleImageiv = (ImageView)convertView.findViewById(R.id.iv_article_on_timeline);
        ToggleButton followIcon = (ToggleButton) convertView.findViewById(R.id.become_follower_icon);
        final ToggleButton sharePostIcon = (ToggleButton) convertView.findViewById(R.id.share_post_icon);
        ToggleButton starIcon = (ToggleButton) convertView.findViewById(R.id.star_icon);
        TextView postMessage = (TextView)convertView.findViewById(R.id.tv_postMsg);

        postMessage.setText(ge.getPostMessage());
        if(ge.getPostImageURL()!=null){
            userProfilePicOnTimeLineiv.setImageBitmap(ImageHelper.getStringToBitMap(ge.getPostImageURL()));
        }
        articleImageiv.setImageBitmap(ImageHelper.getStringToBitMap(ge.getPostImageURL()));



        //TODO: ADD CONDITION TO SET START ICON STATE
//        starIcon.setChecked(true);

        userNameOnTimeLinetv.setText(ge.getPostByUserName());
        timeAgoOnTimeLinetv.setText(ge.getDatePosted());
        numberOfStarstv.setText(String.valueOf(ge.getNumOfStars()));

        starIcon.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int count = Integer.valueOf(numberOfStarstv.getText().toString());
                if (isChecked) {
                    // The toggle is enabled
                    count++;
                    numberOfStarstv.setText(String.valueOf(count));
                } else {
                    // The toggle is disabled
                    count--;
                    numberOfStarstv.setText(String.valueOf(count));
                }
            }
        });

        final View finalConvertView = convertView;

        followIcon.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    Toast toast= Toast.makeText(finalConvertView.getContext(),
                            "You are now following "+ge.getPostByUserName(), Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
                    toast.show();
                } else {
                    // The toggle is disabled
                    Toast toast= Toast.makeText(finalConvertView.getContext(),
                            "Unfollowing "+ge.getPostByUserName(), Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
                    toast.show();
                }
            }
        });

        sharePostIcon.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    Toast toast= Toast.makeText(finalConvertView.getContext(),
                            "You Shared "+ge.getPostByUserName()+" Post", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
                    toast.show();
                } else {
                    sharePostIcon.setChecked(true);
                    //TODO: TEST ALL CASES SHADY CODE
                    // The toggle is disabled

                }
            }
        });
        return convertView;
    }
}
