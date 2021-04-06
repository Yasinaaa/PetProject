package ru.skillbranch.sbdelivery.ui.dish

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import ru.skillbranch.sbdelivery.R
import ru.skillbranch.sbdelivery.databinding.ItemReviewBinding
import ru.skillbranch.sbdelivery.utils.toDp


open class ReviewsAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class CustomerReview(val title: String, val text: String, val stars: Float)

    private lateinit var context: Context

    class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val bindingItem: ItemReviewBinding? = DataBindingUtil.bind(view)
    }

    private var list: MutableList<CustomerReview> = mutableListOf(
        CustomerReview("Екатерина, 19.03.20",
            "Великолепная пицца. Мне очень понравилась! Рекомендую всем!",
            3f)
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        context = parent.context
        return ItemViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_review, parent, false)
        )
    }

    @SuppressLint("CheckResult")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ItemViewHolder) {
            holder.bindingItem?.tvUsernameAndDate?.text = list[position].title
            holder.bindingItem?.tvText?.text = list[position].text
            holder.bindingItem?.rating?.numStars = list[position].stars.toInt()
            holder.bindingItem?.rating?.rating = list[position].stars

            val lp: RecyclerView.LayoutParams = holder.bindingItem?.cv?.layoutParams
                    as RecyclerView.LayoutParams

            if (position == list.lastIndex) {
                lp.bottomMargin = 20.toDp(context)
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

}
