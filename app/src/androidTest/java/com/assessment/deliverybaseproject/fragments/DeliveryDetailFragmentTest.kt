package com.assessment.deliverybaseproject.fragments

import androidx.recyclerview.widget.RecyclerView
import androidx.test.InstrumentationRegistry.getInstrumentation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.rule.ActivityTestRule
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.Until
import com.assessment.deliverybaseproject.R
import com.assessment.deliverybaseproject.ui.deliveryDetail.DeliveryDetailFragment
import com.assessment.deliverybaseproject.ui.deliveryDetail.DeliveryDetailViewModel
import com.assessment.deliverybaseproject.ui.main.DeliveryActivity
import com.assessment.deliverybaseproject.utils.DataBindingIdlingResourceRule
import com.assessment.deliverybaseproject.utils.MockDataUtil
import org.hamcrest.core.StringContains.containsString
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class DeliveryDetailFragmentTest {

    @Rule
    @JvmField
    val activityRule = ActivityTestRule(
        DeliveryActivity::class.java,
        true,
        true
    )

    @Rule
    @JvmField
    val dataBindingIdlingResourceRule = DataBindingIdlingResourceRule(activityRule)

    @Before
    fun setUp() {
        navigateToDetailPage()
    }

    @Test
    fun testInitialLoading_ViewDisplay() {
        onView(withId(R.id.tv_delivery_description)).check(matches(isDisplayed()))
        onView(withId(R.id.tv_delivery_description))
            .check(
                matches(
                    withText(
                        containsString(
                            StringBuilder().append(MockDataUtil.getDeliveryStub().description)
                                .append(" at ").append(MockDataUtil.getDeliveryStub().location.address).toString()
                        )
                    )
                )
            )
        onView(withId((R.id.iv_profile_pic))).check(matches(isDisplayed()))
        onView(withId((R.id.map))).check(matches(isDisplayed()))
    }

    @Test
    fun testMapLoaded() {
        val device = UiDevice.getInstance(getInstrumentation())
        device.wait(Until.hasObject(By.desc(MockDataUtil.getDeliveryStub().location.address)), MAP_LOAD_TIMEOUT)
    }

    private fun navigateToDetailPage() {
        onView(withId(R.id.rv_delivery_list)).perform(
            RecyclerViewActions
                .actionOnItemAtPosition<RecyclerView.ViewHolder>(0, ViewActions.click())
        )

        val viewModel = getFragmentViewModel()
        viewModel?.setSelectedDeliveryDetailInfo(MockDataUtil.getDeliveryStub())
    }

    private fun getFragmentViewModel(): DeliveryDetailViewModel? {
        return (activityRule.activity.supportFragmentManager
            .findFragmentById(R.id.fl_container) as DeliveryDetailFragment)
            .dataBinding.viewModel
    }

    companion object {
        private const val MAP_LOAD_TIMEOUT = 5000L
    }
}