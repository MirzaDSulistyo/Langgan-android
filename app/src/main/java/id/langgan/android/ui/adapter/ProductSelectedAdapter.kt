package id.langgan.android.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import id.langgan.android.AppExecutors
import id.langgan.android.R
import id.langgan.android.databinding.ContentSelectedProductBinding
import id.langgan.android.model.Product
import id.langgan.android.ui.common.DataBoundListAdapter

class ProductSelectedAdapter(
    appExecutors: AppExecutors,
    private val clickCallback: ((Product) -> Unit?)
) : DataBoundListAdapter<Product, ContentSelectedProductBinding>(
    appExecutors = appExecutors,
    diffCallback = object : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.id == newItem.id
        }
    }
)
{
    override fun createBinding(parent: ViewGroup): ContentSelectedProductBinding {
        val binding = DataBindingUtil.inflate<ContentSelectedProductBinding>(
            LayoutInflater.from(parent.context),
            R.layout.content_selected_product,
            parent,
            false
        )

        binding.cvProduct.setOnClickListener {
            binding.product?.let {
                clickCallback.invoke(it)
            }
        }

        return binding
    }

    override fun bind(binding: ContentSelectedProductBinding, item: Product, position: Int) {
        binding.product = item
        binding.position = position
    }
}