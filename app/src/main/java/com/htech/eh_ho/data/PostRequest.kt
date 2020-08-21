package com.htech.eh_ho.data

import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.htech.eh_ho.BuildConfig
import org.json.JSONObject
import java.lang.reflect.Method
import javax.xml.transform.ErrorListener

class PostRequest(
    method: Int,
    url: String,
    body: JSONObject?,
    listener: (response: JSONObject?) -> Unit,
    errorListener: (errorResponse: VolleyError) -> Unit,
    private val username: String? = null,
    private val useApiKey: Boolean = true
) : JsonObjectRequest(
    method, url, body, listener, errorListener
) {
    override fun getHeaders(): MutableMap<String, String> {
        val headers = mutableMapOf<String, String>()
        headers["Content-Type"] = "application/json"
        headers["Accept"] = "application/json"
        if(useApiKey)
            headers["Api-Key"] = BuildConfig.DiscourseApiKey
        username?.let {
            headers["Api-Username"] = it
        }
        return headers
    }
}