package com.example.juanbrusco.botonerasalto;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import static com.example.juanbrusco.botonerasalto.R.id.parent;

/**
 * Created by juan.brusco on 22-May-17.
 */

public class CustomAdapter extends ArrayAdapter<DataModel> implements View.OnClickListener {

    private ArrayList<DataModel> dataSet;
    Context mContext;

    // View lookup cache
    private static class ViewHolder {
        TextView txtName;
        TextView txtDescription;
        TextView txtFIle;
        ImageView play;
        ImageView share;
    }

    public CustomAdapter(ArrayList<DataModel> data, Context context) {
        super(context, R.layout.row_item, data);
        this.dataSet = data;
        this.mContext = context;

    }

    @Override
    public void onClick(View v) {

        int position = (Integer) v.getTag();
        Object object = getItem(position);
        DataModel dataModel = (DataModel) object;

        switch (v.getId()) {
            case R.id.item_play:
                String fileName = dataModel.getFile().toString().toLowerCase();
                int resID = mContext.getResources().getIdentifier(fileName, "raw", mContext.getPackageName());
                MediaPlayer mediaPlayer = MediaPlayer.create(mContext, resID);
                mediaPlayer.start();
                break;
            case R.id.item_share:
//                String sharePath = Environment.getExternalStorageDirectory().getPath()
//                        + "/Soundboard/Ringtones/custom_ringtone.ogg";
                Uri uri = Uri.parse(
                        "android.resource://" + mContext.getPackageName() + "/raw/" + dataModel.getFile().toString().toLowerCase()
                );
                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("audio/*");
                share.putExtra(Intent.EXTRA_STREAM, uri);
                mContext.startActivity(Intent.createChooser(share, "Share Sound File"));
                break;
        }
    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        DataModel dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.row_item, parent, false);
            viewHolder.txtName = (TextView) convertView.findViewById(R.id.name);
            viewHolder.txtDescription = (TextView) convertView.findViewById(R.id.description);
            viewHolder.txtFIle = (TextView) convertView.findViewById(R.id.file);
            viewHolder.play = (ImageView) convertView.findViewById(R.id.item_play);
            viewHolder.share = (ImageView) convertView.findViewById(R.id.item_share);


            result = convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }

        Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        result.startAnimation(animation);
        lastPosition = position;

        viewHolder.txtName.setText(dataModel.getName());
        viewHolder.txtDescription.setText(dataModel.getDescription());
        viewHolder.txtFIle.setText(dataModel.getFile());
        viewHolder.play.setOnClickListener(this);
        viewHolder.play.setTag(position);
        viewHolder.share.setOnClickListener(this);
        viewHolder.share.setTag(position);
        // Return the completed view to render on screen
        return convertView;
    }
}
