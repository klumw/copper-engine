/*
 * Copyright 2002-2012 SCOOP Software GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.scoopgmbh.copper.sample.simulator.orchestration;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.namespace.QName;

import de.scoopgmbh.orchestration.OrchestrationService;
import de.scoopgmbh.orchestration.OrchestrationService_Service;

public final class CreateSubscriptionTestClient {

	private static final QName SERVICE_NAME = new QName("http://orchestration.scoopgmbh.de/", "OrchestrationService");

	private CreateSubscriptionTestClient() {
	}

	public static void main(String args[]) throws java.lang.Exception {
		URL wsdlURL = new URL("http://localhost:9090/OrchestrationServicePort?wsdl");
		if (args.length > 0 && args[0] != null && !"".equals(args[0])) { 
			File wsdlFile = new File(args[0]);
			try {
				if (wsdlFile.exists()) {
					wsdlURL = wsdlFile.toURI().toURL();
				} else {
					wsdlURL = new URL(args[0]);
				}
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
		}

		OrchestrationService_Service ss = new OrchestrationService_Service(wsdlURL, SERVICE_NAME);
		OrchestrationService port = ss.getOrchestrationServicePort();  

		{
			System.out.println("Invoking createSubscription...");
			java.lang.String _createSubscription_msisdn = "491717654321";
			java.lang.String _createSubscription_subscriptionTemplateId = "4711";
			port.createSubscription(_createSubscription_msisdn, _createSubscription_subscriptionTemplateId);
		}

		System.exit(0);
	}

}