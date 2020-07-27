package com.petapp.capybara.database

import androidx.room.*
import com.petapp.capybara.database.entity.ProfileEntity
import com.petapp.capybara.database.entity.SurveyEntity
import com.petapp.capybara.database.entity.TypeEntity
import io.reactivex.Single

@Dao
interface AppDao {

    // profile
    @Query("SELECT * FROM profile")
    fun getProfiles(): Single<List<ProfileEntity>>

    @Query("SELECT * FROM profile WHERE id = :profileId")
    fun getProfile(profileId: String): Single<ProfileEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun createProfile(profile: ProfileEntity)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateProfile(profile: ProfileEntity)

    @Query("DELETE FROM profile WHERE id = :profileId")
    fun deleteProfile(profileId: String)

    // types
    @Query("SELECT * FROM type")
    fun getTypes(): Single<List<TypeEntity>>

    @Query("SELECT * FROM type WHERE id = :typeId")
    fun getType(typeId: String): Single<TypeEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertType(type: TypeEntity)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateType(profile: TypeEntity)

    @Query("DELETE FROM type WHERE id = :typeId")
    fun deleteType(typeId: String)

    @Query("SELECT COUNT(*) FROM type")
    fun count(): Int

    // surveys
    @Query("SELECT * FROM survey WHERE type_id = :typeId")
    fun getSurveys(typeId: String): Single<List<SurveyEntity>>

    @Query("SELECT * FROM survey WHERE id = :surveyId")
    fun getSurvey(surveyId: String): Single<SurveyEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun createSurvey(survey: SurveyEntity)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateSurvey(survey: SurveyEntity)

    @Query("DELETE FROM survey WHERE id = :surveyId")
    fun deleteSurvey(surveyId: String)
}