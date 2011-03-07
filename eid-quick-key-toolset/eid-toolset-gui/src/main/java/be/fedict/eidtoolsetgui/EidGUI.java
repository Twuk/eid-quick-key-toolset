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
package be.fedict.eidtoolsetgui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAKey;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import javax.smartcardio.CardException;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import be.fedict.eidtoolset.engine.BelpicCard;
import be.fedict.eidtoolset.exceptions.AIDNotFound;
import be.fedict.eidtoolset.exceptions.GeneralSecurityException;
import be.fedict.eidtoolset.exceptions.InvalidPinException;
import be.fedict.eidtoolset.exceptions.InvalidResponse;
import be.fedict.eidtoolset.exceptions.NoCardConnected;
import be.fedict.eidtoolset.exceptions.NoReadersAvailable;
import be.fedict.eidtoolset.exceptions.NoSuchFeature;
import be.fedict.eidtoolset.exceptions.SmartCardReaderException;
import be.fedict.eidtoolset.exceptions.UnknownCardException;
import be.fedict.eidtoolset.exceptions.UnsupportedEncodingException;
import be.fedict.eidtoolset.gui.DataParser;
import be.fedict.eidtoolset.gui.ExternalProcesses;
import be.fedict.util.SpecificFileFilter;
import be.fedict.util.TextUtils;
import be.fedict.util.X509Utils;

