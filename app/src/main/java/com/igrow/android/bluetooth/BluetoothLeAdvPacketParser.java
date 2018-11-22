package com.igrow.android.bluetooth;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static com.igrow.android.bluetooth.IGrowGattServices.ENVIRONMENTAL_SENSING_UUID;

public class BluetoothLeAdvPacketParser {

    private List<UUID> mUuids = new ArrayList<UUID>();
    private byte[] mMfgData = new byte[32];
    private byte[] mServiceData = new byte[32];
    private byte[] mAdvertisedData;

    private int mEssSequenceNumber = 0;

    public void parse(final byte[] scanRecord) {

        mAdvertisedData = Arrays.copyOf(scanRecord, scanRecord.length);

        int offset = 0;
        int i = 0;
        while (offset < (mAdvertisedData.length - 2)) {
            int len = mAdvertisedData[offset++];
            if (len == 0)
                break;

            int type = mAdvertisedData[offset++];
            switch (type) {
                case 0x02: // Partial list of 16-bit UUIDs
                case 0x03: // Complete list of 16-bit UUIDs
                    while (len > 1) {
                        int uuid16 = mAdvertisedData[offset++] & 0xFF;
                        uuid16 |= (mAdvertisedData[offset++] << 8);
                        len -= 2;
                        mUuids.add(UUID.fromString(String.format(
                                "%08x-0000-1000-8000-00805f9b34fb", uuid16)));
                    }
                    break;
                case 0x06:// Partial list of 128-bit UUIDs
                case 0x07:// Complete list of 128-bit UUIDs
                    // Loop through the advertised 128-bit UUID's.
                    while (len >= 16) {
                        try {
                            // Wrap the advertised bits and order them.
                            ByteBuffer buffer = ByteBuffer.wrap(mAdvertisedData,
                                    offset++, 16).order(ByteOrder.LITTLE_ENDIAN);
                            long mostSignificantBit = buffer.getLong();
                            long leastSignificantBit = buffer.getLong();
                            mUuids.add(new UUID(leastSignificantBit,
                                    mostSignificantBit));
                        } catch (IndexOutOfBoundsException e) {
                            // Defensive programming.
                            continue;
                        } finally {
                            // Move the offset to read the next uuid.
                            offset += 15;
                            len -= 16;
                        }
                    }
                    break;

                case 0x16:  // Service Data
                    i = 0;
                    // A zero length is possible for early termination of
                    // advertising data
                    if (len > 1) {
                        int uuid16 = mAdvertisedData[offset++] & 0xFF;
                        uuid16 |= (mAdvertisedData[offset++] << 8);
                        len -= 2;

                        while (len > 1) {
                            if (i < 32) {
                                mServiceData[i++] = mAdvertisedData[offset++];
                            }
                            len -= 1;
                        }
                        // Was this ESS Service data and complete?
                        // If so then store the sequence number
                        if (UUID.fromString(String.format(
                                "%08x-0000-1000-8000-00805f9b34fb", uuid16)).equals(ENVIRONMENTAL_SENSING_UUID)
                                && i == 2) {
                            mEssSequenceNumber = mServiceData[0] & 0xFF;
                            mEssSequenceNumber |= mServiceData[1] << 8;
                        }
                    }
                    break;

                case 0xFF:  // Manufacturer Specific Data
                    i = 0;
                    while (len > 1) {
                        if (i < 32) {
                            mMfgData[i++] = mAdvertisedData[offset++];
                        }
                        len -= 1;
                    }
                    break;
                default:
                    offset += (len - 1);
                    break;
            }
        }
    }

    public List<UUID> getUuids() {
        return mUuids;
    }

    public byte[] getMfgData() {
        return mMfgData;
    }

    public byte[] getServiceData() {
        return mServiceData;
    }

    public int getEssSequenceNumber() {
        return mEssSequenceNumber;
    }

}
