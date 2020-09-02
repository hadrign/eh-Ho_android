package com.htech.eh_ho.posts

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.htech.eh_ho.LoadingDialogFragment
import com.htech.eh_ho.R
import com.htech.eh_ho.data.*
import com.htech.eh_ho.inflate
import com.htech.eh_ho.topics.CreateTopicsfragment
import com.htech.eh_ho.topics.TAG_LOADING_DIALOG
import kotlinx.android.synthetic.main.fragment_create_post.*
import kotlinx.android.synthetic.main.fragment_create_topic.*
import kotlinx.android.synthetic.main.fragment_create_topic.container
import kotlinx.android.synthetic.main.fragment_create_topic.inputContent
import java.lang.IllegalArgumentException

class CreatePostFragment: Fragment() {
    var interactionListener: CreatePostInteractionListener? = null
    var topicIDFromActivity: String = ""
    /*val loadingDialogFragment: LoadingDialogFragment by lazy {
        val message = getString(R.string.label_creating_topic)
        LoadingDialogFragment.newInstance(message)
    }*/

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is CreatePostInteractionListener)
            this.interactionListener = context
        else
            throw IllegalArgumentException("Context doesn't implement ${CreatePostFragment.CreatePostInteractionListener::class.java.canonicalName}")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        this.arguments?.let {
            topicIDFromActivity = it.getString(EXTRA_TOPIC_ID).toString()
        }
        return container?.inflate(R.layout.fragment_create_post)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        context?.let {
            var introText = getText(R.string.topic_id).toString()
            labelTopicID.text = "${introText} ${topicIDFromActivity}"
        }

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_create_topic, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_send -> createPost()
        }

        return super.onOptionsItemSelected(item)
    }

    private fun createPost() {
        if (isFormValid()) {
            sendPost()
        } else {
            showErrors()
        }
    }

    private fun sendPost() {
        val model = CreatePostModel(
            topicIDFromActivity,
            inputContentPost.text.toString()
        )
        context?.let {
            TopicsRepo.addPost(
                it.applicationContext,
                model,
                {
                    interactionListener?.onPostCreated()
                },
                {
                    handleError(it)
                }
            )
        }
    }

    private fun handleError(error: RequestError) {
        val message =
            if (error.messageResId != null)
                getString(error.messageResId)
            else error.message ?: getString(R.string.error_default)

        Snackbar.make(container, message, Snackbar.LENGTH_LONG).show()
    }

    private fun showErrors() {
        if (inputContentPost.text.isEmpty())
            inputContentPost.error = getString(R.string.error_empty)
    }

    private fun isFormValid() = inputContentPost.text.isNotEmpty()

    interface CreatePostInteractionListener {
        fun onPostCreated()
    }
}