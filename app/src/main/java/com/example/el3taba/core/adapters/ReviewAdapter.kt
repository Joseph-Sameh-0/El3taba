package com.example.el3taba.core.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.el3taba.R
import com.example.el3taba.core.dataClasses.Review
import com.example.el3taba.databinding.ItemReviewBinding
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.Locale

class ReviewAdapter (private val reviews: List<Review>):RecyclerView.Adapter<ReviewAdapter.ReviewItemViewHolder>()
    {

        inner class ReviewItemViewHolder(val binding: ItemReviewBinding) :
            RecyclerView.ViewHolder(binding.root) {
            @SuppressLint("SetTextI18n")
            fun bind(review: Review) {
                binding.reviewerImage.setImageResource(R.drawable.phones_image)
                binding.reviewerName.text = review.reviewerID ////////
                val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                val dateString = formatter.format(review.reviewDate)
                binding.reviewDate.text = dateString
                binding.productRating.rating = review.rating
                binding.reviewText.text = review.reviewText

            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewItemViewHolder {
            val binding =
                ItemReviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return ReviewItemViewHolder(binding)
        }

        override fun onBindViewHolder(holder: ReviewItemViewHolder, position: Int) {
            holder.bind(reviews[position])
        }

        override fun getItemCount(): Int = reviews.size
    }
