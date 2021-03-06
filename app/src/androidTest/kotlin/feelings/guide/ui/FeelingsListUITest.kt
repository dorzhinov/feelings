package feelings.guide.ui

import android.content.Context
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.FlakyTest
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import feelings.guide.R
import feelings.guide.ui.question.QuestionListActivity
import feelings.guide.ui.util.answerFeelings
import org.junit.Before
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test

@FlakyTest(detail = "Fails on Android 5, 7, 8, 9. Passes on Android 4, 6")
@LargeTest
@Ignore("flaky test")
class FeelingsListUITest {
    private lateinit var context: Context

    @get:Rule
    var activityRule = ActivityTestRule(QuestionListActivity::class.java)

    @Before
    fun before() {
        context = getApplicationContext()
    }

    @Test
    fun selectFeelingWithScroll_closesAnswerActivity() {
        // given
        val feeling = context.resources.getStringArray(R.array.love_array)[4]

        // when
        answerFeelings(R.string.love, feeling)

        // then
        onView(withId(R.id.questionRV)).check(matches(isDisplayed()))
    }
}