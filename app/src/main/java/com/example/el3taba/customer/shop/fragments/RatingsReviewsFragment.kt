package com.example.el3taba.customer.shop.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.el3taba.core.adapters.ReviewAdapter
import com.example.el3taba.core.dataClasses.Review
import com.example.el3taba.databinding.FragmentRatingsReviewsBinding
import com.google.type.Date
import java.text.DecimalFormat
import java.time.LocalDate
import kotlin.math.ceil

class RatingsReviewsFragment : Fragment() {
    private var _binding: FragmentRatingsReviewsBinding? = null
    private val binding get() = _binding!!

    @SuppressLint("SetTextI18n", "NewApi")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRatingsReviewsBinding.inflate(inflater, container, false)

        // Back button logic
        binding.backButton.setOnClickListener {
            findNavController().popBackStack()
        }

        // Write review button logic
        binding.writeReviewButton.setOnClickListener {
            showReviewBottomSheet("")
        }
        val currentDate = LocalDate.now()
        val lst: List<Review> = listOf(
            Review(
                "review1",
                "user1",
                "productA",
                4.5f,
                "Great product!",
                Date.newBuilder()
                    .setYear(currentDate.year)
                    .setMonth(currentDate.monthValue)
                    .setDay(currentDate.dayOfMonth)
                    .build()
            ),
            Review(
                "review2",
                "user2",
                "productB",
                3.0f,
                "Decent but could be better.",
                Date.newBuilder()
                    .setYear(currentDate.year)
                    .setMonth(currentDate.monthValue)
                    .setDay(currentDate.dayOfMonth)
                    .build()

            ),
            Review(
                "review3",
                "user3",
                "productA",
                5.0f,
                "Excellent!",
                Date.newBuilder()
                    .setYear(currentDate.year)
                    .setMonth(currentDate.monthValue)
                    .setDay(currentDate.dayOfMonth)
                    .build()

            ),
            Review(
                "review4",
                "user4",
                "productC",
                2.5f,
                "Not impressed.",
                Date.newBuilder()
                    .setYear(currentDate.year)
                    .setMonth(currentDate.monthValue)
                    .setDay(currentDate.dayOfMonth)
                    .build()

            ),
            Review(
                "review5",
                "user1",
                "productB",
                4.0f,
                "Good value for money.",
                Date.newBuilder()
                    .setYear(currentDate.year)
                    .setMonth(currentDate.monthValue)
                    .setDay(currentDate.dayOfMonth)
                    .build()

            ),
            Review(
                "review6",
                "user3",
                "productC",
                3.5f,
                "Average.",
                Date.newBuilder()
                    .setYear(currentDate.year)
                    .setMonth(currentDate.monthValue)
                    .setDay(currentDate.dayOfMonth)
                    .build()
            ),
            Review(
                "review7",
                "user2",
                "productA",
                4.5f,
                "Highly recommended.\ngggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggg",
                Date.newBuilder()
                    .setYear(currentDate.year)
                    .setMonth(currentDate.monthValue)
                    .setDay(currentDate.dayOfMonth)
                    .build()

            )
        )

        val rateList = mutableMapOf(
            1 to 0,
            2 to 0,
            3 to 0,
            4 to 0,
            5 to 0
        )
        var sumRev = 0.0
        for (rv: Review in lst) {
            when (ceil(rv.rating).toInt()) {
                1 -> rateList[1] = rateList[1]!! + 1
                2 -> rateList[2] = rateList[2]!! + 1
                3 -> rateList[3] = rateList[3]!! + 1
                4 -> rateList[4] = rateList[4]!! + 1
                5 -> rateList[5] = rateList[5]!! + 1
            }
            sumRev += rv.rating
        }
        // Set average rating and total ratings
        binding.averageRating.text = DecimalFormat("#.##").format(sumRev / lst.size).toString()
        binding.totalRatings.text = "${lst.size} ratings"
        binding.fiveStarProgress.progress = (rateList[5]?.times(100))?.div(lst.size) ?: 0
        binding.fiveStarCount.text = "12"
        binding.fourStarProgress.progress = (rateList[4]?.times(100))?.div(lst.size) ?: 0
        binding.fourStarCount.text = "5"
        binding.threeStarProgress.progress = (rateList[3]?.times(100))?.div(lst.size) ?: 0
        binding.threeStarCount.text = "4"
        binding.twoStarProgress.progress = (rateList[2]?.times(100))?.div(lst.size) ?: 0
        binding.twoStarCount.text = "4"
        binding.oneStarProgress.progress = (rateList[1]?.times(100))?.div(lst.size) ?: 0
        binding.oneStarCount.text = "4"
        binding.reviewsTitle.text = "${lst.size} reviews"
        binding.reviewsRecyclerView.adapter = ReviewAdapter(lst)
        binding.reviewsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.reviewsRecyclerView.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                LinearLayoutManager.VERTICAL
            )
        )
        return binding.root
    }

    private fun showReviewBottomSheet(productID: String) {
        val bottomSheetFragment = ReviewBottomSheetFragment.newInstance(productID)
        bottomSheetFragment.show(childFragmentManager, "ReviewBottomSheet")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
