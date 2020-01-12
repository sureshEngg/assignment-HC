package com.assessment.deliverybaseproject.fragments

import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.contrib.RecyclerViewActions.scrollToPosition
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.rule.ActivityTestRule
import com.assessment.deliverybaseproject.R
import com.assessment.deliverybaseproject.ui.main.DeliveryActivity
import com.assessment.deliverybaseproject.utils.DataBindingIdlingResourceRule
import com.assessment.deliverybaseproject.utils.MockDataUtil
import com.assessment.deliverybaseproject.utils.RecyclerViewItemCountAssertion
import com.assessment.deliverybaseproject.utils.RecyclerViewMatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class DeliveryListFragmentTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var activity: DeliveryActivity

    @Rule
    @JvmField
    val activityRule = ActivityTestRule(
        DeliveryActivity::class.java,
        true,
        false
    )

    @Rule
    @JvmField
    val dataBindingIdlingResourceRule = DataBindingIdlingResourceRule(activityRule)

    @Before
    fun setUp() {
        Intents.init()
        mockWebServer = MockWebServer()
        mockWebServer.start(MOCK_API_PORT)
    }

    @Test
    fun testInitialLoading_viewDisplay() {
        callMockDeliveryApi()
        reLaunchActivity()

        onView(withId(R.id.swipeContainer)).check(matches(isDisplayed()))
        onView(withId(R.id.rv_delivery_list)).check(matches(isDisplayed()))
        onView(withId(R.id.rv_delivery_list)).check(RecyclerViewItemCountAssertion(DELIVERY_ITEMS_COUNT))
        onView(withRecyclerView(R.id.rv_delivery_list).atPosition(0))
            .check(matches(hasDescendant(withText("Deliver parcel to Suresh at Kowloon Tong"))))
    }

    @Test
    fun testClickListItem_navigateToDetailPage() {
        callMockDeliveryApi()
        reLaunchActivity()

        onView(withId(R.id.rv_delivery_list)).perform(
            RecyclerViewActions
                .actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click())
        )

        onView(withId(R.id.tv_delivery_description)).check(matches(isDisplayed()))
        onView(withId(R.id.tv_delivery_description))
            .check(matches(withText("Deliver parcel to Suresh at Kowloon Tong")))
    }

    @Test
    fun testScrollDown_loadMoreDisplay() {
        callMockDeliveryApi()
        reLaunchActivity()

        onView(withId(R.id.rv_delivery_list))
            .perform(
                scrollToPosition<RecyclerView.ViewHolder>(
                    DELIVERY_ITEMS_COUNT
                )
            )
        Espresso.onView(withId(R.id.rv_delivery_list)).perform(ViewActions.swipeUp())

        onView(withId(R.id.rv_delivery_list))
            .check(RecyclerViewItemCountAssertion(DELIVERY_ITEMS_COUNT + FOOTER_PROGRESS_BAR))
    }

    @Test
    fun testDeliveryApiLoadingError_snackBarDisplay() {
        callMockDeliveryApiWithError()
        reLaunchActivity()

        onView(withId(R.id.rv_delivery_list))
            .perform(
                scrollToPosition<RecyclerView.ViewHolder>(
                    DELIVERY_ITEMS_COUNT
                )
            )
        Espresso.onView(withId(R.id.rv_delivery_list)).perform(ViewActions.swipeDown())

        onView(withId(com.google.android.material.R.id.snackbar_text))
            .check(matches(withText("Loading failed due to unexpected error!")))
    }

    @Test
    fun testPullToRefreshOnSwipe_dataShouldChanged() {
        reLaunchActivity()
        callFirstMockDeliveryApi()
        Espresso.onView(withId(R.id.rv_delivery_list)).perform(ViewActions.swipeDown())
        onView(withRecyclerView(R.id.rv_delivery_list).atPosition(0))
            .check(matches(hasDescendant(withText("Deliver parcel to Mohit at Kowloon Tong"))))

        callMockDeliveryApi()
        Espresso.onView(withId(R.id.rv_delivery_list)).perform(ViewActions.swipeDown())
        onView(withRecyclerView(R.id.rv_delivery_list).atPosition(0))
            .check(matches(hasDescendant(withText("Deliver parcel to Suresh at Kowloon Tong"))))
    }

    private fun reLaunchActivity() {
        activity = activityRule.launchActivity(Intent())
    }

    private fun callMockDeliveryApi() {
        queueResponse {
            setResponseCode(200)
            setBody(MockDataUtil.getResponseFromJson("deliveries"))
        }
    }

    private fun callFirstMockDeliveryApi() {
        queueResponse {
            setResponseCode(200)
            setBody(MockDataUtil.getResponseFromJson("refresh_deliveries"))
        }
    }

    private fun callMockDeliveryApiWithError() {
        queueResponse {
            setResponseCode(500)
            setBody(MockDataUtil.getResponseFromJson("errorResponse"))
        }
    }

    private fun queueResponse(block: MockResponse.() -> Unit) {
        mockWebServer.enqueue(MockResponse().apply(block))
    }

    private fun withRecyclerView(recyclerViewId: Int): RecyclerViewMatcher {
        return RecyclerViewMatcher(recyclerViewId)
    }

    @After
    fun tierDown() {
        Intents.release()
        mockWebServer.shutdown()
    }

    companion object {
        private const val DELIVERY_ITEMS_COUNT = 18
        private const val FOOTER_PROGRESS_BAR = 1
        private const val MOCK_API_PORT = 8888
    }
}
