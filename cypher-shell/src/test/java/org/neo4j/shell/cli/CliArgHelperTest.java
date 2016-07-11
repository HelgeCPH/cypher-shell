package org.neo4j.shell.cli;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.neo4j.shell.Util.asArray;

public class CliArgHelperTest {

    @Test
    public void testFailFastIsDefault() {
        assertEquals("Unexpected fail-behavior", CliArgHelper.FailBehavior.FAIL_FAST,
                CliArgHelper.parse(asArray()).getFailBehavior());
    }

    @Test
    public void testFailFastIsParsed() {
        assertEquals("Unexpected fail-behavior", CliArgHelper.FailBehavior.FAIL_FAST,
                CliArgHelper.parse(asArray("--fail-fast")).getFailBehavior());
    }

    @Test
    public void testFailAtEndIsParsed() {
        assertEquals("Unexpected fail-behavior", CliArgHelper.FailBehavior.FAIL_AT_END,
                CliArgHelper.parse(asArray("--fail-at-end")).getFailBehavior());
    }

    @Test
    public void singlePositionalArgumentIsFine() {
        String text = "Single string";
        assertEquals("Did not parse cypher string", text,
                CliArgHelper.parse(asArray(text)).getCypher().get());
    }
}
