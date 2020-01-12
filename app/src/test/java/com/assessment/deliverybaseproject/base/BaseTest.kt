package com.assessment.deliverybaseproject.base

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import org.junit.Rule

abstract class BaseTest {
    @get:Rule
    val testRule = InstantTaskExecutorRule()
}