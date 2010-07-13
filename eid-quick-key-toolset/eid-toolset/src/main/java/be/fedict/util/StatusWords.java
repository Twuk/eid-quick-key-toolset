/*
 * Quick-Key Toolset Project.
 * Copyright (C) 2010 FedICT.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License version
 * 3.0 as published by the Free Software Foundation.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, see 
 * http://www.gnu.org/licenses/.
 */
package be.fedict.util;

import be.fedict.eidtoolset.exceptions.InvalidResponse;

public class StatusWords {
	public final static String separator = ": ";
	public static String getCommandStatus(byte[] sw) throws InvalidResponse {
		if (sw == null)
			throw new InvalidResponse("StatusWords should not be null...");
		if (sw.length != 2)
			throw new InvalidResponse("StatusWords should consist of two bytes...");
		String sw1Hex = TextUtils.hexDump(sw, 0, 1);
		String sw2Hex = TextUtils.hexDump(sw, 1, 1);
		return sw1Hex + sw2Hex;
	}
}
