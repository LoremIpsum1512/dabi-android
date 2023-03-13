package com.dabi.dabi.di

import javax.inject.Scope

@Scope
@Retention(value = AnnotationRetention.RUNTIME)
annotation class HomeScope

@Scope
@Retention(value = AnnotationRetention.RUNTIME)
annotation class FeedDetailScope