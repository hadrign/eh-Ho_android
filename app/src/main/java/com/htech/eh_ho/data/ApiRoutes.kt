package com.htech.eh_ho.data

import android.net.Uri
import com.htech.eh_ho.BuildConfig

object ApiRoutes {

    fun signIn(username: String) =
        uriBuilder()
            .appendPath("users")
            .appendPath("${username}.json")
            .build()
            .toString()

    fun SignUp() =
        uriBuilder().appendPath("users").build().toString()

    fun getTopics() =
        uriBuilder().appendPath("latest.json").build().toString()

    fun createTopic() =
        uriBuilder().appendPath("posts.json").build().toString()

    fun getPosts(topicID: String) =
        uriBuilder()
            .appendEncodedPath("t")
            .appendPath("${topicID}.json")
            .build()
            .toString()

    private fun uriBuilder() =
        Uri.Builder()
            .scheme("https")
            .authority(BuildConfig.DiscourseDomain)
}