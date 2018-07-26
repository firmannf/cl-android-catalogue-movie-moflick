package com.firmannf.moflick.data;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

import static android.provider.BaseColumns._ID;
import static com.firmannf.moflick.data.source.local.DatabaseContract.DictionaryColumns.COLUMN_OVERVIEW;
import static com.firmannf.moflick.data.source.local.DatabaseContract.DictionaryColumns.COLUMN_POSTER_PATH;
import static com.firmannf.moflick.data.source.local.DatabaseContract.DictionaryColumns.COLUMN_RELEASE_DATE;
import static com.firmannf.moflick.data.source.local.DatabaseContract.DictionaryColumns.COLUMN_TITLE;
import static com.firmannf.moflick.data.source.local.DatabaseContract.DictionaryColumns.COLUMN_VOTE_AVERAGE;
import static com.firmannf.moflick.data.source.local.DatabaseContract.getColumnFloat;
import static com.firmannf.moflick.data.source.local.DatabaseContract.getColumnInt;
import static com.firmannf.moflick.data.source.local.DatabaseContract.getColumnString;

/**
 * Created by codelabs on 16/07/18.
 */

public class MovieModel implements Parcelable {
    private int id;
    private String title;
    @SerializedName("vote_average")
    private float voteAverage;
    @SerializedName("poster_path")
    private String posterPath;
    private String overview;
    @SerializedName("release_date")
    private String releaseDate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public float getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(float voteAverage) {
        this.voteAverage = voteAverage;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.title);
        dest.writeFloat(this.voteAverage);
        dest.writeString(this.posterPath);
        dest.writeString(this.overview);
        dest.writeString(this.releaseDate);
    }

    public MovieModel() {
    }

    public MovieModel(Cursor cursor) {
        this.id = getColumnInt(cursor, _ID);
        this.title = getColumnString(cursor, COLUMN_TITLE);
        this.voteAverage = getColumnFloat(cursor, COLUMN_TITLE);
        this.posterPath = getColumnString(cursor, COLUMN_POSTER_PATH);
        this.overview = getColumnString(cursor, COLUMN_OVERVIEW);
        this.releaseDate = getColumnString(cursor, COLUMN_RELEASE_DATE);
    }

    protected MovieModel(Parcel in) {
        this.id = in.readInt();
        this.title = in.readString();
        this.voteAverage = in.readFloat();
        this.posterPath = in.readString();
        this.overview = in.readString();
        this.releaseDate = in.readString();
    }

    public static final Creator<MovieModel> CREATOR = new Creator<MovieModel>() {
        @Override
        public MovieModel createFromParcel(Parcel source) {
            return new MovieModel(source);
        }

        @Override
        public MovieModel[] newArray(int size) {
            return new MovieModel[size];
        }
    };
}
