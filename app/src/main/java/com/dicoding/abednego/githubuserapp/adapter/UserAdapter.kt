package com.dicoding.abednego.githubuserapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.abednego.githubuserapp.R

class UserAdapter(private val list1 : List<String>,private val list2 : List<String>, private val list3: List<String>, private val list4: List<Int>): RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    private var onItemClickListener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        onItemClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        UserViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_row_user, parent, false))

    override fun getItemCount() = list1.size

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.tvUsername.text =list1[position]
        holder.tvHtmlUser.text =list3[position]
        Glide.with(holder.itemView.context).load(list2[position]).into(holder.imgUser)
        holder.itemView.setOnClickListener {
            onItemClickListener?.onItemClick(list1[position], list4[position] ,list2[position],list3[position])
        }
    }

    class UserViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val tvUsername : TextView = view.findViewById(R.id.tv_username)
        val tvHtmlUser : TextView = view.findViewById(R.id.tv_html_user)
        val imgUser :ImageView = view.findViewById(R.id.iv_user)
    }

    interface OnItemClickListener {
        fun onItemClick(username: String, id:Int, avatarUrl:String, htmlUrl:String)
    }
}