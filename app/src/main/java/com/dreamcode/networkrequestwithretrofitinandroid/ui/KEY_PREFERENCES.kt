/*
 * Copyright (c) 2024 Kodeco Inc
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * Notwithstanding the foregoing, you may not use, copy, modify, merge, publish,
 * distribute, sublicense, create a derivative work, and/or sell copies of the
 * Software in any work that is designed, intended, or marketed for pedagogical or
 * instructional purposes related to programming, coding, application development,
 * or information technology.  Permission for such use, copying, modification,
 * merger, publication, distribution, sublicensing, creation of derivative works,
 * or sale is expressly withheld.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NON INFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.dreamcode.networkrequestwithretrofitinandroid.ui

import android.app.Application
import android.content.Context
import androidx.core.content.edit
import com.dreamcode.networkrequestwithretrofitinandroid.networking.MovieDiaryApi

private const val KEY_PREFERENCES = "movie_diary_preferences"
private const val KEY_TOKEN = "token"
private const val KEY_REFRESH_TOKEN = "refresh_token"

class App : Application() {

  override fun onCreate() {
    super.onCreate()
    instance = this
  }

  companion object {
    private lateinit var instance: App

    val movieApi by lazy { MovieDiaryApi() }
    private val sharedPrefs by lazy { instance.getSharedPreferences(KEY_PREFERENCES, Context.MODE_PRIVATE) }

    fun saveUserToken(token: String) {
      sharedPrefs.edit { putString(KEY_TOKEN, token) }
    }

    fun getUserToken(): String = sharedPrefs.getString(KEY_TOKEN, "") ?: ""

    fun saveRefreshToken(token: Long) {
      sharedPrefs.edit { putLong(KEY_REFRESH_TOKEN, token) }
    }

    fun getRefreshToken(): Long = sharedPrefs.getLong(KEY_REFRESH_TOKEN, 0L)
  }
}