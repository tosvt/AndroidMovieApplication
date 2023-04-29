package com.example.mymovieapplication.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.Coil
import coil.load
import coil.size.Scale
import com.example.mymovieapplication.R
import com.example.mymovieapplication.databinding.ActivityItemsBinding
import com.example.mymovieapplication.db.MoviesEntity
import javax.inject.Inject

class MoviesAdapter @Inject constructor() : RecyclerView.Adapter<MoviesAdapter.ViewHolder>(){

    private lateinit var binding: ActivityItemsBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = ActivityItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder()
    }

    override fun getItemCount(): Int = differ.currentList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setData(differ.currentList[position])
    }

    inner class ViewHolder : RecyclerView.ViewHolder(binding.root){
        fun setData(item : MoviesEntity){
            binding.apply {
                tvMovieName.text = item.title
                tvRate.text = item.raiting
                tvLang.text = item.country
                tvMovieDateRelease.text = item.yearofissue
                val moviePosterURL = item.poster


                ImgMovie.load(moviePosterURL) {
                    crossfade(true)
                    placeholder(R.drawable.poster_placeholder)
                    //scale(Scale.FILL)
                    scale(Scale.FILL)
                    // xml android:scaleType="fitXY"
                    // xml android:scaleType="centerCrop"

                }

            }
        }
    }

    private val differCallback = object : DiffUtil.ItemCallback<MoviesEntity>(){
        override fun areItemsTheSame(oldItem: MoviesEntity, newItem: MoviesEntity): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: MoviesEntity, newItem: MoviesEntity): Boolean {
            return oldItem == newItem
        }

    }

    val differ = AsyncListDiffer(this, differCallback)
}