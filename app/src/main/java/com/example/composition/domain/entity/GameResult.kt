package com.example.composition.domain.entity

/**
 * Результат игры.
 *
 * @property winner - победа?
 * @property countOfRightAnswers - кол-во правильных ответов
 * @property countOfQuestions - количество вопросов
 * @property gameSettings - настройки игры
 * @constructor Create empty Game result
 */
data class GameResult (
    val winner: Boolean,
    val countOfRightAnswers: Int,
    val countOfQuestions: Int,
    val gameSettings: GameSettings
)