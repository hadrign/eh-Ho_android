package com.htech.eh_ho.data

import android.content.Context
import com.android.volley.NetworkError
import com.android.volley.Request
import com.android.volley.ServerError
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.htech.eh_ho.BuildConfig
import com.htech.eh_ho.R

const val PREFERENCES_SESSION = "session"
const val PREFERENCES_USERNAME = "username"

object UserRepo {

    fun signIn(context: Context,
               signInModel: SignInModel,
               success: (SignInModel) -> Unit,
               error: (RequestError) -> Unit) {

        val request = JsonObjectRequest(
            Request.Method.GET,
            ApiRoutes.signIn(signInModel.username),
            null,
            {response ->
                success(signInModel)
                saveSession(
                    context,
                    signInModel.username
                )
            },
            {e ->
                e.printStackTrace()
                val errorObject = if (e is ServerError && e.networkResponse.statusCode == 404) {
                    RequestError(e, messageResId = R.string.error_not_reg)
                } else if (e is NetworkError) { RequestError(e, messageResId = R.string.error_not_internet)
                } else {
                    RequestError(e)
                }
                error(errorObject)
            }
        )

        ApiRequestQueue.getRequestQueue(context).add(request)
    }

    fun signUp(
        context: Context,
        signUpModel: SignUpModel,
        success: (SignUpModel) -> Unit,
        error: (RequestError) -> Unit
    ) {
        val request = PostRequest(
            Request.Method.POST,
            ApiRoutes.SignUp(),
            signUpModel.toJson(),
            {response ->
                val successStatus = response?.getBoolean("success") ?: false
                if (successStatus) {
                    success(signUpModel)
                } else {
                    error(RequestError(message = response?.getString("message")))
                }
            },
            {e ->
                e.printStackTrace()
                val requestError =
                    if (e is NetworkError)
                        RequestError(e, messageResId = R.string.error_not_internet)
                else
                        RequestError(e)
                error(requestError)
            }
        )

        ApiRequestQueue.getRequestQueue(context).add(request)
    }

    private fun saveSession(context: Context, username: String) {
        val preferences = context.getSharedPreferences(PREFERENCES_SESSION, Context.MODE_PRIVATE)
        preferences.edit().putString(PREFERENCES_USERNAME, username).apply()
    }

    fun getUsername(context: Context): String? {
        val preferences = context.getSharedPreferences(PREFERENCES_SESSION, Context.MODE_PRIVATE)
        return preferences.getString(PREFERENCES_USERNAME, null)
    }

    fun isLogged(context: Context): Boolean {
        val preferences = context.getSharedPreferences(PREFERENCES_SESSION, Context.MODE_PRIVATE)
        val username = preferences.getString(PREFERENCES_USERNAME, null)
        return  username != null
    }

    fun logout(context: Context) {
        val preferences = context.getSharedPreferences(PREFERENCES_SESSION, Context.MODE_PRIVATE)
        preferences.edit().putString(PREFERENCES_USERNAME, null).apply()
    }
}