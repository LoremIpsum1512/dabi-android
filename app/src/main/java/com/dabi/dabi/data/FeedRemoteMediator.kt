package com.dabi.dabi.data

import android.os.Handler
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.dabi.dabi.data.local.AppDatabase
import com.dabi.dabi.api.FeedService
import com.dabi.dabi.utils.PAGING_LIMIT
import kotlinx.coroutines.delay
import timber.log.Timber

@OptIn(ExperimentalPagingApi::class)
class FeedRemoteMediator(
    private val database: AppDatabase,
    private val feedService: FeedService,
    private val remoteKeyDao: FeedRemoteKeyDao,
    private val feedDao: FeedDao,
) : RemoteMediator<Int, Feed>() {


    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }


    override suspend fun load(loadType: LoadType, state: PagingState<Int, Feed>): MediatorResult {
        Timber.v("load: {$loadType}")
        val offset = when (loadType) {
            LoadType.REFRESH -> 0
            LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
            LoadType.APPEND -> {
                val remoteKey = getLastItemRemoteKey(state)
                val nextKey = remoteKey?.nextOffset
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKey != null)
                nextKey
            }
        }

        val listCount = count(state)
        Timber.v("load: {$loadType} count: {$listCount}")
        try {
            val response = feedService.getPagingFeed(
                offset = offset
            )
            val feeds = response.results.mapIndexed { index, feed ->
                feed.copy(fetchedOrder = index + offset)
            }
            val endReached = response.next == null
            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    remoteKeyDao.clearRemoteKeys()
                    feedDao.clearAll()
                }

                val nextOffset = if (endReached) null else offset + PAGING_LIMIT
                val prevOffset = null // if (offset == 0) null else offset - PAGING_LIMIT
                val keys = feeds.map { feed ->
                    FeedRemoteKey(id = feed.pk, nextOffset = nextOffset, prevOffset = prevOffset)
                }
                feedDao.insertAll(feeds)
                remoteKeyDao.insertAll(keys)
            }
            return MediatorResult.Success(endOfPaginationReached = endReached)
        } catch (e: Exception) {
            Timber.e(e)
            return MediatorResult.Error(e)
        }

    }

//    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, Feed>): FeedRemoteKey? {
//        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
//            ?.let { repo ->
//                // Get the remote keys of the first items retrieved
//                remoteKeyDao.remoteKeyBy(repo.pk)
//            }
//    }

    private suspend fun getLastItemRemoteKey(state: PagingState<Int, Feed>): FeedRemoteKey? {
        return state.pages.lastOrNull {
            it.data.isNotEmpty()
        }?.data?.lastOrNull()?.let { feed -> remoteKeyDao.remoteKeyBy(id = feed.pk) }
    }

    private fun count(state: PagingState<Int, Feed>): Int {
        return state.pages.fold(0) { acc, curr ->
            curr.data.size + acc
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, Feed>): FeedRemoteKey? {
        return state.anchorPosition?.let { positions ->
            state.closestItemToPosition(positions)?.pk?.let { pk ->
                remoteKeyDao.remoteKeyBy(pk)
            }
        }
    }
}