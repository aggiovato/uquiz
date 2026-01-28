package com.uquiz.android.data.repository

import com.uquiz.android.data.local.dao.OptionDao
import com.uquiz.android.data.local.dao.QuestionDao
import com.uquiz.android.data.local.entity.AuditTimestamps
import com.uquiz.android.data.local.enums.DifficultyLevel
import com.uquiz.android.data.local.mapper.OptionMapper
import com.uquiz.android.data.local.mapper.QuestionMapper
import com.uquiz.android.data.local.mapper.QuestionWithOptionsMapper
import com.uquiz.android.domain.model.Option
import com.uquiz.android.domain.model.Question
import com.uquiz.android.domain.model.QuestionWithOptions
import com.uquiz.android.domain.repository.CreateOptionData
import com.uquiz.android.domain.repository.QuestionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.UUID

/**
 * Implementation of QuestionRepository
 */
class QuestionRepositoryImpl(
    private val questionDao: QuestionDao,
    private val optionDao: OptionDao
) : QuestionRepository {

    override fun observeQuestion(questionId: String): Flow<Question?> {
        return questionDao.observeQuestionWithOptionsFlow(questionId)
            .map { it?.let { QuestionMapper.toModel(it.question) } }
    }

    override suspend fun getById(questionId: String): Question? {
        return questionDao.getById(questionId)?.let { QuestionMapper.toModel(it) }
    }

    override suspend fun getQuestionWithOptions(questionId: String): QuestionWithOptions? {
        return questionDao.getQuestionWithOptions(questionId)?.let {
            QuestionWithOptionsMapper.toModel(it)
        }
    }

    override fun observeQuestionWithOptions(questionId: String): Flow<QuestionWithOptions?> {
        return questionDao.observeQuestionWithOptionsFlow(questionId)
            .map { it?.let { QuestionWithOptionsMapper.toModel(it) } }
    }

    override suspend fun createQuestion(
        text: String,
        explanation: String?,
        difficulty: DifficultyLevel,
        options: List<CreateOptionData>
    ): Question {
        val now = System.currentTimeMillis()
        val questionId = UUID.randomUUID().toString()

        // Create question
        val question = Question(
            id = questionId,
            text = text,
            explanation = explanation,
            difficulty = difficulty,
            createdAt = now,
            updatedAt = now
        )
        questionDao.upsertQuestion(QuestionMapper.toEntity(question))

        // Create options
        val optionEntities = options.map { optionData ->
            Option(
                id = UUID.randomUUID().toString(),
                questionId = questionId,
                label = optionData.label,
                text = optionData.text,
                isCorrect = optionData.isCorrect,
                createdAt = now,
                updatedAt = now
            )
        }
        optionDao.upsertAll(OptionMapper.toEntityList(optionEntities))

        return question
    }

    override suspend fun updateQuestion(question: Question) {
        val updated = question.copy(updatedAt = System.currentTimeMillis())
        questionDao.upsertQuestion(QuestionMapper.toEntity(updated))
    }

    override suspend fun updateQuestionOptions(questionId: String, options: List<Option>) {
        // Delete existing options
        optionDao.deleteByQuestionId(questionId)

        // Insert new options with updated timestamp
        val now = System.currentTimeMillis()
        val updatedOptions = options.map { it.copy(updatedAt = now) }
        optionDao.upsertAll(OptionMapper.toEntityList(updatedOptions))
    }

    override suspend fun deleteQuestion(question: Question) {
        questionDao.delete(QuestionMapper.toEntity(question))
    }
}
