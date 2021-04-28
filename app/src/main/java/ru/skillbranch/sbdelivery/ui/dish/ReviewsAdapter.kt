package ru.skillbranch.sbdelivery.ui.dish

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import ru.skillbranch.sbdelivery.R
import ru.skillbranch.sbdelivery.data.dto.ReviewDto
import ru.skillbranch.sbdelivery.databinding.ItemReviewBinding
import ru.skillbranch.sbdelivery.ui.main.adapters.CardItem
import ru.skillbranch.sbdelivery.utils.toDp


open class ReviewsAdapter(
    var list: MutableList<ReviewDto> = mutableListOf(),
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var context: Context

    class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val bindingItem: ItemReviewBinding? = DataBindingUtil.bind(view)
    }

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

            if(list[position].text.isEmpty()) {
                holder.bindingItem?.tvText?.visibility = View.GONE
                holder.bindingItem?.tvUsernameAndDate?.setPadding(
                    holder.bindingItem.tvUsernameAndDate.paddingLeft,
                    holder.bindingItem.tvUsernameAndDate.paddingTop,
                    holder.bindingItem.tvUsernameAndDate.paddingRight,
                    13.toDp(context)
                )
            }

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
