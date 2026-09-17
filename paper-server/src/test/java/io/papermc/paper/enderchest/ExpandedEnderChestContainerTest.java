package io.papermc.paper.enderchest;

import io.papermc.paper.configuration.GlobalConfiguration;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.PlayerEnderChestContainer;
import org.bukkit.support.environment.Normal;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

/**
 * Checks that the real player ender chest container is the thing that gets resized, rather than a
 * second inventory that items would have to be copied into.
 */
@Normal
class ExpandedEnderChestContainerTest {

    @ParameterizedTest
    @ValueSource(ints = {9, 18, 27, 36, 45, 54})
    void theRealContainerHoldsTheConfiguredNumberOfSlots(final int slotCount) {
        final GlobalConfiguration.Misc misc = GlobalConfiguration.get().misc;
        final int previous = misc.enderChestSlotCount;
        try {
            misc.enderChestSlotCount = slotCount;

            final PlayerEnderChestContainer container = new PlayerEnderChestContainer(mock(Player.class));

            assertEquals(slotCount, container.getContainerSize(), "the real ender chest container must hold the configured number of slots");
            assertEquals(
                ExpandedEnderChest.rowsForSlotCount(slotCount),
                container.getContainerSize() / ExpandedEnderChest.SLOTS_PER_ROW,
                "the chest menu rows must cover every container slot"
            );
        } finally {
            misc.enderChestSlotCount = previous;
        }
    }
}
