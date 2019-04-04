package id.langgan.android.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import id.langgan.android.ui.fragment.*

@Suppress("unused")
@Module
abstract class FragmentBuildersModule {
    @ContributesAndroidInjector
    abstract fun contributeHomeFragment(): HomeFragment

    @ContributesAndroidInjector
    abstract fun contributeFavoriteFragment(): FavoriteFragment

    @ContributesAndroidInjector
    abstract fun contributeSubscriptionsFragment(): SubscriptionsFragment

    @ContributesAndroidInjector
    abstract fun contributeProfileFragment(): ProfileFragment

    @ContributesAndroidInjector
    abstract fun contributeProductFragment(): ProductFragment

    @ContributesAndroidInjector
    abstract fun contributeBoxFragment(): BoxFragment

    @ContributesAndroidInjector
    abstract fun contributeNewOrderFragment(): NewOrderFragment

    @ContributesAndroidInjector
    abstract fun contributeSubscriberFragment(): SubscriberFragment
}