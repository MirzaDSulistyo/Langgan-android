package id.langgan.android.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import id.langgan.android.AppExecutors
import id.langgan.android.R
import id.langgan.android.databinding.ContentNewOrderBinding
import id.langgan.android.model.Subs
import id.langgan.android.ui.common.DataBoundListAdapter

class SubsAdapter(
    private val dataBindingComponent: DataBindingComponent,
    appExecutors: AppExecutors,
    private val clickCallback: ((Subs) -> Unit?)
) : DataBoundListAdapter<Subs, ContentNewOrderBinding>(
    appExecutors = appExecutors,
    diffCallback = object : DiffUtil.ItemCallback<Subs>() {
        override fun areItemsTheSame(oldItem: Subs, newItem: Subs): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Subs, newItem: Subs): Boolean {
            return oldItem.address == newItem.address && oldItem.city== newItem.city
        }
    }
) {

    override fun createBinding(parent: ViewGroup): ContentNewOrderBinding {
        val binding = DataBindingUtil.inflate<ContentNewOrderBinding>(
            LayoutInflater.from(parent.context),
            R.layout.content_new_order,
            parent,
            false,
            dataBindingComponent
        )

        binding.cvOrder.setOnClickListener {
            binding.data?.let {
                clickCallback.invoke(it)
            }
        }

        return binding
    }

    override fun bind(binding: ContentNewOrderBinding, item: Subs, position: Int) {
        binding.data = item
        binding.position = position

    }
}