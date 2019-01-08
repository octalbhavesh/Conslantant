package com.octalsoftaware.sage.sinch;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.octalsoftaware.sage.view.activity.HomeActiviy;
import com.sage.conslantant.android.R;
import com.sinch.android.rtc.ClientRegistration;
import com.sinch.android.rtc.Sinch;
import com.sinch.android.rtc.SinchClient;
import com.sinch.android.rtc.SinchClientListener;
import com.sinch.android.rtc.SinchError;
import com.sinch.android.rtc.calling.Call;
import com.sinch.android.rtc.calling.CallClient;
import com.sinch.android.rtc.calling.CallClientListener;
import com.sinch.android.rtc.messaging.MessageClientListener;
import com.sinch.android.rtc.messaging.WritableMessage;
import com.sinch.android.rtc.video.VideoController;

import java.util.HashMap;
import java.util.Map;

public class SinchService extends Service {

    // my id
    private static String APP_KEY = "68b5aab7-b7ee-4d11-aaaa-635918eebfb8";
    private static String APP_SECRET = "mOgkSjhYZUmCKTxj5x15Pg==";
    private static final String ENVIRONMENT = "sandbox.sinch.com";
// subhash id
   /* private static String APP_KEY = "3c444b69-e999-40e8-9bd9-51a1a64dc1fd";
    private static String APP_SECRET = "j13sKN4m/EyM/HyxzPvcBw==";*/
    // private static final String ENVIRONMENT = "clientapi.sinch.com";

    private static final String TAG = SinchService.class.getSimpleName();
    public static String CALL_ID = "CALL_ID";

    private final SinchServiceInterface mServiceInterface = new SinchServiceInterface();

    private static SinchClient mSinchClient = null;
    private StartFailedListener mListener;
    public static String mUserId;
    private String regId = "";


    public class SinchServiceInterface extends Binder {


        public Call callUserVideo(String receiverName, String profile_image, String userId, String username) {
            Map<String, String> headers = new HashMap<String, String>();
            headers.put("receiverUserImage", profile_image);
            headers.put("type", "Call");
            headers.put("receiverUserName", username);
            headers.put("receiverUserId", userId);
            return mSinchClient.getCallClient().callUserVideo(receiverName, headers);
        }

        public String getUserName() {
            return mUserId;
        }

        public boolean isStarted() {
            return SinchService.this.isStarted();
        }

        public void startClient(String userName) {
            start(userName);
        }

        public void stopClient() {
            stop();
        }

        public void setStartListener(StartFailedListener listener) {
            mListener = listener;
        }

        public void sendMessage(String receiverUserName, String textBody, String myId,
                                String myUserName, String myProfileImage, String receiverId,
                                String receiverName, String receiverImage, String type,
                                String imagePath, String filePath, String img, String date) {
            SinchService.this.sendMessage(receiverUserName, textBody, myId, myUserName,
                    myProfileImage, receiverId, receiverName, receiverImage, type, imagePath,
                    filePath, img, date);
        }

        public void addMessageClientListener(MessageClientListener listener) {
            SinchService.this.addMessageClientListener(listener);
        }

        public void removeMessageClientListener(MessageClientListener listener) {
            SinchService.this.removeMessageClientListener(listener);
        }

        public Call getCall(String mCallId) {
            return mSinchClient.getCallClient().getCall(mCallId);
        }

        public VideoController getVideoController() {
            if (!isStarted()) {
                return null;
            }
            return mSinchClient.getVideoController();
        }

    }

    @Override
    public void onCreate() {
        super.onCreate();
        APP_KEY = getResources().getString(R.string.sinchKey);
        APP_SECRET = getResources().getString(R.string.sinchSecretKey);
    }

    @Override
    public void onDestroy() {
        if (mSinchClient != null && mSinchClient.isStarted()) {
            mSinchClient.terminate();
        }
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mServiceInterface;
    }

    private boolean isStarted() {
        return (mSinchClient != null && mSinchClient.isStarted());
    }

