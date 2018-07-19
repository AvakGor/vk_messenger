package com.example.gor.testapplication.adapter

import android.annotation.SuppressLint
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.example.gor.testapplication.R
import com.example.gor.testapplication.models.FriendModel
import com.squareup.picasso.Picasso
import com.vk.sdk.api.VKApiConst
import com.vk.sdk.api.VKParameters
import com.vk.sdk.api.VKRequest

import de.hdodenhof.circleimageview.CircleImageView


class FriendsAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var mFriendsList: ArrayList<FriendModel> = ArrayList()
    private var mSourceList: ArrayList<FriendModel> = ArrayList()

    fun setupFriends(friendsList: ArrayList<FriendModel>){
        mSourceList.clear()
        mSourceList.addAll(friendsList)
        filter(query = "")
    }

    fun filter(query: String) {
        mFriendsList.clear()
        mSourceList.forEach {
            if (it.name.contains(query, ignoreCase = true) || it.surname.contains(query, ignoreCase = true)) {
                mFriendsList.add(it)
            } else {
                it.city?.let { city -> if (city.contains(query, ignoreCase = true)) {
                    mFriendsList.add(it)
                }}
            }
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemView = layoutInflater.inflate(R.layout.cell_friend, parent, false)
        return FriendsViewHolder(itemView = itemView)
    }

    override fun getItemCount(): Int {
        return mFriendsList.count()
    }



    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is FriendsViewHolder){
            holder.bind(friendModel = mFriendsList[position])
        }
    }



    class FriendsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        private var mCivAvatar: CircleImageView = itemView.findViewById(R.id.friend_civ_avatar)
        private var mTxtUserName: TextView = itemView.findViewById(R.id.friend_txt_username)
        private var mTxtCity: TextView = itemView.findViewById(R.id.friend_txt_city)
        private var mImgOnline: View = itemView.findViewById(R.id.friend_img_online)
        private var mSendMessage: Button = itemView.findViewById(R.id.message_button)
        private var mMsgForFriend: EditText = itemView.findViewById(R.id.msg_for_friend)

        @SuppressLint("SetTextI18n")
        fun bind(friendModel: FriendModel){
            var int_id: Int = friendModel.id.toInt()

            friendModel?.avatar.let { url -> Picasso.get().load(url)
                    .into(mCivAvatar)  }
            mTxtUserName.text = "${friendModel.name} ${friendModel.surname}"
            mTxtCity.text = itemView.context.getString(R.string.friend_no_sity)
            friendModel?.city.let { city -> mTxtCity.text = city }
            if(friendModel.isOnline) {
                mImgOnline.visibility = View.VISIBLE
            } else{
                mImgOnline.visibility = View.GONE
            }
            mSendMessage.setOnClickListener {
                var text: String = mMsgForFriend.text.toString()
                val request = VKRequest("messages.send", VKParameters.from(VKApiConst.USER_ID, int_id, VKApiConst.MESSAGE, text))
                request.executeWithListener(object : VKRequest.VKRequestListener() {})
                mMsgForFriend.setText("");
            }
        }
    }
}