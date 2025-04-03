package com.example.mycinemamanagerkotlinmvc.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.mycinemamanagerkotlinmvc.activity.ChangePasswordActivity
import com.example.mycinemamanagerkotlinmvc.activity.SignInActivity
import com.example.mycinemamanagerkotlinmvc.constant.GlobalFunction
import com.example.mycinemamanagerkotlinmvc.databinding.FragmentAccountBinding
import com.example.mycinemamanagerkotlinmvc.prefs.DataStoreManager
import com.google.firebase.auth.FirebaseAuth

class AccountFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragmentAccountBinding  =  FragmentAccountBinding.inflate(inflater,container,false)
        fragmentAccountBinding.tvEmail.text = DataStoreManager.getUser()?.email
        fragmentAccountBinding.layoutSignOut.setOnClickListener{onClickSignOut()}
        fragmentAccountBinding.layoutChangePassword.setOnClickListener { onClickChangePassword() }
        return fragmentAccountBinding.root
    }

    private fun onClickChangePassword() {
        GlobalFunction.startActivity(activity, ChangePasswordActivity::class.java)
    }

    private fun onClickSignOut() {
        if (activity == null){
            return
        }
        FirebaseAuth.getInstance().signOut()
        DataStoreManager.setUser(null)
        GlobalFunction.startActivity(activity, SignInActivity::class.java)
        requireActivity().finishAffinity()
    }

}
