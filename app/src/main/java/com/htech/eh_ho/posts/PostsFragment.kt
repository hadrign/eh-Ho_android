package com.htech.eh_ho.posts

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.htech.eh_ho.R
import com.htech.eh_ho.data.GetPostFromTopicModel
import com.htech.eh_ho.data.TopicsRepo
import com.htech.eh_ho.inflate
import com.htech.eh_ho.topics.TopicsAdapter
import kotlinx.android.synthetic.main.fragment_posts.*
import kotlinx.android.synthetic.main.fragment_topics.*
import kotlinx.android.synthetic.main.view_retry.*
import java.lang.IllegalArgumentException


class PostsFragment : Fragment() {

    var postsInteractionListener: PostsInteractionListener? = null
    var topicIDFromActivity: String = ""

    private val postsAdapter: PostsAdapter by lazy {
        val adapter = PostsAdapter {

        }
        adapter
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is PostsInteractionListener)
            postsInteractionListener = context
        else
            throw IllegalArgumentException("Context doesn't implement ${PostsInteractionListener::class.java.canonicalName}")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        this.arguments?.let {
            topicIDFromActivity = it.getString(EXTRA_TOPIC_ID).toString()
        }
        return container?.inflate(R.layout.fragment_posts)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /*buttonCreate.setOnClickListener {
            this.topicsInteractionListener?.onCreateTopic()
        }*/

        postsAdapter.setPosts(TopicsRepo.posts)

        listPosts.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        listPosts.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        listPosts.adapter = postsAdapter
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_create_post, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onResume() {
        super.onResume()
        Log.d("PostsFragment", "******** topicIDFromActivity:  ${topicIDFromActivity}")
        val topicIDModel = GetPostFromTopicModel (
        topicIDFromActivity
        )
        loadPosts(topicIDModel)

    }

    private fun loadPosts(topicIDModel: GetPostFromTopicModel) {
        context?.let {
            TopicsRepo.getPosts(
                it.applicationContext,
                topicIDModel,
                {
                    (listPosts.adapter as PostsAdapter).setPosts(it)
                },
                {
                    // gestionar errores
                }
            )
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            //R.id.action_logout -> this.topicsInteractionListener?.onLogout()

        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDetach() {
        super.onDetach()
        postsInteractionListener = null
    }


    interface PostsInteractionListener {
        fun onCreatePost()
    }

}