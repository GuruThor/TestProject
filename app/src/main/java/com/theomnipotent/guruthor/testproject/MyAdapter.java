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
import android.widget.MediaController;
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
    private static MediaPlayer mediaPlayer = new MediaPlayer();
    private int playingAdapterPosition = -1;
    private int prevPlayingAdapterPosition = -1;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder

    public static class MyAudioViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        SeekBar mSeekBar;
        ImageButton playButton;
        ImageButton pauseButton;
        ImageButton stopButton;
        ImageButton shortSkipBackward;
        ImageButton shortSkipForward;
        ImageButton variableSkipBackward;
        ImageButton variableSkipForward;

        MyAudioViewHolder(View v) {
            super(v);
            mSeekBar = v.findViewById(R.id.audio_scrubber);
            playButton = v.findViewById(R.id.playButton);
            pauseButton = v.findViewById(R.id.pauseButton);
            stopButton = v.findViewById(R.id.stopButton);
            shortSkipBackward = v.findViewById(R.id.audioShortSkipBackward);
            shortSkipForward = v.findViewById(R.id.audioShortSkipForward);
            variableSkipBackward = v.findViewById(R.id.audioVariableSkipBackward);
            variableSkipForward = v.findViewById(R.id.audioVariableSkipForward);

        }
    }

    public static class MyVideoViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        SeekBar mSeekBar;
        VideoView mVideoView;
        MediaController mediaController = new MediaController(MainActivity.context);
        ImageButton videoPlayButton;
        ImageButton videoPauseButton;
        ImageButton videoStopButton;
        ImageButton shortSkipBackward;
        ImageButton shortSkipForward;
        ImageButton variableSkipBackward;
        ImageButton variableSkipForward;

        MyVideoViewHolder(View v) {
            super(v);
            mSeekBar = v.findViewById(R.id.video_scrubber);
            mVideoView = v.findViewById(R.id.video_view);
            mVideoView.setMediaController(mediaController);
            videoPlayButton = v.findViewById(R.id.videoPlayButton);
            videoPauseButton = v.findViewById(R.id.videoPauseButton);
            videoStopButton = v.findViewById(R.id.videoStopButton);
            shortSkipBackward = v.findViewById(R.id.videoShortSkipBackward);
            shortSkipForward = v.findViewById(R.id.videoShortSkipForward);
            variableSkipBackward = v.findViewById(R.id.videoVariableSkipBackward);
            variableSkipForward = v.findViewById(R.id.videoVariableSkipForward);
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

    private static class MyTextViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        private TextView mTextView;

        private MyTextViewHolder(View v) {
            super(v);
            mTextView = v.findViewById(R.id.contact_name);
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
                runAudio(audioViewHolder, uri);
                break;

            case Constants.TYPE_VIDEO:
                MyVideoViewHolder videoViewHolder = (MyVideoViewHolder) holder;
                filePath = ((VideoBean) mDataset.get(position)).getFilePath();
                uri = Uri.parse("android.resource://" + MainActivity.PACKAGE_NAME + "/" + filePath);
                runVideo(videoViewHolder, uri);
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

    private void runAudio(final MyAdapter.MyAudioViewHolder audioViewHolder1, final Uri uri) {


        audioViewHolder1.playButton.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View view) {

                prevPlayingAdapterPosition = playingAdapterPosition;
                playingAdapterPosition = audioViewHolder1.getAdapterPosition();

                if (mediaPlayer.isPlaying()) {

                    mediaPlayer.reset();
                    mediaPlayer.stop();
                    mediaPlayer.release();
                    mediaPlayer = MediaPlayer.create(MainActivity.context, uri);

                } else {

                    mediaPlayer = MediaPlayer.create(MainActivity.context, uri);

                }

                audioViewHolder1.mSeekBar.setMax(mediaPlayer.getDuration());

                new Timer().scheduleAtFixedRate(new TimerTask() {
                    @Override
                    public void run() {
                        if (audioViewHolder1.getAdapterPosition() == playingAdapterPosition)
                            audioViewHolder1.mSeekBar.setProgress(mediaPlayer.getCurrentPosition());
                        else if (audioViewHolder1.getAdapterPosition() == prevPlayingAdapterPosition)
                            audioViewHolder1.mSeekBar.setProgress(0);
                    }
                }, 0, 100);

                audioViewHolder1.mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        if (fromUser) {
                            mediaPlayer.seekTo(progress);
                        }
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });

                mediaPlayer.start();
            }
        });

        audioViewHolder1.pauseButton.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.pause();
            }
        });

        audioViewHolder1.stopButton.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.seekTo(0);
                mediaPlayer.pause();
            }
        });

        audioViewHolder1.variableSkipBackward.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.seekTo(mediaPlayer.getCurrentPosition() - (mediaPlayer.getDuration() * VARIABLE_SKIP_PERCENT) / 100);
            }
        });

        audioViewHolder1.variableSkipForward.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.seekTo(mediaPlayer.getCurrentPosition() + (mediaPlayer.getDuration() * VARIABLE_SKIP_PERCENT) / 100);
            }
        });

        audioViewHolder1.shortSkipBackward.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.seekTo(mediaPlayer.getCurrentPosition() - SHORT_SKIP);
            }
        });

        audioViewHolder1.shortSkipForward.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.seekTo(mediaPlayer.getCurrentPosition() + SHORT_SKIP);
            }
        });
    }

    private void runVideo(final MyAdapter.MyVideoViewHolder videoViewHolder1, final Uri uri) {


        videoViewHolder1.videoPlayButton.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View view) {
                prevPlayingAdapterPosition = playingAdapterPosition;
                playingAdapterPosition = videoViewHolder1.getAdapterPosition();

                videoViewHolder1.mVideoView.canPause();
                videoViewHolder1.mVideoView.canSeekBackward();
                videoViewHolder1.mVideoView.canSeekBackward();

                if (videoViewHolder1.mVideoView.isPlaying()) {

                    videoViewHolder1.mVideoView.stopPlayback();
                    videoViewHolder1.mediaController.setMediaPlayer(videoViewHolder1.mVideoView);
                    videoViewHolder1.mVideoView.setVideoURI(uri);

                } else {

                    videoViewHolder1.mediaController.setMediaPlayer(videoViewHolder1.mVideoView);
                    videoViewHolder1.mVideoView.setVideoURI(uri);

                }

                videoViewHolder1.mSeekBar.setMax(videoViewHolder1.mVideoView.getDuration());

                new Timer().scheduleAtFixedRate(new TimerTask() {
                    @Override
                    public void run() {
                        if (videoViewHolder1.getAdapterPosition() == playingAdapterPosition)
                            videoViewHolder1.mSeekBar.setProgress(videoViewHolder1.mVideoView.getCurrentPosition());
                        else if (videoViewHolder1.getAdapterPosition() == prevPlayingAdapterPosition)
                            videoViewHolder1.mSeekBar.setProgress(0);
                    }
                }, 0, 100);

                videoViewHolder1.mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        if (fromUser) {
                            videoViewHolder1.mVideoView.seekTo(progress);
                        }
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });

                videoViewHolder1.mVideoView.start();
            }
        });

        videoViewHolder1.videoPauseButton.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View view) {
                videoViewHolder1.mVideoView.pause();
            }
        });

        videoViewHolder1.videoStopButton.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View view) {
                videoViewHolder1.mVideoView.seekTo(0);
                videoViewHolder1.mVideoView.pause();
            }
        });

        videoViewHolder1.variableSkipBackward.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View view) {
                videoViewHolder1.mVideoView.seekTo(videoViewHolder1.mVideoView.getCurrentPosition() - (videoViewHolder1.mVideoView.getDuration() * VARIABLE_SKIP_PERCENT) / 100);
            }
        });

        videoViewHolder1.variableSkipForward.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View view) {
                videoViewHolder1.mVideoView.seekTo(videoViewHolder1.mVideoView.getCurrentPosition() + (videoViewHolder1.mVideoView.getDuration() * VARIABLE_SKIP_PERCENT) / 100);
            }
        });

        videoViewHolder1.shortSkipBackward.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View view) {
                videoViewHolder1.mVideoView.seekTo(videoViewHolder1.mVideoView.getCurrentPosition() - SHORT_SKIP);
            }
        });

        videoViewHolder1.shortSkipForward.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View view) {
                videoViewHolder1.mVideoView.seekTo(videoViewHolder1.mVideoView.getCurrentPosition() + SHORT_SKIP);
            }
        });

    }

}