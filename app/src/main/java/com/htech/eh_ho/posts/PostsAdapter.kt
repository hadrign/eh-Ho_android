package com.htech.eh_ho.posts

import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.htech.eh_ho.R
import com.htech.eh_ho.data.Post
import com.htech.eh_ho.inflate
import com.htech.eh_ho.posts.PostsAdapter
import kotlinx.android.synthetic.main.item_post.view.*
import kotlinx.android.synthetic.main.item_topic.view.*
import java.util.*

class PostsAdapter(val postClickListener: ((Post) -> Unit)? = null) : RecyclerView.Adapter<PostsAdapter.PostHolder>() {
    private val posts = mutableListOf<Post>()
    private val listener: ((View) -> Unit) = {
        val tag: Post = it.tag as Post
        postClickListener?.invoke(tag)
    }

    override fun getItemCount(): Int {
        return posts.size
    }

    override fun onCreateViewHolder(list: ViewGroup, viewType: Int): PostHolder {
        val view = list.inflate(R.layout.item_post)
        return PostHolder(view)
    }

    override fun onBindViewHolder(holder: PostHolder, position: Int) {
        val post = posts[position]
        holder.post = post
        holder.itemView.setOnClickListener(listener)
    }

    fun setPosts(topics: List<Post>) {
        this.posts.clear()
        this.posts.addAll(topics)
        notifyDataSetChanged()
    }


    inner class PostHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var post: Post? = null
            set(value) {
                field = value
                itemView.tag = field

                field?.let {
                    itemView.labelAuthor.text = it.author
                    itemView.labelPostContent.text = it.content
                    setTimeOffset(it.getTimeOffset())
                }
            }

        private fun setTimeOffset(timeOffset: Post.TimeOffset) {
            val quantityString = when (timeOffset.unit) {
                Calendar.YEAR -> R.plurals.years
                Calendar.MONTH -> R.plurals.months
                Calendar.DAY_OF_MONTH -> R.plurals.days
                Calendar.HOUR -> R.plurals.hours
                else -> R.plurals.minutes
            }
            Log.d("Models", "date: " + timeOffset.amount)
            if (timeOffset.amount == 0) {
                itemView.labelPostDate.text =
                    itemView.context.resources.getString(R.string.minutes_zero)
            } else {
                itemView.labelPostDate.text =
                    itemView.context.resources.getQuantityString(
                        quantityString,
                        timeOffset.amount,
                        timeOffset.amount
                    )
            }
        }
    }
}

