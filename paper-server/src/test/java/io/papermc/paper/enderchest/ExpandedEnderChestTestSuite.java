package io.papermc.paper.enderchest;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;

/**
 * Focused suite for the expandable ender chest sizing logic.
 *
 * <p>Paper's test task only picks up classes named {@code *TestSuite}, so this exists to make
 * {@link ExpandedEnderChestTest} runnable on its own.</p>
 */
@Suite(failIfNoTests = true)
@SuiteDisplayName("Expanded ender chest configuration and menu sizing")
@SelectClasses({ExpandedEnderChestTest.class, ExpandedEnderChestContainerTest.class})
public class ExpandedEnderChestTestSuite {
}
