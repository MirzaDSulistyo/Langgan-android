package id.langgan.android.data.binding

import android.graphics.Color
import android.text.SpannableString
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.squareup.picasso.Picasso
import java.text.NumberFormat
import java.util.*

/**
 * Data Binding adapters specific to the app.
 */
object BindingAdapters {
    @JvmStatic
    @BindingAdapter("visibleGone")
    fun showHide(view: View, show: Boolean) {
        view.visibility = if (show) View.VISIBLE else View.GONE
    }

    @JvmStatic
    @BindingAdapter("picassoImageUrl")
    fun loadImage(view: ImageView, url: String) {
        Picasso.get().load(url).into(view)
    }

    @JvmStatic
    @BindingAdapter("listBackground")
    fun bindListBackground(view: View, position: Int) {
        if (position % 2 == 0)
            view.setBackgroundColor(Color.WHITE)
        else
            view.setBackgroundColor(Color.parseColor("#F5F5F5"))
    }

    @JvmStatic
    @BindingAdapter("amount")
    fun setCurrencyAndAmount(textView: TextView, amount: Double) {
        val localeID = Locale("in", "ID")
        val format = NumberFormat.getCurrencyInstance(localeID)
        val spannableString = SpannableString(
            format.format(amount))

        //spannableString.setSpan(RelativeSizeSpan(0.6f), 0, 1, 0)

        textView.text = spannableString
    }
}