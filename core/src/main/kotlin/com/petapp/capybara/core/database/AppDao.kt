package com.petapp.capybara.core.database

import androidx.room.*
import com.petapp.capybara.core.database.entity.healthDiary.ItemHealthDiaryEntity
import com.petapp.capybara.core.database.entity.healthDiary.ItemHealthDiaryWithSurveys
import com.petapp.capybara.core.database.entity.healthDiary.SurveyHealthDiaryEntity
import com.petapp.capybara.core.database.entity.profile.ProfileEntity
import com.petapp.capybara.core.database.entity.profile.ProfileWithSurveys
import com.petapp.capybara.core.database.entity.survey.SurveyEntity
import com.petapp.capybara.core.database.entity.type.TypeEntity
import com.petapp.capybara.core.database.entity.type.TypeWithSurveys

@Dao
interface AppDao {

    // profile
    @Query("SELECT * FROM profile WHERE id = :profileId")
    suspend fun getProfile(profileId: Long): ProfileEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun createProfile(profile: ProfileEntity)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateProfile(profile: ProfileEntity)

    @Query("DELETE FROM profile WHERE id = :profileId")
    suspend fun deleteProfile(profileId: Long)

    @Transaction
    @Query("SELECT * FROM profile")
    suspend fun getProfilesWithSurveys(): List<ProfileWithSurveys>

    // type
    @Query("SELECT * FROM type WHERE id = :typeId")
    suspend fun getType(typeId: Long): TypeEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun createType(type: TypeEntity)

    @Query("DELETE FROM type WHERE id = :typeId")
    suspend fun deleteType(typeId: Long)

    @Transaction
    @Query("SELECT * FROM type")
    suspend fun getTypesWithSurveys(): List<TypeWithSurveys>

    // survey
    @Query("SELECT * FROM survey WHERE id = :surveyId")
    suspend fun getSurvey(surveyId: Long): SurveyEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun createSurvey(survey: SurveyEntity)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateSurvey(survey: SurveyEntity)

    @Query("DELETE FROM survey WHERE id = :surveyId")
    suspend fun deleteSurvey(surveyId: Long)

    @Query("SELECT * FROM survey")
    suspend fun getAllSurveys(): List<SurveyEntity>

    @Query("SELECT * FROM survey WHERE type_id = :typeId")
    suspend fun getSurveysByType(typeId: Long): List<SurveyEntity>

    // health_diary
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun createHealthDiaryItem(item: ItemHealthDiaryEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun createHealthDiarySurvey(item: SurveyHealthDiaryEntity)

    @Transaction
    @Query("SELECT * FROM health_diary")
    suspend fun getItemHealthDiaryWithSurveys(): List<ItemHealthDiaryWithSurveys>

    @Query("DELETE FROM survey_health_diary WHERE id = :surveyId")
    suspend fun deleteSurveyHealthDiary(surveyId: Long)

}
