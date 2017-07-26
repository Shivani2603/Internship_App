package com.example.dell.internshipapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.example.dell.internshipapp.extras.Keys;
import com.example.dell.internshipapp.pojo.Profile;
import com.example.dell.internshipapp.R;
import com.example.dell.internshipapp.Network.VolleySingleton;

import java.util.ArrayList;

import static com.example.dell.internshipapp.anims.AnimationUtils.animateSunblind;

/**
 * Created by DELL on 26-07-2017.
 */

public class AdapterProfile extends RecyclerView.Adapter<AdapterProfile.ViewHolderProfile> {
    //contains the list of movies
    private ArrayList<Profile> profileArrayList = new ArrayList<Profile>();
    private LayoutInflater mInflater;
    private VolleySingleton mVolleySingleton;
    private ImageLoader mImageLoader;
    //keep track of the previous position for animations where scrolling down requires a different animation compared to scrolling up
    private int mPreviousPosition = 0;


    public AdapterProfile(Context context) {
        mInflater = LayoutInflater.from(context);
        mVolleySingleton = VolleySingleton.getsInstance();
        mImageLoader = mVolleySingleton.getImageLoader();
    }

    public void setProfileArrayList(ArrayList<Profile> listProfile) {
        this.profileArrayList = listProfile;
        //update the adapter to reflect the new set of movies
        notifyDataSetChanged();
    }

    @Override
    public ViewHolderProfile onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.custom_profile_row, parent, false);
        ViewHolderProfile viewHolder = new ViewHolderProfile(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolderProfile holder, int position) {
        Profile currentProfile = profileArrayList.get(position);
        //one or more fields of the Movie object may be null since they are fetched from the web
        holder.name.setText(currentProfile.getName());
        holder.userId.setText(currentProfile.getId());
        holder.rank.setText(currentProfile.getRank());




        if (position > mPreviousPosition) {
          animateSunblind(holder, true);
//            AnimationUtils.animateSunblind(holder, true);
//            AnimationUtils.animate1(holder, true);
//            AnimationUtils.animate(holder,true);
        } else {
            animateSunblind(holder, false);
//            AnimationUtils.animateSunblind(holder, false);
//            AnimationUtils.animate1(holder, false);
//            AnimationUtils.animate(holder, false);
        }
        mPreviousPosition = position;

        String image = currentProfile.getProfilePic();
        loadImages(image, holder);

    }


    private void loadImages(String urlThumbnail, final ViewHolderProfile holder) {
        if (!urlThumbnail.equals(Keys.Constants.NA)) {
            mImageLoader.get(urlThumbnail, new ImageLoader.ImageListener() {
                @Override
                public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                    holder.image.setImageBitmap(response.getBitmap());
                }

                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return (profileArrayList == null) ? 0 : profileArrayList.size();
        //return profileArrayList.size();
    }







    static class ViewHolderProfile extends RecyclerView.ViewHolder {

        ImageView image;
        TextView name;
        TextView userId;
        TextView rank;


        public ViewHolderProfile(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.profileImage);
            name = (TextView) itemView.findViewById(R.id.name);
            userId= (TextView) itemView.findViewById(R.id.userId);
           rank= (TextView) itemView.findViewById(R.id.rank);

        }
    }
    }
