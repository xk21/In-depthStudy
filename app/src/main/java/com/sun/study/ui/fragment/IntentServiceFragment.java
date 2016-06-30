package com.sun.study.ui.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.sun.study.R;
import com.sun.study.service.MyIntentService;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by sunfusheng on 16/6/30.
 */
public class IntentServiceFragment extends BaseFragment {

    @Bind(R.id.tv_info)
    TextView tvInfo;
    @Bind(R.id.progressBar)
    ProgressBar progressBar;
    @Bind(R.id.tv_progress)
    TextView tvProgress;
    @Bind(R.id.tv_start)
    TextView tvStart;
    @Bind(R.id.tv_cancel)
    TextView tvCancel;

    public final static String ACTION_INTENTSERVICE = "action.my.intentservice";

    private LocalBroadcastManager mLocalBroadcastManager;
    private MyBroadcastReceiver mBroadcastReceiver;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLocalBroadcastManager = LocalBroadcastManager.getInstance(getContext());
        mBroadcastReceiver = new MyBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter(ACTION_INTENTSERVICE);
        mLocalBroadcastManager.registerReceiver(mBroadcastReceiver, intentFilter);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_intent_service, container, false);
        ButterKnife.bind(this, rootView);

        initView();
        initListener();
        return rootView;
    }

    private void initView() {
        tvInfo.setText("状态：未运行");
        progressBar.setMax(100);
        progressBar.setProgress(0);
        tvProgress.setText(0 + "%");
    }

    private void initListener() {
        tvStart.setOnClickListener(this);
        tvCancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_start:
                Intent startIntent = new Intent(getActivity(), MyIntentService.class);
                getActivity().startService(startIntent);
                break;
            case R.id.tv_cancel:
                Intent stopIntent = new Intent(getActivity(), MyIntentService.class);
                getActivity().stopService(stopIntent);
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        mLocalBroadcastManager.unregisterReceiver(mBroadcastReceiver);
    }

    class MyBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (TextUtils.isEmpty(action)) return;

            switch (action) {
                case ACTION_INTENTSERVICE:
                    String status = intent.getStringExtra("status");
                    int progress = intent.getIntExtra("progress", 0);
                    tvInfo.setText("状态："+status);
                    progressBar.setProgress(progress);
                    tvProgress.setText(progress + "%");
                    break;
            }
        }
    }
}
