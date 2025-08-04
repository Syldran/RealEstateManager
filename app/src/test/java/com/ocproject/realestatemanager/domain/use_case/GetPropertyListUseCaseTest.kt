package com.ocproject.realestatemanager.domain.use_case

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.ocproject.realestatemanager.core.DataState
import com.ocproject.realestatemanager.data.repositories.FakePropertiesRepository
import com.ocproject.realestatemanager.domain.models.Property
import com.ocproject.realestatemanager.domain.usecases.GetPropertyListUseCase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

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
    fun `get properties from empty list, returns empty list`() = runTest {
        fakePropertyRepository.shouldHaveFilledList(false)

        val result = getPropertyList.invoke().toList()

        assertThat(result).hasSize(3) // Loading(true), Success(empty), Loading(false)
        assertThat(result[1]).isInstanceOf(DataState.Success::class.java)
        assertThat((result[1] as DataState.Success<List<Property>>).data).isEmpty()
    }

    @Test
    fun `get properties from filled list, returns list of properties`() = runTest {
        fakePropertyRepository.shouldHaveFilledList(true)

        val result = getPropertyList.invoke().toList()

        assertThat(result).hasSize(3) // Loading(true), Success(data), Loading(false)
        assertThat(result[1]).isInstanceOf(DataState.Success::class.java)
        val properties = (result[1] as DataState.Success<List<Property>>).data
        assertThat(properties).hasSize(5) // 5 properties in fake repository
    }

    @Test
    fun `get properties flow emits loading states correctly`() = runTest {
        fakePropertyRepository.shouldHaveFilledList(true)

        val result = getPropertyList.invoke().toList()

        assertThat(result[0]).isInstanceOf(DataState.Loading::class.java)
        assertThat((result[0] as DataState.Loading).isLoading).isTrue()
        assertThat(result[2]).isInstanceOf(DataState.Loading::class.java)
        assertThat((result[2] as DataState.Loading).isLoading).isFalse()
    }
}