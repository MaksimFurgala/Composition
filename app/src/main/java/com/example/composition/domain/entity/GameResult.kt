package com.example.composition.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.io.Serializable

/**
 * Результат игры.
 *
 * @property winner - победа?
 * @property countOfRightAnswers - кол-во правильных ответов
 * @property countOfQuestions - количество вопросов
 * @property gameSettings - настройки игры
 * @constructor Create empty Game result
 */
@Parcelize
data class GameResult (
    val winner: Boolean,
    val countOfRightAnswers: Int,
    val countOfQuestions: Int,
    val gameSettings: GameSettings
): Parcelable