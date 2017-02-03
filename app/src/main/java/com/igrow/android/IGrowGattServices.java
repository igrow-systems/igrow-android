
/*
 * (c) Copyright 2015 Argusat Limited
 *
 * This file is part of iGrow Ladybird Android.
 *
 * iGrow Ladybird Android is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * iGrow Ladybird Android is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with iGrow Ladybird Android.  If not, see <http://www.gnu.org/licenses/>.

*/

package com.igrow.android;

import java.util.UUID;

/**
 * Created by jsr on 02/06/15.
 */
public class IGrowGattServices {

    // Service String representation of UUID
    public static String GENERIC_ACCESS = "00001800-0000-1000-8000-00805f9b34fb";

    public static String GENERIC_ATTRIBUTE = "00001801-0000-1000-8000-00805f9b34fb";

    public static String DEVICE_INFORMATION = "0000180a-0000-1000-8000-00805f9b34fb";

    public static String IMMEDIATE_ALERT = "00001802-0000-1000-8000-00805f9b34fb";

    public static String BATTERY = "0000180f-0000-1000-8000-00805f9b34fb";

    public static String ENVIRONMENTAL_SENSING = "0000181a-0000-1000-8000-00805f9b34fb";

    // Service UUIDs
    public static UUID GENERIC_ACCESS_UUID = UUID.fromString(GENERIC_ACCESS);

    public static UUID GENERIC_ATTRIBUTE_UUID = UUID.fromString(GENERIC_ATTRIBUTE);

    public static UUID DEVICE_INFORMATION_UUID = UUID.fromString(DEVICE_INFORMATION);

    public static UUID IMMEDIATE_ALERT_UUID = UUID.fromString(IMMEDIATE_ALERT);

    public static UUID BATTERY_UUID = UUID.fromString(BATTERY);

    public static UUID ENVIRONMENTAL_SENSING_UUID = UUID.fromString(ENVIRONMENTAL_SENSING);
}
