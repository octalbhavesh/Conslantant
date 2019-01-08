package com.octalsoftaware.sage.view.fragment;

import android.app.Fragment;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.octalsoftaware.sage.view.activity.HomeActiviy;
import com.sage.conslantant.android.R;
import com.sage.conslantant.android.databinding.FragmentContactBinding;


public class ContactFragment extends Fragment {
private FragmentContactBinding mBinding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_contact, container, false);
        View rootView = mBinding.getRoot();
        // callApi();
        mBinding.ivHomeDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((HomeActiviy) getActivity()).openDrawer();
            }
        });
        return rootView;
    }

}
