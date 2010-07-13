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
package be.fedict.eidtoolset.engine;

import be.fedict.eidtoolset.exceptions.InvalidResponse;
import be.fedict.eidtoolset.interfaces.SmartCardResponseInterface;
import be.fedict.util.StatusWords;

public final class SmartCardResponse implements SmartCardResponseInterface {
	private byte[] data = null;
	private byte[] statusWords = null;
	public SmartCardResponse(byte[] res) throws InvalidResponse {
		if (res == null)
			throw new InvalidResponse("SmartCardResponse should not be null");
		if (res.length < 2)
			throw new InvalidResponse("SmartCardResponse should consist of at least two bytes");
		data = new byte[res.length - 2];
		System.arraycopy(res, 0, data, 0, res.length - 2);
		statusWords = new byte[2];
		System.arraycopy(res, res.length - 2, statusWords, 0, 2);
	}
	public byte[] getData() {
		return data;
	}
	public String getCommandStatus() throws InvalidResponse {
		return getCommandStatus(statusWords);
	}
	public String getCommandStatus(byte[] sw) throws InvalidResponse {
		return StatusWords.getCommandStatus(sw);
	}
}
