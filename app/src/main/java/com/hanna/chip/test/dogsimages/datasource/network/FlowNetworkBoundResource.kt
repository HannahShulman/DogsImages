package com.hanna.chip.test.dogsimages.datasource.network

import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.*


/**
 * Based on the logic implemented here: (NetworkBoundResource)
 * https://github.com/android/architecture-components-samples/blob/main/GithubBrowserSample/app/src/main/java/com/android/example/github/repository/NetworkBoundResource.kt
 */
abstract class FlowNetworkBoundResource<ResultType, RequestType> {

    fun asFlow() = flow<Resource<ResultType>> {
        emit(Resource.loading(null))
        loadFromDb().first { dbValue ->
            emit(Resource.loading(dbValue))
            if (shouldFetch()) {
                fetchFromNetwork().take(1).collect { apiResponse ->
                    when (apiResponse) {
                        is ApiSuccessResponse -> {
                            saveNetworkResult(processResponse(apiResponse.body))
                            val data = loadFromDb().first()
                            emit(Resource.success(data))
                        }
                        is ApiErrorResponse -> {
                            val data = loadFromDb().first()
                            emit(Resource.error(apiResponse.errorMessage, data))
                        }
                        else -> { }
                    }
                }

            } else {
                emit(Resource.success(dbValue))
            }
            return@first true
        }
    }

    protected open fun onFetchFailed() {
        // Implement in sub-classes to handle errors
    }

    @WorkerThread
    protected open fun processResponse(response: RequestType) = response

    @WorkerThread
    protected abstract suspend fun saveNetworkResult(item: RequestType)

    @MainThread
    protected abstract fun shouldFetch(): Boolean

    @MainThread
    protected abstract fun loadFromDb(): Flow<ResultType>

    @MainThread
    protected abstract suspend fun fetchFromNetwork(): Flow<ApiResponse<RequestType>>
}
