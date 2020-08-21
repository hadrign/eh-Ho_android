package com.htech.eh_ho

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.htech.eh_ho.data.Topic
import com.htech.eh_ho.data.TopicsRepo
import kotlinx.android.synthetic.main.activity_post.*
const val EXTRA_TOPIC_ID = "TOPIC_ID"

class PostActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post)


        val topicId = intent.getStringExtra(EXTRA_TOPIC_ID) ?: ""
        val topic: Topic? = TopicsRepo.getTopic(topicId)
        topic?.let {
            lableTitle.text = it.title
        }

    }
}