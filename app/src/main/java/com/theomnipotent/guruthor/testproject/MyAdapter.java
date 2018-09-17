package com.theomnipotent.guruthor.testproject;

import android.media.MediaPlayer;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<BaseBean> mDataset;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder

    public static class MyTextViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mTextView;

        public MyTextViewHolder(View v) {
            super(v);
            mTextView = v.findViewById(R.id.contact_name);
        }
    }


    public static class MyImageViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public ImageView mImageView;

        public MyImageViewHolder(View v) {
            super(v);
            mImageView = v.findViewById(R.id.image);
        }
    }


    public static class MyVideoViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public VideoView mVideoView;

        public MyVideoViewHolder(View v) {
            super(v);
            mVideoView = v.findViewById(R.id.video_view);
        }
    }


    public static class MyAudioViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        MediaPlayer mediaPlayer;
        public SeekBar mSeekBar;
        public ImageButton playButton;
        public ImageButton pauseButton;

        public MyAudioViewHolder(View v) {
            super(v);
            mSeekBar = v.findViewById(R.id.scrubber);
            playButton = v.findViewById(R.id.playButton);
            pauseButton = v.findViewById(R.id.pauseButton);

            playButton.setOnClickListener(new ImageButton.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mediaPlayer.start();
                }
            });

            pauseButton.setOnClickListener(new ImageButton.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mediaPlayer.pause();
                }
            });
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapter(ArrayList<BaseBean> myDataset) {
        mDataset = myDataset;
    }

    @Override
    public int getItemViewType(int position) {
        return mDataset.get(position).getType();
    }

    // Create new views (invoked by the layout manager)
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {
        // create a new view
        switch (viewType) {
            case Constants.TYPE_AUDIO: {
                View v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.my_audio_view, parent, false);
                MyAudioViewHolder vh = new MyAudioViewHolder(v);
                return vh;
            }
            case Constants.TYPE_VIDEO: {
                View v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.my_video_view, parent, false);
                MyVideoViewHolder vh = new MyVideoViewHolder(v);
                return vh;
            }
            case Constants.TYPE_IMAGE: {
                View v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.my_image_view, parent, false);
                MyImageViewHolder vh = new MyImageViewHolder(v);
                return vh;
            }
            default: {
                View v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.my_text_view, parent, false);
                MyTextViewHolder vh = new MyTextViewHolder(v);
                return vh;
            }
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        Uri uri;
        String filePath;
        switch (holder.getItemViewType()) {
            case Constants.TYPE_AUDIO:
                MyAudioViewHolder audioViewHolder = (MyAudioViewHolder) holder;
//                filePath = ((AudioBean) mDataset.get(position)).getFilePath();
                uri = Uri.parse("android.resource://" + MainActivity.PACKAGE_NAME + "/" + R.raw.audio_file);
                audioViewHolder.mediaPlayer = MediaPlayer.create(MainActivity.context, uri);
                audioViewHolder.mSeekBar.setMax(audioViewHolder.mediaPlayer.getDuration());
                runTimer(audioViewHolder);
                break;
            case Constants.TYPE_VIDEO:
                MyVideoViewHolder videoViewHolder = (MyVideoViewHolder) holder;
                filePath = ((VideoBean) mDataset.get(position)).getFilePath();
                uri = Uri.parse("android.resource://" + MainActivity.PACKAGE_NAME + "/" + R.raw.video_file);
                videoViewHolder.mVideoView.setVideoURI(uri);
                break;
            case Constants.TYPE_IMAGE:
                MyImageViewHolder imageViewHolder = (MyImageViewHolder) holder;
                filePath = ((ImageBean) mDataset.get(position)).getFilePath();
                uri = Uri.parse("android.resource://" + MainActivity.PACKAGE_NAME + "/" + R.drawable.ic_menu_gallery);
                imageViewHolder.mImageView.setImageURI(uri);
                break;
            default:
                MyTextViewHolder textViewHolder = (MyTextViewHolder) holder;
                String text = ((TextBean) mDataset.get(position)).getText();
                textViewHolder.mTextView.setText(text);
                break;
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public void runTimer(final MyAudioViewHolder audioViewHolder1) {
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                audioViewHolder1.mSeekBar.setProgress(audioViewHolder1.mediaPlayer.getCurrentPosition());
            }
        }, 0, 100);
        audioViewHolder1.mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    audioViewHolder1.mediaPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
}
