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
public interface SmartCardReaderCommandsInterface {
	public final byte[] changePINCommand = {
	// change pin vasco dp850 -- begin
			(byte) 0xf1, // cla: reader commands
			(byte) 0x95, // ins: local pin change
			(byte) 0x70, // p1: local pin change
			(byte) 0xf1, // p2: full mask is present
			(byte) 0x18, // p3: length of data sent to the smartcard reader
			(byte) 0x46, // between 4 and 6 digits are allowed
			(byte) 0x00, // cla: change pin command
			(byte) 0x24, // ins: change pin command
			(byte) 0x00, // p1: change pin command
			(byte) 0x01, // p2: change pin command
			(byte) 0x10, // p3: change pin command, 16 bytes will follow
			(byte) 0x03, // pin is in bcd format
			(byte) 0x00, // no watchdog
			(byte) 0x2f, // header of first pin block
			(byte) 0xff, // pin block byte 2
			(byte) 0xff, // pin block byte 3
			(byte) 0xFF, // pin block byte 4
			(byte) 0xFF, // pin block byte 5
			(byte) 0xFF, // pin block byte 6
			(byte) 0xFF, // pin block byte 7
			(byte) 0xFF, // pin block byte 8
			(byte) 0x2f, // header of second pin block
			(byte) 0xff, // pin block byte 2
			(byte) 0xff, // pin block byte 3
			(byte) 0xFF, // pin block byte 4
			(byte) 0xFF, // pin block byte 5
			(byte) 0xFF, // pin block byte 6
			(byte) 0xFF, // pin block byte 7
			(byte) 0xFF, // pin block byte 8
	};
	public final byte[] VerifyPINCommand = {
	// verify pin vasco dp850 -- begin
			(byte) 0xf1, // cla: verify pin for reader
			(byte) 0x95, // ins: local verify pin
			(byte) 0x30, // p1: local pin entry
			(byte) 0xf1, // p2: full mask follows
			(byte) 0x10, // p3: 12 data bytes follow
			(byte) 0x46, // between 4 and 6 digits are allowed
			(byte) 0x00, // cla: verify pin for smartcard
			(byte) 0x20, // ins: verify pin
			(byte) 0x00, // p1: verify pin
			(byte) 0x01, // p2: verify pin
			(byte) 0x08, // p3: 8 pin block bytes follow
			(byte) 0x03, // pin is in bcd format
			(byte) 0x00, // no watchdog
			(byte) 0x24, // pin block byte 1
			(byte) 0xFF, // pin block byte 2
			(byte) 0xFF, // pin block byte 3
			(byte) 0xFF, // pin block byte 4
			(byte) 0xFF, // pin block byte 5
			(byte) 0xFF, // pin block byte 6
			(byte) 0xFF, // pin block byte 7
			(byte) 0xFF, // pin block byte 8
	};
	public final byte[] PollingCommand = {
	// polling end of verify pin vasco dp850 -- end
			(byte) 0xf1, (byte) 0x61, (byte) 0x00, (byte) 0x00, (byte) 0x04 };
	public final byte[] GetReaderResultCommand = {
	// result vasco dp850 reader command -- begin
			(byte) 0xf1, (byte) 0x93, (byte) 0x30, (byte) 0x00, (byte) 0x02 };
	public final byte[] ResetReaderCommand = { (byte) 0xf1, (byte) 0x63, (byte) 0x00, (byte) 0x00, (byte) 0x04 };
}
