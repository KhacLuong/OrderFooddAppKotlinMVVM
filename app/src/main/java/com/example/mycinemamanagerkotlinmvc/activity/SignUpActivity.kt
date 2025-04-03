package com.example.mycinemamanagerkotlinmvc.activity

import android.os.Bundle
import android.widget.Toast
import com.example.mycinemamanagerkotlinmvc.R
import com.example.mycinemamanagerkotlinmvc.constant.ConstantKey
import com.example.mycinemamanagerkotlinmvc.constant.GlobalFunction
import com.example.mycinemamanagerkotlinmvc.databinding.ActivitySignUpBinding
import com.example.mycinemamanagerkotlinmvc.model.User
import com.example.mycinemamanagerkotlinmvc.prefs.DataStoreManager
import com.example.mycinemamanagerkotlinmvc.util.StringUtil
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth

class SignUpActivity : BaseActivity(){
        val firebaseAuth = FirebaseAuth.getInstance()

    private var mActivitySignUpBinding : ActivitySignUpBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mActivitySignUpBinding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(mActivitySignUpBinding!!.root)
        mActivitySignUpBinding!!.rdbUser.isChecked = true
        mActivitySignUpBinding!!.imgBack.setOnClickListener{
            onBackPressedDispatcher.onBackPressed()
        }
        mActivitySignUpBinding!!.layoutSignIn.setOnClickListener{ finish()
        }
        mActivitySignUpBinding!!.btnSignUp.setOnClickListener { onClickValidateSignUp() }
    }

    private fun onClickValidateSignUp() {
        val strEmail = mActivitySignUpBinding!!.edtEmail.text.toString().trim(){it <= ' '}
        val strPassword = mActivitySignUpBinding!!.edtPassword.text.toString().trim { it <= ' ' }
        if (StringUtil.isEmpty(strEmail)){
            Toast.makeText(
                this@SignUpActivity,
                getString(R.string.msg_email_require),
                Toast.LENGTH_SHORT
            ).show()
        }else if (StringUtil.isEmpty(strPassword)) {
            Toast.makeText(
                this@SignUpActivity,
                getString(R.string.msg_password_require),
                Toast.LENGTH_SHORT
            ).show()
        }else if (!StringUtil.isValidEmail(strEmail)) {
            Toast.makeText(
                this@SignUpActivity,
                getString(R.string.msg_email_invalid),
                Toast.LENGTH_SHORT
            ).show()
        } else{
            if (mActivitySignUpBinding!!.rdbAdmin.isChecked){
                if (!strEmail.contains(ConstantKey.ADMIN_EMAIL_FORMAT)) {
                    Toast.makeText(
                        this@SignUpActivity,
                        getString(R.string.msg_email_invalid_admin),
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    signUpUser(strEmail, strPassword)
                }
                return
            }
            if (strEmail.contains(ConstantKey.ADMIN_EMAIL_FORMAT)) {
                Toast.makeText(
                    this@SignUpActivity,
                    getString(R.string.msg_email_invalid_user),
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                signUpUser(strEmail, strPassword)
            }
        }
    }

    private fun signUpUser(email: String, password: String) {
        showProgressDialog(true)
//        val firebaseAuth = FirebaseAuth.getInstance()
        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this){task : Task<AuthResult?> ->
            showProgressDialog(false)
            if (task.isSuccessful){

                sendEmailVerification()
//                val user = firebaseAuth.currentUser
//                if (user!=null){
//                    val userObject = User(user.email, password)
//                    if (user.email!=null && user.email!!.contains(ConstantKey.ADMIN_EMAIL_FORMAT)){
//                        userObject.isAdmin = true
//                    }
//                    DataStoreManager.setUser(userObject )
//                    GlobalFunction.gotoMainActivity(this)
//                    finishAffinity()
//                }
            }else{
                Toast.makeText(
                    this@SignUpActivity, getString(R.string.msg_sign_up_error )+ "  " + task.exception?.message,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun sendEmailVerification() {
        val user = firebaseAuth.currentUser
        user?.sendEmailVerification()
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(
                        this,
                        "📩 Email xác thực đã được gửi. Vui lòng kiểm tra hộp thư!",
                        Toast.LENGTH_LONG
                    ).show()
                    GlobalFunction.startActivity(this, SignInActivity::class.java)
                } else {
                    Toast.makeText(
                        this,
                        "❌ Xác thực không thành công ,${task.exception?.message}",
                        Toast.LENGTH_LONG
                    ).show()

                }
            }
    }

}