public class EidGUI extends javax.swing.JFrame {
	private static final long serialVersionUID = 1L;
	private JMenuBar jMenuBar1;
	private JMenuItem jMenu2Write;
	private JTextField jTextFieldName;
	private JTextArea jTextAreaCertAuthority;
	private JTextArea jTextAreaCertOwner;
	private JToggleButton jToggleButtonNonRepudiationCertificate;
	private JToggleButton jToggleButtonAuthenticationCertificate;
	private JToggleButton jToggleButtonCACertificate;
	private JToggleButton jToggleButtonRootCACertificate;
	private JToggleButton jToggleButtonRnnCertificate;
	private JLabel jLabelCertificateInfo;
	private JToolBar jToolBarCertificates;
	private JSplitPane jSplitPaneCertificates;
	private JButton jButtonPhoto;
	private JTextField jTextFieldChipNumber;
	private JTextField jTextFieldTitle;
	private JTextField jTextFieldSpecialStatus;
	private JTextField jTextFieldMunicipality;
	private JTextField jTextFieldPlaceOfIssue;
	private JTextField jTextFieldCountry;
	private JTextField jTextFieldPostalCode;
	private JTextField jTextFieldStreet;
	private JTextField jTextFieldNationalNumber;
	private JTextField jTextFieldValidity2;
	private JTextField jTextFieldCardNo2;
	private JTextField jTextFieldValidity;
	private JTextField jTextFieldSex;
	private JTextField jTextFieldCardNo;
	private JTextField jTextFieldNationality;
	private JTextField jTextFieldBirth;
	private JTextField jTextFieldGivenNames;
	private JLabel jLabel2;
	private JLabel jLabel1;
	private JScrollPane jScrollPaneIdentityExtra;
	private JTextField jTextFieldCertAuthority;
	private JMenuItem jMenu3;
	private JComboBox jComboBoxCardType;
	private JTextArea jTextAreaCardType;
	private JMenuItem jMenu2Activate;
	private JComboBox jComboBoxWriters;
	private JComboBox jComboBoxReaders;
	private JMenuItem jMenu2WriteEmpty;
	private JButton jButtonRefresh;
	private JTextArea jTextAreaCurrentWriter;
	private JTextArea jTextAreaCurrentReader;
	private JLabel jLabelReaders;
	private JScrollPane jScrollPaneReaders;
	private JTextArea jTextAreaNoteOnPin;
	private JButton jButtonChangePin;
	private JButton jButtonTestPin;
	private JTextArea jTextAreaAppletVersion;
	private JTextField jTextFieldAppletVersion;
	private JLabel jLabel3;
	private JButton jButtonSaveCert;
	private JButton jButtonCertChange;
	private JButton jButtonCertDetails;
	private JTextField jTextFieldCertValidityTo;
	private JTextField jTextFieldCertValidityFrom;
	private JTextField jTextFieldCertKeyLength;
	private JTextField jTextFieldCertOwner;
	private JTextArea jTextAreaCertValidityTo;
	private JTextArea jTextAreaCertValidityFrom;
	private JTextArea jTextAreaCertKey;
	private JScrollPane jScrollPaneCardPin;
	private JScrollPane jScrollPaneCertificates;
	private JScrollPane jScrollPaneIdentity;
	private JTabbedPane jTabbedPane1;
	private JMenuItem jMenu2Read;
	private JMenuItem jMenu1Exit;
	private JMenuItem jMenu1Save;
	private JMenuItem jMenu1Load;
	private JMenu jMenu2;
	private JMenu jMenu1;
	//	
	private static BelpicCard belpic;
	@SuppressWarnings("unchecked")
	private Hashtable idTable = new Hashtable();
	@SuppressWarnings("unchecked")
	private Hashtable addressTable = new Hashtable();
	// This value stores the current certificate being displayed and thus
	// possibly changed
	// 0 = RNNCert, 1 = rootCA, 2 = caCert, 3 = authCert, 4 = nonrepCert
	// By default this is set to the non-repudiation certificate
	private int currentCert = 4;
	private X509Certificate rNNCert;
	private X509Certificate rootCACert;
	private X509Certificate caCert;
	private X509Certificate authCert;
	private X509Certificate nonRepCert;
	private String readReader;
	private String writeReader;
	{
		// Set Look & Feel
		try {
			if (System.getProperty("os.name").startsWith("Windows"))
				javax.swing.UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
			else
				javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getCrossPlatformLookAndFeelClassName());
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Error during set look and feel: " + e, "Error", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}
	/**
	 * Auto-generated main method to display this JFrame
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					belpic = new BelpicCard("eID");
					// Start a thread that will extract the jar files and
					// convert to .cap file
					Thread thread = new ExtractAndConvert();
					thread.setPriority(Thread.MIN_PRIORITY);
					thread.start();
					EidGUI inst = new EidGUI();
					inst.setLocationRelativeTo(null);
					inst.setVisible(true);
					inst.getJMenuBar().setName("eID");
					inst.getJMenuBar().setRequestFocusEnabled(false);
					inst.setName("eID");
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, "Error during execution: " + e, "Error", JOptionPane.ERROR_MESSAGE);
					e.printStackTrace();
				}
			}
		});
	}
	public EidGUI() {
		super();
		initGUI();
	}
	private void initGUI() {
		try {
			setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			this.setResizable(false);
			{
				jTabbedPane1 = new JTabbedPane();
				getContentPane().add(jTabbedPane1, BorderLayout.CENTER);
				jTabbedPane1.setPreferredSize(new java.awt.Dimension(628, 422));
				{
					jScrollPaneIdentity = new JScrollPane();
					jTabbedPane1.addTab("Identity", null, jScrollPaneIdentity, null);
					{
						jLabel1 = new JLabel();
						jScrollPaneIdentity.setViewportView(jLabel1);
						jLabel1.setIcon(new ImageIcon(getClass().getClassLoader().getResource("emptyeid.JPG")));
						{
							jTextFieldName = new JTextField();
							jLabel1.add(getJTextFieldName());
							jTextFieldName.setText("Name");
							jTextFieldName.setBounds(178, 63, 135, 20);
						}
						{
							jTextFieldGivenNames = new JTextField();
							jLabel1.add(getJTextFieldGivenNames());
							jTextFieldGivenNames.setText("Given names");
							jTextFieldGivenNames.setBounds(178, 83, 135, 21);
						}
						{
							jTextFieldBirth = new JTextField();
							jLabel1.add(getJTextFieldBirth());
							jTextFieldBirth.setText("Place and date of Birth");
							jTextFieldBirth.setBounds(178, 117, 181, 22);
						}
						{
							jTextFieldNationality = new JTextField();
							jLabel1.add(getJTextFieldNationality());
							jTextFieldNationality.setText("Nationality");
							jTextFieldNationality.setBounds(236, 150, 123, 24);
						}
						{
							jTextFieldCardNo = new JTextField();
							jLabel1.add(getJTextFieldCardNo());
							jTextFieldCardNo.setText("Card Number");
							jTextFieldCardNo.setBounds(178, 198, 135, 20);
							jTextFieldCardNo.addActionListener(new ActionListener() {
								public void actionPerformed(ActionEvent evt) {
									jTextFieldCardNo2.setText(getJTextFieldCardNo().getText());
								}
							});
						}
						{
							jTextFieldSex = new JTextField();
							jLabel1.add(getJTextFieldSex());
							jTextFieldSex.setText("Sex");
							jTextFieldSex.setBounds(558, 117, 32, 22);
						}
						{
							jTextFieldValidity = new JTextField();
							jLabel1.add(getJTextFieldValidity());
							jTextFieldValidity.setText("Validity");
							jTextFieldValidity.setBounds(10, 297, 168, 20);
							jTextFieldValidity.addActionListener(new ActionListener() {
								public void actionPerformed(ActionEvent evt) {
									jTextFieldValidity2.setText(getJTextFieldValidity().getText());
								}
							});
						}
						{
							jButtonPhoto = new JButton();
							jLabel1.add(getJButtonPhoto());
							jButtonPhoto.setBounds(458, 182, 140, 198);
							jButtonPhoto.setBorderPainted(false);
							jButtonPhoto.setOpaque(false);
							jButtonPhoto.addActionListener(new ActionListener() {
								public void actionPerformed(ActionEvent evt) {
									jButtonPhotoActionPerformed(evt);
								}
							});
						}
					}
				}
				{
					jScrollPaneIdentityExtra = new JScrollPane();
					jTabbedPane1.addTab("Identity Extra", null, jScrollPaneIdentityExtra, null);
					{
						jLabel2 = new JLabel();
						jScrollPaneIdentityExtra.setViewportView(jLabel2);
						jLabel2.setIcon(new ImageIcon(getClass().getClassLoader().getResource("emptyeidback.JPG")));
						{
							jTextFieldCardNo2 = new JTextField();
							jLabel2.add(getJTextFieldCardNo2());
							jTextFieldCardNo2.setText(getJTextFieldCardNo().getText());
							jTextFieldCardNo2.setBounds(10, 255, 227, 20);
							jTextFieldCardNo2.addActionListener(new ActionListener() {
								public void actionPerformed(ActionEvent evt) {
									jTextFieldCardNo.setText(getJTextFieldCardNo2().getText());
								}
							});
						}
						{
							jTextFieldValidity2 = new JTextField();
							jLabel2.add(getJTextFieldValidity2());
							jTextFieldValidity2.setText(getJTextFieldValidity().getText());
							jTextFieldValidity2.setBounds(11, 290, 226, 20);
							jTextFieldValidity2.addActionListener(new ActionListener() {
								public void actionPerformed(ActionEvent evt) {
									jTextFieldValidity.setText(getJTextFieldValidity2().getText());
								}
							});
						}
						{
							jTextFieldNationalNumber = new JTextField();
							jLabel2.add(getJTextFieldNationalNumber());
							jTextFieldNationalNumber.setText("National Number");
							jTextFieldNationalNumber.setBounds(11, 64, 142, 20);
						}
						{
							jTextFieldStreet = new JTextField();
							jLabel2.add(getJTextFieldStreet());
							jTextFieldStreet.setText("Street");
							jTextFieldStreet.setBounds(10, 109, 143, 20);
						}
						{
							jTextFieldPostalCode = new JTextField();
							jLabel2.add(getJTextFieldPostalCode());
							jTextFieldPostalCode.setText("Postal Code");
							jTextFieldPostalCode.setBounds(10, 153, 75, 20);
						}
						{
							jTextFieldCountry = new JTextField();
							jLabel2.add(getJTextFieldCountry());
							jTextFieldCountry.setText("Country");
							jTextFieldCountry.setBounds(10, 199, 75, 20);
						}
						{
							jTextFieldPlaceOfIssue = new JTextField();
							jLabel2.add(getJTextFieldPlaceOfIssue());
							jTextFieldPlaceOfIssue.setText("Place of issue");
							jTextFieldPlaceOfIssue.setBounds(186, 64, 98, 20);
						}
						{
							jTextFieldMunicipality = new JTextField();
							jLabel2.add(getJTextFieldMunicipality());
							jTextFieldMunicipality.setText("Municipality");
							jTextFieldMunicipality.setBounds(186, 153, 98, 19);
						}
						{
							jTextFieldSpecialStatus = new JTextField();
							jLabel2.add(getJTextFieldSpecialStatus());
							jTextFieldSpecialStatus.setBounds(186, 199, 51, 19);
						}
						{
							jTextFieldTitle = new JTextField();
							jLabel2.add(getJTextFieldTitle());
							jTextFieldTitle.setBounds(333, 64, 38, 21);
						}
						{
							jTextFieldChipNumber = new JTextField();
							jLabel2.add(getJTextFieldChipNumber());
							jTextFieldChipNumber.setText("Chip Number");
							jTextFieldChipNumber.setBounds(333, 255, 266, 20);
						}
					}
				}
				{
					jScrollPaneCertificates = new JScrollPane();
					jTabbedPane1.addTab("Certificates", null, jScrollPaneCertificates, null);
					{
						jSplitPaneCertificates = new JSplitPane();
						jScrollPaneCertificates.setViewportView(jSplitPaneCertificates);
						{
							jLabelCertificateInfo = new JLabel();
							jSplitPaneCertificates.add(getJLabelCertificateInfo(), JSplitPane.RIGHT);
							{
								jTextAreaCertOwner = new JTextArea();
								jLabelCertificateInfo.add(jTextAreaCertOwner);
								jTextAreaCertOwner.setText("Owner:");
								jTextAreaCertOwner.setBounds(10, 11, 75, 21);
								jTextAreaCertOwner.setBackground(new java.awt.Color(212, 208, 200));
								jTextAreaCertOwner.setEditable(false);
							}
							{
								jTextAreaCertAuthority = new JTextArea();
								jLabelCertificateInfo.add(getJTextAreaCertAuthority());
								jTextAreaCertAuthority.setText("Authority:");
								jTextAreaCertAuthority.setBounds(10, 34, 85, 24);
								jTextAreaCertAuthority.setBackground(new java.awt.Color(212, 208, 200));
								jTextAreaCertAuthority.setEditable(false);
							}
							{
								jTextAreaCertKey = new JTextArea();
								jLabelCertificateInfo.add(getJTextAreaCertKey());
								jTextAreaCertKey.setText("Key Length:");
								jTextAreaCertKey.setBounds(10, 56, 86, 26);
								jTextAreaCertKey.setBackground(new java.awt.Color(212, 208, 200));
								jTextAreaCertKey.setEditable(false);
							}
							{
								jTextAreaCertValidityFrom = new JTextArea();
								jLabelCertificateInfo.add(getJTextAreaCertValidityFrom());
								jTextAreaCertValidityFrom.setText("Valid from:");
								jTextAreaCertValidityFrom.setBounds(10, 81, 93, 20);
								jTextAreaCertValidityFrom.setBackground(new java.awt.Color(212, 208, 200));
								jTextAreaCertValidityFrom.setEditable(false);
							}
							{
								jTextAreaCertValidityTo = new JTextArea();
								jLabelCertificateInfo.add(getJTextAreaCertValidityTo());
								jTextAreaCertValidityTo.setText("Until:");
								jTextAreaCertValidityTo.setBounds(244, 80, 52, 23);
								jTextAreaCertValidityTo.setBackground(new java.awt.Color(212, 208, 200));
								jTextAreaCertValidityTo.setEditable(false);
							}
							{
								jTextFieldCertOwner = new JTextField();
								jLabelCertificateInfo.add(getJTextFieldCertOwner());
								jTextFieldCertOwner.setBounds(254, 11, 211, 23);
								jTextFieldCertOwner.setEditable(false);
							}
							{
								jTextFieldCertAuthority = new JTextField();
								jLabelCertificateInfo.add(getJTextFieldCertAuthority());
								jTextFieldCertAuthority.setBounds(254, 34, 211, 22);
								jTextFieldCertAuthority.setEditable(false);
							}
							{
								jTextFieldCertKeyLength = new JTextField();
								jLabelCertificateInfo.add(getJTextFieldCertKeyLength());
								jTextFieldCertKeyLength.setBounds(385, 56, 80, 22);
								jTextFieldCertKeyLength.setEditable(false);
							}
							{
								jTextFieldCertValidityFrom = new JTextField();
								jLabelCertificateInfo.add(getJTextFieldCertValidityFrom());
								jTextFieldCertValidityFrom.setBounds(126, 80, 104, 23);
								jTextFieldCertValidityFrom.setEditable(false);
							}
							{
								jTextFieldCertValidityTo = new JTextField();
								jLabelCertificateInfo.add(getJTextFieldCertValidityTo());
								jTextFieldCertValidityTo.setBounds(357, 78, 108, 23);
								jTextFieldCertValidityTo.setEditable(false);
							}
							{
								jButtonCertDetails = new JButton();
								jLabelCertificateInfo.add(getJButtonCertDetails());
								jButtonCertDetails.setText("View Details");
								jButtonCertDetails.setBounds(129, 356, 95, 24);
								jButtonCertDetails.addActionListener(new ActionListener() {
									public void actionPerformed(ActionEvent evt) {
										certDetailsAction(evt);
									}
								});
							}
							{
								jButtonCertChange = new JButton();
								jLabelCertificateInfo.add(getJButtonCertChange());
								jButtonCertChange.setText("Change Certificate");
								jButtonCertChange.setBounds(335, 356, 129, 24);
								jButtonCertChange.addActionListener(new ActionListener() {
									public void actionPerformed(ActionEvent evt) {
										certChangeAction(evt);
									}
								});
							}
							{
								jButtonSaveCert = new JButton();
								jLabelCertificateInfo.add(getJButtonSaveCert());
								jButtonSaveCert.setText("Save Certificate");
								jButtonSaveCert.setBounds(224, 356, 111, 24);
								jButtonSaveCert.addActionListener(new ActionListener() {
									public void actionPerformed(ActionEvent evt) {
										certSaveAction(evt);
									}
								});
							}
						}
						{
							jToolBarCertificates = new JToolBar();
							jSplitPaneCertificates.add(jToolBarCertificates, JSplitPane.LEFT);
							jToolBarCertificates.setBorderPainted(false);
							jToolBarCertificates.setOrientation(SwingConstants.VERTICAL);
							{
								jToggleButtonRnnCertificate = new JToggleButton();
								jToggleButtonRnnCertificate.setLayout(null);
								jToolBarCertificates.add(getJButtonRnnCertificate());
								jToggleButtonRnnCertificate.setText("RNN Certificate                ");
								jToggleButtonRnnCertificate.setSize(new Dimension(135, 25));
								jToggleButtonRnnCertificate.setPreferredSize(new java.awt.Dimension(112, 25));
								jToggleButtonRnnCertificate.addActionListener(new ActionListener() {
									public void actionPerformed(ActionEvent evt) {
										jButtonRnnCertificateActionPerformed(evt);
									}
								});
							}
							{
								jToggleButtonRootCACertificate = new JToggleButton();
								jToggleButtonRootCACertificate.setLayout(null);
								jToolBarCertificates.add(getJButtonRootCACertificate());
								jToggleButtonRootCACertificate.setText("Root CA Certificate          ");
								jToggleButtonRootCACertificate.setPreferredSize(new java.awt.Dimension(133, 25));
								jToggleButtonRootCACertificate.addActionListener(new ActionListener() {
									public void actionPerformed(ActionEvent evt) {
										jButtonRootCACertificateActionPerformed(evt);
									}
								});
							}
							{
								jToggleButtonCACertificate = new JToggleButton();
								jToggleButtonCACertificate.setLayout(null);
								jToolBarCertificates.add(getJButtonCACertificate());
								jToggleButtonCACertificate.setText("Citizen CA Certificate       ");
								jToggleButtonCACertificate.setPreferredSize(new java.awt.Dimension(133, 25));
								jToggleButtonCACertificate.addActionListener(new ActionListener() {
									public void actionPerformed(ActionEvent evt) {
										jButtonCACertificateActionPerformed(evt);
									}
								});
							}
							{
								jToggleButtonAuthenticationCertificate = new JToggleButton();
								jToggleButtonAuthenticationCertificate.setLayout(null);
								jToolBarCertificates.add(getJButtonAuthenticationCertificate());
								jToggleButtonAuthenticationCertificate.setText("Authentication Certificate");
								jToggleButtonAuthenticationCertificate.setPreferredSize(new java.awt.Dimension(133, 25));
								jToggleButtonAuthenticationCertificate.addActionListener(new ActionListener() {
									public void actionPerformed(ActionEvent evt) {
										jButtonAuthenticationCertificateActionPerformed(evt);
									}
								});
							}
							{
								jToggleButtonNonRepudiationCertificate = new JToggleButton();
								jToggleButtonNonRepudiationCertificate.setLayout(null);
								jToolBarCertificates.add(getJButtonNonRepudiationCertificate());
								jToggleButtonNonRepudiationCertificate.setText("Signature Certificate        ");
								jToggleButtonNonRepudiationCertificate.setPreferredSize(new java.awt.Dimension(133, 25));
								jToggleButtonNonRepudiationCertificate.addActionListener(new ActionListener() {
									public void actionPerformed(ActionEvent evt) {
										jButtonNonRepudiationCertificateActionPerformed(evt);
									}
								});
							}
						}
					}
				}
				{
					jScrollPaneCardPin = new JScrollPane();
					jTabbedPane1.addTab("Card and PIN", null, jScrollPaneCardPin, null);
					{
						jLabel3 = new JLabel();
						jScrollPaneCardPin.setViewportView(jLabel3);
						{
							jTextFieldAppletVersion = new JTextField();
							jLabel3.add(getJTextFieldAppletVersion());
							jTextFieldAppletVersion.setEditable(false);
							jTextFieldAppletVersion.setBounds(224, 17, 125, 19);
							jTextFieldAppletVersion.setBackground(new java.awt.Color(212, 208, 200));
							jTextFieldAppletVersion.setSize(125, 20);
						}
						{
							jTextAreaAppletVersion = new JTextArea();
							jLabel3.add(jTextAreaAppletVersion);
							jTextAreaAppletVersion.setText("Applet Version:");
							jTextAreaAppletVersion.setBounds(10, 17, 126, 20);
							jTextAreaAppletVersion.setEditable(false);
							jTextAreaAppletVersion.setBackground(new java.awt.Color(212, 208, 200));
						}
						{
							jButtonTestPin = new JButton();
							jLabel3.add(getJButtonTestPin());
							jButtonTestPin.setText("Test PIN");
							jButtonTestPin.setBounds(10, 86, 75, 25);
							jButtonTestPin.addActionListener(new ActionListener() {
								public void actionPerformed(ActionEvent evt) {
									jButtonTestPinActionPerformed(evt);
								}
							});
						}
						{
							jButtonChangePin = new JButton();
							jLabel3.add(getJButtonChangePin());
							jButtonChangePin.setText("Change PIN");
							jButtonChangePin.setBounds(85, 86, 93, 25);
							jButtonChangePin.addActionListener(new ActionListener() {
								public void actionPerformed(ActionEvent evt) {
									jButtonChangePinActionPerformed(evt);
								}
							});
						}
						{
							jTextAreaNoteOnPin = new JTextArea();
							jLabel3.add(jTextAreaNoteOnPin);
							jTextAreaNoteOnPin.setText("Note: empty eID PIN is '1234'");
							jTextAreaNoteOnPin.setBounds(14, 128, 271, 18);
							jTextAreaNoteOnPin.setBackground(new java.awt.Color(212, 208, 200));
							jTextAreaNoteOnPin.setEditable(false);
						}
					}
				}
				{
					jScrollPaneReaders = new JScrollPane();
					jTabbedPane1.addTab("Readers", null, jScrollPaneReaders, null);
					{
						jLabelReaders = new JLabel();
						jScrollPaneReaders.setViewportView(jLabelReaders);
						{
							jTextAreaCurrentReader = new JTextArea();
							jLabelReaders.add(jTextAreaCurrentReader);
							jTextAreaCurrentReader.setText("Current reader:");
							jTextAreaCurrentReader.setBounds(10, 23, 129, 23);
							jTextAreaCurrentReader.setEditable(false);
							jTextAreaCurrentReader.setBackground(new java.awt.Color(212, 208, 200));
						}
						{
							jTextAreaCurrentWriter = new JTextArea();
							jLabelReaders.add(jTextAreaCurrentWriter);
							jTextAreaCurrentWriter.setText("Current writer:");
							jTextAreaCurrentWriter.setBounds(10, 57, 130, 23);
							jTextAreaCurrentWriter.setEditable(false);
							jTextAreaCurrentWriter.setBackground(new java.awt.Color(212, 208, 200));
						}
						{
							jButtonRefresh = new JButton();
							jLabelReaders.add(jButtonRefresh);
							jButtonRefresh.setText("Refresh readers");
							jButtonRefresh.setBounds(10, 125, 123, 31);
							jButtonRefresh.addActionListener(new ActionListener() {
								public void actionPerformed(ActionEvent evt) {
									jButtonRefreshActionPerformed(evt);
								}
							});
						}
						{
							ComboBoxModel jComboBoxReadersModel;
							try {
								jComboBoxReadersModel = new DefaultComboBoxModel(belpic.getSmartCardReaders());
							} catch (Exception e) {
								// Readers could not be found
								jComboBoxReadersModel = new DefaultComboBoxModel(new String[] { "No readers available", "" });
							}
							jComboBoxReaders = new JComboBox();
							jLabelReaders.add(jComboBoxReaders);
							jComboBoxReaders.setModel(jComboBoxReadersModel);
							jComboBoxReaders.setBounds(139, 23, 219, 23);
							jComboBoxReaders.addActionListener(new ActionListener() {
								public void actionPerformed(ActionEvent evt) {
									jComboBoxReadersActionPerformed(evt);
								}
							});
						}
						{
							ComboBoxModel jComboBoxWritersModel;
							try {
								jComboBoxWritersModel = new DefaultComboBoxModel(belpic.getSmartCardReaders());
							} catch (Exception e) {
								// Readers could not be found
								jComboBoxWritersModel = new DefaultComboBoxModel(new String[] { "No readers available", "" });
							}
							jComboBoxWriters = new JComboBox();
							jLabelReaders.add(jComboBoxWriters);
							jComboBoxWriters.setModel(jComboBoxWritersModel);
							jComboBoxWriters.setBounds(140, 57, 218, 23);
							jComboBoxWriters.addActionListener(new ActionListener() {
								public void actionPerformed(ActionEvent evt) {
									jComboBoxWritersActionPerformed(evt);
								}
							});
						}
						{
							jTextAreaCardType = new JTextArea();
							jLabelReaders.add(jTextAreaCardType);
							jTextAreaCardType.setText("Current card type:");
							jTextAreaCardType.setBounds(10, 91, 145, 23);
							jTextAreaCardType.setEditable(false);
							jTextAreaCardType.setBackground(new java.awt.Color(212, 208, 200));
						}
						{
							ComboBoxModel jComboBoxCardTypeModel = new DefaultComboBoxModel(belpic.getSupportedCardTypes());
							jComboBoxCardType = new JComboBox();
							jLabelReaders.add(getJComboBoxCardType());
							jComboBoxCardType.setModel(jComboBoxCardTypeModel);
							jComboBoxCardType.setBounds(200, 91, 158, 23);
						}
					}
				}
			}
			{
				jMenuBar1 = new JMenuBar();
				setJMenuBar(jMenuBar1);
				{
					jMenu1 = new JMenu();
					jMenuBar1.add(jMenu1);
					jMenu1.setText("File");
					{
						jMenu1Load = new JMenuItem();
						jMenu1.add(jMenu1Load);
						jMenu1Load.setText("Load");
						jMenu1Load.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent evt) {
								jMenu1LoadActionPerformed(evt);
							}
						});
					}
					{
						jMenu1Save = new JMenuItem();
						jMenu1.add(jMenu1Save);
						jMenu1Save.setText("Save");
						jMenu1Save.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent evt) {
								jMenu1SaveActionPerformed(evt);
							}
						});
					}
					{
						jMenu1Exit = new JMenuItem();
						jMenu1.add(jMenu1Exit);
						jMenu1Exit.setText("Exit");
						jMenu1Exit.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent evt) {
								jMenu1ExitActionPerformed(evt);
							}
						});
					}
				}
				{
					jMenu2 = new JMenu();
					jMenuBar1.add(jMenu2);
					jMenu2.setText("Actions");
					{
						jMenu2Read = new JMenuItem();
						jMenu2.add(jMenu2Read);
						jMenu2Read.setText("Read");
						jMenu2Read.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent evt) {
								jMenu2ReadActionPerformed(evt);
							}
						});
					}
					{
						jMenu2Write = new JMenuItem();
						jMenu2.add(jMenu2Write);
						jMenu2Write.setText("Write");
						jMenu2Write.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent evt) {
								jMenu2WriteActionPerformed(evt);
							}
						});
					}
					{
						jMenu2WriteEmpty = new JMenuItem();
						jMenu2.add(jMenu2WriteEmpty);
						jMenu2WriteEmpty.setText("Write Empty");
						jMenu2WriteEmpty.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent evt) {
								try {
									jMenu2WriteEmptyActionPerformed(evt);
								} catch (Exception e) {
									// Exception already handled: do nothing
								}
							}
						});
					}
					{
						jMenu2Activate = new JMenuItem();
						jMenu2.add(jMenu2Activate);
						jMenu2Activate.setText("Activate card");
						jMenu2Activate.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent evt) {
								jMenu2ActivateActionPerformed(evt);
							}
						});
					}
				}
				{
					jMenu3 = new JMenuItem();
					jMenuBar1.add(jMenu3);
					jMenu3.setText("Help");
					jMenu3.setHorizontalAlignment(SwingConstants.LEFT);
					jMenu3.setHorizontalTextPosition(SwingConstants.RIGHT);
					jMenu3.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							jMenu3ActionPerformed(evt);
						}
					});
				}
			}
			pack();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Error during GUI initialisation: " + e, "GUI Error", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}
	public JScrollPane getjScrollPaneIdentityExtra() {
		return jScrollPaneIdentityExtra;
	}
	public JScrollPane getjScrollPaneCardPin() {
		return jScrollPaneCardPin;
	}
	public JScrollPane getjScrollPaneCertificates() {
		return jScrollPaneCertificates;
	}
	public JScrollPane getjScrollPaneIdentity() {
		return jScrollPaneIdentity;
	}
	public JTextField getJTextFieldName() {
		return jTextFieldName;
	}
	public JTextField getJTextFieldGivenNames() {
		return jTextFieldGivenNames;
	}
	public JTextField getJTextFieldBirth() {
		return jTextFieldBirth;
	}
	public JTextField getJTextFieldNationality() {
		return jTextFieldNationality;
	}
	public JTextField getJTextFieldCardNo() {
		return jTextFieldCardNo;
	}
	public JTextField getJTextFieldSex() {
		return jTextFieldSex;
	}
	public JTextField getJTextFieldValidity() {
		return jTextFieldValidity;
	}
	public JTextField getJTextFieldCardNo2() {
		return jTextFieldCardNo2;
	}
	public JTextField getJTextFieldValidity2() {
		return jTextFieldValidity2;
	}
	public JTextField getJTextFieldNationalNumber() {
		return jTextFieldNationalNumber;
	}
	public JTextField getJTextFieldStreet() {
		return jTextFieldStreet;
	}
	public JTextField getJTextFieldPostalCode() {
		return jTextFieldPostalCode;
	}
	public JTextField getJTextFieldCountry() {
		return jTextFieldCountry;
	}
	public JTextField getJTextFieldPlaceOfIssue() {
		return jTextFieldPlaceOfIssue;
	}
	public JTextField getJTextFieldMunicipality() {
		return jTextFieldMunicipality;
	}
	public JTextField getJTextFieldSpecialStatus() {
		return jTextFieldSpecialStatus;
	}
	public JTextField getJTextFieldTitle() {
		return jTextFieldTitle;
	}
	public JTextField getJTextFieldChipNumber() {
		return jTextFieldChipNumber;
	}
	public JButton getJButtonPhoto() {
		return jButtonPhoto;
	}
	private void jMenu1LoadActionPerformed(ActionEvent evt) {
		System.out.println("jMenu1Load.actionPerformed, event=" + evt);
		try {
			JFileChooser chooser = new JFileChooser();
			// Note: source for ExampleFileFilter can be found in
			// FileChooserDemo,
			// under the demo/jfc directory in the Java 2 SDK, Standard Edition.
			SpecificFileFilter filter = new SpecificFileFilter();
			filter.addExtension("xml");
			filter.setDescription("XML Files");
			chooser.setFileFilter(filter);
			int returnVal = chooser.showOpenDialog(jTabbedPane1);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				System.out.println("You chose to open this file: " + chooser.getSelectedFile().getAbsolutePath());
			} else
				return;
			belpic.libraryToEid(chooser.getSelectedFile().getAbsolutePath());
			// Clear GUI tables
			idTable.clear();
			addressTable.clear();
			loadGUIwithEid();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Error during execution: " + e, "Error", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}
	private void jMenu1SaveActionPerformed(ActionEvent evt) {
		System.out.println("jMenu1Save.actionPerformed, event=" + evt);
		try {
			JFileChooser chooser = new JFileChooser();
			SpecificFileFilter filter = new SpecificFileFilter();
			filter.addExtension("xml");
			filter.setDescription("XML Files");
			chooser.setFileFilter(filter);
			int returnVal = chooser.showSaveDialog(jTabbedPane1);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				System.out.println("You chose to save this file: " + chooser.getSelectedFile().getAbsolutePath());
			} else
				return;
			loadEidwithGUI();
			if (chooser.getSelectedFile().getAbsolutePath().endsWith(".xml"))
				belpic.eIDToLibrary(chooser.getSelectedFile().getAbsolutePath());
			else
				belpic.eIDToLibrary(chooser.getSelectedFile().getAbsolutePath() + ".xml");
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Error during execution: " + e, "Error", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}
	private void jMenu1ExitActionPerformed(ActionEvent evt) {
		System.out.println("jMenu1Exit.actionPerformed, event=" + evt);
		try {
			this.dispose();
			this.finalize();
		} catch (Exception e) {
			e.printStackTrace();
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
	private void jMenu2ReadActionPerformed(ActionEvent evt) {
		System.out.println("jMenu2Read.actionPerformed, event=" + evt);
		try {
			readReader = (String) jComboBoxReaders.getSelectedItem();
			belpic.setPreferredReader(readReader);
			// Try connecting to the card
			belpic.fetchATR();
			// If card connected, clear previous data
			idTable.clear();
			addressTable.clear();
			belpic.clearCache();
			// Read new data
			belpic.readFullEid();
			loadGUIwithEid();
		} catch (NoCardConnected e) {
			JOptionPane.showMessageDialog(null, "No Smart Cards found.", "No cards detected", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		} catch (CardException e) {
			JOptionPane.showMessageDialog(null, "No Smart Cards found.", "No cards detected", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Error during execution: " + e, "Error", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}
	private void jMenu2WriteActionPerformed(ActionEvent evt) {
		System.out.println("jMenu2Write.actionPerformed, event=" + evt);
		try {
			loadEidwithGUI();
			writeReader = (String) jComboBoxWriters.getSelectedItem();
			belpic.setPreferredReader(writeReader);
			// write empty eid first?
			int n = JOptionPane.showConfirmDialog(null, "Write empty eID first?", "Write empty eID", JOptionPane.YES_NO_OPTION);
			if (n == 0) {// Yes answer
				try {
					jMenu2WriteEmptyActionPerformed(evt);
				} catch (Exception e) {
					return;
				}
			}
			belpic.writeEid();
			// Generate new key pairs
			byte[] auth = belpic.createNewAuthenticationKey();
			byte[] nonRep = belpic.createPublicNonRepudiationKey();
			// store the public keys in the corresponding certificates (in GUI
			// only!)
			belpic.setAuthenticationCertificate(X509Utils.changeCertPublicKey(belpic.readAuthCertificateBytes(), auth));
			belpic.setNonRepudiationCertificate(X509Utils.changeCertPublicKey(belpic.readNonRepCertificateBytes(), nonRep));
			JOptionPane.showMessageDialog(null, "The new public keys were stored in the corresponding certificates.", "New public keys", JOptionPane.DEFAULT_OPTION);
			// Set new certificates in GUI:
			CertificateFactory cf = CertificateFactory.getInstance("X.509");
			InputStream inStream = new ByteArrayInputStream(belpic.readAuthCertificateBytes());
			authCert = (X509Certificate) cf.generateCertificate(inStream);
			inStream.close();
			inStream = new ByteArrayInputStream(belpic.readNonRepCertificateBytes());
			nonRepCert = (X509Certificate) cf.generateCertificate(inStream);
			inStream.close();
			n = JOptionPane.showConfirmDialog(null, "Card written. Activate eID card?\nThis will prevent the applet from being re-written.", "Activate eID card?", JOptionPane.YES_NO_OPTION);
			if (n == 0) {// Yes answer
				jMenu2ActivateActionPerformed(evt);
			}
			readReader = (String) jComboBoxReaders.getSelectedItem();
			belpic.setPreferredReader(readReader);
		} catch (NoSuchFeature e) {
			JOptionPane.showMessageDialog(null, "Impossible to access cards.", "Card Invalid", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		} catch (ArrayIndexOutOfBoundsException e) {
			JOptionPane.showMessageDialog(null, "Current eID invalid.", "Invalid eID", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		} catch (NullPointerException e) {
			JOptionPane.showMessageDialog(null, "Current eID incomplete.", "Incomplete eID", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		} catch (GeneralSecurityException e) {
			JOptionPane.showMessageDialog(null, "Security exception writing card.", "Security Exception", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Error during execution: " + e, "Error", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}
	private void jButtonPhotoActionPerformed(ActionEvent evt) {
		System.out.println("jButtonPhoto.actionPerformed, event=" + evt);
		try {
			JFileChooser chooser = new JFileChooser();
			SpecificFileFilter filter = new SpecificFileFilter();
			filter.addExtension("JPG");
			filter.setDescription("JPEG Files");
			chooser.setFileFilter(filter);
			int returnVal = chooser.showOpenDialog(jTabbedPane1);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				System.out.println("You chose to open this file: " + chooser.getSelectedFile().getAbsolutePath());
			} else
				return;
			FileInputStream file = new FileInputStream(chooser.getSelectedFile());
			int length = file.available();
			byte[] currentPhoto = new byte[length];
			file.read(currentPhoto);
			belpic.setCitizenPhoto(currentPhoto);
			file.close();
			jButtonPhoto.setIcon(new ImageIcon(chooser.getSelectedFile().getAbsolutePath()));
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Error during execution: " + e, "Error", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}
	private void loadGUIwithEid() throws NoSuchAlgorithmException, UnknownCardException, NoReadersAvailable, NoSuchFeature, SmartCardReaderException, InvalidResponse, CardException, AIDNotFound, NoCardConnected {
		try {
			// Parse identity data
			DataParser.ParseIdentityData(belpic.readCitizenIdentityDataBytes(), idTable);
			getJTextFieldName().setText((String) idTable.get("Name"));
			getJTextFieldGivenNames().setText((String) idTable.get("First names"));
			getJTextFieldBirth().setText(((String) idTable.get("Birth Location")) + " / " + ((String) idTable.get("Birth Date")));
			getJTextFieldNationality().setText((String) idTable.get("Nationality"));
			getJTextFieldCardNo().setText((String) idTable.get("Card Number"));
			getJTextFieldSex().setText((String) idTable.get("Sex"));
			getJTextFieldValidity().setText(((String) idTable.get("Card validity start date")) + " - " + ((String) idTable.get("Card validity end date")));
			getJTextFieldCardNo2().setText((String) idTable.get("Card Number"));
			getJTextFieldValidity2().setText(((String) idTable.get("Card validity start date")) + " - " + ((String) idTable.get("Card validity end date")));
			getJTextFieldNationalNumber().setText((String) idTable.get("National Number"));
			getJTextFieldTitle().setText((String) idTable.get("Noble condition"));
			getJTextFieldChipNumber().setText((String) idTable.get("Chip Number"));
			getJTextFieldPlaceOfIssue().setText((String) idTable.get("Card delivery municipality"));
			if (!idTable.get("Special status").equals("0"))
				getJTextFieldSpecialStatus().setText((String) idTable.get("Special status"));
			// Parse address data
			DataParser.ParseIdentityAddressData(belpic.readCitizenAddressBytes(), addressTable);
			getJTextFieldStreet().setText((String) addressTable.get("Address"));
			getJTextFieldPostalCode().setText((String) addressTable.get("Zip code"));
			getJTextFieldMunicipality().setText((String) addressTable.get("Municipality"));
	
		if (((String) (addressTable.get("Zip code"))).length() == 4)
				getJTextFieldCountry().setText("Belgium");
			else
				getJTextFieldCountry().setText("Other");
			jButtonPhoto.setIcon(new ImageIcon(belpic.readCitizenPhotoBytes()));
			// Load Certificates:
			InputStream inStream = new ByteArrayInputStream(belpic.readRRNCertificateBytes());
			CertificateFactory cf = CertificateFactory.getInstance("X.509");
			rNNCert = (X509Certificate) cf.generateCertificate(inStream);
			inStream.close();
			inStream = new ByteArrayInputStream(belpic.readRootCACertificateBytes());
			rootCACert = (X509Certificate) cf.generateCertificate(inStream);
			inStream.close();
			inStream = new ByteArrayInputStream(belpic.readCACertificateBytes());
			caCert = (X509Certificate) cf.generateCertificate(inStream);
			inStream.close();
			inStream = new ByteArrayInputStream(belpic.readAuthCertificateBytes());
			authCert = (X509Certificate) cf.generateCertificate(inStream);
			inStream.close();
			inStream = new ByteArrayInputStream(belpic.readNonRepCertificateBytes());
			nonRepCert = (X509Certificate) cf.generateCertificate(inStream);
			inStream.close();
			// Set the current certificate to this one
			switch (currentCert) {
			case 0:
				showCertificateInfo(rNNCert);
				break;
			case 1:
				showCertificateInfo(rootCACert);
				break;
			case 2:
				showCertificateInfo(caCert);
				break;
			case 3:
				showCertificateInfo(authCert);
				break;
			case 4:
				showCertificateInfo(nonRepCert);
				break;
			}
			// Display card info:
			displayCardInfo();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void loadEidwithGUI() throws NoSuchAlgorithmException, UnknownCardException, NoReadersAvailable, NoSuchFeature, SmartCardReaderException, InvalidResponse, CardException, AIDNotFound, NoCardConnected, InvalidKeyException, SignatureException,
			CertificateException, UnsupportedEncodingException, IOException {
		// Store GUI identity data
		idTable.put("Name", getJTextFieldName().getText());
		idTable.put("First names", getJTextFieldGivenNames().getText());
		idTable.put("Birth Location", getJTextFieldBirth().getText().split(" / ")[0]);
		idTable.put("Birth Date", getJTextFieldBirth().getText().split(" / ")[1]);
		idTable.put("Nationality", getJTextFieldNationality().getText());
		idTable.put("Card Number", getJTextFieldCardNo().getText());
		idTable.put("Sex", getJTextFieldSex().getText());
		idTable.put("Card validity start date", getJTextFieldValidity().getText().split(" - ")[0]);
		idTable.put("Card validity end date", getJTextFieldValidity().getText().split(" - ")[1]);
		idTable.put("National Number", getJTextFieldNationalNumber().getText());
		idTable.put("Noble condition", getJTextFieldTitle().getText());
		idTable.put("Chip Number", getJTextFieldChipNumber().getText());
		idTable.put("Card delivery municipality", getJTextFieldPlaceOfIssue().getText());
		idTable.put("Hash photo", TextUtils.hexDump(new byte[20]));
		if (getJTextFieldSpecialStatus().getText().equals(""))
			idTable.put("Special status", "0");
		else
			idTable.put("Special status", getJTextFieldSpecialStatus().getText());
		belpic.setCitizenIdentityFileBytes(DataParser.ParseHashTableToIdentityData(idTable));
		// Store GUI address data
		addressTable.put("Address", getJTextFieldStreet().getText());
		addressTable.put("Zip code", getJTextFieldPostalCode().getText());
		addressTable.put("Municipality", getJTextFieldMunicipality().getText());
		belpic.setCitizenAddressBytes(DataParser.ParseHashTableToAddressData(addressTable));
		// Store GUI picture: is done during Photo change
		// belpic.setCitizenPhoto(data);
		belpic.setAuthenticationCertificate(authCert.getEncoded());
		belpic.setCACertificate(caCert.getEncoded());
		belpic.setNonRepudiationCertificate(nonRepCert.getEncoded());
		belpic.setRnnCertificate(rNNCert.getEncoded());
		belpic.setRootCACertificate(rootCACert.getEncoded());
		// Card info is hard coded: no need to store
	}
	private void displayCardInfo() throws NoSuchFeature, NoSuchAlgorithmException, UnknownCardException, NoReadersAvailable, SmartCardReaderException, InvalidResponse, CardException, AIDNotFound, NoCardConnected {
		// display necessary info in fields.
		getJTextFieldAppletVersion().setText(DataParser.ParseCardInfo(belpic.readCardData()));
	}
	public JLabel getJLabelCertificateInfo() {
		return jLabelCertificateInfo;
	}
	public JToggleButton getJButtonRnnCertificate() {
		return jToggleButtonRnnCertificate;
	}
	public JToggleButton getJButtonRootCACertificate() {
		return jToggleButtonRootCACertificate;
	}
	public JToggleButton getJButtonCACertificate() {
		return jToggleButtonCACertificate;
	}
	public JToggleButton getJButtonAuthenticationCertificate() {
		return jToggleButtonAuthenticationCertificate;
	}
	public JToggleButton getJButtonNonRepudiationCertificate() {
		return jToggleButtonNonRepudiationCertificate;
	}
	private void jButtonRnnCertificateActionPerformed(ActionEvent evt) {
		System.out.println("jButtonRnnCertificate.actionPerformed, event=" + evt);
		try {
			currentCert = 0;
			// Reset other button of table
			resetCertToggleButtons();
			// Show certificateInfo
			showCertificateInfo(rNNCert);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private void jButtonRootCACertificateActionPerformed(ActionEvent evt) {
		System.out.println("jButtonRootCACertificate.actionPerformed, event=" + evt);
		try {
			currentCert = 1;
			// Reset other button of table
			resetCertToggleButtons();
			// Show certificateInfo
			showCertificateInfo(rootCACert);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private void jButtonCACertificateActionPerformed(ActionEvent evt) {
		System.out.println("jButtonCACertificate.actionPerformed, event=" + evt);
		try {
			currentCert = 2;
			// Reset other button of table
			resetCertToggleButtons();
			// Show certificateInfo
			showCertificateInfo(caCert);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private void jButtonAuthenticationCertificateActionPerformed(ActionEvent evt) {
		System.out.println("jButtonAuthenticationCertificate.actionPerformed, event=" + evt);
		try {
			currentCert = 3;
			// Reset other button of table
			resetCertToggleButtons();
			// Show certificateInfo
			showCertificateInfo(authCert);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private void jButtonNonRepudiationCertificateActionPerformed(ActionEvent evt) {
		System.out.println("jButtonNonRepudiationCertificate.actionPerformed, event=" + evt);
		try {
			currentCert = 4;
			// Reset other button of table
			resetCertToggleButtons();
			// Show certificateInfo
			showCertificateInfo(nonRepCert);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private void resetCertToggleButtons() {
		if (currentCert != 0)
			getJButtonRnnCertificate().setSelected(false);
		if (currentCert != 1)
			getJButtonRootCACertificate().setSelected(false);
		if (currentCert != 2)
			getJButtonCACertificate().setSelected(false);
		if (currentCert != 3)
			getJButtonAuthenticationCertificate().setSelected(false);
		if (currentCert != 4)
			getJButtonNonRepudiationCertificate().setSelected(false);
	}
	private void certDetailsAction(ActionEvent evt) {
		System.out.println("jButtonCertDetails.actionPerformed, event=" + evt);
		try {
			File tempFile = File.createTempFile("currentCert", ".crt");
			tempFile.deleteOnExit();
			FileOutputStream file = new FileOutputStream(tempFile.getAbsolutePath());
			switch (currentCert) {
			case 0:
				file.write(rNNCert.getEncoded());
				break;
			case 1:
				file.write(rootCACert.getEncoded());
				break;
			case 2:
				file.write(caCert.getEncoded());
				break;
			case 3:
				file.write(authCert.getEncoded());
				break;
			case 4:
				file.write(nonRepCert.getEncoded());
				break;
			}
			if (System.getProperty("os.name").startsWith("Windows"))
				Runtime.getRuntime().exec("rundll32 cryptext.dll,CryptExtOpenCER " + tempFile.getAbsolutePath());
			else
				JOptionPane.showMessageDialog(null, "Viewing certificate details not supported under this operating system.\nPlease store certificate and use your preferred tool to open it.", "Certificate viewing not supported", JOptionPane.ERROR_MESSAGE);
			file.close();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Error during execution: " + e, "Error", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}
	private void certSaveAction(ActionEvent evt) {
		System.out.println("jButtonCertSave.actionPerformed, event=" + evt);
		try {
			JFileChooser chooser = new JFileChooser();
			SpecificFileFilter filter = new SpecificFileFilter();
			filter.addExtension("CRT");
			filter.setDescription("Certificate Files");
			chooser.setFileFilter(filter);
			int returnVal = chooser.showSaveDialog(jTabbedPane1);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				System.out.println("You chose to open this file: " + chooser.getSelectedFile().getAbsolutePath());
			} else
				return;
			switch (currentCert) {
			case 0:
				X509Utils.dumpCertificateToFile(chooser.getSelectedFile().getAbsolutePath() + ".crt", rNNCert);
				break;
			case 1:
				X509Utils.dumpCertificateToFile(chooser.getSelectedFile().getAbsolutePath() + ".crt", rootCACert);
				break;
			case 2:
				X509Utils.dumpCertificateToFile(chooser.getSelectedFile().getAbsolutePath() + ".crt", caCert);
				break;
			case 3:
				X509Utils.dumpCertificateToFile(chooser.getSelectedFile().getAbsolutePath() + ".crt", authCert);
				break;
			case 4:
				X509Utils.dumpCertificateToFile(chooser.getSelectedFile().getAbsolutePath() + ".crt", nonRepCert);
				break;
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Error during execution: " + e, "Error", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}
	private void certChangeAction(ActionEvent evt) {
		System.out.println("jButtonCertChange.actionPerformed, event=" + evt);
		try {
			JFileChooser chooser = new JFileChooser();
			// Note: source for ExampleFileFilter can be found in
			// FileChooserDemo,
			// under the demo/jfc directory in the Java 2 SDK, Standard Edition.
			SpecificFileFilter filter = new SpecificFileFilter();
			filter.addExtension("CRT");
			filter.setDescription("Certificate Files");
			chooser.setFileFilter(filter);
			int returnVal = chooser.showOpenDialog(jTabbedPane1);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				System.out.println("You chose to open this file: " + chooser.getSelectedFile().getAbsolutePath());
			} else
				return;
			// Load certificate:
			InputStream inStream = new FileInputStream(chooser.getSelectedFile().getAbsolutePath());
			CertificateFactory cf = CertificateFactory.getInstance("X.509");
			// Set the current certificate to this one
			switch (currentCert) {
			case 0:
				rNNCert = (X509Certificate) cf.generateCertificate(inStream);
				// Show certificateInfo
				showCertificateInfo(rNNCert);
				break;
			case 1:
				rootCACert = (X509Certificate) cf.generateCertificate(inStream);
				// Show certificateInfo
				showCertificateInfo(rootCACert);
				break;
			case 2:
				caCert = (X509Certificate) cf.generateCertificate(inStream);
				// Show certificateInfo
				showCertificateInfo(caCert);
				break;
			case 3:
				authCert = (X509Certificate) cf.generateCertificate(inStream);
				// Show certificateInfo
				showCertificateInfo(authCert);
				break;
			case 4:
				nonRepCert = (X509Certificate) cf.generateCertificate(inStream);
				// Show certificateInfo
				showCertificateInfo(nonRepCert);
				break;
			}
			inStream.close();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Error during execution: " + e, "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
	private void showCertificateInfo(X509Certificate cert) {
		try {
			getJTextFieldCertOwner().setText(cert.getSubjectX500Principal().getName().split("N=")[1].split(",")[0]);
			getJTextFieldCertAuthority().setText(cert.getIssuerX500Principal().getName().split("N=")[1].split(",")[0]);
			getJTextFieldCertKeyLength().setText(Integer.toString(((RSAKey) (cert.getPublicKey())).getModulus().bitLength()));
			GregorianCalendar cal = new GregorianCalendar();
			cal.setTime(cert.getNotBefore());
			getJTextFieldCertValidityFrom().setText(cal.get(Calendar.DAY_OF_MONTH) + "/" + Integer.toString(cal.get(Calendar.MONTH) + 1) + "/" + cal.get(Calendar.YEAR));
			cal.setTime(cert.getNotAfter());
			getJTextFieldCertValidityTo().setText(cal.get(Calendar.DAY_OF_MONTH) + "/" + Integer.toString(cal.get(Calendar.MONTH) + 1) + "/" + cal.get(Calendar.YEAR));
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Error during certificate loading. Check if valid certificate available. \nError: " + e, "Error during certificate parsing", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}
	public JTextArea getJTextAreaCertAuthority() {
		return jTextAreaCertAuthority;
	}
	public JTextArea getJTextAreaCertKey() {
		return jTextAreaCertKey;
	}
	public JTextArea getJTextAreaCertValidityFrom() {
		return jTextAreaCertValidityFrom;
	}
	public JTextArea getJTextAreaCertValidityTo() {
		return jTextAreaCertValidityTo;
	}
	public JTextField getJTextFieldCertOwner() {
		return jTextFieldCertOwner;
	}
	public JTextField getJTextFieldCertAuthority() {
		return jTextFieldCertAuthority;
	}
	public JTextField getJTextFieldCertKeyLength() {
		return jTextFieldCertKeyLength;
	}
	public JTextField getJTextFieldCertValidityFrom() {
		return jTextFieldCertValidityFrom;
	}
	public JTextField getJTextFieldCertValidityTo() {
		return jTextFieldCertValidityTo;
	}
	public JButton getJButtonCertDetails() {
		return jButtonCertDetails;
	}
	public JButton getJButtonCertChange() {
		return jButtonCertChange;
	}
	public JButton getJButtonSaveCert() {
		return jButtonSaveCert;
	}
	public JTextField getJTextFieldAppletVersion() {
		return jTextFieldAppletVersion;
	}
	public JButton getJButtonTestPin() {
		return jButtonTestPin;
	}
	public JButton getJButtonChangePin() {
		return jButtonChangePin;
	}
	private void jButtonTestPinActionPerformed(ActionEvent evt) {
		System.out.println("jButtonTestPin.actionPerformed, event=" + evt);
		try {
			String pin = (String) JOptionPane.showInputDialog(null, "Please enter your 4 digit PIN:", "Enter PIN", JOptionPane.QUESTION_MESSAGE);
			belpic.setPin(pin);
			belpic.pinValidationEngine();
			JOptionPane.showMessageDialog(null, "Correct PIN.", "PIN ok", JOptionPane.DEFAULT_OPTION);
			System.out.println("Pin OK.");
		} catch (InvalidPinException e) {
			JOptionPane.showMessageDialog(null, "Invalid PIN. " + e.getMessage() + "tries left.", "PIN invalid", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Error during execution: " + e, "Error", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}
	private void jButtonChangePinActionPerformed(ActionEvent evt) {
		System.out.println("jButtonChangePin.actionPerformed, event=" + evt);
		try {
			String oldPin = (String) JOptionPane.showInputDialog(null, "Please enter your old 4 digit PIN:", "Enter old PIN", JOptionPane.QUESTION_MESSAGE);
			String newPin = (String) JOptionPane.showInputDialog(null, "Please enter your new 4 digit PIN:", "Enter new PIN", JOptionPane.QUESTION_MESSAGE);
			String newPin2 = (String) JOptionPane.showInputDialog(null, "Please confirm your new 4 digit PIN:", "Confirm new PIN", JOptionPane.QUESTION_MESSAGE);
			if (!newPin.equals(newPin2)) {
				JOptionPane.showMessageDialog(null, "New PIN confirmation failed.", "Confirmation failed", JOptionPane.ERROR_MESSAGE);
				return;
			}
			belpic.changeThisPin(oldPin, newPin, 0);
			JOptionPane.showMessageDialog(null, "PIN changed.", "PIN changed", JOptionPane.DEFAULT_OPTION);
			System.out.println("Pin change OK.");
		} catch (InvalidPinException e) {
			JOptionPane.showMessageDialog(null, "Invalid PIN. " + e.getMessage() + "tries left.", "PIN invalid", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Error during execution: " + e, "Error", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}
	private void jMenu2WriteEmptyActionPerformed(ActionEvent evt) throws Exception {
		System.out.println("jMenu2WriteEmpty.actionPerformed, event=" + evt);
		try {
			writeReader = (String) jComboBoxWriters.getSelectedItem();
			belpic.setPreferredReader(writeReader);
			JOptionPane.showMessageDialog(null, "Please wait for the applet to be loaded.\nThis can take some time.", "Applet loading", JOptionPane.INFORMATION_MESSAGE);
			// Run gpshell to upload cap file
			ExternalProcesses.uploadCapFile((String) getJComboBoxCardType().getSelectedItem(), writeReader);
			JOptionPane.showMessageDialog(null, "Empty applet loaded.", "Loading finished", JOptionPane.INFORMATION_MESSAGE);
			readReader = (String) jComboBoxReaders.getSelectedItem();
			belpic.setPreferredReader(readReader);
		} catch (IllegalArgumentException e) {
			JOptionPane.showMessageDialog(null, "Error loading applet. Check reader and smart card connection: \n" + e.getMessage(), "Applet loading failed", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
			throw e;
		} catch (GeneralSecurityException e) {
			JOptionPane.showMessageDialog(null, "Security exception writing card.", "Security Exception", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			// JOptionPane.showMessageDialog(null,"Error during execution.",
			// "Error", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
			throw e;
		}
	}
	private void jButtonRefreshActionPerformed(ActionEvent evt) {
		System.out.println("jButtonRefresh.actionPerformed, event=" + evt);
		try {
			ComboBoxModel jComboBoxReadersModel;
			jComboBoxReadersModel = new DefaultComboBoxModel(belpic.getSmartCardReaders());
			jComboBoxReaders.setModel(jComboBoxReadersModel);
			jComboBoxWriters.setModel(jComboBoxReadersModel);
		} catch (Exception e) {
			// Readers could not be found
			ComboBoxModel jComboBoxWritersModel = new DefaultComboBoxModel(new String[] { "No readers available", "" });
			jComboBoxWriters.setModel(jComboBoxWritersModel);
			ComboBoxModel jComboBoxReadersModel = new DefaultComboBoxModel(new String[] { "No readers available", "" });
			jComboBoxReaders.setModel(jComboBoxReadersModel);
		}
	}
	private void jComboBoxReadersActionPerformed(ActionEvent evt) {
		System.out.println("jComboBoxReaders.actionPerformed, event=" + evt);
		readReader = (String) jComboBoxReaders.getSelectedItem();
	}
	private void jComboBoxWritersActionPerformed(ActionEvent evt) {
		System.out.println("jComboBoxWriters.actionPerformed, event=" + evt);
		writeReader = (String) jComboBoxWriters.getSelectedItem();
	}
	private void jMenu2ActivateActionPerformed(ActionEvent evt) {
		System.out.println("jMenu2Activate.actionPerformed, event=" + evt);
		byte[] response = null;
		try {
			writeReader = (String) jComboBoxWriters.getSelectedItem();
			belpic.setPreferredReader(writeReader);
			// System.out.println(TextUtils.hexDump(belpic.fetchATR()));
			response = belpic.activateCard();
			readReader = (String) jComboBoxReaders.getSelectedItem();
			belpic.setPreferredReader(readReader);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Error during activation: " + TextUtils.hexDump(response), "Error", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}
	public JComboBox getJComboBoxCardType() {
		return jComboBoxCardType;
	}
	private void jMenu3ActionPerformed(ActionEvent evt) {
		System.out.println("jMenu3.actionPerformed, event=" + evt);
		// Launch help file in browser
		try {
			File help = new File("help.html");
			// Now, launch the default browser.
			java.awt.Desktop.getDesktop().browse(java.net.URI.create("file:///" + help.getAbsolutePath().replace("\\", "/").replace(" ", "%20")));
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "The application was not able to launch the help page using the default browser.", "Error", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}
}

/**
 * This thread is called upon launching the GUI to extract the necessary jar
 * files and convert the eid applet to a .cap file on the background.
 * 
 * @author Gauthier
 * 
 */
class ExtractAndConvert extends Thread {
	public void run() {
		try {
			// Extract the jar files
			ExternalProcesses.extractJarFiles();
			// Convert the empty eid class file to a .cap file.
			//ExternalProcesses.runJavaCardConverter();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Error during jar extraction and .cap conversion: " + e, "Error", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}
}
