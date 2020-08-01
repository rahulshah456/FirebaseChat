package com.smart.chatmodule;

import android.app.Application;
import android.util.Log;

import androidx.fragment.app.Fragment;

import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.PhoneAuthProvider;
import com.smart.chatmodule.fragments.CustomPrivateThreadsFragment;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.disposables.Disposable;
import sdk.chat.core.dao.ContactLink;
import sdk.chat.core.dao.ContactLinkDao;
import sdk.chat.core.dao.DaoCore;
import sdk.chat.core.dao.User;
import sdk.chat.core.events.NetworkEvent;
import sdk.chat.core.session.ChatSDK;
import sdk.chat.core.utils.Device;
import sdk.chat.firebase.adapter.module.FirebaseModule;
import sdk.chat.firebase.push.FirebasePushModule;
import sdk.chat.firebase.ui.FirebaseUIModule;
import sdk.chat.firebase.upload.FirebaseUploadModule;
import sdk.chat.ui.ChatSDKUI;
import sdk.chat.ui.extras.ExtrasModule;
import sdk.chat.ui.module.UIModule;

public class ChatApplication extends Application {

    public static final String TAG = ChatApplication.class.getName();

    @Override
    public void onCreate() {
        super.onCreate();
        try {
            firebase();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void firebase() throws Exception {
        String rootPath = "firebase";

        ChatSDK.builder()
                .setAnonymousLoginEnabled(false)
                .setDebugModeEnabled(true)
                .setRemoteConfigEnabled(false)
                .setPublicChatRoomLifetimeMinutes(TimeUnit.HOURS.toMinutes(24))
                .setSendSystemMessageWhenRoleChanges(true)
                .setRemoteConfigEnabled(true)
                .build()

                // Add the network adapter module
                .addModule(
                        FirebaseModule.builder()
                                .setFirebaseRootPath(rootPath)
                                .setDisableClientProfileUpdate(false)
                                .setEnableCompatibilityWithV4(true)
                                .setDevelopmentModeEnabled(true)
                                .build()
                )

                // Add the UI module
                .addModule(UIModule.builder()
                        .setPublicRoomCreationEnabled(true)
                        .setPublicRoomsEnabled(true)
                        .setTheme(R.style.AppTheme)
                        .build()
                )

                // Add modules to handle file uploads, push notifications
                .addModule(FirebaseUploadModule.shared())
                .addModule(FirebasePushModule.shared())

                .addModule(ExtrasModule.builder(config -> {
                    if (Device.honor(this)) {
                        config.setDrawerEnabled(false);
                    }
                }))



                .addModule(FirebaseUIModule.builder()
                        .setProviders(EmailAuthProvider.PROVIDER_ID, PhoneAuthProvider.PROVIDER_ID)
                        .build()
                )

                // Activate
                .build()
                .activate(this);

        //ChatSDK.shared().setInterfaceAdapter(new ChatModuleInterfaceAdapter(this));
        ChatSDKUI.setPrivateThreadsFragment(new CustomPrivateThreadsFragment());

        Log.d(TAG, "firebase: " + ChatSDK.core().currentUser().toString());
        List<User> users = ChatSDK.contact().contacts();
        Log.d(TAG, "firebase: " + users.size());
        Log.d(TAG, "firebase: " + users.toString());

    }

}
