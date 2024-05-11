package com.example.composition.presentation

import android.content.Context
import android.content.res.ColorStateList
import android.widget.ExpandableListView.OnChildClickListener
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.example.composition.R
import com.example.composition.domain.entity.GameResult

interface OnOptionClickListener {
    fun onOptionClick(option: Int)
}

/**
 * Адаптер для вывода кол-ва требуемых ответов.
 *
 * @param textView
 * @param count
 */
@BindingAdapter("requiredAnswers")
fun bindRequiredAnswers(textView: TextView, count: Int) {
            textView.text = String.format(
                textView.context.getString(R.string.tv_required_answers),
                count
            )
}

/**
 * Адаптер для вывода счета.
 *
 * @param textView
 * @param score
 */
@BindingAdapter("scoreAnswers")
fun bindScoreAnswers(textView: TextView, score: Int) {
    textView.text = String.format(
        textView.context.getString(R.string.tv_score_answers),
        score
    )
}

/**
 * Адаптер для вывода требуемых процентов результата игры.
 *
 * @param textView
 * @param percentage
 */
@BindingAdapter("requiredPercentage")
fun bindRequiredPercentage(textView: TextView, percentage: Int) {
    textView.text = String.format(
        textView.context.getString(R.string.tv_required_percentage),
        percentage
    )
}

/**
 * Адаптер для вывода процентов правильных ответов.
 *
 * @param textView
 * @param gameResult - результат игры
 */
@BindingAdapter("scorePercentage")
fun bindScorePercentage(textView: TextView, gameResult: GameResult) {
    textView.text = String.format(
        textView.context.getString(R.string.tv_score_percentage),
        getPercentOfRightAnswers(gameResult)
    )
}

/**
 * Получить процент правильных ответов.
 *
 * @param gameResult - результат игры
 */
private fun getPercentOfRightAnswers(gameResult: GameResult) = with(gameResult) {
    if (countOfQuestions == 0)
        0
    else
        ((countOfRightAnswers / countOfQuestions.toDouble()) * 100).toInt()
}

/**
 * Адаптер для картинки со смайликом
 *
 * @param imageView
 * @param winner
 */
@BindingAdapter("resultSmile")
fun bindSmileImage(imageView: ImageView, winner: Boolean) {
    imageView.setImageResource(getSmileResId(winner))
}

/**
 * Получить ресурс картинки со смайликом, в зависимости от статуса игры.
 *
 * @param winner - выйрали?
 * @return - id ресурса картинки смайлика, .
 */
private fun getSmileResId(winner: Boolean): Int {
    return if (winner)
        R.mipmap.ic_smile_foreground
    else
        R.mipmap.ic_upset_smile_foreground
}

/**
 * Адаптер для смены цвета textView, в зависимоти от статуса игры.
 *
 * @param textView
 * @param enough
 */
@BindingAdapter("enoughCount")
fun bindEnoughCount(textView: TextView, enough: Boolean) {
    textView.setTextColor(getColorByState(textView.context, enough))
}

/**
 * Адаптер для смены цвета progressBar, в зависимоти от статуса игры.
 *
 * @param progressBar
 * @param enough
 */
@BindingAdapter("enoughPercent")
fun bindEnoughPercent(progressBar: ProgressBar, enough: Boolean) {
    val color = getColorByState(progressBar.context, enough)
    progressBar.progressTintList = ColorStateList.valueOf(color)
}

/**
 * Получить id ресурса с цветом в зависимости от статуса игры.
 *
 * @param goodState - все хорошо?
 * @return - id ресурса
 */
private fun getColorByState(context: Context, goodState: Boolean): Int {
    val colorResId = if (goodState) {
        android.R.color.holo_green_light
    } else {
        android.R.color.holo_red_light
    }
    return ContextCompat.getColor(context, colorResId)
}

/**
 * Адаптер для парсинга в разметку чисел в виде строки в элементах textView.
 *
 * @param textView
 * @param number - число
 */
@BindingAdapter("numberAsText")
fun bindNumberAsText(textView: TextView, number: Int) {
    textView.text = number.toString()
}

/**
 * Адаптер для обработчика событий в вариантах ответах.
 *
 * @param textView
 * @param clickListener - обработчик
 */
@BindingAdapter("onOptionClickListener")
fun bindOnOptionClickListener(textView: TextView, clickListener: OnOptionClickListener) {
    textView.setOnClickListener {
        clickListener.onOptionClick(textView.text.toString().toInt())
    }
}