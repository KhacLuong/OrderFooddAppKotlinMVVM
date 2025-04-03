package com.example.mycinemamanagerkotlinmvc.fragment.admin

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mycinemamanagerkotlinmvc.MyApplication
import com.example.mycinemamanagerkotlinmvc.R
import com.example.mycinemamanagerkotlinmvc.activity.admin.AddMovieActivity
import com.example.mycinemamanagerkotlinmvc.adapter.admin.AdminMovieAdapter
import com.example.mycinemamanagerkotlinmvc.constant.ConstantKey
import com.example.mycinemamanagerkotlinmvc.constant.GlobalFunction
import com.example.mycinemamanagerkotlinmvc.constant.GlobalFunction.getTextSearch
import com.example.mycinemamanagerkotlinmvc.constant.GlobalFunction.hideSoftKeyboard
import com.example.mycinemamanagerkotlinmvc.databinding.FragmentAdminHomeBinding
import com.example.mycinemamanagerkotlinmvc.model.Category
import com.example.mycinemamanagerkotlinmvc.model.Movie
import com.example.mycinemamanagerkotlinmvc.util.StringUtil.isEmpty
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.wefika.flowlayout.FlowLayout
import java.util.ArrayList
import java.util.Locale

class AdminHomeFragment : Fragment(), View.OnClickListener {
    private var mFragmentAdminHomeBinding: FragmentAdminHomeBinding? = null
    private var mListMovies: MutableList<Movie>? = null
    private var mAdminMovieAdapter: AdminMovieAdapter? = null
    private var mListCategory: MutableList<Category>? = null
    private var mCategorySelected: Category? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        mFragmentAdminHomeBinding = FragmentAdminHomeBinding.inflate(inflater, container, false)
        initListener()
        getListCategory()
        return mFragmentAdminHomeBinding!!.root
    }

    private fun initListener() {
        mFragmentAdminHomeBinding!!.btnAddMovie.setOnClickListener { onClickAddMovie() }
        mFragmentAdminHomeBinding!!.imgSearch.setOnClickListener { searchMovie() }
        mFragmentAdminHomeBinding!!.edtSearchName.setOnEditorActionListener { _: TextView?, actionId: Int, _: KeyEvent? ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                searchMovie()
                return@setOnEditorActionListener true
            }
            false
        }
        mFragmentAdminHomeBinding!!.edtSearchName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                val strKey = s.toString().trim { it <= ' ' }
                if (strKey == "" || strKey.isEmpty()) {
                    searchMovie()
                }
            }
        })
    }

    private fun onClickAddMovie() {
       GlobalFunction.startActivity(activity, AddMovieActivity::class.java)
    }

    private fun getListCategory() {
        if (activity == null) {
            return
        }
        MyApplication[activity].getCategoryDatabaseReference().addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (mListCategory != null) {
                    mListCategory!!.clear()
                } else {
                    mListCategory = ArrayList()
                }
                for (dataSnapshot in snapshot.children) {
                    val category = dataSnapshot.getValue(Category::class.java)
                    if (category != null) {
                        mListCategory!!.add(0, category)
                    }
                }
                mCategorySelected = Category(0, getString(R.string.label_all), "")
                mListCategory!!.add(0, mCategorySelected!!)
                initLayoutCategory("0")
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private fun initLayoutCategory(tag: String) {
        mFragmentAdminHomeBinding!!.layoutCategory.removeAllViews()
        if (mListCategory != null && mListCategory!!.isNotEmpty()) {
            for (i in mListCategory!!.indices) {
                val category = mListCategory!![i]
                val params = FlowLayout.LayoutParams(
                    FlowLayout.LayoutParams.WRAP_CONTENT,
                    FlowLayout.LayoutParams.WRAP_CONTENT)
                val textView = TextView(activity)
                params.setMargins(0, 10, 20, 10)
                textView.layoutParams = params
                textView.setPadding(30, 10, 30, 10)
                textView.tag = category.id.toString()
                textView.text = category.name
                if (tag == category.id.toString()) {
                    mCategorySelected = category
                    textView.setBackgroundResource(R.drawable.bg_white_shape_round_corner_border_red)
                    textView.setTextColor(resources.getColor(R.color.red))
                    searchMovie()
                } else {
                    textView.setBackgroundResource(R.drawable.bg_white_shape_round_corner_border_grey)
                    textView.setTextColor(resources.getColor(R.color.colorPrimary))
                }
                textView.textSize = resources.getDimension(R.dimen.text_size_xsmall).toInt() /
                        resources.displayMetrics.density
                textView.setOnClickListener(this)
                mFragmentAdminHomeBinding!!.layoutCategory.addView(textView)
            }
        }
    }

    private fun searchMovie() {
        if (activity == null) {
            return
        }
        hideSoftKeyboard(activity)
        MyApplication[activity].getMovieDatabaseReference().addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (mListMovies != null) {
                    mListMovies!!.clear()
                } else {
                    mListMovies = ArrayList()
                }
                for (dataSnapshot in snapshot.children) {
                    val movie = dataSnapshot.getValue(Movie::class.java)!!
                    if (isMovieResult(movie)) {
                        mListMovies!!.add(0, movie)
                    }
                }
                loadListMovie()
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private fun loadListMovie() {
        if (activity == null) {
            return
        }
        val linearLayoutManager = LinearLayoutManager(activity)
        mFragmentAdminHomeBinding!!.rcvMovie.layoutManager = linearLayoutManager
        mAdminMovieAdapter = AdminMovieAdapter(activity, mListMovies, object : AdminMovieAdapter.IManagerMovieListener {
            override fun editMovie(movie: Movie) {
                onClickEditMovie(movie)
            }

            override fun deleteMovie(movie: Movie) {
                deleteMovieItem(movie)
            }

            override fun clickItemMovie(movie: Movie?) {}
        })
        mFragmentAdminHomeBinding!!.rcvMovie.adapter = mAdminMovieAdapter
    }

    private fun deleteMovieItem(movie: Movie) {
        AlertDialog.Builder(activity)
            .setTitle(getString(R.string.msg_delete_title))
            .setMessage(getString(R.string.msg_confirm_delete))
            .setPositiveButton(getString(R.string.action_ok)) { _: DialogInterface?, _: Int ->
                if (activity == null) {
                    return@setPositiveButton
                }
                MyApplication[activity].getMovieDatabaseReference()
                    .child(movie.id.toString()).removeValue { _, _ ->
                        Toast.makeText(activity,
                            getString(R.string.msg_delete_movie_successfully), Toast.LENGTH_SHORT).show()
                    }
            }
            .setNegativeButton(getString(R.string.action_cancel), null)
            .show()
    }

    private fun onClickEditMovie(movie: Movie) {
        val bundle = Bundle()
        bundle.putSerializable(ConstantKey.KEY_INTENT_MOVIE_OBJECT, movie)
        GlobalFunction.startActivity(activity, AddMovieActivity::class.java, bundle)
    }

    private fun isMovieResult(movie: Movie?): Boolean {
        if (movie == null) {
            return false
        }
        val key = mFragmentAdminHomeBinding!!.edtSearchName.text.toString().trim { it <= ' ' }
        var categoryId: Long = 0
        if (mCategorySelected != null) {
            categoryId = mCategorySelected!!.id
        }
        return if (isEmpty(key)) {
            if (categoryId == 0L) {
                true
            } else movie.categoryId == categoryId
        } else {
            val isMatch = getTextSearch(movie.name).toLowerCase(Locale.getDefault()).trim { it <= ' ' }
                .contains(getTextSearch(key).toLowerCase(Locale.getDefault()).trim { it <= ' ' })
            if (categoryId == 0L) {
                isMatch
            } else isMatch && movie.categoryId == categoryId
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        if (mAdminMovieAdapter != null) {
            mAdminMovieAdapter!!.release()
        }
    }
    override fun onClick(v: View?) {
        initLayoutCategory(v?.tag.toString())
    }

}
