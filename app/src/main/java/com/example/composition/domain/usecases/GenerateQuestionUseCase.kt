package com.example.composition.domain.usecases

import com.example.composition.domain.entity.GameSettings
import com.example.composition.domain.entity.Question
import com.example.composition.domain.repository.GameRepository

/**
 * Use-case для генерации вопроса.
 *
 * @property repository - репозиторий
 * @constructor Create empty Generate question use case
 */
class GenerateQuestionUseCase(private val repository: GameRepository) {
    operator fun invoke(maxSumValue: Int): Question {
        return repository.generateQuestion(maxSumValue, COUNT_OF_OPTIONS)
    }

    private companion object {
        // Количество возможных вариантов ответа.
        private const val COUNT_OF_OPTIONS = 6
    }
}