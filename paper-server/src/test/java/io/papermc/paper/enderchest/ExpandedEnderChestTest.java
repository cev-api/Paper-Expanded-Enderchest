package io.papermc.paper.enderchest;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ExpandedEnderChestTest {

    @Test
    void defaultSlotCountIsSupported() {
        assertEquals(27, ExpandedEnderChest.DEFAULT_SLOT_COUNT);
        assertTrue(ExpandedEnderChest.isValidSlotCount(ExpandedEnderChest.DEFAULT_SLOT_COUNT));
    }

    @ParameterizedTest
    @ValueSource(ints = {9, 18, 27, 36, 45, 54})
    void multiplesOfNineBetweenNineAndFiftyFourAreSupported(final int slotCount) {
        assertTrue(ExpandedEnderChest.isValidSlotCount(slotCount), slotCount + " should be a supported size");
    }

    @ParameterizedTest
    @ValueSource(ints = {-54, -9, 0, 1, 8, 10, 14, 26, 28, 53, 55, 63, 100})
    void otherValuesAreNotSupported(final int slotCount) {
        assertFalse(ExpandedEnderChest.isValidSlotCount(slotCount), slotCount + " should not be a supported size");
    }

    @ParameterizedTest
    @CsvSource({
        // configured, expected normalized
        "9,  9",
        "18, 18",
        "27, 27",
        "36, 36",
        "45, 45",
        "54, 54",
        "0,  9",
        "8,  9",
        "10, 9",
        "13, 9",
        "14, 18",
        "26, 27",
        "28, 27",
        "31, 27",
        "49, 45",
        "53, 54",
        "55, 54",
        "1000, 54",
        "-1, 9",
    })
    void normalizeSlotCountAlwaysYieldsASupportedSize(final int configured, final int expected) {
        final int normalized = ExpandedEnderChest.normalizeSlotCount(configured);
        assertEquals(expected, normalized, () -> "normalizeSlotCount(" + configured + ")");
        assertTrue(ExpandedEnderChest.isValidSlotCount(normalized), "normalized value must be usable as is");
    }

    @ParameterizedTest
    @ValueSource(ints = {-100, -1, 0, 1, 7, 8, 9, 17, 27, 53, 54, 55, 999})
    void normalizedSlotCountIsAlwaysInRange(final int configured) {
        final int normalized = ExpandedEnderChest.normalizeSlotCount(configured);
        assertTrue(normalized >= ExpandedEnderChest.MIN_SLOT_COUNT, "normalized value must not be below the minimum");
        assertTrue(normalized <= ExpandedEnderChest.MAX_SLOT_COUNT, "normalized value must not be above the maximum");
        assertEquals(0, normalized % ExpandedEnderChest.SLOTS_PER_ROW, "normalized value must be a multiple of nine");
    }

    @ParameterizedTest
    @CsvSource({
        // configured, expected rows
        "9,  1",
        "18, 2",
        "27, 3",
        "36, 4",
        "45, 5",
        "54, 6",
    })
    void supportedSizesSelectTheMatchingNumberOfRows(final int slotCount, final int expectedRows) {
        assertEquals(expectedRows, ExpandedEnderChest.rowsForSlotCount(slotCount));
    }

    @ParameterizedTest
    @CsvSource({
        "9,  1",
        "18, 2",
        "27, 3",
        "36, 4",
        "45, 5",
        "54, 6",
    })
    void selectedRowsAlwaysMatchTheConfiguredContainerSize(final int slotCount, final int expectedRows) {
        final int rows = ExpandedEnderChest.rowsForSlotCount(slotCount);
        assertEquals(expectedRows, rows);
        assertEquals(slotCount, rows * ExpandedEnderChest.SLOTS_PER_ROW, "the menu must expose every container slot");
    }

    @ParameterizedTest
    @ValueSource(ints = {Integer.MIN_VALUE, -1, 0, 1, 4, 5, 13, 53, 54, 60, Integer.MAX_VALUE})
    void rowsAreAlwaysWithinTheSupportedMenuSizes(final int configured) {
        final int rows = ExpandedEnderChest.rowsForSlotCount(configured);
        assertTrue(rows >= ExpandedEnderChest.MIN_ROWS, "rows must not be below one");
        assertTrue(rows <= ExpandedEnderChest.MAX_ROWS, "rows must not be above six");
    }

    @Test
    void normalizeSlotCountIsIdempotent() {
        for (int configured = -20; configured <= 80; configured++) {
            final int normalized = ExpandedEnderChest.normalizeSlotCount(configured);
            assertSame(normalized, ExpandedEnderChest.normalizeSlotCount(normalized), "normalizing twice must be stable");
        }
    }
}
