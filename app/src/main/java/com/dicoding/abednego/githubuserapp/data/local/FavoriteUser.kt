package com.dicoding.abednego.githubuserapp.data.local

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "favorite_user")
@Parcelize
data class FavoriteUser(
    var login: String,
    @PrimaryKey
    var id: Int,
    var avatar_url: String,
    val html_url: String
): Parcelable