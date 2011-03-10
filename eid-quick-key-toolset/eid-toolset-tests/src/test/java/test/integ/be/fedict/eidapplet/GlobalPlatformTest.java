/*
 * Quick-Key Toolset Project.
 * Copyright (C) 2011 FedICT.
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

package test.integ.be.fedict.eidapplet;

import static org.junit.Assert.*;

import net.sourceforge.gpj.cardservices.GlobalPlatformService;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;

public class GlobalPlatformTest {

	private static final Log LOG = LogFactory.getLog(GlobalPlatformTest.class);

	@Test
	public void testListing() throws Exception {
		LOG.debug("test");
		GlobalPlatformService.main(new String[] { "-list" });
	}
}
