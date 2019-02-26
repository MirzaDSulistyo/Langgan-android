package id.langgan.android.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import id.langgan.android.R
import id.langgan.android.ui.adapter.TabAdapter

class InboxFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_inbox, container, false)

        val tabLayout = view.findViewById<TabLayout>(R.id.tab_layout)
        val viewPager = view.findViewById<ViewPager>(R.id.view_pager_tabs)
        val adapter = TabAdapter(fragmentManager!!)

        // add fragment to adapter
        adapter.addFragment(ChatInboxFragment(), getString(R.string.inbox))
        adapter.addFragment(NotificationsFragment(), getString(R.string.notifications))

        // setup viewpager
        viewPager.adapter = adapter

        // setup TabLayout
        tabLayout.setupWithViewPager(viewPager)

        return view
    }
}