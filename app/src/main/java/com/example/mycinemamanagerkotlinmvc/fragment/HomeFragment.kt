package com.example.mycinemamanagerkotlinmvc.fragment

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.example.mycinemamanagerkotlinmvc.MyApplication
import com.example.mycinemamanagerkotlinmvc.activity.SearchActivity
import com.example.mycinemamanagerkotlinmvc.activity.CategoryActivity
import com.example.mycinemamanagerkotlinmvc.activity.MovieDetailActivity
import com.example.mycinemamanagerkotlinmvc.adapter.BannerMovieAdapter
import com.example.mycinemamanagerkotlinmvc.adapter.CategoryAdapter
import com.example.mycinemamanagerkotlinmvc.adapter.MovieAdapter
import com.example.mycinemamanagerkotlinmvc.constant.ConstantKey
import com.example.mycinemamanagerkotlinmvc.constant.GlobalFunction


import com.example.mycinemamanagerkotlinmvc.databinding.FragmentHomeBinding
import com.example.mycinemamanagerkotlinmvc.model.Category
import com.example.mycinemamanagerkotlinmvc.model.Movie
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.Runnable
import java.util.ArrayList
import java.util.Collections


class HomeFragment : Fragment() {

    private var fragmentHomeBinding: FragmentHomeBinding? = null
    private var mListMovies: MutableList<Movie>? = null
    private var mListMoviesBanner: MutableList<Movie>? = null
    private var mListCategory: MutableList<Category>? = null

    private val mHandlerBanner = Handler(Looper.getMainLooper())
    private val mRunnableBanner = Runnable {
        if (mListMoviesBanner == null || mListMoviesBanner!!.isEmpty()) {
            return@Runnable
        }


        val nextItem = fragmentHomeBinding!!.viewPager2.currentItem + 1
        if (nextItem >= mListMoviesBanner!!.size) {
            fragmentHomeBinding!!.viewPager2.setCurrentItem(0, false)
        } else {
            fragmentHomeBinding!!.viewPager2.setCurrentItem(nextItem, true)
        }
//        if (fragmentHomeBinding!!.viewPager2.currentItem == mListMoviesBanner!!.size - 1) {
//            fragmentHomeBinding!!.viewPager2.currentItem = 0
//            return@Runnable
//        }
//        fragmentHomeBinding!!.viewPager2.currentItem =
//            fragmentHomeBinding!!.viewPager2.currentItem + 1
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        fragmentHomeBinding = FragmentHomeBinding.inflate(inflater, container, false)
        getListMovies()
        getListCategory()
        initListener()
        return fragmentHomeBinding!!.root
    }

    private fun initListener() {
        fragmentHomeBinding!!.layoutSearch.setOnClickListener { GlobalFunction.startActivity(activity, SearchActivity::class.java)
        }
    }

    private fun getListCategory() {
        if (activity == null){
            return
        }
        MyApplication[activity].getCategoryDatabaseReference().addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (mListCategory != null) {
                    mListCategory!!.clear()
                } else {
                    mListCategory = ArrayList()
                }
                for (dataSnapshot in snapshot.children) {
                    val category = dataSnapshot.getValue(Category::class.java)
                    if (category != null) {
                        mListCategory!!.add(0, category as Category)
                    }
                }

                displayListCategories()
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }

    private fun displayListCategories() {
        val linearLayoutManager = LinearLayoutManager(activity,
            LinearLayoutManager.HORIZONTAL, false)
        fragmentHomeBinding!!.rcvCategory.layoutManager = linearLayoutManager
        val categoryAdapter = CategoryAdapter(mListCategory, object : CategoryAdapter.IManagerCategoryListener{
            override fun clickItemCategory(category: Category?) {
                val bundle = Bundle()
                bundle.putSerializable(ConstantKey.KEY_INTENT_CATEGORY_OBJECT, category)
                GlobalFunction.startActivity(activity, CategoryActivity::class.java, bundle)
            }
        })
        fragmentHomeBinding!!.rcvCategory.adapter = categoryAdapter

    }

    private fun getListMovies() {
        if (activity == null) return
        MyApplication[activity].getMovieDatabaseReference().addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (mListMovies != null) {
                    mListMovies!!.clear()
                } else {
                    mListMovies = ArrayList()
                }
                for (dataSnapshot in snapshot.children) {
                    val movie = dataSnapshot.getValue(Movie::class.java)
                    if (movie != null) {
                        mListMovies!!.add(0, movie as Movie)
                    }
                }
                displayListBannerMovies()
                displayListAllMovies()
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }

    private fun displayListAllMovies() {
        val gridLayoutManager = GridLayoutManager(activity, 3)
        fragmentHomeBinding!!.rcvMovie.layoutManager = gridLayoutManager

        val movieAdapter = MovieAdapter(mListMovies, object : MovieAdapter.IManagerMovieListener {
            override fun clickItemMovie(movie: Movie?) {
                goToMovieDetail(activity, movie)

            }

        })
        fragmentHomeBinding!!.rcvMovie.adapter = movieAdapter

    }

    private fun displayListBannerMovies() {
        val bannerMovieAdapter = BannerMovieAdapter(getListBannerMovies(), object :
            BannerMovieAdapter.IClickItemListener {
            override fun onClickItem(movie: Movie?) {
                goToMovieDetail(activity,movie)
            }
        })
        fragmentHomeBinding!!.viewPager2.adapter = bannerMovieAdapter
        fragmentHomeBinding!!.indicator3.setViewPager(fragmentHomeBinding!!.viewPager2)
        fragmentHomeBinding!!.viewPager2.registerOnPageChangeCallback(object : OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                mHandlerBanner.removeCallbacks(mRunnableBanner)
                mHandlerBanner.postDelayed(mRunnableBanner, 3000)
            }
        })
    }

    fun goToMovieDetail(context: Context?, movie: Movie?) {
        val bundle = Bundle()
        bundle.putSerializable(ConstantKey.KEY_INTENT_MOVIE_OBJECT, movie)
        GlobalFunction.startActivity(context, MovieDetailActivity::class.java, bundle)
    }

    private fun getListBannerMovies(): List<Movie>? {
        if (mListMoviesBanner != null) {
            mListMoviesBanner!!.clear()
        } else {
            mListMoviesBanner = ArrayList()
        }
        if (mListMovies == null || mListMovies!!.isEmpty()){
            return mListMoviesBanner
        }
        val listClone : List<Movie> = ArrayList(mListMovies)
        Collections.sort(listClone){
                movie1: Movie, movie2: Movie -> movie2.booked - movie1.booked
        }
        for (movie in listClone) {
            if (mListMoviesBanner!!.size < MAX_BANNER_SIZE) {
                mListMoviesBanner!!.add(movie)
            }
        }
        return mListMoviesBanner!!
    }
    companion object {
        private const val MAX_BANNER_SIZE = 4
    }

}
