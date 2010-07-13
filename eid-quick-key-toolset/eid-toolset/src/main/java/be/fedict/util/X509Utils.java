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

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Hashtable;
import javax.security.auth.x500.X500Principal;
import be.fedict.eidtoolset.gui.DataParser;

public class X509Utils {
	public static X509Certificate deriveCertificateFrom(InputStream inStream) throws IOException, CertificateException {
		CertificateFactory cf = CertificateFactory.getInstance("X.509");
		X509Certificate cert = (X509Certificate) cf.generateCertificate(inStream);
		inStream.close();
		return cert;
	}
	public static X509Certificate deriveCertificateFrom(byte[] bytes) throws IOException, CertificateException {
		return deriveCertificateFrom(new ByteArrayInputStream(bytes));
	}
	public static void dumpCertificateToFile(String filename, X509Certificate cert) throws CertificateEncodingException, FileNotFoundException, IOException {
		FileOutputStream file = new FileOutputStream(filename);
		file.write(cert.getEncoded());
		file.close();
	}
	public static X509Certificate importCertificateFrom(File file) throws CertificateException, IOException {
		return deriveCertificateFrom(new FileInputStream(file));
	}
	public static byte[] convertCertificateToDER(java.security.cert.Certificate cert) throws CertificateEncodingException {
		return cert.getEncoded();
	}
	/**
	 * This method allows to change the subject data of the certificate. It is
	 * not used yet.
	 * 
	 * @param cert
	 * @param id
	 * @return new certificate
	 * @throws CertificateException
	 * @throws IOException
	 */
	public static byte[] changeCertSubjectData(byte[] cert, byte[] id) throws CertificateException, IOException {
		// The subject data is located at tag 8 in the certificate
		Hashtable table = new Hashtable();
		DataParser.ParseIdentityData(id, table);
		String first = table.get("First names").toString().substring(0, (table.get("First names").toString().length() - 3));
		String surname = table.get("Name").toString();
		String[] names = first.split(" ");
		String name = names[0] + " " + surname;
		String subj = "SERIALNUMBER = " + table.get("National Number").toString() + ", GIVENNAME = " + first + ", SURNAME = " + surname + ", CN = " + name + " (Signature)" + ", C = BE";
		return setDataAtTag(cert, new X500Principal(subj).getEncoded(), 8);
	}
	/**
	 * This method puts a new public key in the given certificate. The key is in
	 * the form: '0x02, 0x08, 8 byte exponent, 0x03, 0x8180, 128 byte modulus
	 * 
	 * @param key
	 * @param id
	 * @throws CertificateException
	 * @throws IOException
	 */
	public static byte[] changeCertPublicKey(byte[] cert, byte[] key) throws CertificateException, IOException {
		// The public key is located at tag 13 (modulus) and 14 (exponent) in
		// the certificate
		// The key is in the form: '0x02, 0x08, 8 byte exponent, 0x03, 0x8180,
		// 128 byte modulus
		// For the exponent: in the cert it is always in three bytes, so here as
		// well
		byte[] keyExp = new byte[3];
		// Note: the modulus in a certificate is preceded by a 0 byte: include
		// this here
		byte[] keyMod = new byte[(short) (key[12] & 0xff) + 1];
		System.arraycopy(key, 7, keyExp, 0, keyExp.length);
		System.arraycopy(key, 13, keyMod, 1, keyMod.length - 1);
		byte[] newCert = setDataAtTag(cert, keyMod, 13);
		return setDataAtTag(newCert, keyExp, 14);
	}
	public static byte[] returnDataAtTag(byte[] cert, int tag) {
		int tagnumber = 1;
		int i = 0;
		int offset = 0;
		int length = 0;
		while (i < cert.length) {
			if ((cert[i] & 0xff) == 0)
				i += 1;
			if (((cert[i] & 0xff) > (short) 0x30)) {
				// Tag id is larger than one byte
				i += 1;
				while ((short) (cert[i] & 0xff) > 127) {
					i += 1;
				}
				i += 1;
			} else {
				i += 1;
			}
			// Check if encoding length fits in one byte: MSB is 0
			if ((cert[i] & 0xff) <= 127) {
				length = cert[i] & 0xff;
				i = i + 1;
			} else {// If not the length fits in more bytes
				if ((cert[i] & 0xff) == 129) {
					i = i + 2;
					length = (cert[i - 1] & 0xff);
				} else {
					i = i + (cert[i] & 0xff) - 127;// should be i + 3
					byte[] lengthArray = new byte[2];// We assume no length
														// bigger than short
					System.arraycopy(cert, (i - 2), lengthArray, 0, 2);
					length = TextUtils.byteArrayToShort(lengthArray);
				}
			}
			offset = i;
			// When nested data: do not jump over it
			if (tagnumber != 1 && tagnumber != 2 && tagnumber != 9 && tagnumber != 11 && tagnumber != 12) {
				i = offset + length;
			} else
				;
			if (tagnumber == tag) {
				byte[] data = new byte[length];
				System.arraycopy(cert, offset, data, 0, length);
				return data;
			}
			tagnumber += 1;
		}
		return null;
	}
	private static byte[] setDataAtTag(byte[] cert, byte[] data, int tag) {
		int tagnumber = 1;
		int i = 0;
		int offset = 0;
		int length = 0;
		while (i < cert.length) {
			if ((cert[i] & 0xff) == 0)
				i += 1;
			if (((cert[i] & 0xff) > (short) 0x30)) {
				// Tag id is larger than one byte
				i += 1;
				while ((short) (cert[i] & 0xff) > 127) {
					i += 1;
				}
				i += 1;
			} else {
				i += 1;
			}
			// Check if encoding length fits in one byte: MSB is 0
			if ((cert[i] & 0xff) <= 127) {
				length = cert[i] & 0xff;
				i = i + 1;
			} else {// If not the length fits in more bytes
				if ((cert[i] & 0xff) == 129) {
					i = i + 2;
					length = (cert[i - 1] & 0xff);
				} else {
					i = i + (cert[i] & 0xff) - 127;// should be i + 3
					byte[] lengthArray = new byte[2];// We assume no length
														// bigger than short
					System.arraycopy(cert, (i - 2), lengthArray, 0, 2);
					length = TextUtils.byteArrayToShort(lengthArray);
				}
			}
			offset = i;
			// When nested data: do not jump over it
			if (tagnumber != 1 && tagnumber != 2 && tagnumber != 9 && tagnumber != 11 && tagnumber != 12) {
				i = offset + length;
			} else
				;
			if (tagnumber == tag) {
				// THis is only valid if the new data is of the same length as
				// the old
				// TODO: consider the behavior if length changes in the future
				byte[] newCert = new byte[cert.length];
				System.arraycopy(cert, 0, newCert, 0, newCert.length);
				System.arraycopy(data, 0, newCert, offset, length);
				return newCert;
			}
			tagnumber += 1;
		}
		return null;
	}
}
