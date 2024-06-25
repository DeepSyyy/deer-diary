package com.example.deerdiary.ui.loginScreen

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.JsonConverter
import com.example.deerdiary.R
import com.example.deerdiary.data.services.ApiConfig
import com.example.deerdiary.ui.homeScreen.HomeActivity
import com.example.deerdiary.ui.settingScreen.SettingActivity
import com.example.deerdiary.utils.EspressoIdlingResource
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoginActivityTest {
    private val mockWebServer = MockWebServer()

    @Before
    fun setUp() {
        ActivityScenario.launch(LoginActivity::class.java)
        mockWebServer.start(8080)
        ApiConfig.base_url = "http://127.0.0.1:8080/"
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
    }

    @Test
    fun login() {
        val mockResponse =
            MockResponse()
                .setResponseCode(200)
                .setBody(JsonConverter.readStringFromFile("success.response.json"))
        mockWebServer.enqueue(mockResponse)

        Intents.init()

        onView(withId(R.id.tv_welcome))
            .check(matches(isDisplayed()))

        onView(withId(R.id.ed_login_email))
            .perform(typeText("firman@gmail.com"), closeSoftKeyboard())

        onView(withId(R.id.ed_login_password))
            .perform(typeText("12345678"), closeSoftKeyboard())

        onView(withId(R.id.btn_login))
            .perform(click())

        Intents.intended(IntentMatchers.hasComponent(HomeActivity::class.java.name))

        onView(withId(R.id.iv_home))
            .check(matches(isDisplayed()))

        onView(withId(R.id.setting))
            .perform(click())

        Intents.intended(IntentMatchers.hasComponent(SettingActivity::class.java.name))

        onView(withId(R.id.action_logout))
            .perform(click())

        Intents.intended(IntentMatchers.hasComponent(LoginActivity::class.java.name))
    }
}
