//package com.example.projecttask.di
//
//import com.example.projecttask.data.AuthDataStoreManager
//import com.example.projecttask.data.api.AuthInterceptor
//import com.example.projecttask.data.AuthInterceptorManager
//import dagger.Module
//import dagger.Provides
//import dagger.hilt.InstallIn
//import dagger.hilt.components.SingletonComponent
//import javax.inject.Singleton
//
//@Module
//@InstallIn(SingletonComponent::class)
//internal object AuthModule {
//    @Provides
//    @Singleton
//    fun provideAuthInterceptorManager(
//        authInterceptor: AuthInterceptor,
//        authDataStoreManager: AuthDataStoreManager
//    ): AuthInterceptorManager = AuthInterceptorManager(authInterceptor, authDataStoreManager)
//}