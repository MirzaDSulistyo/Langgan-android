package id.langgan.android.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import id.langgan.android.ui.activity.BoxActivity
import id.langgan.android.ui.activity.LoginActivity
import id.langgan.android.ui.activity.MainActivity
import id.langgan.android.ui.activity.ProductActivity

@Suppress("unused")
@Module
abstract class MainActivityModule {
    @ContributesAndroidInjector(modules = [FragmentBuildersModule::class])
    abstract fun contributeMainActivity(): MainActivity

    @ContributesAndroidInjector(modules = [FragmentBuildersModule::class])
    abstract fun contributeLoginActivity(): LoginActivity

    @ContributesAndroidInjector(modules = [FragmentBuildersModule::class])
    abstract fun contributeProductActivity(): ProductActivity

    @ContributesAndroidInjector(modules = [FragmentBuildersModule::class])
    abstract fun contributeBoxActivity(): BoxActivity

}