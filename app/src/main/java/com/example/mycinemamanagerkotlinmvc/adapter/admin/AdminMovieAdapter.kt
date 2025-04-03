package com.example.mycinemamanagerkotlinmvc.adapter.admin

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mycinemamanagerkotlinmvc.constant.ConstantKey
import com.example.mycinemamanagerkotlinmvc.databinding.ItemMovieAdminBinding
import com.example.mycinemamanagerkotlinmvc.model.Movie
import com.example.mycinemamanagerkotlinmvc.util.GlideUtils

class AdminMovieAdapter(private var mContext: Context?, private val mListMovies: List<Movie>?,
                        private val iManagerMovieListener: IManagerMovieListener
) : RecyclerView.Adapter<AdminMovieAdapter.MovieViewHolder>() {
    interface IManagerMovieListener {
        fun editMovie(movie: Movie)
        fun deleteMovie(movie: Movie)
        fun clickItemMovie(movie: Movie?)
    }

    fun release() {
        if (mContext != null) {
            mContext = null
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val itemMovieAdminBinding = ItemMovieAdminBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(itemMovieAdminBinding)
    }

    override fun getItemCount(): Int {
        return mListMovies?.size ?: 0
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = mListMovies!![position]
        GlideUtils.loadUrl(movie.image, holder.mItemMovieAdminBinding.imgMovie)
        holder.mItemMovieAdminBinding.tvName.text = movie.name
        holder.mItemMovieAdminBinding.tvCategory.text = movie.categoryName
        holder.mItemMovieAdminBinding.tvDescription.text = movie.description
        val strPrice = movie.price.toString() + ConstantKey.UNIT_CURRENCY_MOVIE
        holder.mItemMovieAdminBinding.tvPrice.text = strPrice
        holder.mItemMovieAdminBinding.tvDate.text = movie.date
        holder.mItemMovieAdminBinding.imgEdit.setOnClickListener { iManagerMovieListener.editMovie(movie) }
        holder.mItemMovieAdminBinding.imgDelete.setOnClickListener { iManagerMovieListener.deleteMovie(movie) }
        holder.mItemMovieAdminBinding.layoutItem.setOnClickListener { iManagerMovieListener.clickItemMovie(movie) }
    }


    class MovieViewHolder(val mItemMovieAdminBinding: ItemMovieAdminBinding) : RecyclerView.ViewHolder(mItemMovieAdminBinding.root)

}
