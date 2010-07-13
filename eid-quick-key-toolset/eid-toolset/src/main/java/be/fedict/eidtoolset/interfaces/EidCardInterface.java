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

import java.security.NoSuchAlgorithmException;
import javax.smartcardio.CardException;
import be.fedict.eidtoolset.engine.SmartCardReader;
import be.fedict.eidtoolset.exceptions.AIDNotFound;
import be.fedict.eidtoolset.exceptions.InvalidPinException;
import be.fedict.eidtoolset.exceptions.InvalidResponse;
import be.fedict.eidtoolset.exceptions.NoCardConnected;
import be.fedict.eidtoolset.exceptions.NoReadersAvailable;
import be.fedict.eidtoolset.exceptions.NoSuchFeature;
import be.fedict.eidtoolset.exceptions.PinException;
import be.fedict.eidtoolset.exceptions.SignatureGenerationException;
import be.fedict.eidtoolset.exceptions.SmartCardReaderException;
import be.fedict.eidtoolset.exceptions.UnknownCardException;

public interface EidCardInterface {
	public final static String defaultPreferredSmartCardReader = "VASCO";
	public final static int minNofPinDigits = 4;
	public final static int maxNofPinDigits = 6;
	public final static byte acceptedPinLengths = minNofPinDigits * 16 + maxNofPinDigits;
	public final static int defaultTimeoutInMilliSecondsBeforeTryingAnotherReader = 250;
	public final static String eid = "electronic identity card";
	public final static String purse = "electronic purse";
	public final static int UNKOWN_CARD = 0;
	public final static int GENERIC_EID_CARD = 1;
	public final static int BELPIC_CARD = 2;
	public final static int ESTEID_CARD = 3;
	public final static int FINEID_CARD = 4;
	public final static int EMV_CARD = 5;
	public final static int PROTON_CARD = 6;
	public final static String blanc = " ";
	public void changeThisPin(String currentpinvalue, String newpinvalue, int nofMilliSecondsBeforeReturning) throws NoSuchFeature, NoReadersAvailable, InvalidPinException, PinException, InvalidResponse, NoSuchAlgorithmException, CardException,
			AIDNotFound, NoCardConnected;
	public final static String[] cardName = { "Generic" + blanc + eid, "Belpic" + blanc + eid, "Estonian" + blanc + eid, "Finnish" + blanc + eid, "EMV" + blanc + purse, "Proton" + blanc + purse };
	public SmartCardReader getSmartCardReader() throws InvalidResponse, NoReadersAvailable, NoSuchAlgorithmException, CardException, AIDNotFound, NoCardConnected;
	public int returnMyType();
	public int type = UNKOWN_CARD;
	public byte[] selectFile(int fileid) throws UnknownCardException, NoReadersAvailable, NoSuchFeature, SmartCardReaderException, InvalidResponse, NoSuchAlgorithmException, CardException, AIDNotFound, NoCardConnected;
	public byte[] readAuthCertificateBytes() throws UnknownCardException, NoSuchFeature, SmartCardReaderException, InvalidResponse;
	public byte[] readNonRepCertificateBytes() throws UnknownCardException, NoSuchFeature, SmartCardReaderException, InvalidResponse;
	public byte[] readCACertificateBytes() throws UnknownCardException, NoSuchFeature, SmartCardReaderException, InvalidResponse;
	public byte[] readIdentityFileSignatureBytes() throws UnknownCardException, NoSuchFeature, SmartCardReaderException, InvalidResponse;
	public byte[] readAddressFileSignatureBytes() throws UnknownCardException, NoSuchFeature, SmartCardReaderException, InvalidResponse;
	public byte[] readRRNCertificateBytes() throws UnknownCardException, NoReadersAvailable, NoSuchFeature, SmartCardReaderException, InvalidResponse;
	public byte[] readRootCACertificateBytes() throws UnknownCardException, NoReadersAvailable, NoSuchFeature, SmartCardReaderException, InvalidResponse, NoSuchAlgorithmException, CardException, AIDNotFound, NoCardConnected;
	public byte[] readCitizenIdentityDataBytes() throws UnknownCardException, NoReadersAvailable, NoSuchFeature, SmartCardReaderException, InvalidResponse, NoSuchAlgorithmException, CardException, AIDNotFound, NoCardConnected;
	public byte[] readCitizenAddressBytes() throws UnknownCardException, NoReadersAvailable, NoSuchFeature, SmartCardReaderException, InvalidResponse, NoSuchAlgorithmException, CardException, AIDNotFound, NoCardConnected;
	public byte[] readCitizenPhotoBytes() throws NoReadersAvailable, NoSuchFeature, InvalidResponse, NoSuchAlgorithmException, CardException, AIDNotFound, NoCardConnected;
	public byte[] generateAuthenticationSignature(byte[] datahash) throws NoReadersAvailable, SignatureGenerationException, NoSuchFeature, InvalidResponse, NoSuchAlgorithmException, CardException, AIDNotFound, NoCardConnected;
	public byte[] generateNonRepudiationSignature(byte[] datahash) throws NoReadersAvailable, SignatureGenerationException, InvalidResponse, NoReadersAvailable, Exception;
	public byte[] retrieveSignatureBytes() throws NoReadersAvailable, InvalidResponse, NoSuchFeature, NoCardConnected, CardException, NoSuchAlgorithmException, AIDNotFound;
	public byte[] reActivate(String puk1, String puk2) throws InvalidPinException, InvalidResponse, NoSuchFeature, NoReadersAvailable, NoCardConnected, CardException, NoSuchAlgorithmException, AIDNotFound;
	public void setPin(String pinvalue) throws InvalidResponse, NoSuchFeature, NoReadersAvailable, UnknownCardException, SmartCardReaderException, InvalidPinException;
	public String nameOfActiveReader() throws NoReadersAvailable, NoSuchAlgorithmException, CardException, AIDNotFound, NoCardConnected;
	public byte[] fetchATR() throws NoReadersAvailable, NoSuchAlgorithmException, CardException, AIDNotFound, NoCardConnected;
	public void clearCache();
}
