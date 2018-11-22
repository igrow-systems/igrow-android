package com.igrow.android.bluetooth;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class BluetoothLeAdvPacketParserTest {

    private BluetoothLeAdvPacketParser mAdvPacketParser;

    private static final byte[] CAPTURED_SCAN_RECORD = new byte[]{
            13,
            9,
            105,
            71,
            114,
            111,
            119,
            32,
            80,
            101,
            98,
            98,
            108,
            101,
            3,
            25,
            0,
            3,
            2,
            1,
            6,
            3,
            3,
            26,
            24,
            5,
            22,
            26,
            24,
            8,
            -128,
            0
    };

    private static final int EXPECTED_SEQUENCE = -32760;

    @Before
    public void setUp() throws Exception {
        mAdvPacketParser = new BluetoothLeAdvPacketParser();
        mAdvPacketParser.parse(CAPTURED_SCAN_RECORD);
    }

    @After
    public void tearDown() throws Exception {
    }


    @Test
    public void parse() {

        assertEquals(EXPECTED_SEQUENCE, mAdvPacketParser.getEssSequenceNumber());
    }

    @Test
    public void parseTwice() {

        assertEquals(EXPECTED_SEQUENCE, mAdvPacketParser.getEssSequenceNumber());
        mAdvPacketParser.parse(CAPTURED_SCAN_RECORD);
        assertEquals(EXPECTED_SEQUENCE, mAdvPacketParser.getEssSequenceNumber());
    }
}