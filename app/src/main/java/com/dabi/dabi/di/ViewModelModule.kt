/*
 * Copyright (C) 2018 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.dabi.dabi.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dabi.dabi.viewmodels.FeedListViewModel
import com.dabi.dabi.viewmodels.HomeViewModel

import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Suppress("unused")
@Module
abstract class ViewModelModule {
}

@Suppress("unused")
@Module
abstract class HomeViewModule {

    @Binds
    abstract fun bindViewModelFactory(factory: AppViewModelFactory): ViewModelProvider.Factory
    @HomeScope
    @Binds
    @IntoMap
    @ViewModelKey(HomeViewModel::class)
    abstract fun bindMainViewModel(viewModel: HomeViewModel): ViewModel
    @HomeScope
    @Binds
    @IntoMap
    @ViewModelKey(FeedListViewModel::class)
    abstract fun bindFeedListViewModel(viewModel: FeedListViewModel): ViewModel
}


@Module
abstract class FeedListViewModule {
    @Binds
    abstract fun bindViewModelFactory(factory: AppViewModelFactory): ViewModelProvider.Factory
    @Binds
    @IntoMap
    @ViewModelKey(FeedListViewModel::class)
    abstract fun bindFeedListViewModel(viewModel: FeedListViewModel): ViewModel
}

