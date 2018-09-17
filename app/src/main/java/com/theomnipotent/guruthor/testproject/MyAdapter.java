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

import static com.theomnipotent.guruthor.testproject.Constants.SHORT_SKIP;
import static com.theomnipotent.guruthor.testproject.Constants.VARIABLE_SKIP_PERCENT;

public class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<BaseBean> mDataset;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder

    private static class MyTextViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        private TextView mTextView;

        private MyTextViewHolder(View v) {
            super(v);
            mTextView = v.findViewById(R.id.contact_name);
        }
    }


    public static class MyImageViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        ImageView mImageView;

        MyImageViewHolder(View v) {
            super(v);
            mImageView = v.findViewById(R.id.image);
        }
    }


    public static class MyVideoViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        VideoView mVideoView;

        MyVideoViewHolder(View v) {
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
        public ImageButton shortSkipBackward;
        public ImageButton shortSkipForward;
        public ImageButton variableSkipBackward;
        public ImageButton variableSkipForward;

        MyAudioViewHolder(View v) {
            super(v);
            mSeekBar = v.findViewById(R.id.scrubber);
            playButton = v.findViewById(R.id.playButton);
            pauseButton = v.findViewById(R.id.pauseButton);
            shortSkipBackward = v.findViewById(R.id.shortSkipBackward);
            shortSkipForward = v.findViewById(R.id.shortSkipForward);
            variableSkipBackward = v.findViewById(R.id.variableSkipBackward);
            variableSkipForward = v.findViewById(R.id.variableSkipForward);

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

            variableSkipBackward.setOnClickListener(new ImageButton.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mediaPlayer.seekTo(mediaPlayer.getCurrentPosition() - (mediaPlayer.getDuration()*VARIABLE_SKIP_PERCENT)/100);
                }
            });

            variableSkipForward.setOnClickListener(new ImageButton.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mediaPlayer.seekTo(mediaPlayer.getCurrentPosition() + (mediaPlayer.getDuration()*VARIABLE_SKIP_PERCENT)/100);
                }
            });

            shortSkipBackward.setOnClickListener(new ImageButton.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mediaPlayer.seekTo(mediaPlayer.getCurrentPosition() - SHORT_SKIP);
                }
            });

            shortSkipForward.setOnClickListener(new ImageButton.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mediaPlayer.seekTo(mediaPlayer.getCurrentPosition() + SHORT_SKIP);
                }
            });
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    MyAdapter(ArrayList<BaseBean> myDataset) {
        mDataset = myDataset;
    }

    @Override
    public int getItemViewType(int position) {
        return mDataset.get(position).getType();
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                      int viewType) {
        // create a new view
        switch (viewType) {
            case Constants.TYPE_AUDIO: {
                View v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.my_audio_view, parent, false);
                return new MyAudioViewHolder(v);
            }
            case Constants.TYPE_VIDEO: {
                View v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.my_video_view, parent, false);
                return new MyVideoViewHolder(v);
            }
            case Constants.TYPE_IMAGE: {
                View v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.my_image_view, parent, false);
                return new MyImageViewHolder(v);
            }
            default: {
                View v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.my_text_view, parent, false);
                return new MyTextViewHolder(v);
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
                filePath = ((AudioBean) mDataset.get(position)).getFilePath();
                uri = Uri.parse("android.resource://" + MainActivity.PACKAGE_NAME + "/" + filePath);
                audioViewHolder.mediaPlayer = MediaPlayer.create(MainActivity.context, uri);
                audioViewHolder.mSeekBar.setMax(audioViewHolder.mediaPlayer.getDuration());
                runTimer(audioViewHolder);
                break;
            case Constants.TYPE_VIDEO:
                MyVideoViewHolder videoViewHolder = (MyVideoViewHolder) holder;
                filePath = ((VideoBean) mDataset.get(position)).getFilePath();
                uri = Uri.parse("android.resource://" + MainActivity.PACKAGE_NAME + "/" + filePath);
                videoViewHolder.mVideoView.setVideoURI(uri);
                break;
            case Constants.TYPE_IMAGE:
                MyImageViewHolder imageViewHolder = (MyImageViewHolder) holder;
                filePath = ((ImageBean) mDataset.get(position)).getFilePath();
                uri = Uri.parse("android.resource://" + MainActivity.PACKAGE_NAME + "/" + filePath);
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

    private void runTimer(final MyAudioViewHolder audioViewHolder1) {
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
