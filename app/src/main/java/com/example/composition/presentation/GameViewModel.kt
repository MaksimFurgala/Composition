package com.example.composition.presentation

import android.app.Application
import android.content.Context
import android.os.CountDownTimer
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.composition.R
import com.example.composition.data.GameRepositoryImpl
import com.example.composition.domain.entity.GameResult
import com.example.composition.domain.entity.GameSettings
import com.example.composition.domain.entity.Level
import com.example.composition.domain.entity.Question
import com.example.composition.domain.usecases.GenerateQuestionUseCase
import com.example.composition.domain.usecases.GetGameSettingsUseCase

class GameViewModel(
  private val application: Application,
  private val level: Level
) : ViewModel() {
    // Настройки игры.
    private lateinit var gameSettings: GameSettings

    // Репозиторий.
    private val repository = GameRepositoryImpl

    // Use cas'ы бизнес логики.
    private val generateQuestionUseCase = GenerateQuestionUseCase(repository)
    private val getGameSettingsUseCase = GetGameSettingsUseCase(repository)

    // Таймер игры.
    private var timer: CountDownTimer? = null

    // Отформатированное время в виде строки.
    private val _formattedTime = MutableLiveData<String>()
    val formattedTime: LiveData<String>
        get() = _formattedTime

    // Тек. вопрос.
    private val _question = MutableLiveData<Question>()
    val question: LiveData<Question>
        get() = _question

    // Процент правильных ответов.
    private val _percentOfRightAnswer = MutableLiveData<Int>()
    val percentOfRightAnswer: LiveData<Int>
        get() = _percentOfRightAnswer

    // Прогресс игры.
    private val _progressAnswers = MutableLiveData<String>()
    val progressAnswers: LiveData<String>
        get() = _progressAnswers

    // Достаточное количество ответов на вопросы?
    private val _enoughCount = MutableLiveData<Boolean>()
    val enoughCount: LiveData<Boolean>
        get() = _enoughCount

    // Достаточный процент ответов на вопросы?
    private val _enoughPercent = MutableLiveData<Boolean>()
    val enoughPercent: LiveData<Boolean>
        get() = _enoughPercent

    // Минимальный процент ответов на вопросы
    private val _minPercent = MutableLiveData<Int>()
    val minPercent: LiveData<Int>
        get() = _minPercent

    // Резульат игры.
    private val _gameResult = MutableLiveData<GameResult>()
    val gameResult: LiveData<GameResult>
        get() = _gameResult

    // Кол-во правильных ответов.
    private var countOfRightAnswers = 0

    // Кол-во вопросов.
    private var countOfQuestions = 0

    init {
        startGame()
    }

    /**
     * Запуск игры в зависимости от уровня сложности.
     *
     * @param level - уровень сложности
     */
    private fun startGame() {
        getGameSettings()
        startTimer()
        generateQuestion()
        updateProgress()
    }

    /**
     * Выбор ответа, обновление прогресса и генерация нового вопроса.
     *
     * @param number - номер ответа
     */
    fun chooseAnswer(number: Int) {
        checkAnswer(number)
        updateProgress()
        generateQuestion()
    }

    /**
     * Обновление прогресса игры:
     * процент правильных ответов,
     * текстовый статус выполнения задания (количество правильных ответов, необходимое количество ответов),
     * статусы о выполнении целей (кол-во правильных ответов, процент правильных ответов).
     */
    private fun updateProgress() {
        val percent = calculatePercentOfRightAnswers()
        _percentOfRightAnswer.value = percent
        _progressAnswers.value = String.format(
            application.resources.getString(R.string.progress_answers),
            countOfRightAnswers,
            gameSettings.minCountOfRightAnswers
        )

        _enoughCount.value = countOfRightAnswers >= gameSettings.minCountOfRightAnswers
        _enoughPercent.value = percent >= gameSettings.minPercentOfRightAnswers
    }

    /**
     * Получить процент правильных ответов.
     *
     * @return процент правильных ответов
     */
    private fun calculatePercentOfRightAnswers(): Int {
        if (countOfQuestions == 0)
            return 0
        return ((countOfRightAnswers / countOfQuestions.toDouble()) * 100).toInt()
    }

    /**
     * Проверка правильности ответа.
     *
     * @param number выбранный вариант ответа
     */
    private fun checkAnswer(number: Int) {
        val rightAnswer = question.value?.rightAnswer
        if (number == rightAnswer)
            countOfRightAnswers++
        countOfQuestions++
    }

    /**
     * Получить настройки игры, в зависимости от выбранного уровня сложности.
     *
     * @param level - уровень сложности игры
     */
    private fun getGameSettings() {
        this.gameSettings = getGameSettingsUseCase(level)
        _minPercent.value = gameSettings.minPercentOfRightAnswers
    }

    /**
     * Запуск таймера игры.
     */
    private fun startTimer() {
        timer = object :
            CountDownTimer(gameSettings.gameTimeInSeconds * MILLIS_IN_SECONDS, MILLIS_IN_SECONDS) {
            override fun onTick(millisUntilFinished: Long) {
                _formattedTime.value = formatTime(millisUntilFinished)
            }

            override fun onFinish() {
                finishGame()
            }

        }
        timer?.start()
    }

    /**
     * Генерация нового вопроса.
     */
    private fun generateQuestion() {
        _question.value = generateQuestionUseCase(gameSettings.maxSumValue)
    }

    /**
     * Получить отформатированное время игры в формате: mm:ss.
     *
     * @param millisUntilFinished - милисекунды игры
     * @return - отформатированное время игры
     */
    private fun formatTime(millisUntilFinished: Long): String {
        val seconds = millisUntilFinished / MILLIS_IN_SECONDS
        val minutes = seconds / SECONDS_IN_MINUTES
        val leftSeconds = seconds - minutes * SECONDS_IN_MINUTES
        return String.format("%02d:%02d", minutes, leftSeconds)
    }

    /**
     * Завершение игры.
     */
    private fun finishGame() {
        _gameResult.value = GameResult(
            enoughCount.value == true && enoughPercent.value == true,
            countOfRightAnswers,
            countOfQuestions,
            gameSettings
        )
    }

    override fun onCleared() {
        super.onCleared()
        timer?.cancel()
    }

    companion object {
        // Кол-во милесекунд в секунде.
        private const val MILLIS_IN_SECONDS = 1000L

        // Кол-во секунд в минуте.
        private const val SECONDS_IN_MINUTES = 60
    }
}