package com.htech.eh_ho.login

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.htech.eh_ho.R
import com.htech.eh_ho.data.SignUpModel
import com.htech.eh_ho.inflate
import kotlinx.android.synthetic.main.fragment_sign_up.*
import java.lang.IllegalArgumentException

class SignUpFragment : Fragment() {
    var signUpInteractionListener: SignUpInteractionListener?
            = null
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is SignUpInteractionListener)
            signUpInteractionListener = context
        else
            throw IllegalArgumentException("Context doesn't implement ${SignUpInteractionListener::class.java.canonicalName}")
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return container?.inflate(R.layout.fragment_sign_up)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        buttonSignUp.setOnClickListener {
            val model = SignUpModel(
                inputUsername.text.toString(),
                inputEmail.text.toString(),
                inputPassword.text.toString()
            )
            signUpInteractionListener?.onSignUp(model)
        }
        labelReturnSignIn.setOnClickListener {
            signUpInteractionListener?.onGoToSignIn()
        }
    }
    override fun onDetach() {
        super.onDetach()
        this.signUpInteractionListener = null
    }
    interface SignUpInteractionListener {
        fun onGoToSignIn()
        fun onSignUp(signUpModel: SignUpModel)
    }
}
