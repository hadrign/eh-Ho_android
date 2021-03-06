package com.htech.eh_ho.topics

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.htech.eh_ho.R
import com.htech.eh_ho.data.Topic
import com.htech.eh_ho.data.TopicsRepo
import com.htech.eh_ho.inflate
import kotlinx.android.synthetic.main.fragment_topics.*
import kotlinx.android.synthetic.main.fragment_topics.viewLoading
import kotlinx.android.synthetic.main.login_main.*
import kotlinx.android.synthetic.main.view_retry.*
import java.lang.IllegalArgumentException

class TopicsFragment : Fragment() {

    var topicsInteractionListener: TopicsInteractionListener? = null

    private val topicsAdapter: TopicsAdapter by lazy {
        val adapter = TopicsAdapter {
            this.topicsInteractionListener?.onShowPosts(it)
        }
        adapter
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is TopicsInteractionListener)
            topicsInteractionListener = context
        else
            throw IllegalArgumentException("Context doesn't implement ${TopicsInteractionListener::class.java.canonicalName}")
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
        return container?.inflate(R.layout.fragment_topics)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        buttonCreate.setOnClickListener {
            this.topicsInteractionListener?.onCreateTopic()
        }

        buttonRetry.setOnClickListener {
            this.loadTopics()
        }

        swipeRefresh.setOnRefreshListener {
            this.loadTopics()
            swipeRefresh.isRefreshing = false
        }

        listTopics.addOnScrollListener(object: RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0 && buttonCreate.isVisible) {
                    buttonCreate.hide()
                } else if (dy < 0 && !buttonCreate.isVisible) {
                    buttonCreate.show()
                }
            }
        })

        topicsAdapter.setTopics(TopicsRepo.topics)

        listTopics.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        listTopics.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        listTopics.adapter = topicsAdapter
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_topics, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onResume() {
        super.onResume()
        loadTopics()

    }

    private fun enableLoading(enabled: Boolean = true) {
        if (enabled) {
            this.listTopics.visibility = View.INVISIBLE
            viewLoading.visibility = View.VISIBLE
        } else {
            this.listTopics.visibility = View.VISIBLE
            viewLoading.visibility = View.INVISIBLE
        }
    }

    private fun enableRetry(enabled: Boolean = true) {
        if (enabled) {
            this.listTopics.visibility = View.INVISIBLE
            viewRetry.visibility = View.VISIBLE
        } else {
            this.listTopics.visibility = View.VISIBLE
            viewRetry.visibility = View.INVISIBLE
        }
    }

    private fun loadTopics() {
        enableLoading()
        enableRetry(false)
        context?.let {
            TopicsRepo.getTopics(it.applicationContext,
                {
                    enableLoading(false)
                    (listTopics.adapter as TopicsAdapter).setTopics(it)
                },
                {
                    enableLoading(false)
                    enableRetry()
                }
            )
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.action_logout -> this.topicsInteractionListener?.onLogout()

        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDetach() {
        super.onDetach()
        topicsInteractionListener = null
    }


    interface TopicsInteractionListener {
        fun onCreateTopic()
        fun onLogout()
        fun onShowPosts(topic: Topic)
    }
}