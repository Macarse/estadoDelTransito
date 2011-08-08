package com.appspot.estadodeltransito.parsers;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import com.appspot.estadodeltransito.domain.highway.Highway;


public class AvenuesParserTest {

    @Test
    public void getUrlShouldNotBeNull() {
        assertNotNull("HTML couldn't be downloaded", AvenuesParser.getUrlContent());
    }

    @Test
    public void testParser() {
        List<Highway> avenues = AvenuesParser.getHighways(AvenuesParser.getUrlContent());
        assertFalse("Avenues group couldn't be matched", avenues.isEmpty());
    }
}
