package com.example.desafiobtg.data

import android.arch.lifecycle.LiveData
import android.content.Context
import com.example.desafiobtg.AppController
import com.example.desafiobtg.data.local.LocalDataSource
import com.example.desafiobtg.data.remote.PopularMoviesResponse
import com.example.desafiobtg.data.remote.RemoteDataSource
import com.example.desafiobtg.db.entities.Movie
import com.example.desafiobtg.di.qualifiers.utils.ApplicationContext
import com.example.desafiobtg.manager.AppDataManager
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Repository @Inject constructor(private val remoteDataSource: RemoteDataSource,
                                     private val localDataSource: LocalDataSource,
                                     private val appDataManager: AppDataManager,
                                     @ApplicationContext private val context: Context) {

    fun getMovieList(page: Int, success: (response: PopularMoviesResponse) -> Unit, failure: (error: String?) -> Unit) {
        if (appDataManager.shouldClearLocalMovieData) {
            localDataSource.clearMovieTable()
            getRemotePopularMovieList(page, success, failure)
            return
        }

        AppController.runOnBG {
            localDataSource.getLocalMovieList(page)?.takeIf { it.isNotEmpty() }?.let {
                AppController.runOnMain { success(PopularMoviesResponse(page, it)) }
            } ?: run {
                getRemotePopularMovieList(page, success, failure)
            }
        }
    }

    fun getRemotePopularMovieList(page: Int, success: (response: PopularMoviesResponse) -> Unit, failure: (error: String?) -> Unit) {
        remoteDataSource.getMovieList(page, { response ->
            localDataSource.insertMovies(response.movieList)
            AppController.runOnMain { success(response) }
        }, { error ->
            AppController.runOnMain { failure(error) }
        })
    }

    fun getGenreForIds(genreIds: List<Int>, success: (genreList: List<String>) -> Unit) {
        AppController.runOnBG {
            val genreList = localDataSource.getGenreByIds(genreIds)
            if (genreIds.size == genreList.size) {
                AppController.runOnMain { success(genreList) }
            } else {
                getRemoteGenreList (genreIds) { genreNames ->
                    AppController.runOnMain { success(genreNames) }
                }
            }
        }
    }

    private fun getRemoteGenreList(genreIds: List<Int>, success: (genreList: List<String>) -> Unit) {
        remoteDataSource.getGenreList(success = { genres ->
            localDataSource.addGenres(genres)
            val returnList = ArrayList<String>()
            genres.forEach { genre ->
                if (genreIds.contains(genre.id)) {
                    genre.name?.let { returnList.add(it) }
                }
            }
            success(returnList)
        }, failure = {

        })
    }

    fun addFavoriteMovie(movieId: String) {
        return localDataSource.addFavoriteMovie(movieId)
    }

    fun removeFavoriteMovie(movieId: String) {
        return localDataSource.removeFavoriteMovie(movieId)
    }

    fun getFavoriteMovieList() : LiveData<List<Movie>?> {
        return localDataSource.getFavoriteMovies()
    }

    fun getFavoriteIds() : LiveData<List<String>> {
        return localDataSource.getFavoriteIdsLiveData()
    }

    fun getMovieById(movieId: String?, success: (Movie?) -> Unit) {
        return localDataSource.getMovieById(movieId, success)
    }
}