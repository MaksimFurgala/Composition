package com.example.composition.domain.entity

/**
 * Класс настройки игры.
 *
 * @property maxSumValue - максимальное значение игры
 * @property minCountOfRightAnswers - мин. количество правильных ответов
 * @property minPercentOfRightAnswers - мин. процент правильных ответов
 * @property gameTimeInSeconds - время игры в секундах
 * @constructor Create empty Game settings
 */
data class GameSettings(
    val maxSumValue: Int,
    val minCountOfRightAnswers: Int,
    val minPercentOfRightAnswers: Int,
    val gameTimeInSeconds: Int
)
