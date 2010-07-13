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

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import javax.smartcardio.CardException;
import javax.xml.bind.JAXBException;
import be.fedict.eidtoolset.exceptions.AIDNotFound;
import be.fedict.eidtoolset.exceptions.InvalidPinException;
import be.fedict.eidtoolset.exceptions.InvalidResponse;
import be.fedict.eidtoolset.exceptions.NoCardConnected;
import be.fedict.eidtoolset.exceptions.NoReadersAvailable;
import be.fedict.eidtoolset.exceptions.NoSuchFeature;
import be.fedict.eidtoolset.exceptions.SmartCardReaderException;
import be.fedict.eidtoolset.exceptions.UnknownCardException;
import be.fedict.eidtoolset.interfaces.BelpicCommandsInterface;
import be.fedict.eidtoolset.interfaces.EidCardInterface;

public final class BelpicCard extends EidCard implements BelpicCommandsInterface {
	public BelpicCard(String appName) throws UnknownCardException, SmartCardReaderException, CertificateException, NoSuchAlgorithmException, JAXBException, IOException {
		super(EidCardInterface.BELPIC_CARD, appName);
	}
	public void reActivateCard(String puk1, String puk2) throws InvalidPinException, InvalidResponse, NoReadersAvailable, NoSuchFeature, SmartCardReaderException, UnknownCardException, NoSuchAlgorithmException, NoCardConnected, CardException, AIDNotFound {
		reActivate(puk1, puk2);
	}
}
