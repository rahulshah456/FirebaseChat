package com.smart.chatmodule.fragments;

import android.app.AlertDialog;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.LayoutRes;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import com.bumptech.glide.Glide;
import com.smart.chatmodule.R;
import com.stfalcon.chatkit.dialogs.DialogsListAdapter;

import sdk.chat.core.dao.Thread;
import sdk.chat.core.session.ChatSDK;
import sdk.chat.core.utils.Dimen;
import sdk.chat.ui.chat.model.ThreadHolder;
import sdk.chat.ui.fragments.PrivateThreadsFragment;
import sdk.chat.ui.module.UIModule;
import sdk.chat.ui.utils.ThreadImageBuilder;
import sdk.chat.ui.view_holders.ThreadViewHolder;

public class CustomPrivateThreadsFragment extends PrivateThreadsFragment {

    public static final String TAG = CustomPrivateThreadsFragment.class.getName();

    // You can customize the layout file here
//    @Override
//    protected @LayoutRes
//    int getLayout() {
//        return R.layout.fragment_threads;
//    }

    @Override
    protected boolean allowThreadCreation() {
        return false;
    }

    @Override
    public void addListeners() {
        //add listeners to the viewHolder here
    }

    @Override
    public void initViews() {

        dialogsListAdapter = new DialogsListAdapter<>(R.layout.custom_thread_view_holder,
                ThreadViewHolder.class, (imageView, url, payload) -> {
            if (getContext() != null) {
                int size = Dimen.from(getContext(), sdk.chat.ui.R.dimen.action_bar_avatar_size);

                if (payload instanceof ThreadHolder) {
                    if (url == null) {
                        ThreadHolder threadHolder = (ThreadHolder) payload;
                        dm.add(ThreadImageBuilder.load(imageView, threadHolder.getThread(), size));
                    } else {
                        Drawable placeholder = ThreadImageBuilder.defaultDrawable(null);
                        Glide.with(this).load(url).dontAnimate().override(size).placeholder(placeholder).into(imageView);
                    }
                } else {
                    int placeholder = UIModule.config().defaultProfilePlaceholder;
                    Glide.with(this).load(url).dontAnimate().override(size).placeholder(placeholder).into(imageView);
                }
            }
        });
        dialogsList.setAdapter(dialogsListAdapter);


        // Stop the image from flashing when the list is reloaded
        RecyclerView.ItemAnimator animator = dialogsList.getItemAnimator();
        if (animator instanceof SimpleItemAnimator) {
            ((SimpleItemAnimator) animator).setSupportsChangeAnimations(false);
        }

        dialogsListAdapter.setOnDialogViewClickListener((view, dialog) -> {
            dialog.markRead();
            startChatActivity(dialog.getId());
        });
        dialogsListAdapter.setOnDialogLongClickListener(dialog -> {
            Thread thread = ChatSDK.db().fetchThreadWithEntityID(dialog.getId());
            if (thread != null) {
                onLongClickPublishRelay.accept(thread);
            }
        });

    }

}
