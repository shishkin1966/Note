package com.cleanarchitecture.sl.repository;

import android.content.Context;
import androidx.annotation.NonNull;

import com.cleanarchitecture.common.utils.ApplicationUtils;
import com.cleanarchitecture.sl.data.Result;
import com.cleanarchitecture.sl.event.ui.ImageEvent;
import com.cleanarchitecture.sl.request.Request;
import com.cleanarchitecture.sl.request.ResponseListener;
import com.cleanarchitecture.sl.sl.ApplicationModule;
import com.cleanarchitecture.sl.sl.SLUtil;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

public class NetImageProvider extends AbsProvider implements INetImageProvider {

    public static final String NAME = NetImageProvider.class.getName();
    private Picasso mPicasso;

    public NetImageProvider() {
        final Context context = ApplicationModule.getInstance();
        if (context == null) {
            return;
        }

        mPicasso = Picasso.with(context);
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public void request(final Request request) {
        if (request != null && validate()) {
            SLUtil.getRequestModule().request(this, request);
        }
    }

    @Override
    public Result<Boolean> validateExt() {
        return new Result<>(mPicasso != null);
    }

    @Override
    public boolean isPersistent() {
        return false;
    }

    @Override
    public void onUnRegisterModule() {
    }

    public void cancelRequests(ResponseListener listener) {
        SLUtil.getRequestModule().cancelRequests(this, listener);
    }

    @Override
    public void downloadImage(final ImageEvent event) {
        ApplicationUtils.runOnUiThread(() -> {
            if (!event.isWithCache()) {
                final RequestCreator requestCreator = mPicasso.load(event.getUrl());
                if (event.getPlaceHolder() > 0) {
                    requestCreator.placeholder(event.getPlaceHolder());
                }
                if (event.getErrorHolder() > 0) {
                    requestCreator.error(event.getErrorHolder());
                }
                requestCreator.into(event.getView().get(), new Callback() {
                    @Override
                    public void onSuccess() {
                    }

                    @Override
                    public void onError() {
                    }
                });
            } else {
                final RequestCreator requestCreator = mPicasso.load(event.getUrl());
                if (event.getPlaceHolder() > 0) {
                    requestCreator.placeholder(event.getPlaceHolder());
                }
                if (event.getErrorHolder() > 0) {
                    requestCreator.error(event.getErrorHolder());
                }
                requestCreator.networkPolicy(NetworkPolicy.OFFLINE)
                        .into(event.getView().get(), new Callback() {
                            @Override
                            public void onSuccess() {
                            }

                            @Override
                            public void onError() {
                                //Try again online if cache failed
                                final RequestCreator requestCreator = mPicasso.load(event.getUrl());
                                if (event.getPlaceHolder() > 0) {
                                    requestCreator.placeholder(event.getPlaceHolder());
                                }
                                if (event.getErrorHolder() > 0) {
                                    requestCreator.error(event.getErrorHolder());
                                }
                                requestCreator.into(event.getView().get(), new Callback() {
                                            @Override
                                            public void onSuccess() {
                                            }

                                            @Override
                                            public void onError() {
                                            }
                                        });
                            }
                        });

            }
        });
    }

    @Override
    public int compareTo(@NonNull Object o) {
        return (INetProvider.class.isInstance(o)) ? 0 : 1;
    }
}


