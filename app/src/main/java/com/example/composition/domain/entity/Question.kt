package com.example.composition.domain.entity

/**
 * Вопрос
 *
 * @property sum - сумма (отображается на экране)
 * @property visibleNumber - значение видимого числа (отображается на экране)
 * @property options - варианты ответов
 * @constructor Create empty Question
 */
data class Question (
    val sum: Int,
    val visibleNumber: Int,
    val options: List<Int>
) {
    /**
     * Правильный ответ, в зависимости
     */
    val rightAnswer: Int
        get() = sum - visibleNumber
}