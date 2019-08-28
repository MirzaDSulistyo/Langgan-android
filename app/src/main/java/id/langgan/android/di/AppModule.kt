package id.langgan.android.di

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import id.langgan.android.data.api.ApiService
import id.langgan.android.data.database.AppDb
import id.langgan.android.data.database.dao.*
import id.langgan.android.utility.LiveDataCallAdapterFactory
import id.langgan.android.utility.Vars
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module(includes = [ViewModelModule::class])
class AppModule {

    /********
     * URLS
     */
    private val LOCAL = Vars.getLocale()

    @Singleton
    @Provides
    fun provideApiService(): ApiService {
        return Retrofit.Builder()
            //.baseUrl("https://tranquil-shore-53254.herokuapp.com/api/")
            .baseUrl("http://192.168.43.216:3000/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(LiveDataCallAdapterFactory())
            .build()
            .create(ApiService::class.java)
    }

    @Singleton
    @Provides
    fun provideDb(app: Application): AppDb {
        return Room
            .databaseBuilder(app, AppDb::class.java, "langgan.db")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideUserDao(db: AppDb): UserDao {
        return db.userDao()
    }

    @Singleton
    @Provides
    fun provideProductDao(db: AppDb): ProductDao {
        return db.productDao()
    }

    @Singleton
    @Provides
    fun provideFavoriteDao(db: AppDb): FavoriteDao {
        return db.favoriteDao()
    }

    @Singleton
    @Provides
    fun provideBoxDao(db: AppDb): BoxDao {
        return db.boxDao()
    }

    @Singleton
    @Provides
    fun providePlanDao(db: AppDb): PlanDao {
        return db.planDao()
    }

    @Singleton
    @Provides
    fun provideSubsDao(db: AppDb): SubsDao {
        return db.subsDao()
    }

}
