package com.example.mycinemamanagerkotlinmvc.activity.admin

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.viewpager2.widget.ViewPager2
import com.afollestad.materialdialogs.DialogAction
import com.afollestad.materialdialogs.MaterialDialog
import com.example.cinema.event.ResultQrCodeEvent
import com.example.mycinemamanagerkotlinmvc.R
import com.example.mycinemamanagerkotlinmvc.activity.BaseActivity
import com.example.mycinemamanagerkotlinmvc.adapter.admin.AdminViewPagerAdapter
import com.example.mycinemamanagerkotlinmvc.databinding.ActivityAdminMainBinding

import com.google.zxing.integration.android.IntentIntegrator
import org.greenrobot.eventbus.EventBus

class AdminMainActivity : BaseActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val activityAdminMainBinding = ActivityAdminMainBinding.inflate(layoutInflater)
        setContentView(activityAdminMainBinding.root)

        val adminViewPagerAdapter = AdminViewPagerAdapter(this)
        activityAdminMainBinding.viewpager2.adapter = adminViewPagerAdapter
        activityAdminMainBinding.viewpager2.isUserInputEnabled = false

        activityAdminMainBinding.viewpager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                when (position) {
                    0 -> {
                        activityAdminMainBinding.bottomNavigation.menu.findItem(R.id.nav_admin_category).isChecked = true
                        activityAdminMainBinding.tvTitle.text = getString(R.string.nav_admin_category)
                    }
                    1 -> {
                        activityAdminMainBinding.bottomNavigation.menu.findItem(R.id.nav_admin_food_drink).isChecked = true
                        activityAdminMainBinding.tvTitle.text = getString(R.string.nav_admin_food_drink)
                    }
                    2 -> {
                        activityAdminMainBinding.bottomNavigation.menu.findItem(R.id.nav_admin_movie).isChecked = true
                        activityAdminMainBinding.tvTitle.text = getString(R.string.nav_admin_movie)
                    }
                    3 -> {
                        activityAdminMainBinding.bottomNavigation.menu.findItem(R.id.nav_admin_booking).isChecked = true
                        activityAdminMainBinding.tvTitle.text = getString(R.string.nav_admin_booking)
                    }
                    4 -> {
                        activityAdminMainBinding.bottomNavigation.menu.findItem(R.id.nav_admin_manage).isChecked = true
                        activityAdminMainBinding.tvTitle.text = getString(R.string.nav_admin_manage)
                    }
                }
            }
        })

        activityAdminMainBinding.bottomNavigation.setOnItemSelectedListener { item: MenuItem ->
            when (item.itemId) {
                R.id.nav_admin_category -> {
                    activityAdminMainBinding.viewpager2.currentItem = 0
                    activityAdminMainBinding.tvTitle.text = getString(R.string.nav_admin_category)
                }
                R.id.nav_admin_food_drink -> {
                    activityAdminMainBinding.viewpager2.currentItem = 1
                    activityAdminMainBinding.tvTitle.text = getString(R.string.nav_admin_food_drink)
                }
                R.id.nav_admin_movie -> {
                    activityAdminMainBinding.viewpager2.currentItem = 2
                    activityAdminMainBinding.tvTitle.text = getString(R.string.nav_admin_movie)
                }
                R.id.nav_admin_booking -> {
                    activityAdminMainBinding.viewpager2.currentItem = 3
                    activityAdminMainBinding.tvTitle.text = getString(R.string.nav_admin_booking)
                }
                R.id.nav_admin_manage -> {
                    activityAdminMainBinding.viewpager2.currentItem = 4
                    activityAdminMainBinding.tvTitle.text = getString(R.string.nav_admin_manage)
                }
            }
            true
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (intentResult != null && intentResult.contents != null) {
            EventBus.getDefault().post(ResultQrCodeEvent(intentResult.contents))
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
    private fun showDialogLogout() {
        MaterialDialog.Builder(this)
            .title(getString(R.string.app_name))
            .content(getString(R.string.msg_confirm_login_another_device))
            .positiveText(getString(R.string.action_ok))
            .negativeText(getString(R.string.action_cancel))
            .onPositive { dialog: MaterialDialog, _: DialogAction? ->
                dialog.dismiss()
                finishAffinity()
            }
            .onNegative { dialog: MaterialDialog, _: DialogAction? -> dialog.dismiss() }
            .cancelable(true)
            .show()
    }


    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
        showDialogLogout()
    }

}
