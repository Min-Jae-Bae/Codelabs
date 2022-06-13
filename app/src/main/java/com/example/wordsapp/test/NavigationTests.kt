package com.example.wordsapp.test

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.wordsapp.LetterListFragment
import com.example.wordsapp.R
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class NavigationTests {

    val navController = TestNavHostController(
        ApplicationProvider.getApplicationContext()
    )

    val letterListScenario = launchFragmentInContainer<LetterListFragment>(themeResId = R.style.Theme_Words)

}
