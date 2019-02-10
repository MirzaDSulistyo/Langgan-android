package id.langgan.android.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import id.langgan.android.ui.activity.LoginActivity
import id.langgan.android.ui.activity.MainActivity

@Suppress("unused")
@Module
abstract class MainActivityModule {
    @ContributesAndroidInjector(modules = [FragmentBuildersModule::class])
    abstract fun contributeMainActivity(): MainActivity

    @ContributesAndroidInjector(modules = [FragmentBuildersModule::class])
    abstract fun contributeLoginActivity(): LoginActivity

}