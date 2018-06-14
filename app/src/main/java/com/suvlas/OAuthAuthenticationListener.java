package com.suvlas;

/**
 * Created by hp on 11/22/2017.
 */

public interface OAuthAuthenticationListener {

    public abstract void onSuccess();

    public abstract void onFail(String error);
}
