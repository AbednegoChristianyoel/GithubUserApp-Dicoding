package com.dicoding.abednego.githubuserapp.ui.main

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.abednego.githubuserapp.R
import com.dicoding.abednego.githubuserapp.adapter.UserAdapter
import com.dicoding.abednego.githubuserapp.data.response.ItemsItem
import com.dicoding.abednego.githubuserapp.databinding.ActivityMainBinding
import com.dicoding.abednego.githubuserapp.ui.detail.DetailUserActivity
import com.dicoding.abednego.githubuserapp.ui.favorite.FavoriteActivity
import com.dicoding.abednego.githubuserapp.ui.settings.SettingActivity
import com.dicoding.abednego.githubuserapp.ui.settings.SettingPreferences
import com.dicoding.abednego.githubuserapp.ui.settings.SettingViewModel
import com.dicoding.abednego.githubuserapp.ui.settings.ViewModelFactory

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val layoutManager = LinearLayoutManager(this)
        binding.rvUsers.layoutManager = layoutManager

        val mainViewModel  = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[MainViewModel::class.java]

        mainViewModel.listUsers.observe(this) { user ->
            setUserList(user)
        }

        mainViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        val pref = SettingPreferences.getInstance(dataStore)
        val settingViewModel = ViewModelProvider(this, ViewModelFactory(pref))[SettingViewModel::class.java]

        settingViewModel.getThemeSettings().observe(this) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.search_menu, menu)

        val favoriteMenuItem = menu.findItem(R.id.favorite)
        favoriteMenuItem.setOnMenuItemClickListener {
                val intent = Intent(this, FavoriteActivity::class.java)
                startActivity(intent)
            true
        }

        val settingsMenuItem = menu.findItem(R.id.settings)
        settingsMenuItem.setOnMenuItemClickListener {
            val intent = Intent(this, SettingActivity::class.java)
            startActivity(intent)
            true
        }

        val mainViewModel  = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[MainViewModel::class.java]
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.search).actionView as SearchView

        searchView.setIconifiedByDefault(false)
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.search_hint)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                if(!query.isEmpty()){
                    mainViewModel.findUsers(query)
                }
                return true
            }
            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })
        return true
    }

    private fun setUserList(user: List<ItemsItem>) {
        val listName = ArrayList<String>()
        val listAvatar = ArrayList<String>()
        val listHtmlUser = ArrayList<String>()
        val listUserId = ArrayList<Int>()

        for (users in user){
            listName.add(users.login)
            listAvatar.add(users.avatarUrl)
            listHtmlUser.add(users.htmlUrl)
            listUserId.add(users.id)
        }

        val adapter = UserAdapter(listName, listAvatar, listHtmlUser, listUserId)
        adapter.setOnItemClickListener(object : UserAdapter.OnItemClickListener {
            override fun onItemClick(username: String, id: Int, avatarUrl:String, htmlUrl:String) {
                val intent = Intent(this@MainActivity, DetailUserActivity::class.java)
                intent.putExtra("username", username)
                intent.putExtra("id", id)
                intent.putExtra("avatarUrl", avatarUrl)
                intent.putExtra("htmlUrl",htmlUrl)
                startActivity(intent)
            }
        })
        binding.rvUsers.adapter = adapter
    }


    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}