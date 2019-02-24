package id.langgan.android.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import id.langgan.android.ui.fragment.FavoriteFragment
import id.langgan.android.ui.fragment.HomeFragment
import id.langgan.android.ui.fragment.SubscriptionsFragment

@Suppress("unused")
@Module
abstract class FragmentBuildersModule {
    @ContributesAndroidInjector
    abstract fun contributeHomeFragment(): HomeFragment

    @ContributesAndroidInjector
    abstract fun contributeFavoriteFragment(): FavoriteFragment

    @ContributesAndroidInjector
    abstract fun contributeSubscriptionsFragment(): SubscriptionsFragment
}