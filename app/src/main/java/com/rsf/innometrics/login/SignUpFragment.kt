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
import com.rsf.innometrics.data.RegistrationResponse
import com.rsf.innometrics.data.RestClient
import com.rsf.innometrics.data.SessionManager
import com.rsf.innometrics.data.isOkResponseCode
import kotlinx.android.synthetic.main.sign_up_fragment.*
import okhttp3.RequestBody
import org.json.JSONObject
import org.koin.android.ext.android.inject
import retrofit2.Call
import retrofit2.Callback

class SignUpFragment : Fragment() {
    var communicate: LoginCommunacator? = null
    lateinit var restClient: RestClient
    private val sessionManager: SessionManager by inject()

    companion object {
        fun newInstance() = LoginActivity()
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        restClient = RestClient()

        return inflater.inflate(R.layout.sign_up_fragment, container, false)
    }

    override fun onStart() {
        super.onStart()

        signUpButton.setOnClickListener {
            if (password.text.toString() != passwordRepeat.text.toString()) {
                Toast.makeText(
                        activity,
                        "Passwords don't match", Toast.LENGTH_LONG
                ).show()
                return@setOnClickListener
            }

            val credentials =
                    jsonLogin(email.text.toString(), password.text.toString())

            restClient.getApiService(requireActivity().applicationContext).register(credentials)
                    .enqueue(
                            object : Callback<RegistrationResponse> {
                                override fun onFailure(call: Call<RegistrationResponse>, t: Throwable) {
                                    Toast.makeText(
                                            activity,
                                            "Error", Toast.LENGTH_LONG
                                    ).show()
                                }

                                override fun onResponse(
                                        call: Call<RegistrationResponse>,
                                        response: retrofit2.Response<RegistrationResponse>
                                ) {
                                    if (isOkResponseCode(response.code())) {
                                        sessionManager.saveAuthToken(response.body()!!.token)
                                        val intent = Intent(activity, MainActivity::class.java).apply {}
                                        activity?.startActivity(intent)
                                    } else {
                                        Toast.makeText(
                                                activity,
                                                response.message(), Toast.LENGTH_LONG
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
            createJsonRequestBody("email" to email, "password" to password)
}
