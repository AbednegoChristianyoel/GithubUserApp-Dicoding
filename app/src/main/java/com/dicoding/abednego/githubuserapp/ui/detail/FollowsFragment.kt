package com.dicoding.abednego.githubuserapp.ui.detail

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.abednego.githubuserapp.R
import com.dicoding.abednego.githubuserapp.adapter.UserAdapter
import com.dicoding.abednego.githubuserapp.data.response.ItemsItem
import com.dicoding.abednego.githubuserapp.databinding.FragmentFollowsBinding

class FollowsFragment : Fragment(R.layout.fragment_follows) {

    private var position: Int = 0
    private lateinit var binding: FragmentFollowsBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentFollowsBinding.bind(view)

        binding.rvFollow.layoutManager = LinearLayoutManager(requireActivity())

        val followersViewModel  = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[FollowersViewModel::class.java]

        val followingViewModel  = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[FollowingViewModel::class.java]

        val sharedPreferences = requireActivity().getSharedPreferences("MySharedPreferences", Context.MODE_PRIVATE)
        val username = sharedPreferences.getString("username", "")
        arguments?.let {
            position = it.getInt(ARG_POSITION)
        }
        if (position == 1) {
            username?.let {
                followingViewModel.getFollowing(it)
            }
            followingViewModel.listFollowing.observe(viewLifecycleOwner) { following ->
                setFollowingList(following)
            }
            followingViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
                showLoading(isLoading)
            }
        } else {
            username?.let {
                followersViewModel.getFollowers(it)
            }
            followersViewModel.listFollowers.observe(viewLifecycleOwner) { follower ->
                setFollowerList(follower)
            }
            followersViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
                showLoading(isLoading)
            }
        }
    }

    private fun setFollowingList(following: List<ItemsItem>) {
        val listNama = ArrayList<String>()
        val listAvatar = ArrayList<String>()
        val listHtmlUser = ArrayList<String>()
        val listUserId = ArrayList<Int>()

        for (followings in following){
            listNama.add(followings.login)
            listAvatar.add(followings.avatarUrl)
            listHtmlUser.add(followings.htmlUrl)
            listUserId.add(followings.id)
        }
        val adapter = UserAdapter(listNama, listAvatar,listHtmlUser,listUserId)
        binding.rvFollow.adapter = adapter
    }

    private fun setFollowerList(follower: List<ItemsItem>) {
        val listNama = ArrayList<String>()
        val listAvatar = ArrayList<String>()
        val listHtmlUser = ArrayList<String>()
        val listUserId = ArrayList<Int>()


        for (followers in follower){
            listNama.add(followers.login)
            listAvatar.add(followers.avatarUrl)
            listHtmlUser.add(followers.htmlUrl)
            listUserId.add(followers.id)

        }
        val adapter = UserAdapter(listNama, listAvatar, listHtmlUser, listUserId)
        binding.rvFollow.adapter = adapter
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        const val ARG_POSITION = "position"
        const val ARG_USERNAME = "username"
    }
}