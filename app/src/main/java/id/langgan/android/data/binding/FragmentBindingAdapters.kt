package id.langgan.android.data.binding

import android.graphics.Color
import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import id.langgan.android.R
import javax.inject.Inject

/**
 * Binding adapters that work with a fragment instance.
 */
class FragmentBindingAdapters @Inject constructor(val fragment: Fragment) {
    @BindingAdapter("imageUrl")
    fun bindImage(imageView: ImageView, url: String?) {
        Glide.with(fragment).load(url).into(imageView)
    }

    @BindingAdapter("imageUrlRounded")
    fun bindImageRounded(imageView: ImageView, url: String?) {
        val corner = fragment.activity!!.resources.getDimensionPixelSize(R.dimen.image_corner_radius)
        val option = RequestOptions()
        option.transform(RoundedCorners(corner))
        Glide.with(fragment)
            .load(url)
            .apply(option)
            .into(imageView)
    }

    @BindingAdapter("imageUri")
    fun bindImageFromUri(imageView: ImageView, uri: String?) {
        Glide.with(fragment).load(uri).into(imageView)
    }

    @BindingAdapter("zebraBackground")
    fun bindZebraBackground(view: View, position: Int) {
        if (position % 2 == 0)
            view.setBackgroundColor(Color.WHITE)
        else
            view.setBackgroundColor(Color.parseColor("#F5F5F5"))
    }
}