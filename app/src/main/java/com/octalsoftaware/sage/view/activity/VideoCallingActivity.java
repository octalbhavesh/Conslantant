package com.octalsoftaware.sage.view.activity;

import android.databinding.DataBindingUtil;
import android.media.AudioManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.octalsoftaware.sage.sinch.AudioPlayer;
import com.octalsoftaware.sage.sinch.SinchService;
import com.sage.conslantant.android.R;
import com.sage.conslantant.android.databinding.ActivityVideoCallingBinding;
import com.sinch.android.rtc.PushPair;
import com.sinch.android.rtc.calling.Call;
import com.sinch.android.rtc.calling.CallEndCause;
import com.sinch.android.rtc.video.VideoCallListener;
import com.sinch.android.rtc.video.VideoController;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class VideoCallingActivity extends BaseActivity implements View.OnClickListener {

    private ActivityVideoCallingBinding mBinding;
    private AudioPlayer mAudioPlayer;
    private boolean mVideoViewsAdded = false;
    private String TAG = VideoCallingActivity.class.getSimpleName();
    static final String CALL_START_TIME = "callStartTime";
    static final String ADDED_LISTENER = "addedListener";
    private Bundle bundle = null;
    private long mCallStart = 0;
    private boolean mAddedListener = false;
    private String mCallId;
    private String receiverUserId;


    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putLong(CALL_START_TIME, mCallStart);
        savedInstanceState.putBoolean(ADDED_LISTENER, mAddedListener);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        mCallStart = savedInstanceState.getLong(CALL_START_TIME);
        mAddedListener = savedInstanceState.getBoolean(ADDED_LISTENER);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_video_calling);
        if (savedInstanceState == null) {
            mCallStart = System.currentTimeMillis();
        }

        init();
    }

    private void init() {
        this.mAudioPlayer = new AudioPlayer(VideoCallingActivity.this);
        bundle = getIntent().getExtras();
        if (bundle != null) {
            mCallId = "CALL_ID";

            try {
                receiverUserId = bundle.getString("receiverId");
            } catch (Exception e) {
                receiverUserId = "";
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        Call call = getSinchServiceInterface().getCall(mCallId);

        if (call != null) {
            if (!mAddedListener) {
                call.addCallListener(new SinchCallListener());
                mAddedListener = true;
            }

            SinchService.speakerOff();

        } else {
            Log.e(TAG, "Started with invalid callId, aborting.");
            finish();
        }
        Map<String, String> profile_detailArr = call.getHeaders();
        Iterator myVeryOwnIterator = profile_detailArr.keySet().iterator();

        while (myVeryOwnIterator.hasNext()) {
            String key = (String) myVeryOwnIterator.next();
            try {
                if (key.equalsIgnoreCase("type")) {
                    String value = (String) profile_detailArr.get(key);
                    if (value.equalsIgnoreCase("video")) {
//                        mCallState.setText(call.getState().toString() + " Video Calling");
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
       // updateUI();
    }

    private class SinchCallListener implements VideoCallListener {

        @Override
        public void onCallEnded(Call call) {
            CallEndCause cause = call.getDetails().getEndCause();
            Log.d(TAG, "Call ended. Reason: " + cause.toString());
            mAudioPlayer.stopProgressTone();
            mAudioPlayer.playDevicePlayer();
            setVolumeControlStream(AudioManager.USE_DEFAULT_STREAM_TYPE);
            String endMsg = "Call ended: " + call.getDetails().toString();
            try {
               /* if (mCountTimer != null) {
                    mCountTimer.cancel();
                }*/
            } catch (Exception e) {
                e.printStackTrace();
            }


        }

        @Override
        public void onCallEstablished(Call call) {
            Log.d(TAG, "Call established");
            mAudioPlayer.stopProgressTone();
            setVolumeControlStream(AudioManager.STREAM_VOICE_CALL);
            mCallStart = System.currentTimeMillis();
            String voiceType = "";
            Map<String, String> profile_detailArr = call.getHeaders();
            Iterator myVeryOwnIterator = profile_detailArr.keySet().iterator();
            while (myVeryOwnIterator.hasNext()) {
                String key = (String) myVeryOwnIterator.next();
                String value = (String) profile_detailArr.get(key);

                try {
                    if (key.equalsIgnoreCase("receiverUserImage")) {
                       // ImageLoader.getInstance().displayImage(value, ivUser, options);
                    } else if (key.equalsIgnoreCase("receiverUserId")) {
                        receiverUserId = value;
                    } else if (key.equalsIgnoreCase("type")) {
                        voiceType = value;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

           // ivUser.setVisibility(View.GONE);
            /*if (SexityPreferences.getGender(mContext).equalsIgnoreCase("male")) {
                tvTimeDuration.setVisibility(View.VISIBLE);
                tvTimeDuration.setText(call.getState().toString() + " Video Calling");

                if (!SexityConstant.FREE_CHAT_STATUS) {
                    mCountTimer = new CountDownTimer(Integer.parseInt(SexityConstant.CHAT_DURATION) * 1000, 1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            tvTimeDuration.setText("Time Left: " + millisUntilFinished / 1000 + " sec");
                        }

                        @Override
                        public void onFinish() {
                            endCall();
                            Intent intent = new Intent(VideoCallActivity.this, FeedbackActivity.class);
                            intent.putExtra("receiverUserImage", bundle.getString("receiverUserImage"));
                            intent.putExtra("receiverUserName", bundle.getString("receiverUserName"));
                            intent.putExtra("threadId", SexityConstant.THREAD_ID);
                            intent.putExtra("walletBalance", walletBalance);
                            intent.putExtra("videoPrice", videoPrice);
                            intent.putExtra("receiverId", getIntent().getExtras().getString("receiverId"));
                            intent.putExtra("screenType", "video");
                            startActivity(intent);
                            finish();
                        }
                    }.start();
                } else {
                    mCountTimer = new CountDownTimer(60000, 1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            tvTimeDuration.setText("Time Left: " + millisUntilFinished / 1000 + " sec");
                        }

                        @Override
                        public void onFinish() {
                            endCall();
                            Intent intent = new Intent(VideoCallActivity.this, FeedbackActivity.class);
                            intent.putExtra("receiverUserImage", bundle.getString("receiverUserImage"));
                            intent.putExtra("receiverUserName", bundle.getString("receiverUserName"));
                            intent.putExtra("threadId", SexityConstant.THREAD_ID);
                            intent.putExtra("walletBalance", walletBalance);
                            intent.putExtra("videoPrice", videoPrice);
                            intent.putExtra("receiverId", getIntent().getExtras().getString("receiverId"));
                            intent.putExtra("screenType", "video");
                            startActivity(intent);
                            finish();
                        }
                    }.start();
                }

            } else {
                tvTimeDuration.setVisibility(View.GONE);
                SexityApplication.flagChatVideo = true;
                updateDisplay();
                if (SexityApplication.flagChatVideo) {
                    if (SexityUtils.isNetworkAvailable(mContext)) {
                        USER_AVAILABLE_ASYNTASK_CALLING("off", SexityConstant.THREAD_ID, String.valueOf(mSecond));
                    } else {
                        SexityUtils.showToast(mContext, getResources().getString(R.string.noInternetConnection));
                    }
                }
            }
*/

        }


        @Override
        public void onVideoTrackAdded(Call call) {
            Log.d(TAG, "Video track added");
            if (call != null) {
                SinchService.speakerOn();
            }
            addVideoViews();
        }

        @Override
        public void onVideoTrackPaused(Call call) {

        }

        @Override
        public void onVideoTrackResumed(Call call) {

        }

        @Override
        public void onCallProgressing(Call call) {
            Log.d(TAG, "Call progressing");
            mAudioPlayer.playProgressTone();
        }

        @Override
        public void onShouldSendPushNotification(Call call, List<PushPair> list) {

        }
    }
    private void addVideoViews() {
        if (mVideoViewsAdded || getSinchServiceInterface() == null) {
            return; //early
        }
        final VideoController vc = getSinchServiceInterface().getVideoController();
        if (vc != null) {
           /* RelativeLayout rlLocalVideo = (RelativeLayout) findViewById(R.id.rlLocalVideo);
            rlLocalVideo.addView(vc.getLocalView());*/

           /* rlLocalVideo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    vc.toggleCaptureDevicePosition();
                }
            });*/
            LinearLayout llRemoteVideo = (LinearLayout) findViewById(R.id.llRemoteVideo);
            llRemoteVideo.addView(vc.getRemoteView());
            mVideoViewsAdded = true;
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        mBinding.ivVideoCallReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
