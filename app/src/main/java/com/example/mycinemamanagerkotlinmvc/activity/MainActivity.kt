package com.example.mycinemamanagerkotlinmvc.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.OnBackPressedCallback
import androidx.viewpager2.widget.ViewPager2
import com.afollestad.materialdialogs.DialogAction
import com.afollestad.materialdialogs.MaterialDialog
import com.example.mycinemamanagerkotlinmvc.R
import com.example.mycinemamanagerkotlinmvc.adapter.MyViewPagerAdapter
import com.example.mycinemamanagerkotlinmvc.databinding.ActivityMainBinding

class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)
        val myViewPagerAdapter = MyViewPagerAdapter(this)

        activityMainBinding.viewpager2.adapter = myViewPagerAdapter
        activityMainBinding.viewpager2.isUserInputEnabled = false
        activityMainBinding.viewpager2.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                when (position) {
                    0 -> {
                        activityMainBinding.bottomNavigation.menu.findItem(R.id.nav_home).isChecked =
                            true
                        activityMainBinding.tvTitle.text = getString(R.string.nav_home)
                    }

                    1 -> {
                        activityMainBinding.bottomNavigation.menu.findItem(R.id.nav_booking).isChecked =
                            true
                        activityMainBinding.tvTitle.text = getString(R.string.nav_booking)
                    }

                    setOnItemSelectedListener    2 -> {
                        activityMainBinding.bottomNavigation.menu.findItem(R.id.nav_user).isChecked =
                            true
                        activityMainBinding.tvTitle.text = getString(R.string.nav_user)
                    }
                }
            }
        })
        activityMainBinding.bottomNavigation.(fun(item: MenuItem): Boolean {
            val id = item.itemId
            when (id) {
                R.id.nav_home -> {
                    activityMainBinding.viewpager2.currentItem = 0
                    activityMainBinding.tvTitle.text = getString(R.string.nav_home)
                }

                R.id.nav_booking -> {
                    activityMainBinding.viewpager2.currentItem = 1
                    activityMainBinding.tvTitle.text = getString(R.string.nav_booking)

                }

                R.id.nav_user -> {
                    activityMainBinding.viewpager2.currentItem = 2
                    activityMainBinding.tvTitle.text = getString(R.string.nav_user)
                }
            }
            return true
        })

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                showDialogLogout()
            }
        })
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
            .onNegative { dialog, _ ->
                dialog.dismiss()
            }
            .cancelable(true)
            .show()
    }


}
