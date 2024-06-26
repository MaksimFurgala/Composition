package com.example.composition.data

import com.example.composition.domain.entity.GameSettings
import com.example.composition.domain.entity.Level
import com.example.composition.domain.entity.Question
import com.example.composition.domain.repository.GameRepository
import kotlin.math.max
import kotlin.math.min
import kotlin.random.Random

object GameRepositoryImpl: GameRepository {
    private const val MIN_SUM_VALUE = 2
    private const val MIN_ANSWER_VALUE = 1

    /**
     * Генерация вопроса.
     *
     * @param maxSumValue - максимальное значение суммы
     * @param countOfOptions - количество вариантов ответа
     * @return вопрос
     */
    override fun generateQuestion(maxSumValue: Int, countOfOptions: Int): Question {
        val sum = Random.nextInt(MIN_SUM_VALUE, maxSumValue + 1)//сгенерируем сумму чисел
        val visibleNumber = Random.nextInt(MIN_ANSWER_VALUE, sum)//видимое значение операнда
        val options = HashSet<Int>()//варианты ответов
        val rightAnswer = sum - visibleNumber//правильный ответ
        options += rightAnswer
        val from = max(rightAnswer - countOfOptions, MIN_ANSWER_VALUE)//нижняя граница
        val to = min(maxSumValue, rightAnswer + countOfOptions)//верхняя граница

        while (options.size < countOfOptions)
            options.add(Random.nextInt(from, to))

        return Question(sum, visibleNumber, options.toList())
    }

    /**
     * Получить настройки игры.
     *
     * @param level - уровень
     * @return - настройки игры
     */
    override fun getGameSettings(level: Level): GameSettings {
        return when (level) {
            Level.TEST -> GameSettings(
                10,
                3,
                50,
                8)
            Level.EASY -> GameSettings(
                10,
                10,
                70,
                60
            )
            Level.NORMAL -> GameSettings(
                20,
                20,
                80,
                40
            )
            Level.HARD -> GameSettings(
                30,
                30,
                90,
                40
            )
        }
    }
}