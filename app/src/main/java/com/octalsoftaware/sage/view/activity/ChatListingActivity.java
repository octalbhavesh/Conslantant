package com.octalsoftaware.sage.view.activity;

import android.app.FragmentTransaction;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.octalsoftaware.sage.view.fragment.SimpleChatFragment;
import com.octalsoftaware.sage.view.fragment.VoiceChatFragment;
import com.sage.conslantant.android.R;
import com.sage.conslantant.android.databinding.ActivityChatListingBinding;

public class ChatListingActivity extends BaseActivity {

    private ActivityChatListingBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_chat_listing);
        final FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.mainFrame, new SimpleChatFragment(), "NewFragmentTag");
        ft.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();

        mBinding.layoutChatSimple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.mainFrame, new SimpleChatFragment(), "NewFragmentTag");
                ft.commit();
                mBinding.layoutChatVoice.setBackground(getDrawable(R.drawable.round_chat_unselected));
                mBinding.layoutChatSimple.setBackground(getDrawable(R.drawable.round_chat_selected));
            }
        });
        mBinding.ivSortDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBinding.dialogListingSort.getVisibility() == View.VISIBLE) {
                    mBinding.dialogListingSort.setVisibility(View.GONE);
                } else {
                    mBinding.dialogListingSort.setVisibility(View.VISIBLE);
                }
            }
        });
        mBinding.layoutChatVoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.mainFrame, new VoiceChatFragment(), "NewFragmentTag");
                ft.commit();
                mBinding.layoutChatSimple.setBackground(getDrawable(R.drawable.round_chat_unselected));
                mBinding.layoutChatVoice.setBackground(getDrawable(R.drawable.round_chat_selected));
            }
        });

        mBinding.ivHomeDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
