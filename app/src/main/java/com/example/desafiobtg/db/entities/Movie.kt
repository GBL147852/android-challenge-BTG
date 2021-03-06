package com.example.desafiobtg.db.entities

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Entity
data class Movie(@PrimaryKey
                 @ColumnInfo(name = ID) @SerializedName(ID) @Expose var id: String,
                 @ColumnInfo(name = POSTER_URL) @SerializedName(POSTER_URL) @Expose var posterUrl: String?,
                 @ColumnInfo(name = BACKDROP_URL) @SerializedName(BACKDROP_URL) @Expose var backdropUrl: String?,
                 @ColumnInfo(name = TITLE) @SerializedName(TITLE) @Expose var title: String?,
                 @ColumnInfo(name = RELEASE_DATE) @SerializedName(RELEASE_DATE) @Expose var releaseDate: String?,
                 @ColumnInfo(name = OVERVIEW) @SerializedName(OVERVIEW) @Expose var overview: String?,
                 @ColumnInfo(name = RATING) @SerializedName(RATING) @Expose var rating: Float?,
                 @ColumnInfo(name = POPULARITY) @SerializedName(POPULARITY) @Expose var popularity: Float?,
                 @ColumnInfo(name = GENRES) @SerializedName(GENRES) @Expose var genreIds: List<Int>?,
                 @ColumnInfo(name = IS_FAVORITE) var isFavorite: Boolean = false): Serializable {

    companion object {
        const val ID = "id"
        const val POSTER_URL = "poster_path"
        const val BACKDROP_URL = "backdrop_path"
        const val TITLE = "title"
        const val RELEASE_DATE = "release_date"
        const val OVERVIEW = "overview"
        const val RATING = "vote_average"
        const val POPULARITY = "popularity"
        const val GENRES = "genre_ids"
        const val IS_FAVORITE = "isFavorite"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Movie

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}