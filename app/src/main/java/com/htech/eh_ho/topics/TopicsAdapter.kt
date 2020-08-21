package com.htech.eh_ho.topics

import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.htech.eh_ho.R
import com.htech.eh_ho.data.Topic
import com.htech.eh_ho.inflate
import kotlinx.android.synthetic.main.item_topic.view.*
import java.util.*

class TopicsAdapter(val topicClickListener: ((Topic) -> Unit)? = null) : RecyclerView.Adapter<TopicsAdapter.TopicHolder>(){
    private val topics = mutableListOf<Topic>()
    private val listener : ((View) -> Unit) = {
        val tag: Topic = it.tag as Topic
        topicClickListener?.invoke(tag)
    }

    override fun getItemCount(): Int {
        return topics.size
    }

    override fun onCreateViewHolder(list: ViewGroup, viewType: Int): TopicHolder {
        val view = list.inflate(R.layout.item_topic)
        return TopicHolder(view)
    }

    override fun onBindViewHolder(holder: TopicHolder, position: Int) {
        val topic = topics[position]
        holder.topic = topic
        holder.itemView.setOnClickListener(listener)
    }

    fun setTopics(topics: List<Topic>) {
        this.topics.clear()
        this.topics.addAll(topics)
        notifyDataSetChanged()
    }



    inner class TopicHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var topic: Topic? = null
        set(value) {
            field = value
            itemView.tag = field

            field?.let {
                itemView.labelTitle.text = it.title
                itemView.labelPosts.text = it.posts.toString()
                itemView.labelViews.text = it.views.toString()
                setTimeOffset(it.getTimeOffset())
            }
        }

        private fun setTimeOffset(timeOffset: Topic.TimeOffset) {
            val quantityString =  when(timeOffset.unit) {
                Calendar.YEAR -> R.plurals.years
                    Calendar.MONTH -> R.plurals.months
                Calendar.DAY_OF_MONTH -> R.plurals.days
                Calendar.HOUR -> R.plurals.hours
                else -> R.plurals.minutes
            }
            Log.d("Models", "date: " + timeOffset.amount)
            if (timeOffset.amount == 0) {
                itemView.labelDate.text = itemView.context.resources.getString(R.string.minutes_zero)
            } else {
                itemView.labelDate.text =
                    itemView.context.resources.getQuantityString(quantityString, timeOffset.amount, timeOffset.amount)
            }
        }
    }

}