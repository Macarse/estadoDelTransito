package com.appspot.estadodeltransito.parsers;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import com.appspot.estadodeltransito.domain.highway.Highway;


public class HighwaysParserTest {

    @Test
    public void getUrlShouldNotBeNull() {
        assertNotNull("HTML couldn't be downloaded", HighwaysParser.getUrlContent());
    }

    @Test
    public void testParser() {
        List<Highway> highways = HighwaysParser.getHighways(HighwaysParser.getUrlContent());
        assertFalse("Highways group couldn't be matched", highways.isEmpty());
    }
}
