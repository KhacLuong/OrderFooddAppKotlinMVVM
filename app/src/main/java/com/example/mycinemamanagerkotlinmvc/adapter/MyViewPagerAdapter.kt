package com.example.mycinemamanagerkotlinmvc.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.mycinemamanagerkotlinmvc.fragment.AccountFragment
import com.example.mycinemamanagerkotlinmvc.fragment.BookingFragment
import com.example.mycinemamanagerkotlinmvc.fragment.HomeFragment

class MyViewPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            1 -> BookingFragment()
            2 -> AccountFragment()
            else -> HomeFragment()
        }
    }

    override fun getItemCount(): Int {
        return 3
    }
}