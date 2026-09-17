package io.papermc.paper.enderchest;

/**
 * Slot count maths for the expandable native ender chest.
 *
 * <p>The ender chest is always backed by the player's real player ender chest container, so this
 * only decides how many slots that container holds and how large the chest menu wrapping it is.</p>
 */
public final class ExpandedEnderChest {

    /** Slot count used when the server does not configure a custom value. */
    public static final int DEFAULT_SLOT_COUNT = 27;
    /** Smallest supported ender chest size. */
    public static final int MIN_SLOT_COUNT = 9;
    /** Largest supported ender chest size. */
    public static final int MAX_SLOT_COUNT = 54;
    /** Width of a chest row, in slots. */
    public static final int SLOTS_PER_ROW = 9;
    /** Number of rows in the smallest supported menu. */
    public static final int MIN_ROWS = 1;
    /** Number of rows in the largest supported menu. */
    public static final int MAX_ROWS = 6;

    private ExpandedEnderChest() {
    }

    /**
     * Returns whether the given slot count is one of the supported ender chest sizes, that is a
     * multiple of {@value #SLOTS_PER_ROW} between {@value #MIN_SLOT_COUNT} and
     * {@value #MAX_SLOT_COUNT}.
     *
     * @param slotCount the configured slot count
     * @return true if the slot count can be used as is
     */
    public static boolean isValidSlotCount(final int slotCount) {
        return slotCount >= MIN_SLOT_COUNT && slotCount <= MAX_SLOT_COUNT && slotCount % SLOTS_PER_ROW == 0;
    }

    /**
     * Clamps the given slot count into the supported range and rounds it to the nearest multiple of
     * {@value #SLOTS_PER_ROW}, so that an unusable configured value never reaches the container.
     *
     * @param slotCount the configured slot count
     * @return a supported slot count
     */
    public static int normalizeSlotCount(final int slotCount) {
        final int clamped = Math.clamp(slotCount, MIN_SLOT_COUNT, MAX_SLOT_COUNT);
        final int rounded = (clamped + SLOTS_PER_ROW / 2) / SLOTS_PER_ROW * SLOTS_PER_ROW;
        return Math.clamp(rounded, MIN_SLOT_COUNT, MAX_SLOT_COUNT);
    }

    /**
     * Returns the number of chest rows needed for the given slot count, which is the number of rows
     * the native chest menu must be created with.
     *
     * @param slotCount the configured slot count
     * @return a row count between {@value #MIN_ROWS} and {@value #MAX_ROWS}
     */
    public static int rowsForSlotCount(final int slotCount) {
        return Math.clamp(normalizeSlotCount(slotCount) / SLOTS_PER_ROW, MIN_ROWS, MAX_ROWS);
    }
}
