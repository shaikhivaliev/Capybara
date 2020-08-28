package com.petapp.capybara.database

import androidx.room.*
import com.petapp.capybara.database.entity.*
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

    @Transaction
    @Query("SELECT * FROM profile")
    fun getProfilesWithSurveys(): Single<List<ProfileWithSurveys>>

    // type
    @Query("SELECT * FROM type WHERE id = :typeId")
    fun getType(typeId: String): Single<TypeEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertType(type: TypeEntity)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateType(profile: TypeEntity)

    @Query("DELETE FROM type WHERE id = :typeId")
    fun deleteType(typeId: String)

    @Transaction
    @Query("SELECT * FROM type")
    fun getTypesWithSurveys(): Single<List<TypeWithSurveys>>

    // survey
    @Query("SELECT * FROM survey WHERE id = :surveyId")
    fun getSurvey(surveyId: String): Single<SurveyEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun createSurvey(survey: SurveyEntity)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateSurvey(survey: SurveyEntity)

    @Query("DELETE FROM survey WHERE id = :surveyId")
    fun deleteSurvey(surveyId: String)

    @Query("SELECT * FROM survey WHERE type_id = :typeId")
    fun getSurveys(typeId: String): Single<List<SurveyEntity>>
}