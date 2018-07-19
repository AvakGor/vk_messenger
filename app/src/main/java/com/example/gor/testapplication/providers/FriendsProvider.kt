package com.example.gor.testapplication.providers

import com.example.gor.testapplication.R
import com.example.gor.testapplication.models.FriendModel
import com.example.gor.testapplication.presenters.FriendsPresenter
import com.google.gson.JsonParser
import com.vk.sdk.api.*

class FriendsProvider(var presenter: FriendsPresenter) {

    fun loadFriends() {
        val request = VKApi.friends().get(VKParameters.from(VKApiConst.COUNT, 10000, VKApiConst.FIELDS, "city, country, photo_100, online"))
        request.executeWithListener(object: VKRequest.VKRequestListener() {
            override fun onComplete(response: VKResponse) {
                super.onComplete(response)

                val jsonParser = JsonParser()
                val parsedJson = jsonParser.parse(response.json.toString()).asJsonObject
                val friendsList: ArrayList<FriendModel> = ArrayList()

                parsedJson.get("response").asJsonObject.getAsJsonArray("items").forEach {
                    val city = if (it.asJsonObject.get("city") == null) {
                        null
                    } else {
                        it.asJsonObject.get("city").asJsonObject.get("title").asString
                    }

                    val friend = FriendModel(
                            id = it.asJsonObject.get("id").asString,
                            name = it.asJsonObject.get("first_name").asString,
                            surname = it.asJsonObject.get("last_name").asString,
                            city = city,
                            avatar = it.asJsonObject.get("photo_100").asString,
                            isOnline = it.asJsonObject.get("online").asInt == 1)
                    friendsList.add(friend)
                }

                presenter.friendsLoaded(friendsList = friendsList)
            }

            override fun onError(error: VKError?) {
                super.onError(error)
                presenter.showError(textResource = R.string.friends_error_loading)
            }
        })
    }

}