    public void sendMessage(String receiverUserName, String textBody, String myId, String myUserName,
                            String myProfileImage, String receiverId, String receiverName,
                            String receiverImage, String type, String imagePath, String filePath,
                            String img, String date) {
        if (isStarted()) {
            WritableMessage message = new WritableMessage(receiverUserName, textBody);
            message.addHeader("consaltantId", myId);
            message.addHeader("consaltantName", myUserName);
            message.addHeader("consaltantImage", myProfileImage);
            message.addHeader("seekerId", receiverId);
            message.addHeader("seekerName", receiverName);
            message.addHeader("seekerImage", receiverImage);
            message.addHeader("messageType", type);
            message.addHeader("message", textBody);
            message.addHeader("imagePath", textBody);
            message.addHeader("filePath", filePath);
            message.addHeader("img", img);
            message.addHeader("senderDate", date);
            mSinchClient.getMessageClient().send(message);
        }
    }

    public void addMessageClientListener(MessageClientListener listener) {
        if (mSinchClient != null) {
            mSinchClient.getMessageClient().addMessageClientListener(listener);
        }
    }

    public void removeMessageClientListener(MessageClientListener listener) {
        if (mSinchClient != null) {
            mSinchClient.getMessageClient().removeMessageClientListener(listener);
        }
    }

    private void start(String userName) {
        if (mSinchClient == null) {
            mUserId = userName;
            mSinchClient = Sinch.getSinchClientBuilder().context(getApplicationContext()).userId(userName)
                    .applicationKey(APP_KEY)
                    .applicationSecret(APP_SECRET)
                    .environmentHost(ENVIRONMENT).build();

            mSinchClient.setSupportMessaging(true);
            mSinchClient.setSupportCalling(true);
            mSinchClient.setSupportActiveConnectionInBackground(true);
            mSinchClient.startListeningOnActiveConnection();

            // mSinchClient.setSupportPushNotifications(true);
            mSinchClient.addSinchClientListener(new MySinchClientListener());
            CallClient callClient = mSinchClient.getCallClient();
            callClient.addCallClientListener(new SinchCallClientListener());
            mSinchClient.start();
            Log.d(TAG, "Starting SinchClient");
        }
    }

    private void stop() {
        try {
            if (mSinchClient != null) {
                mSinchClient.terminate();
                mSinchClient = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public interface StartFailedListener {

        void onStartFailed(SinchError error);

        void onStarted();
    }

    private class MySinchClientListener implements SinchClientListener {

        @Override
        public void onClientFailed(SinchClient client, SinchError error) {
            if (mListener != null) {
                mListener.onStartFailed(error);
            }
            mSinchClient.terminate();
            mSinchClient = null;
        }

        @Override
        public void onClientStarted(SinchClient client) {
            Log.d(TAG, "SinchClient started");
            if (mListener != null) {
                mListener.onStarted();
            }
        }

        @Override
        public void onClientStopped(SinchClient client) {
            Log.d(TAG, "SinchClient stopped");
        }

        @Override
        public void onLogMessage(int level, String area, String message) {
            switch (level) {
                case Log.DEBUG:
                    Log.d(area, message);
                    break;
                case Log.ERROR:
                    Log.e(area, message);
                    break;
                case Log.INFO:
                    Log.i(area, message);
                    break;
                case Log.VERBOSE:
                    Log.v(area, message);
                    break;
                case Log.WARN:
                    Log.w(area, message);
                    break;
            }
        }

        @Override
        public void onRegistrationCredentialsRequired(SinchClient client,
                                                      ClientRegistration clientRegistration) {
        }


    }

    public static void speakerOn() {
        try {
            if (mSinchClient != null && mSinchClient.isStarted()) {
                mSinchClient.getAudioController().enableSpeaker();
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }

    }

    public static void speakerOff() {
        if (mSinchClient != null && mSinchClient.isStarted()) {
            mSinchClient.getAudioController().disableSpeaker();
        }
    }

    public static void toggleMute() {
        if (mSinchClient != null && mSinchClient.isStarted()) {
            mSinchClient.getAudioController().mute();
        }
    }

    public static void toggleUnMute() {
        if (mSinchClient != null && mSinchClient.isStarted()) {
            mSinchClient.getAudioController().unmute();
        }
    }

    private class SinchCallClientListener implements CallClientListener {

        @Override
        public void onIncomingCall(CallClient callClient, Call call) {
            Log.d(TAG, "Incoming call");
            Intent intent = new Intent(SinchService.this, HomeActiviy.class);
            intent.putExtra(CALL_ID, call.getCallId());
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
            SinchService.this.startActivity(intent);

        }
    }

}
