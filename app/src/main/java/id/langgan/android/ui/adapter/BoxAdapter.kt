package id.langgan.android.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import id.langgan.android.AppExecutors
import id.langgan.android.R
import id.langgan.android.model.Box
import id.langgan.android.ui.common.DataBoundListAdapter
import id.langgan.android.databinding.ContentBoxBinding

class BoxAdapter(
    private val dataBindingComponent: DataBindingComponent,
    appExecutors: AppExecutors,
    private val clickCallback: ((Box) -> Unit?)
) : DataBoundListAdapter<Box, ContentBoxBinding>(
    appExecutors = appExecutors,
    diffCallback = object : DiffUtil.ItemCallback<Box>() {
        override fun areItemsTheSame(oldItem: Box, newItem: Box): Boolean {
            return oldItem.id == newItem.id && oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: Box, newItem: Box): Boolean {
            return oldItem.price == newItem.price && oldItem.description == newItem.description
        }
    }
) {

    override fun createBinding(parent: ViewGroup): ContentBoxBinding {
        val binding = DataBindingUtil.inflate<ContentBoxBinding>(
            LayoutInflater.from(parent.context),
            R.layout.content_box,
            parent,
            false,
            dataBindingComponent
        )

        binding.cvBox.setOnClickListener {
            binding.box?.let {
                clickCallback.invoke(it)
            }
        }

        return binding
    }

    override fun bind(binding: ContentBoxBinding, item: Box, position: Int) {
        binding.box = item
        binding.position = position

    }
}