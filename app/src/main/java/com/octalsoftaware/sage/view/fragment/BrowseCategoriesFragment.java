package com.octalsoftaware.sage.view.fragment;

import android.app.FragmentTransaction;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.octalsoftaware.sage.view.activity.HomeActiviy;
import com.sage.conslantant.android.R;
import com.sage.conslantant.android.databinding.FragmentBrowseCategoriesBinding;


public class BrowseCategoriesFragment extends android.app.Fragment {

    private FragmentBrowseCategoriesBinding mBinding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_browse_categories, container, false);
        View rootView = mBinding.getRoot();
        mBinding.ivHomeDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((HomeActiviy) getActivity()).openDrawer();
            }
        });
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        firstCall();
        mBinding.layoutHomeNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.mainFrame1, new NewRequestFragment(), "NewFragmentTag");
                ft.commit();
                mBinding.layoutHomeGoing.setBackground(getActivity().getDrawable(R.drawable.round_chat_unselected));
                mBinding.layoutHomeNew.setBackground(getActivity().getDrawable(R.drawable.round_chat_selected));
            }
        });
        mBinding.layoutHomeGoing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.mainFrame1, new OnGoingRequestFragment(), "NewFragmentTag");
                ft.commit();
                mBinding.layoutHomeNew.setBackground(getActivity().getDrawable(R.drawable.round_chat_unselected));
                mBinding.layoutHomeGoing.setBackground(getActivity().getDrawable(R.drawable.round_chat_selected));
            }
        });

    }

    private void firstCall() {
        final FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.mainFrame1, new NewRequestFragment(), "NewFragmentTag");
        ft.commit();
        mBinding.layoutHomeGoing.setBackground(getActivity().getDrawable(R.drawable.round_chat_unselected));
        mBinding.layoutHomeNew.setBackground(getActivity().getDrawable(R.drawable.round_chat_selected));
    }
}
