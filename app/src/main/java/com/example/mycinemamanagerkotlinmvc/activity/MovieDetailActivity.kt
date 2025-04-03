package com.example.mycinemamanagerkotlinmvc.activity

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast

import androidx.appcompat.app.AppCompatActivity
import androidx.media3.common.MediaItem

import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.trackselection.DefaultTrackSelector
import com.example.mycinemamanagerkotlinmvc.MyApplication
import com.example.mycinemamanagerkotlinmvc.R
import com.example.mycinemamanagerkotlinmvc.constant.ConstantKey
import com.example.mycinemamanagerkotlinmvc.util.GlideUtils
import com.example.mycinemamanagerkotlinmvc.constant.GlobalFunction
import com.example.mycinemamanagerkotlinmvc.databinding.ActivityMovieDetailBinding
import com.example.mycinemamanagerkotlinmvc.model.Movie
import com.example.mycinemamanagerkotlinmvc.util.DateTimeUtils
import com.example.mycinemamanagerkotlinmvc.util.StringUtil
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class MovieDetailActivity():AppCompatActivity() {
    private var mActivityMovieDetailBinding: ActivityMovieDetailBinding? = null
    private var mMovie: Movie? = null
    private var mPlayer: ExoPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mActivityMovieDetailBinding = ActivityMovieDetailBinding.inflate(layoutInflater)
        setContentView(mActivityMovieDetailBinding!!.root)
        getDataIntent()
    }

    private fun getDataIntent() {
        val bundle = intent.extras ?: return

        val movie = bundle[ConstantKey.KEY_INTENT_MOVIE_OBJECT] as Movie?
        getMovieInformation(movie!!.id)
    }

    private fun getMovieInformation(movieId: Long) {
        MyApplication[this].getMovieDatabaseReference().child(movieId.toString())
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    mMovie = snapshot.getValue(Movie::class.java)
                    displayDataMovie()
                    initListener()
                }

                override fun onCancelled(error: DatabaseError) {}
            })

    }

    private fun displayDataMovie() {
        if (mMovie == null) {
            return
        }
        GlideUtils.loadUrl(mMovie?.image, mActivityMovieDetailBinding!!.imgMovie)
        mActivityMovieDetailBinding!!.tvTitleMovie.text = mMovie?.name
        mActivityMovieDetailBinding!!.tvCategoryName.text = mMovie?.categoryName
        mActivityMovieDetailBinding!!.tvDateMovie.text = mMovie?.date
        val strPrice = mMovie?.price.toString() + ConstantKey.UNIT_CURRENCY_MOVIE
        mActivityMovieDetailBinding!!.tvPriceMovie.text = strPrice
        mActivityMovieDetailBinding!!.tvDescriptionMovie.text = mMovie?.description
        if (!StringUtil.isEmpty(mMovie?.url)) {
            initExoPlayer()
        }
    }


    @SuppressLint("UnsafeOptInUsageError")
    private fun initExoPlayer() {

        if (mPlayer != null) {
            return
        }
        val trackSelector = DefaultTrackSelector(this)
        mPlayer = ExoPlayer.Builder(this).setTrackSelector(trackSelector).build()
        mActivityMovieDetailBinding!!.playerView.player = mPlayer
        val mediaItem = MediaItem.fromUri(Uri.parse(mMovie?.url))
        mPlayer?.setMediaItem(mediaItem)
        mActivityMovieDetailBinding!!.playerView.hideController()
    }

    private fun initListener() {
        mActivityMovieDetailBinding!!.imgBack.setOnClickListener { onBackPressedDispatcher.onBackPressed() }
        mActivityMovieDetailBinding!!.btnWatchTrailer.setOnClickListener { scrollToLayoutTrailer() }
        mActivityMovieDetailBinding!!.imgPlayMovie.setOnClickListener { startVideo() }
        mActivityMovieDetailBinding!!.btnBooking.setOnClickListener { onClickGoToConfirmBooking() }
    }

    private fun onClickGoToConfirmBooking() {
        if (mMovie == null) {
            return
        }
        if (DateTimeUtils.convertDateToTimeStamp(mMovie?.date) < DateTimeUtils.getLongCurrentTimeStamp()) {
            Toast.makeText(this, getString(R.string.msg_movie_date_invalid), Toast.LENGTH_SHORT).show()
            return
        }
        val bundle = Bundle()
        bundle.putSerializable(ConstantKey.KEY_INTENT_MOVIE_OBJECT, mMovie)
        GlobalFunction.startActivity(this, ConfirmBookingActivity::class.java, bundle)
    }

    private fun startVideo() {
        mActivityMovieDetailBinding!!.imgPlayMovie.visibility = View.GONE
        if (mPlayer != null) {
            // Prepare video source
            mPlayer!!.prepare()
            // Set play video
            mPlayer!!.playWhenReady = true
        }
    }

    private fun scrollToLayoutTrailer() {
        val duration: Long = 500
        Handler(Looper.getMainLooper()).postDelayed({
            val y = mActivityMovieDetailBinding!!.labelMovieTrailer.y
            val sv = mActivityMovieDetailBinding!!.scrollView
            val objectAnimator = ObjectAnimator.ofInt(sv, "scrollY", 0, y.toInt())
            objectAnimator.start()
            startVideo()
        }, duration)
    }

    override fun onPause() {
        super.onPause()
        mPlayer?.playWhenReady = false
        mPlayer?.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mPlayer?.release()
        mPlayer = null
    }

}
