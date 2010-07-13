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
package be.fedict.eidtoolset.interfaces;

import be.fedict.eidtoolset.exceptions.InvalidPinException;
import be.fedict.eidtoolset.exceptions.InvalidResponse;
import be.fedict.eidtoolset.exceptions.SmartCardReaderException;
import be.fedict.eidtoolset.exceptions.UnknownCardException;

public interface SmartCardInterface {
	public int type = 0;
	public byte[] getRandom(int length) throws Exception;
	public void verifyPin(String pinvalue) throws UnknownCardException, SmartCardReaderException, InvalidPinException;
	public void changePin(String currentPin, String newPin, String newPinConfirmation) throws UnknownCardException, SmartCardReaderException, InvalidPinException;
	public byte[] readBinaryFile(int fileSelectionCommand) throws UnknownCardException, SmartCardReaderException, InvalidResponse;
	public byte[] sendCommand(byte[] command);
	public void open() throws UnknownCardException, SmartCardReaderException;
	public String getSmartCardReaderName();
	public void close();
	public byte[] fetchATR();
}
