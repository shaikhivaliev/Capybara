package com.petapp.capybara.database

import androidx.room.*
import com.petapp.capybara.database.entity.*
import com.petapp.capybara.database.entity.healthDiary.ItemHealthDiaryEntity
import com.petapp.capybara.database.entity.healthDiary.ItemHealthDiaryWithSurveys
import com.petapp.capybara.database.entity.healthDiary.SurveyHealthDiaryEntity
import io.reactivex.Single

@Dao
interface AppDao {

    // profile
    @Query("SELECT * FROM profile")
    fun getProfiles(): Single<List<ProfileEntity>>

    @Query("SELECT * FROM profile WHERE id = :profileId")
    fun getProfile(profileId: Long): Single<ProfileEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun createProfile(profile: ProfileEntity)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateProfile(profile: ProfileEntity)

    @Query("DELETE FROM profile WHERE id = :profileId")
    fun deleteProfile(profileId: Long)

    @Transaction
    @Query("SELECT * FROM profile")
    fun getProfilesWithSurveys(): Single<List<ProfileWithSurveys>>

    // type
    @Query("SELECT * FROM type WHERE id = :typeId")
    fun getType(typeId: Long): Single<TypeEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun createType(type: TypeEntity)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateType(profile: TypeEntity)

    @Query("DELETE FROM type WHERE id = :typeId")
    fun deleteType(typeId: Long)

    @Transaction
    @Query("SELECT * FROM type")
    fun getTypesWithSurveys(): Single<List<TypeWithSurveys>>

    // survey
    @Query("SELECT * FROM survey WHERE id = :surveyId")
    fun getSurvey(surveyId: Long): Single<SurveyEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun createSurvey(survey: SurveyEntity)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateSurvey(survey: SurveyEntity)

    @Query("DELETE FROM survey WHERE id = :surveyId")
    fun deleteSurvey(surveyId: Long)

    @Query("SELECT * FROM survey WHERE type_id = :typeId")
    fun getSurveysByType(typeId: Long): Single<List<SurveyEntity>>

    @Query("SELECT * FROM survey WHERE month = :month")
    fun getSurveysByMonth(month: String?): Single<List<SurveyEntity>>

    // health_diary
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun createHealthDiaryItem(item: ItemHealthDiaryEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun createHealthDiarySurvey(item: SurveyHealthDiaryEntity)

    @Transaction
    @Query("SELECT * FROM health_diary")
    fun getItemHealthDiaryWithSurveys(): Single<List<ItemHealthDiaryWithSurveys>>

    @Query("DELETE FROM survey_health_diary WHERE id = :surveyId")
    fun deleteSurveyHealthDiary(surveyId: Long)
}
