package com.ocproject.realestatemanager.presentation.scene.funding

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.ocproject.realestatemanager.MainCoroutineRule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class FundingViewModelTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    val testDispatcher = StandardTestDispatcher()

    private lateinit var viewModel: FundingViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        viewModel = FundingViewModel()

    }

    @After
    fun after() {
        Dispatchers.resetMain()
    }

    @Test
    fun `price input test`() = runTest {
        val price = "165A.2"
        viewModel.onEvent(FundingEvent.OnPriceInput(price))
        advanceUntilIdle()
        assert(viewModel.price == 1652)
    }

    @Test
    fun `rating bottom sheet state test`() = runTest {
        viewModel.onEvent(FundingEvent.OpenRatingSelectionSheet)
        advanceUntilIdle()
        assert(viewModel.state.value.isRatingListSheetOpen)

        viewModel.onEvent(FundingEvent.DismissRatingSelectionSheet)
        advanceUntilIdle()
        assert(!viewModel.state.value.isRatingListSheetOpen)
    }

    @Test
    fun `rate option chosen test`() = runTest {
        viewModel.onEvent(FundingEvent.OnRateOptionChosen(FundingRate.FIFTEEN_YEARS, "ratio : ${FundingRate.FIFTEEN_YEARS.ratio*100}"))
        advanceUntilIdle()
        assert(viewModel.state.value.chosenRate == FundingRate.FIFTEEN_YEARS)
        assert(viewModel.state.value.chosenText == "ratio : ${FundingRate.FIFTEEN_YEARS.ratio*100}")
    }

    @Test
    fun `calculus monthly payment test`() = runTest {
        // for 100000 to borrow at rate 0.05 sur 12 month
        val monthlyPayment = 8560.639F

        assert(viewModel.calcMonthlyPayment( amountToBorrow = 100000F, rate = 0.05F, durationInMonth = 12F) == monthlyPayment )
    }

    @Test
    fun `display functions test`() = runTest {
        assert( viewModel.displayPercent(0.5F) == "50.0 %")
        assert( viewModel.displayTotalCost(2500F, 24) == "60000.00")
        assert( viewModel.displayInterest(8560.639F, 12, 100000F) == "2727.66")
    }

}