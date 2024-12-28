package com.ocproject.realestatemanager.domain.use_case

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.ocproject.realestatemanager.data.repositories.FakePropertiesRepository
import com.ocproject.realestatemanager.domain.models.Property
import com.ocproject.realestatemanager.domain.usecases.GetPropertyListUseCase
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.invoke

class GetPropertyListUseCaseTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var fakePropertyRepository: FakePropertiesRepository
    private lateinit var getPropertyList: GetPropertyListUseCase

    @Before
    fun setUp() {
        fakePropertyRepository = FakePropertiesRepository()
        getPropertyList = GetPropertyListUseCase(fakePropertyRepository)

    }

    @Test
    fun `get properties from empty list, empty list`() = runTest {
        fakePropertyRepository.shouldHaveFilledList(false)

        val properties = getPropertyList.invoke()

        assertThat(properties == emptyList<Property>())
    }

    @Test
    fun `get properties from list, list of properties`() = runTest {
        // 3 properties in repo
        fakePropertyRepository.shouldHaveFilledList(true)

        val properties = getPropertyList.invoke()

        assertThat(properties.toList().size == 3)
    }
}