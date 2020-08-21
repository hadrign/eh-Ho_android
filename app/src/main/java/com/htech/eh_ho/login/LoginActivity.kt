package com.htech.eh_ho.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.android.material.snackbar.Snackbar
import com.htech.eh_ho.*
import com.htech.eh_ho.data.RequestError
import com.htech.eh_ho.data.SignInModel
import com.htech.eh_ho.data.SignUpModel
import com.htech.eh_ho.data.UserRepo
import com.htech.eh_ho.topics.TopicsActivity
import kotlinx.android.synthetic.main.login_main.*

class LoginActivity : AppCompatActivity(), SignInFragment.SignInInteractionListener, SignUpFragment.SignUpInteractionListener {

    val signUpFragment = SignUpFragment()
    val signInFragment = SignInFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_main)

        if (isFirstTimeCreated(savedInstanceState)) {
            checkSession()

        }

        /*button.setOnClickListener {
            Toast.makeText(it?.context, "Welcome to Eh-Ho ${inputUsername.text}", Toast.LENGTH_LONG).show()
            val  intent: Intent = Intent(this, TopicsActivity::class.java)
            startActivity(intent)
        }*/
    }

    private fun checkSession() {
        if (UserRepo.isLogged(this.applicationContext)) {
            showTopics()
        } else {
            onGoToSignIn()
        }
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    private fun showTopics() {
        val  intent: Intent = Intent(this, TopicsActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onGoToSignUp() {
        supportFragmentManager.beginTransaction().replace(R.id.fragmentContainer, signUpFragment).commit()
    }

    override fun onSignIn(signInModel: SignInModel) {
        //Toggle de vista de carga
        enableLoading()
        //simulateLoading(signInModel)
        UserRepo.signIn(this.applicationContext,
            signInModel,
            { showTopics() },
            { error ->
                enableLoading(false)
                handleError(error)
            }
        )
    }

    private fun handleError(error: RequestError) {
        if (error.messageResId != null) {
            Snackbar.make(container, error.messageResId, Snackbar.LENGTH_LONG).show()
        } else if (error.message != null)
            Snackbar.make(container, error.message, Snackbar.LENGTH_LONG).show()
        else
            Snackbar.make(container, R.string.error_default, Snackbar.LENGTH_LONG).show()
    }


    override fun onGoToSignIn() {
        supportFragmentManager.beginTransaction().replace(R.id.fragmentContainer, signInFragment).commit()
    }

    override fun onSignUp(signUpModel: SignUpModel) {
        enableLoading()
        UserRepo.signUp(this.applicationContext, signUpModel,
            {
                enableLoading(false)
                Snackbar.make(container, R.string.message_sign_up, Snackbar.LENGTH_LONG).show()
            },
            {
                enableLoading(false)
                handleError(it)
            })
    }

    private fun enableLoading(enabled: Boolean = true) {
        if (enabled) {
            fragmentContainer.visibility = View.INVISIBLE
            viewLoading.visibility = View.VISIBLE
        } else {
            fragmentContainer.visibility = View.VISIBLE
            viewLoading.visibility = View.INVISIBLE
        }
    }

    private fun simulateLoading(signInModel: SignInModel) {
        val runnable = Runnable {
            Thread.sleep(3000)
            viewLoading.post {
                showTopics()
            }
        }
        Thread(runnable).start()
       /* val task = object : AsyncTask<Long, Void, Boolean>() {
            override fun doInBackground(vararg time: Long?): Boolean {
                Thread.sleep(time[0] ?: 3000)
                return  true
            }

            override fun onPostExecute(result: Boolean?) {
                super.onPostExecute(result)
                showTopics()
            }
        }
        task.execute(5000)*/
    }


}

/*// 1. Definición de interfaz a partir de una clase
class Listener: View.OnClickListener {
    override fun onClick(view: View?) {
        Toast.makeText(view?.context, "Welcome to Eh-Ho", Toast.LENGTH_LONG).show()
    }
}
// 2. Definición de una clase anónima
val listener: View.OnClickListener = object : View.OnClickListener{
    override fun onClick(view: View?) {
        Toast.makeText(view?.context, "Welcome to Eh-Ho", Toast.LENGTH_LONG).show()
    }
}
// 3. Función anónima
val listenerLambda: (View) -> Unit = { view: View ->
    Toast.makeText(view?.context, "Welcome to Eh-Ho", Toast.LENGTH_LONG).show()
}*/


