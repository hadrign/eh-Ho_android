package com.htech.eh_ho.topics

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.htech.eh_ho.*
import com.htech.eh_ho.data.Topic
import com.htech.eh_ho.data.UserRepo
import com.htech.eh_ho.login.LoginActivity

const val TRANSATION_CREATE_TOPIC = "create_topic"
class TopicsActivity: AppCompatActivity(), TopicsFragment.TopicsInteractionListener, CreateTopicsfragment.CreateTopicInteractionListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_topics)

        if (isFirstTimeCreated(savedInstanceState))
            supportFragmentManager.beginTransaction().add(R.id.fragmentContainer, TopicsFragment()).commit()

    }

    private fun goToPost(topic: Topic) {
        val intent = Intent(this, PostActivity::class.java)
        intent.putExtra(EXTRA_TOPIC_ID, topic.id)
        startActivity(intent)
    }

    override fun onCreateTopic() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, CreateTopicsfragment())
            .addToBackStack(TRANSATION_CREATE_TOPIC)
            .commit()
    }

    override fun onLogout() {
        UserRepo.logout(this.applicationContext)
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onShowPosts(topic: Topic) {
        goToPost(topic)
    }

    override fun onTopicCreated() {
        supportFragmentManager.popBackStack()
    }
}