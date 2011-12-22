/*
 * Copyright 2002-2011 SCOOP Software GmbH
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
package de.scoopgmbh.copper.test.tranzient.simple;

import org.apache.log4j.Logger;

import de.scoopgmbh.copper.InterruptException;
import de.scoopgmbh.copper.Response;
import de.scoopgmbh.copper.WaitMode;
import de.scoopgmbh.copper.Workflow;

public abstract class AbstractIssueClassCastExceptionWorkflow extends Workflow<CompletionIndicator> {
	
	private static final Logger logger = Logger.getLogger(AbstractIssueClassCastExceptionWorkflow.class);
	private int retriesLeft = 5;

	protected abstract void callAbstractExceptionSimulation0(String partnerLink);

	protected abstract void callAbstractExceptionSimulation1() throws InterruptException;
	
	protected abstract void callAbstractExceptionSimulation2(String partnerLink);


	protected void callPartner(int theWaitInterval) throws InterruptException {
		logger.warn("Start " + this.getClass().getName());
		boolean retryInterrupted = false;
		while (!retryInterrupted && retriesLeft > 0) {
			boolean callWait = callIt();
			if (callWait) {
				retryInterrupted = waitForNetRetry(theWaitInterval);
			}
		}
		logger.info("Done callPartner");
	}

	private boolean callIt()  {
		try {
			callAbstractExceptionSimulation0("partnerLink");
			return false;
		} 
		catch (Exception e) {
			logger.warn("Handle exception");
			return true;
		}
	}

	private boolean waitForNetRetry(int theWaitInterval) throws InterruptException {
		logger.info("waitForNetRetry("+theWaitInterval+")");
		boolean interupted = false;
		if (retriesLeft > 0) {
			retriesLeft--;
			String correlationID = "RETRY-" + this.getEngine().createUUID();
			logger.info("before WAIT");
			wait(WaitMode.FIRST, theWaitInterval, correlationID);
			logger.info("after WAIT");
			Response<String> r = getAndRemoveResponse(correlationID);
			if (logger.isInfoEnabled())
				logger.info("Response for " + correlationID + ": " + r);
			if (!r.isTimeout()) {
				if (logger.isInfoEnabled())
					logger.info("Receiver no TIMEOUT while retring, so must be INTERRUPT_RETRY.");
				interupted = true;
			}
		}
		return interupted;
	}
}
