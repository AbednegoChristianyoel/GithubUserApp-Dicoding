package com.dicoding.abednego.githubuserapp.ui.favorite

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.abednego.githubuserapp.adapter.UserAdapter
import com.dicoding.abednego.githubuserapp.data.local.FavoriteUser
import com.dicoding.abednego.githubuserapp.databinding.ActivityFavoriteBinding
import com.dicoding.abednego.githubuserapp.ui.detail.DetailUserActivity

class FavoriteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoriteBinding
    private lateinit var adapter: UserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Daftar Favorit"

        val layoutManager = LinearLayoutManager(this)
        binding.rvUsers.layoutManager = layoutManager

        adapter = UserAdapter(emptyList(), emptyList(), emptyList(), emptyList())
        binding.rvUsers.adapter = adapter

        val viewModel = ViewModelProvider(this)[FavoriteViewModel::class.java]

        viewModel.getFavoriteUser()?.observe(this) {
            if (it!=null){
                setUserList(it)
            }
        }
    }

    private fun setUserList(user: List<FavoriteUser>) {
        val listName = ArrayList<String>()
        val listAvatar = ArrayList<String>()
        val listHtmlUser = ArrayList<String>()
        val listUserId = ArrayList<Int>()

        for (users in user){
            listName.add(users.login)
            listAvatar.add(users.avatar_url)
            listHtmlUser.add(users.html_url)
            listUserId.add(users.id)
        }

        val adapter = UserAdapter(listName, listAvatar, listHtmlUser, listUserId)
        adapter.setOnItemClickListener(object : UserAdapter.OnItemClickListener {
            override fun onItemClick(username: String, id: Int, avatarUrl:String, htmlUrl:String) {
                val intent = Intent(this@FavoriteActivity, DetailUserActivity::class.java)
                intent.putExtra("username", username)
                intent.putExtra("id", id)
                intent.putExtra("avatarUrl", avatarUrl)
                intent.putExtra("htmlUrl",htmlUrl)
                startActivity(intent)
            }
        })
        binding.rvUsers.adapter = adapter
    }
}