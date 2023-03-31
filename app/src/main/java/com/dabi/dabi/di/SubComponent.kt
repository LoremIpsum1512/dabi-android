package com.dabi.dabi.di

import com.dabi.dabi.fragments.FeedDetailFragment
import com.dabi.dabi.FeedListFragment
import com.dabi.dabi.fragments.HomeFragment
import com.dabi.dabi.fragments.ModalBottomSheet
import dagger.Subcomponent

@HomeScope
@Subcomponent(modules = [HomeViewModule::class])
interface HomeComponent {
    @Subcomponent.Factory
    interface Factory {
        fun create(): HomeComponent
    }
    fun inject(homeFragment: HomeFragment)
    fun inject(feedListFragment: FeedListFragment)

    fun inject(modalBottomSheet: ModalBottomSheet)
}

@Subcomponent(modules = [FeedListViewModule::class])
interface FeedListComponent {
    @Subcomponent.Factory
    interface Factory {
        fun create(): FeedListComponent
    }
    fun inject(feedListFragment: FeedListFragment)
}



@FeedDetailScope
@Subcomponent()
interface FeedDetailComponent {
    @Subcomponent.Factory
    interface Factory {
        fun create(): FeedDetailComponent
    }

    fun inject(feedDetailFragment: FeedDetailFragment)
}
