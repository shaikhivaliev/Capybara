package com.petapp.capybara.database

import androidx.room.*
import com.petapp.capybara.profiles.data.ProfileEntity
import com.petapp.capybara.profiles.domain.Profile
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface AppDao {

    @Query("SELECT * FROM profile")
    fun getProfiles(): Single<MutableList<ProfileEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertProfile(profile: ProfileEntity)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateProfile(profile: ProfileEntity)

    @Query("DELETE FROM profile WHERE id = :profileId")
    fun deleteProfile(profileId: String)

    @Query("SELECT * FROM profile WHERE id = :profileId")
    fun getProfile(profileId: String): Single<ProfileEntity>
}