package com.rsf.innometrics.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.rsf.innometrics.R
import kotlinx.android.synthetic.main.activity_login.*

interface LoginCommunacator {
    fun loginButtonsHandler(buttonType: String)
}

class LoginActivity : Fragment() {
    var communicate: LoginCommunacator? = null

    companion object {
        fun newInstance() = LoginActivity()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.activity_login, container, false)
    }

    override fun onStart() {
        super.onStart()

        toSignInButton.setOnClickListener {
            communicate?.loginButtonsHandler("signIn")
        }

        toSignUpButton.setOnClickListener {
            communicate?.loginButtonsHandler("signUp")
        }
    }
}
