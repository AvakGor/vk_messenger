package com.example.gor.testapplication.presenters

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.example.gor.testapplication.R
import com.example.gor.testapplication.models.FriendModel
import com.example.gor.testapplication.providers.FriendsProvider
import com.example.gor.testapplication.views.FriendsView

@InjectViewState
class FriendsPresenter: MvpPresenter<FriendsView>() {
    fun loadFriends(){
        viewState.startLoading()

        //FriendsProvider(presenter = this).testLoadFriends(hasFriends = true)
        FriendsProvider(presenter = this).loadFriends()
    }

    fun friendsLoaded(friendsList: ArrayList<FriendModel>){
        viewState.endLoading()
        if(friendsList.size == 0){
            viewState.setupEmptyList()
            viewState.showError(textResource =  R.string.friends_no_items)
        } else{
            viewState.setupFriendsList(friendsList = friendsList)
        }
    }

    fun showError(textResource: Int) {
        viewState.showError(textResource = textResource)
    }

}