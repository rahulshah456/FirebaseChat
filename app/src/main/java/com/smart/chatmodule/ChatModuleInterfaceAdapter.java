package com.smart.chatmodule;

import android.content.Context;

import androidx.fragment.app.Fragment;

import com.smart.chatmodule.fragments.CustomPrivateThreadsFragment;

import sdk.chat.ui.BaseInterfaceAdapter;

public class ChatModuleInterfaceAdapter extends BaseInterfaceAdapter {

    public ChatModuleInterfaceAdapter(Context context) {
        super(context);
    }

    @Override
    public Fragment privateThreadsFragment() {
        return new CustomPrivateThreadsFragment();
    }
}
