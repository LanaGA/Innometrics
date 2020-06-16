package com.rsf.innometrics.login

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.rsf.innometrics.login.LoginActivity
import com.rsf.innometrics.login.LoginCommunacator
import com.rsf.innometrics.login.SignInFragment
import com.rsf.innometrics.R

class MainLoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_login)

        /*val loginFragment = LoginActivity()
        loginFragment.communicate = this*/

        supportFragmentManager.beginTransaction()
            .add(R.id.fragmentContainer, SignInFragment())
            .addToBackStack(null)
            .commit()
    }

/*
    override fun loginButtonsHandler(buttonType: String) {
        val nextFragment: Fragment?

        if (buttonType == "signIn") {
            nextFragment = SignInFragment()
            nextFragment.communicate = this
        } else {
            nextFragment = SignUpFragment()
            nextFragment.communicate = this
        }

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, nextFragment)
            .addToBackStack(null)
            .commit()
    }*/
}
