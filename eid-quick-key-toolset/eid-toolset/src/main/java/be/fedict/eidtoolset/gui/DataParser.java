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
package be.fedict.eidtoolset.gui;

import java.util.Hashtable;
import be.fedict.util.TextUtils;

public class DataParser {
	private final static int TAG_TWO_FIRST_FIRST_NAMES = 8;
	private final static int TAG_NOBLE_CONDITION = 14;
	private final static int TAG_SPECIAL_STATUS = 16;
	@SuppressWarnings("unchecked")
	public static void ParseIdentityAddressData(byte[] data, Hashtable addressInfo) {
		try {
			int pos = 0;
			TextUtils.getTagIdentifier(data, pos);
			pos += TextUtils.tagLen;
			int dataLen = TextUtils.getDataLen(data, pos);
			int lenLen = TextUtils.getLenLen(data, pos);
			pos += lenLen;
			String address = TextUtils.getString(data, pos, dataLen);
			pos += dataLen;
			addressInfo.put("Address", address);
			TextUtils.getTagIdentifier(data, pos);
			pos += TextUtils.tagLen;
			dataLen = TextUtils.getDataLen(data, pos);
			lenLen = TextUtils.getLenLen(data, pos);
			pos += lenLen;
			String zip = TextUtils.getString(data, pos, dataLen);
			pos += dataLen;
			addressInfo.put("Zip code", zip);
			TextUtils.getTagIdentifier(data, pos);
			pos += TextUtils.tagLen;
			dataLen = TextUtils.getDataLen(data, pos);
			lenLen = TextUtils.getLenLen(data, pos);
			pos += lenLen;
			String municipality = TextUtils.getString(data, pos, dataLen);
			pos += dataLen;
			addressInfo.put("Municipality", municipality);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@SuppressWarnings("unchecked")
	public static void ParseIdentityData(byte[] data, Hashtable identityInfo) {
		int pos = 0;
		try {
			int tagIdentifier = TextUtils.getTagIdentifier(data, pos);
			pos += TextUtils.tagLen;
			int dataLen = TextUtils.getDataLen(data, pos);
			int lenLen = TextUtils.getLenLen(data, pos);
			pos += lenLen;
			String cardNumber = TextUtils.getString(data, pos, dataLen);
			pos += dataLen;
			identityInfo.put("Card Number", cardNumber);
			tagIdentifier = TextUtils.getTagIdentifier(data, pos);
			pos += TextUtils.tagLen;
			dataLen = TextUtils.getDataLen(data, pos);
			lenLen = TextUtils.getLenLen(data, pos);
			pos += lenLen;
			String chipNumber = TextUtils.hexDump(data, pos, dataLen);
			pos += dataLen;
			identityInfo.put("Chip Number", chipNumber);
			tagIdentifier = TextUtils.getTagIdentifier(data, pos);
			pos += TextUtils.tagLen;
			dataLen = TextUtils.getDataLen(data, pos);
			lenLen = TextUtils.getLenLen(data, pos);
			pos += lenLen;
			String cardValidityBegin = TextUtils.getString(data, pos, dataLen);
			pos += dataLen;
			identityInfo.put("Card validity start date", cardValidityBegin);
			tagIdentifier = TextUtils.getTagIdentifier(data, pos);
			pos += TextUtils.tagLen;
			dataLen = TextUtils.getDataLen(data, pos);
			lenLen = TextUtils.getLenLen(data, pos);
			pos += lenLen;
			String cardValidityEnd = TextUtils.getString(data, pos, dataLen);
			pos += dataLen;
			identityInfo.put("Card validity end date", cardValidityEnd);
			tagIdentifier = TextUtils.getTagIdentifier(data, pos);
			pos += TextUtils.tagLen;
			dataLen = TextUtils.getDataLen(data, pos);
			lenLen = TextUtils.getLenLen(data, pos);
			pos += lenLen;
			String cardDeliveryMunicipality = TextUtils.getString(data, pos, dataLen);
			pos += dataLen;
			identityInfo.put("Card delivery municipality", cardDeliveryMunicipality);
			tagIdentifier = TextUtils.getTagIdentifier(data, pos);
			pos += TextUtils.tagLen;
			dataLen = TextUtils.getDataLen(data, pos);
			lenLen = TextUtils.getLenLen(data, pos);
			pos += lenLen;
			String nationalNumber = TextUtils.getString(data, pos, dataLen);
			pos += dataLen;
			identityInfo.put("National Number", nationalNumber);
			tagIdentifier = TextUtils.getTagIdentifier(data, pos);
			pos += TextUtils.tagLen;
			dataLen = TextUtils.getDataLen(data, pos);
			lenLen = TextUtils.getLenLen(data, pos);
			pos += lenLen;
			String name = TextUtils.getString(data, pos, dataLen);
			pos += dataLen;
			identityInfo.put("Name", name);
			String twoFirstFirstNames = "";
			tagIdentifier = TextUtils.getTagIdentifier(data, pos);
			if (tagIdentifier == TAG_TWO_FIRST_FIRST_NAMES) {
				pos += TextUtils.tagLen;
				dataLen = TextUtils.getDataLen(data, pos);
				lenLen = TextUtils.getLenLen(data, pos);
				pos += lenLen;
				twoFirstFirstNames = TextUtils.getString(data, pos, dataLen);
				pos += dataLen;
				tagIdentifier = TextUtils.getTagIdentifier(data, pos);
			}
			pos += TextUtils.tagLen;
			dataLen = TextUtils.getDataLen(data, pos);
			lenLen = TextUtils.getLenLen(data, pos);
			pos += lenLen;
			String firstLetterThirdFirstName = TextUtils.getString(data, pos, dataLen);
			pos += dataLen;
			identityInfo.put("First names", twoFirstFirstNames + " " + firstLetterThirdFirstName + ".");
			tagIdentifier = TextUtils.getTagIdentifier(data, pos);
			pos += TextUtils.tagLen;
			dataLen = TextUtils.getDataLen(data, pos);
			lenLen = TextUtils.getLenLen(data, pos);
			pos += lenLen;
			String nationality = TextUtils.getString(data, pos, dataLen);
			pos += dataLen;
			identityInfo.put("Nationality", nationality);
			tagIdentifier = TextUtils.getTagIdentifier(data, pos);
			pos += TextUtils.tagLen;
			dataLen = TextUtils.getDataLen(data, pos);
			lenLen = TextUtils.getLenLen(data, pos);
			pos += lenLen;
			String birthLocation = TextUtils.getString(data, pos, dataLen);
			pos += dataLen;
			identityInfo.put("Birth Location", birthLocation);
			tagIdentifier = TextUtils.getTagIdentifier(data, pos);
			pos += TextUtils.tagLen;
			dataLen = TextUtils.getDataLen(data, pos);
			lenLen = TextUtils.getLenLen(data, pos);
			pos += lenLen;
			String birthDate = TextUtils.getString(data, pos, dataLen);
			pos += dataLen;
			identityInfo.put("Birth Date", birthDate);
			tagIdentifier = TextUtils.getTagIdentifier(data, pos);
			pos += TextUtils.tagLen;
			dataLen = TextUtils.getDataLen(data, pos);
			lenLen = TextUtils.getLenLen(data, pos);
			pos += lenLen;
			String gender = TextUtils.getString(data, pos, dataLen);
			pos += dataLen;
			identityInfo.put("Sex", gender);
			tagIdentifier = TextUtils.getTagIdentifier(data, pos);
			if (tagIdentifier == TAG_NOBLE_CONDITION) {
				pos += TextUtils.tagLen;
				dataLen = TextUtils.getDataLen(data, pos);
				lenLen = TextUtils.getLenLen(data, pos);
				pos += lenLen;
				String nobleCondition = TextUtils.getString(data, pos, dataLen);
				pos += dataLen;
				identityInfo.put("Noble condition", nobleCondition);
				tagIdentifier = TextUtils.getTagIdentifier(data, pos);
			}
			pos += TextUtils.tagLen;
			dataLen = TextUtils.getDataLen(data, pos);
			lenLen = TextUtils.getLenLen(data, pos);
			pos += lenLen;
			String documentType = TextUtils.getString(data, pos, dataLen);
			pos += dataLen;
			identityInfo.put("Document type", documentType);
			tagIdentifier = TextUtils.getTagIdentifier(data, pos);
			if (tagIdentifier == TAG_SPECIAL_STATUS) {
				pos += TextUtils.tagLen;
				dataLen = TextUtils.getDataLen(data, pos);
				lenLen = TextUtils.getLenLen(data, pos);
				pos += lenLen;
				String specialStatus = TextUtils.getString(data, pos, dataLen);
				pos += dataLen;
				identityInfo.put("Special status", specialStatus);
				tagIdentifier = TextUtils.getTagIdentifier(data, pos);
			}
			pos += TextUtils.tagLen;
			dataLen = TextUtils.getDataLen(data, pos);
			lenLen = TextUtils.getLenLen(data, pos);
			pos += lenLen;
			String photoHash = TextUtils.hexDump(data, pos, dataLen);
			pos += dataLen;
			identityInfo.put("Hash photo", photoHash);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	public static String ParseCardInfo(byte[] data) {
		try {
			return TextUtils.hexDump(data, data.length - 12, 12);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}
	public static byte[] ParseHashTableToIdentityData(Hashtable info) {
		byte[] data = new byte[1000];
		int pos = 0;
		try {
			// Set tag
			data[pos] = (byte) 0x01;
			pos += TextUtils.tagLen;
			// Store length
			String str = info.get("Card Number").toString();
			int dataLen = str.getBytes("UTF-8").length;
			while (dataLen > 255) {
				data[pos] = (byte) 0xFF;
				pos++;
				dataLen -= 255;
			}
			data[pos] = (byte) dataLen;
			pos++;
			// Store Data
			System.arraycopy(str.getBytes("UTF-8"), 0, data, pos, str.getBytes("UTF-8").length);
			pos += str.getBytes("UTF-8").length;
			// Set tag
			data[pos] = (byte) 0x02;
			pos += TextUtils.tagLen;
			// Store length
			str = info.get("Chip Number").toString();
			// Warning: Chip number is a string of hex values: need to convert
			// in right way
			dataLen = TextUtils.hexStringToByteArray(str).length;
			while (dataLen > 255) {
				data[pos] = (byte) 0xFF;
				pos++;
				dataLen -= 255;
			}
			data[pos] = (byte) dataLen;
			pos++;
			// Store Data
			System.arraycopy(TextUtils.hexStringToByteArray(str), 0, data, pos, TextUtils.hexStringToByteArray(str).length);
			pos += TextUtils.hexStringToByteArray(str).length;
			// Set tag
			data[pos] = (byte) 0x03;
			pos += TextUtils.tagLen;
			// Store length
			str = info.get("Card validity start date").toString();
			dataLen = str.getBytes("UTF-8").length;
			while (dataLen > 255) {
				data[pos] = (byte) 0xFF;
				pos++;
				dataLen -= 255;
			}
			data[pos] = (byte) dataLen;
			pos++;
			// Store Data
			System.arraycopy(str.getBytes("UTF-8"), 0, data, pos, str.getBytes("UTF-8").length);
			pos += str.getBytes("UTF-8").length;
			// Set tag
			data[pos] = (byte) 0x04;
			pos += TextUtils.tagLen;
			// Store length
			str = info.get("Card validity end date").toString();
			dataLen = str.getBytes("UTF-8").length;
			while (dataLen > 255) {
				data[pos] = (byte) 0xFF;
				pos++;
				dataLen -= 255;
			}
			data[pos] = (byte) dataLen;
			pos++;
			// Store Data
			System.arraycopy(str.getBytes("UTF-8"), 0, data, pos, str.getBytes("UTF-8").length);
			pos += str.getBytes("UTF-8").length;
			// Set tag
			data[pos] = (byte) 0x05;
			pos += TextUtils.tagLen;
			// Store length
			str = info.get("Card delivery municipality").toString();
			dataLen = str.getBytes("UTF-8").length;
			while (dataLen > 255) {
				data[pos] = (byte) 0xFF;
				pos++;
				dataLen -= 255;
			}
			data[pos] = (byte) dataLen;
			pos++;
			// Store Data
			System.arraycopy(str.getBytes("UTF-8"), 0, data, pos, str.getBytes("UTF-8").length);
			pos += str.getBytes("UTF-8").length;
			// Set tag
			data[pos] = (byte) 0x06;
			pos += TextUtils.tagLen;
			// Store length
			str = info.get("National Number").toString();
			dataLen = str.getBytes("UTF-8").length;
			while (dataLen > 255) {
				data[pos] = (byte) 0xFF;
				pos++;
				dataLen -= 255;
			}
			data[pos] = (byte) dataLen;
			pos++;
			// Store Data
			System.arraycopy(str.getBytes("UTF-8"), 0, data, pos, str.getBytes("UTF-8").length);
			pos += str.getBytes("UTF-8").length;
			// Set tag
			data[pos] = (byte) 0x07;
			pos += TextUtils.tagLen;
			// Store length
			str = info.get("Name").toString();
			dataLen = str.getBytes("UTF-8").length;
			while (dataLen > 255) {
				data[pos] = (byte) 0xFF;
				pos++;
				dataLen -= 255;
			}
			data[pos] = (byte) dataLen;
			pos++;
			// Store Data
			System.arraycopy(str.getBytes("UTF-8"), 0, data, pos, str.getBytes("UTF-8").length);
			pos += str.getBytes("UTF-8").length;
			str = info.get("First names").toString();
			// If only the first letter of the first name is given, str should
			// contain 2 characters
			if (str.length() > 2) {
				// Set tag
				data[pos] = TAG_TWO_FIRST_FIRST_NAMES;
				pos += TextUtils.tagLen;
				// Store length
				str = str.substring(0, (str.length() - 3));
				dataLen = str.getBytes("UTF-8").length;
				while (dataLen > 255) {
					data[pos] = (byte) 0xFF;
					pos++;
					dataLen -= 255;
				}
				data[pos] = (byte) dataLen;
				pos++;
				// Store Data
				System.arraycopy(str.getBytes("UTF-8"), 0, data, pos, str.getBytes("UTF-8").length);
				pos += str.getBytes("UTF-8").length;
			}
			// Set tag
			data[pos] = (byte) 0x09;
			pos += TextUtils.tagLen;
			// Store length
			str = info.get("First names").toString();
			str = str.substring((str.length() - 2), (str.length() - 1));
			dataLen = str.getBytes("UTF-8").length;
			while (dataLen > 255) {
				data[pos] = (byte) 0xFF;
				pos++;
				dataLen -= 255;
			}
			data[pos] = (byte) dataLen;
			pos++;
			// Store Data
			System.arraycopy(str.getBytes("UTF-8"), 0, data, pos, str.getBytes("UTF-8").length);
			pos += str.getBytes("UTF-8").length;
			// Set tag
			data[pos] = (byte) 0x0a;
			pos += TextUtils.tagLen;
			// Store length
			str = info.get("Nationality").toString();
			dataLen = str.getBytes("UTF-8").length;
			while (dataLen > 255) {
				data[pos] = (byte) 0xFF;
				pos++;
				dataLen -= 255;
			}
			data[pos] = (byte) dataLen;
			pos++;
			// Store Data
			System.arraycopy(str.getBytes("UTF-8"), 0, data, pos, str.getBytes("UTF-8").length);
			pos += str.getBytes("UTF-8").length;
			// Set tag
			data[pos] = (byte) 0x0b;
			pos += TextUtils.tagLen;
			// Store length
			str = info.get("Birth Location").toString();
			dataLen = str.getBytes("UTF-8").length;
			while (dataLen > 255) {
				data[pos] = (byte) 0xFF;
				pos++;
				dataLen -= 255;
			}
			data[pos] = (byte) dataLen;
			pos++;
			// Store Data
			System.arraycopy(str.getBytes("UTF-8"), 0, data, pos, str.getBytes("UTF-8").length);
			pos += str.getBytes("UTF-8").length;
			// Set tag
			data[pos] = (byte) 0x0c;
			pos += TextUtils.tagLen;
			// Store length
			str = info.get("Birth Date").toString();
			dataLen = str.getBytes("UTF-8").length;
			while (dataLen > 255) {
				data[pos] = (byte) 0xFF;
				pos++;
				dataLen -= 255;
			}
			data[pos] = (byte) dataLen;
			pos++;
			// Store Data
			System.arraycopy(str.getBytes("UTF-8"), 0, data, pos, str.getBytes("UTF-8").length);
			pos += str.getBytes("UTF-8").length;
			// Set tag
			data[pos] = (byte) 0x0d;
			pos += TextUtils.tagLen;
			// Store length
			str = info.get("Sex").toString();
			dataLen = str.getBytes("UTF-8").length;
			while (dataLen > 255) {
				data[pos] = (byte) 0xFF;
				pos++;
				dataLen -= 255;
			}
			data[pos] = (byte) dataLen;
			pos++;
			// Store Data
			System.arraycopy(str.getBytes("UTF-8"), 0, data, pos, str.getBytes("UTF-8").length);
			pos += str.getBytes("UTF-8").length;
			if (info.containsKey("Noble condition")) {
				// Set tag
				data[pos] = TAG_NOBLE_CONDITION;
				pos += TextUtils.tagLen;
				// Store length
				str = info.get("Noble condition").toString();
				dataLen = str.getBytes("UTF-8").length;
				while (dataLen > 255) {
					data[pos] = (byte) 0xFF;
					pos++;
					dataLen -= 255;
				}
				data[pos] = (byte) dataLen;
				pos++;
				// Store Data
				System.arraycopy(str.getBytes("UTF-8"), 0, data, pos, str.getBytes("UTF-8").length);
				pos += str.getBytes("UTF-8").length;
			}
			// Set tag
			data[pos] = (byte) 0x0f;
			pos += TextUtils.tagLen;
			// Store length
			str = info.get("Document type").toString();
			dataLen = str.getBytes("UTF-8").length;
			while (dataLen > 255) {
				data[pos] = (byte) 0xFF;
				pos++;
				dataLen -= 255;
			}
			data[pos] = (byte) dataLen;
			pos++;
			// Store Data
			System.arraycopy(str.getBytes("UTF-8"), 0, data, pos, str.getBytes("UTF-8").length);
			pos += str.getBytes("UTF-8").length;
			if (info.containsKey("Special status")) {
				// Set tag
				data[pos] = TAG_SPECIAL_STATUS;
				pos += TextUtils.tagLen;
				// Store length
				str = info.get("Special status").toString();
				dataLen = str.getBytes("UTF-8").length;
				while (dataLen > 255) {
					data[pos] = (byte) 0xFF;
					pos++;
					dataLen -= 255;
				}
				data[pos] = (byte) dataLen;
				pos++;
				// Store Data
				System.arraycopy(str.getBytes("UTF-8"), 0, data, pos, str.getBytes("UTF-8").length);
				pos += str.getBytes("UTF-8").length;
			}
			// The last data field contains the hash of the picture (SHA-1)
			// Set tag
			data[pos] = (byte) 0x11;
			pos += TextUtils.tagLen;
			// Store length
			str = info.get("Hash photo").toString();
			// Warning: Hash is a string of hex values: need to convert in right
			// way
			dataLen = TextUtils.hexStringToByteArray(str).length;
			while (dataLen > 255) {
				data[pos] = (byte) 0xFF;
				pos++;
				dataLen -= 255;
			}
			data[pos] = (byte) dataLen;
			pos++;
			// Store Data
			System.arraycopy(TextUtils.hexStringToByteArray(str), 0, data, pos, TextUtils.hexStringToByteArray(str).length);
			pos += TextUtils.hexStringToByteArray(str).length;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return java.util.Arrays.copyOfRange(data, 0, pos);
	}
	public static byte[] ParseHashTableToAddressData(Hashtable info) {
		byte[] data = new byte[113];
		int pos = 0;
		try {
			// Set tag
			data[pos] = (byte) 0x01;
			pos += TextUtils.tagLen;
			// Store length
			String str = info.get("Address").toString();
			int dataLen = str.getBytes("UTF-8").length;
			while (dataLen > 255) {
				data[pos] = (byte) 0xFF;
				pos++;
				dataLen -= 255;
			}
			data[pos] = (byte) dataLen;
			pos++;
			// Store Data
			System.arraycopy(str.getBytes("UTF-8"), 0, data, pos, str.getBytes("UTF-8").length);
			pos += str.getBytes("UTF-8").length;
			// Set tag
			data[pos] = (byte) 0x02;
			pos += TextUtils.tagLen;
			// Store length
			str = info.get("Zip code").toString();
			dataLen = str.getBytes("UTF-8").length;
			while (dataLen > 255) {
				data[pos] = (byte) 0xFF;
				pos++;
				dataLen -= 255;
			}
			data[pos] = (byte) dataLen;
			pos++;
			// Store Data
			System.arraycopy(str.getBytes("UTF-8"), 0, data, pos, str.getBytes("UTF-8").length);
			pos += str.getBytes("UTF-8").length;
			// Set tag
			data[pos] = (byte) 0x03;
			pos += TextUtils.tagLen;
			// Store length
			str = info.get("Municipality").toString();
			dataLen = str.getBytes("UTF-8").length;
			while (dataLen > 255) {
				data[pos] = (byte) 0xFF;
				pos++;
				dataLen -= 255;
			}
			data[pos] = (byte) dataLen;
			pos++;
			// Store Data
			System.arraycopy(str.getBytes("UTF-8"), 0, data, pos, str.getBytes("UTF-8").length);
			pos += str.getBytes("UTF-8").length;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		// NOTE: as address is 113 bytes, we need to pad the remaining bytes
		// with zeros
		// As data is initialised to 113 bytes, this is not a problem
		return data;
	}
}
