package com.example.mycinemamanagerkotlinmvc.activity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.mycinemamanagerkotlinmvc.MyApplication
import com.example.mycinemamanagerkotlinmvc.R
import com.example.mycinemamanagerkotlinmvc.constant.ConstantKey
import com.example.mycinemamanagerkotlinmvc.constant.GlobalFunction
import com.example.mycinemamanagerkotlinmvc.databinding.ActivitySignInBinding
import com.example.mycinemamanagerkotlinmvc.model.User
import com.example.mycinemamanagerkotlinmvc.prefs.DataStoreManager
import com.example.mycinemamanagerkotlinmvc.util.StringUtil
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class SignInActivity : BaseActivity() {
    private var mActivitySignInBinding: ActivitySignInBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mActivitySignInBinding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(mActivitySignInBinding!!.root)
        mActivitySignInBinding!!.rdbUser.isChecked = true
        mActivitySignInBinding!!.layoutSignUp.setOnClickListener {
            GlobalFunction.startActivity(this@SignInActivity, SignUpActivity::class.java)
        }
        mActivitySignInBinding!!.btnSignIn.setOnClickListener {
            onClickValidateSignIn()
        }
        mActivitySignInBinding!!.tvForgotPassword.setOnClickListener {
            onClickForgotPassword()
        }
    }

    private fun onClickValidateSignIn() {
        val strEmail = mActivitySignInBinding!!.edtEmail.text.toString().trim() { it <= ' ' }
        val strPassword = mActivitySignInBinding!!.edtPassword.text.toString().trim { it <= ' ' }
        if (StringUtil.isEmpty(strEmail)) {
            Toast.makeText(
                this@SignInActivity,
                getString(R.string.msg_email_require),
                Toast.LENGTH_SHORT
            ).show()

        } else if (StringUtil.isEmpty(strPassword)) {
            Toast.makeText(
                this@SignInActivity,
                getString(R.string.msg_password_require),
                Toast.LENGTH_SHORT
            ).show()
        } else if (!StringUtil.isValidEmail(strEmail)) {
            Toast.makeText(
                this@SignInActivity,
                getString(R.string.msg_email_invalid),
                Toast.LENGTH_SHORT
            ).show()
        } else {
            if (mActivitySignInBinding!!.rdbAdmin.isChecked) {
                checkAdminEmail(strEmail) { isAdmin ->
                    if (isAdmin) {
                        signInUser(strEmail, strPassword, isAdmin)
                    } else {
                        Toast.makeText(
                            this@SignInActivity,
                            getString(R.string.msg_sign_in_error), Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                return
            }else{
                signInUser(strEmail, strPassword, false)
            }

        }
    }

    private fun signInUser(email: String, password: String,isAdmin  : Boolean) {
        showProgressDialog(true)
        val firebaseAuth = FirebaseAuth.getInstance()
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task: Task<AuthResult?> ->
                showProgressDialog(false)
                if (task.isSuccessful) {

                    val user = firebaseAuth.currentUser
                    user?.reload()?.addOnCompleteListener {
                        if (user.isEmailVerified) {
                            val userObject = User(user.email, password)
                            if (user.email != null && isAdmin) {
                                userObject.isAdmin = true
                            }
                            DataStoreManager.setUser(userObject)
                            GlobalFunction.gotoMainActivity(this)
                            finishAffinity()
                        } else {
                            Toast.makeText(
                                this,
                                "⚠️ Email chưa được xác thực. Vui lòng kiểm tra hộp thư!",
                                Toast.LENGTH_LONG
                            ).show()
                            showVerifyEmailDialog(user)
                        }
                    }


//                val user = firebaseAuth.currentUser
//                if (user!=null){
//                    val userObject = User(user.email, password)
//                    if (user.email!=null && user.email!!.contains(ConstantKey.ADMIN_EMAIL_FORMAT)){
//                        userObject.isAdmin = true
//                    }
//                    DataStoreManager.setUser(userObject)
//                    GlobalFunction.gotoMainActivity(this)
//                    finishAffinity()
//                }
                } else {
                    Toast.makeText(
                        this@SignInActivity, getString(R.string.msg_sign_in_error),
                        Toast.LENGTH_SHORT
                    ).show()
                }

            }

    }

    fun showVerifyEmailDialog(user: FirebaseUser) {
        AlertDialog.Builder(this)
            .setTitle("Xác thực Email")
            .setMessage("Bạn chưa xác thực email. Hãy kiểm tra hộp thư và nhấn vào liên kết xác nhận.")
            .setPositiveButton("Gửi lại Email") { _, _ ->
                sendEmailVerification(user)
            }
            .setNegativeButton("OK", null)
            .show()
    }

    fun sendEmailVerification(user: FirebaseUser) {
        user.sendEmailVerification()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(
                        this,
                        "📩 Email xác thực đã được gửi. Hãy kiểm tra hộp thư!",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    Toast.makeText(
                        this,
                        "❌ Gửi email thất bại. Vui lòng thử lại!",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
    }


    fun checkAdminEmail(emailToCheck: String, callback: (Boolean) -> Unit) {
        val adminRef = MyApplication[this].getAdminDatabaseReference();
        val query = adminRef.orderByChild("email").equalTo(emailToCheck)

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    callback(true)
                } else {
                    callback(false)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                println("Lỗi Firebase: ${error.message}")
                callback(false)
            }
        })
    }

    private fun onClickForgotPassword() {
        GlobalFunction.startActivity(this, ForgotPasswordActivity::class.java)
    }

}
