package com.rsf.innometrics.login

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.rsf.innometrics.MainActivity
import com.rsf.innometrics.R
import com.rsf.innometrics.data.SessionManager
import com.rsf.innometrics.data.LoginResponse
import com.rsf.innometrics.data.RestClient
import com.rsf.innometrics.data.isOkResponseCode
import kotlinx.android.synthetic.main.sign_in_fragment.*
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback

class SignInFragment : Fragment() {
    var communicate: LoginCommunacator? = null
    lateinit var restClient: RestClient
    lateinit var sessionManager: SessionManager

    companion object {
        fun newInstance() = LoginActivity()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        restClient = RestClient()
        sessionManager = SessionManager(requireActivity().applicationContext)

        return inflater.inflate(R.layout.sign_in_fragment, container, false)
    }

    override fun onStart() {
        super.onStart()

        signInButton.setOnClickListener {
            val credentials = jsonLogin(login.text.toString(), password.text.toString())
            restClient
                    .getApiService(requireActivity()
                    .applicationContext)
                    .login(credentials)
                    .enqueue(
                    object : Callback<LoginResponse> {
                        override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                            Toast.makeText(
                                activity,
                                "Error", Toast.LENGTH_LONG
                            ).show()
                        }

                        override fun onResponse(
                            call: Call<LoginResponse>,
                            response: retrofit2.Response<LoginResponse>
                        ) {
                            if (isOkResponseCode(response.code())){
                                sessionManager.saveAuthToken(response.body()!!.token)

                                if (isOkResponseCode(response.code())) {
                                    val intent = Intent(activity, MainActivity::class.java).apply {}
                                    intent.putExtra("login",login.text.toString())
                                    activity?.startActivity(intent)
                                }
                            } else {
                                Toast.makeText(
                                    this@SignInFragment.context,
                                    "Wrong login or password",
                                    Toast.LENGTH_LONG
                                ).show()
                            }

                        }
                    }
                )
        }
    }

    private fun createJsonRequestBody(vararg params: Pair<String, String>) =
        RequestBody.create(
            okhttp3.MediaType.parse("application/json; charset=utf-8"),
            JSONObject(mapOf(*params)).toString()
        )


    private fun jsonLogin(email: String, password: String) =
        createJsonRequestBody("email" to email, "password" to password, "projectID" to "")
}
