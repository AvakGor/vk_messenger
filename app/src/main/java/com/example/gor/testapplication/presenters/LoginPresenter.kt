package com.example.gor.testapplication.presenters

import android.content.Intent
import android.os.Handler
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.example.gor.testapplication.R
import com.example.gor.testapplication.views.LoginView
import com.vk.sdk.VKAccessToken
import com.vk.sdk.VKCallback
import com.vk.sdk.VKSdk
import com.vk.sdk.api.VKError

@InjectViewState
class LoginPresenter: MvpPresenter<LoginView>() {
    fun login(isSuccess: Boolean) {
        viewState.startLoading()
        Handler().postDelayed({
            viewState.endLoading()
            if (isSuccess)
                viewState.openFriends()
            else
                viewState.showError(textResource = R.string.login_error)
        }, 500)
    }

    fun loginVk(requestCode: Int, resultCode: Int, data: Intent?): Boolean {
        if (!VKSdk.onActivityResult(requestCode, resultCode, data, object : VKCallback<VKAccessToken> {
                    override fun onResult(res: VKAccessToken?) {
                        viewState.openFriends()
                    }

                    override fun onError(error: VKError?) {
                        viewState.showError(textResource = R.string.login_error)
                    }
                })) {
            return false
        }

        return true
    }
}