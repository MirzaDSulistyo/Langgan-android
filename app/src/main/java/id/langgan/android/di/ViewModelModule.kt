package id.langgan.android.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import id.langgan.android.viewmodel.*

@Suppress("unused")
@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(UserViewModel::class)
    abstract fun bindDataUserViewModel(user: UserViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ProductViewModel::class)
    abstract fun bindDataProductViewModel(product: ProductViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(FavoriteViewModel::class)
    abstract fun bindDataFavoriteViewModel(favorite: FavoriteViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(PlanViewModel::class)
    abstract fun bindDataPlanViewModel(plan: PlanViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(BoxViewModel::class)
    abstract fun bindDataBoxViewModel(plan: BoxViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SubsViewModel::class)
    abstract fun bindDataSubsViewModel(model: SubsViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

}