package id.langgan.android.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import id.langgan.android.AppExecutors
import id.langgan.android.R
import id.langgan.android.databinding.ContentPlanBinding
import id.langgan.android.model.Plan
import id.langgan.android.ui.common.DataBoundListAdapter

class PlanAdapter(
    private val dataBindingComponent: DataBindingComponent,
    appExecutors: AppExecutors,
    private val clickCallback: ((Plan) -> Unit?)
) : DataBoundListAdapter<Plan, ContentPlanBinding>(
    appExecutors = appExecutors,
    diffCallback = object : DiffUtil.ItemCallback<Plan>() {
        override fun areItemsTheSame(oldItem: Plan, newItem: Plan): Boolean {
            return oldItem.id == newItem.id && oldItem.userId == newItem.userId
        }

        override fun areContentsTheSame(oldItem: Plan, newItem: Plan): Boolean {
            return oldItem.updatedAt == newItem.updatedAt && oldItem.plan?.size == newItem.plan?.size
        }
    }
) {
    override fun createBinding(parent: ViewGroup): ContentPlanBinding {
        val binding = DataBindingUtil.inflate<ContentPlanBinding>(
            LayoutInflater.from(parent.context),
            R.layout.content_plan,
            parent,
            false,
            dataBindingComponent
        )

        binding.cvPlan.setOnClickListener {
            binding.plan?.let {
                clickCallback.invoke(it)
            }
        }

        return binding
    }

    override fun bind(binding: ContentPlanBinding, item: Plan, position: Int) {
        binding.plan = item
        binding.boxes = item.plan
        binding.position = position
    }
}