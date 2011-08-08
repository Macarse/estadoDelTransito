package com.appspot.estadodeltransito.parsers;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Test;

import com.appspot.estadodeltransito.domain.train.Train;


public class TrainsParserTest {

    @Test
    public void getUrlShouldNotBeNull() {
        assertNotNull("HTML couldn't be downloaded", TrainsParser.getUrlContent());
    }

    @Test
    public void testParser() {
        List<Train> trains = TrainsParser.getTrains(TrainsParser.getUrlContent());
        assertFalse("Trains group couldn't be matched", trains.isEmpty());
    }
}
