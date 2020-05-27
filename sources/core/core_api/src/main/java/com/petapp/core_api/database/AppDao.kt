package com.petapp.core_api.database

import androidx.room.*
import com.petapp.core_api.database.entity.ProfileEntity
import io.reactivex.Single

@Dao
interface AppDao {

    @Query("SELECT * FROM profile")
    fun getProfiles(): Single<List<ProfileEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertProfile(profile: ProfileEntity)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateProfile(profile: ProfileEntity)

    @Query("DELETE FROM profile WHERE id = :profileId")
    fun deleteProfile(profileId: String)

    @Query("SELECT * FROM profile WHERE id = :profileId")
    fun getProfile(profileId: String): Single<ProfileEntity>
}