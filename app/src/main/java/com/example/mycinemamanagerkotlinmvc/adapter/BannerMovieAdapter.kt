package com.example.mycinemamanagerkotlinmvc.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mycinemamanagerkotlinmvc.util.GlideUtils
import com.example.mycinemamanagerkotlinmvc.databinding.ItemBannerMovieBinding
import com.example.mycinemamanagerkotlinmvc.model.Movie

class BannerMovieAdapter(private val mListMovies: List<Movie>?,
                         private val iClickItemListener: IClickItemListener) : RecyclerView.Adapter<BannerMovieAdapter.BannerMovieViewHolder>() {
    interface IClickItemListener {
        fun onClickItem(movie: Movie?)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BannerMovieViewHolder {
        val itemBannerMovieBinding = ItemBannerMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BannerMovieViewHolder(itemBannerMovieBinding)
    }

    override fun onBindViewHolder(holder: BannerMovieViewHolder, position: Int) {
        val movie = mListMovies!![position]
        GlideUtils.loadUrlBanner(movie.imageBanner, holder.mItemBannerMovieBinding.imgBanner)
        holder.mItemBannerMovieBinding.tvTitle.text = movie.name
        holder.mItemBannerMovieBinding.tvBooked.text = movie.booked.toString()
        holder.mItemBannerMovieBinding.layoutItem.setOnClickListener { iClickItemListener.onClickItem(movie) }
    }

    override fun getItemCount(): Int {
        return mListMovies?.size ?: 0
    }

    class BannerMovieViewHolder(val mItemBannerMovieBinding: ItemBannerMovieBinding) : RecyclerView.ViewHolder(mItemBannerMovieBinding.root)

}