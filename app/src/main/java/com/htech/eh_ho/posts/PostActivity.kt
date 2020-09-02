package com.htech.eh_ho.posts

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.htech.eh_ho.R
import com.htech.eh_ho.data.Topic
import com.htech.eh_ho.data.TopicsRepo
import com.htech.eh_ho.isFirstTimeCreated
import com.htech.eh_ho.topics.CreateTopicsfragment
import com.htech.eh_ho.topics.TRANSATION_CREATE_TOPIC
import com.htech.eh_ho.topics.TopicsFragment
import kotlinx.android.synthetic.main.activity_post.*
const val TRANSATION_CREATE_POST = "create_post"
const val EXTRA_TOPIC_ID = "TOPIC_ID"

class PostActivity : AppCompatActivity(), PostsFragment.PostsInteractionListener, CreatePostFragment.CreatePostInteractionListener {
    var topicID: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post)

        if (isFirstTimeCreated(savedInstanceState)) {
            var bundle = Bundle()
            topicID = intent.getStringExtra(EXTRA_TOPIC_ID)
            bundle.putString(EXTRA_TOPIC_ID, topicID)
            var postsFragment = PostsFragment()
            postsFragment.arguments = bundle
            supportFragmentManager.beginTransaction().add(R.id.fragmentContainer, postsFragment).commit()
        }

    }

    override fun onCreatePost() {
        var bundle = Bundle()
        bundle.putString(EXTRA_TOPIC_ID, topicID)
        var createPostFragment = CreatePostFragment()
        createPostFragment.arguments = bundle
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, createPostFragment)
            .addToBackStack(TRANSATION_CREATE_TOPIC)
            .commit()
    }

    override fun onPostCreated() {
        supportFragmentManager.popBackStack()
    }
}