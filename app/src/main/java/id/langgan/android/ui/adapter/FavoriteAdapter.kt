package id.langgan.android.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import id.langgan.android.AppExecutors
import id.langgan.android.R
import id.langgan.android.model.Favorite
import id.langgan.android.ui.common.DataBoundListAdapter
import id.langgan.android.databinding.ContentFavoriteBinding

class FavoriteAdapter(
    private val dataBindingComponent: DataBindingComponent,
    appExecutors: AppExecutors,
    private val clickCallback: ((Favorite) -> Unit?)
) : DataBoundListAdapter<Favorite, ContentFavoriteBinding>(
    appExecutors = appExecutors,
    diffCallback = object : DiffUtil.ItemCallback<Favorite>() {
        override fun areItemsTheSame(oldItem: Favorite, newItem: Favorite): Boolean {
            return oldItem.id == newItem.id && oldItem.userId == newItem.userId
        }

        override fun areContentsTheSame(oldItem: Favorite, newItem: Favorite): Boolean {
            return oldItem.product?.id == newItem.product?.id && oldItem.product?.name == newItem.product?.name
        }
    }
) {

    override fun createBinding(parent: ViewGroup): ContentFavoriteBinding {
        val binding = DataBindingUtil.inflate<ContentFavoriteBinding>(
            LayoutInflater.from(parent.context),
            R.layout.content_favorite,
            parent,
            false,
            dataBindingComponent
        )

        binding.cvFavorite.setOnClickListener {
            binding.favorite?.let {
                clickCallback.invoke(it)
            }
        }

        return binding
    }

    override fun bind(binding: ContentFavoriteBinding, item: Favorite, position: Int) {
        binding.favorite = item
        binding.position = position
    }
}