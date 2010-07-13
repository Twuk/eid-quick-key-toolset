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

import javax.smartcardio.CardException;
import be.fedict.eidtoolset.exceptions.GeneralSecurityException;
import be.fedict.eidtoolset.exceptions.InvalidResponse;
import be.fedict.eidtoolset.exceptions.NoCardConnected;
import be.fedict.eidtoolset.exceptions.NoSuchFileException;
import be.fedict.eidtoolset.interfaces.SmartCardCommandsInterface;
import be.fedict.util.MathUtils;
import be.fedict.util.TextUtils;

public class SmartCard implements SmartCardCommandsInterface {
	protected SmartCardReader myReader = null;
	public final static byte[] readBinaryBlockCommand = { (byte) 0x00, (byte) 0xB0, (byte) 0x00, (byte) 0x00, (byte) 0x00 };
	public byte[] readBinaryFile(byte[] selectFileCommand) throws InvalidResponse, NoCardConnected, CardException, NoSuchFileException {
		int wordLength = 1;
		int blocklength = 0x60;
		wordLength = 1;
		blocklength = 0xf8;
		int len = 0;
		int enough = -5000;
		byte[] certificate = new byte[10000];
		byte[] keyBytes = myReader.sendCommand(selectFileCommand);
		
		if ((keyBytes[keyBytes.length - 2] == (byte) 0x6A) && (keyBytes[keyBytes.length - 1] == (byte) 0x82)){
			throw new NoSuchFileException();
		} else if ((keyBytes[keyBytes.length - 2] != (byte) 0x90) || (keyBytes[keyBytes.length - 1] != 0)){
			throw new InvalidResponse();
		}
		
		while (enough < 0) {
			readBinaryBlockCommand[2] = (byte) (len / wordLength / 256);
			readBinaryBlockCommand[3] = (byte) (len / wordLength % 256);
			readBinaryBlockCommand[4] = (byte) blocklength;
			keyBytes = myReader.sendCommand(readBinaryBlockCommand);
			if ((keyBytes[keyBytes.length - 2] == (byte) 0x90) && (keyBytes[keyBytes.length - 1] == 0)) {
				for (int j = 0; j < MathUtils.min(blocklength, keyBytes.length - 2); j++) {
					certificate[len + j] = keyBytes[j];
				}
				if ((keyBytes.length - 2) < blocklength) {
					len += (keyBytes.length - 2);
					enough = 0;
				} else {
					len += blocklength;
					enough = -1;
				}
			} else throw new InvalidResponse();
		}
		len = MathUtils.unsignedInt(len);
		byte[] tmp = new byte[len];
		for (int k = 0; k < len; k++)
			tmp[k] = certificate[k];
		return tmp;
	}
	public byte[] writeBinaryFile(byte[] selectFileCommand, byte[] data) throws InvalidResponse, NoCardConnected, CardException, GeneralSecurityException {
		// Start selecting the File to write. Returns APDU response. Should be
		// 0x9000
		byte[] keyBytes = myReader.sendCommand(selectFileCommand);
		if (keyBytes[0] != (byte) 0x90 && keyBytes[1] != (byte) 0x00)
			throw new InvalidResponse();
		// Write data to the selected File:
		// NOTE only 255 bytes of data can be sent in one APDU (on top of the 5
		// byte header)
		byte[] writeBinaryBlockCommand = new byte[5 + 255];
		// Put the CLA and the INS for binary update in the command
		writeBinaryBlockCommand[0] = (byte) 0x00;
		writeBinaryBlockCommand[1] = (byte) 0xD6;
		int offset = 0;
		int writeDataLength = 0;
		int apdusLeft = 0;
		if ((data.length) % (255) == 0) {
			apdusLeft = (data.length) / (255);
		} else
			apdusLeft = (data.length / (255)) + 1;
		for (int i = 0; i < apdusLeft; i++) {
			offset = i * 255;
			if (i == apdusLeft - 1 && (data.length) % (255) != 0)
				writeDataLength = (data.length) % (255);
			else
				writeDataLength = 255;
			// NOTE: P1 and P2 should contain the offset in the file where the
			// currently send bytes should be stored
			// Lc contains the number of bytes send and can not be bigger than
			// the available bytes in the file, starting at the offset
			// This means that sending data that is to long for the selected
			// File will result in an exception thrown: illegal length
			// No Le is provided as no response data is expected
			writeBinaryBlockCommand[2] = (byte) (offset >>> 8);// P1
			writeBinaryBlockCommand[3] = (byte) (offset);// P2
			writeBinaryBlockCommand[4] = (byte) writeDataLength;
			// copy data in command, starting at offset 5
			System.arraycopy(data, offset, writeBinaryBlockCommand, 5, writeDataLength);
			// send first writeDataLength + 5 bytes of writeCommand
			keyBytes = myReader.sendCommand(java.util.Arrays.copyOfRange(writeBinaryBlockCommand, 0, (5 + writeDataLength)));
			// check if the card is writable
			// (ISO7816.SW_SECURITY_STATUS_NOT_SATISFIED)
			if (keyBytes[0] == (byte) 0x69 && keyBytes[1] == (byte) 0x82) {
				System.err.println("No write access to card. Exception in response: <" + TextUtils.hexDump(keyBytes) + ">");
				throw new GeneralSecurityException();
			} else if (keyBytes[0] == (byte) 0x67 && keyBytes[1] == (byte) 0x00) {
				System.err.println("Invalid data length: <" + TextUtils.hexDump(keyBytes) + ">");
				throw new InvalidResponse();
			} else if (keyBytes[0] != (byte) 0x90 || keyBytes[1] != (byte) 0x00) {
				System.err.println("Invalid response during write: <" + TextUtils.hexDump(keyBytes) + ">");
				System.out.println("total apdus: " + apdusLeft);
				System.out.println("apdu number: " + i);
				System.out.println("write data length: " + writeDataLength);
				throw new InvalidResponse();
			}
		}
		return keyBytes;
	}
	public String[] getSupportedCardTypes() {
		return supportedCardTypes;
	}
}
