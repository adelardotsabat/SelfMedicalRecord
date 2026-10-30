package com.example.selfmedicalrecord.ui.profil

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.selfmedicalrecord.R
import com.example.selfmedicalrecord.databinding.ItemFaqBinding

class FaqAdapter(private val onToggle: (Int) -> Unit) :
    ListAdapter<FaqUi, FaqAdapter.ViewHolder>(DiffCallback) {

    inner class ViewHolder(private val binding: ItemFaqBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: FaqUi) {
            val colorRes = if (item.expanded) R.color.blue_primary else R.color.text_primary
            binding.tvQuestion.text = item.question
            binding.tvQuestion.setTextColor(ContextCompat.getColor(binding.root.context, colorRes))
            binding.tvAnswer.text = item.answer
            binding.tvAnswer.isVisible = item.expanded
            binding.ivChevron.rotation = if (item.expanded) 180f else 0f
            binding.faqRoot.setOnClickListener { onToggle(item.id) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(ItemFaqBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(getItem(position))

    private object DiffCallback : DiffUtil.ItemCallback<FaqUi>() {
        override fun areItemsTheSame(old: FaqUi, new: FaqUi) = old.id == new.id
        override fun areContentsTheSame(old: FaqUi, new: FaqUi) = old == new
    }
}
