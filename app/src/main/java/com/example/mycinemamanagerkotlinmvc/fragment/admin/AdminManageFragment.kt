package com.example.mycinemamanagerkotlinmvc.fragment.admin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.mycinemamanagerkotlinmvc.activity.ChangePasswordActivity
import com.example.mycinemamanagerkotlinmvc.activity.SignInActivity
import com.example.mycinemamanagerkotlinmvc.activity.admin.AdminRevenueActivity
import com.example.mycinemamanagerkotlinmvc.constant.GlobalFunction
import com.example.mycinemamanagerkotlinmvc.databinding.FragmentAdminManageBinding
import com.example.mycinemamanagerkotlinmvc.prefs.DataStoreManager
import com.google.firebase.auth.FirebaseAuth

class AdminManageFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val fragmentAdminManageBinding = FragmentAdminManageBinding.inflate(inflater, container, false)
        fragmentAdminManageBinding.tvEmail.text = DataStoreManager.getUser()?.email
        fragmentAdminManageBinding.layoutReport.setOnClickListener { onClickReport() }
        fragmentAdminManageBinding.layoutSignOut.setOnClickListener { onClickSignOut() }
        fragmentAdminManageBinding.layoutChangePassword.setOnClickListener { onClickChangePassword() }
        return fragmentAdminManageBinding.root
    }

    private fun onClickChangePassword() {
        GlobalFunction.startActivity(activity, ChangePasswordActivity::class.java)
    }


    private fun onClickSignOut() {
        if (activity == null) {
            return
        }
        FirebaseAuth.getInstance().signOut()
        DataStoreManager.setUser(null)
        GlobalFunction.startActivity(activity, SignInActivity::class.java)
        requireActivity().finishAffinity()
    }

    private fun onClickReport() {
        GlobalFunction.startActivity(activity, AdminRevenueActivity::class.java)
    }
}
