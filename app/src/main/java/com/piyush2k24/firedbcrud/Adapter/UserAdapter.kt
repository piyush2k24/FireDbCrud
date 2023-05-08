package com.piyush2k24.firedbcrud.Adapter

import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.piyush2k24.firedbcrud.Holder.UserHolder
import com.piyush2k24.firedbcrud.R

class UserAdapter(
    private val UserList : ArrayList<UserHolder>
) : RecyclerView.Adapter<UserAdapter.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView= LayoutInflater.from(parent.context)
            .inflate(R.layout.firedb_user_list,parent,false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return UserList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentUser=UserList[position]

        val bytes=Base64.decode(currentUser.userImage,Base64.DEFAULT)
        val bitmap=BitmapFactory.decodeByteArray(bytes,0,bytes.size)
        holder.UImage.setImageBitmap(bitmap)
        holder.UName.text=currentUser.userName
        holder.UEmail.text=currentUser.userEmail
        holder.UDesignation.text=currentUser.userDesignation
    }

    class ViewHolder(itemView:View) : RecyclerView.ViewHolder(itemView) {
        val UImage : ImageView = itemView.findViewById(R.id.UImage)
        val UName : TextView = itemView.findViewById(R.id.UName)
        val UEmail: TextView = itemView.findViewById(R.id.UEmail)
        val UDesignation : TextView = itemView.findViewById(R.id.UDesignation)
    }
}