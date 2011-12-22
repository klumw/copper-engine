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

import java.util.concurrent.TimeUnit;

import junit.framework.TestCase;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import de.scoopgmbh.copper.EngineState;
import de.scoopgmbh.copper.Workflow;
import de.scoopgmbh.copper.WorkflowFactory;
import de.scoopgmbh.copper.test.backchannel.BackChannelQueue;
import de.scoopgmbh.copper.test.backchannel.WorkflowResult;
import de.scoopgmbh.copper.tranzient.TransientScottyEngine;

public class SubWorkflowTransientEngineTest extends TestCase {
	

	public void testWorkflow() throws Exception {
		final ConfigurableApplicationContext context = new ClassPathXmlApplicationContext(new String[] {"transient-engine-application-context.xml", "SimpleTransientEngineTest-application-context.xml"});
		final TransientScottyEngine engine = (TransientScottyEngine) context.getBean("transientEngine");
		final BackChannelQueue backChannelQueue = context.getBean(BackChannelQueue.class);
		
		assertEquals(EngineState.STARTED,engine.getEngineState());
		
		try {
			WorkflowFactory<String> wfFactory = engine.createWorkflowFactory(SimpleTestParentWorkflow.class.getName());
			Workflow<String> wf = wfFactory.newInstance();
			wf.setData("testData");
			engine.run(wf);
			WorkflowResult r = backChannelQueue.dequeue(2000, TimeUnit.MILLISECONDS);
			assertNotNull(r);
		}
		finally {
			context.close();
		}
		assertEquals(EngineState.STOPPED,engine.getEngineState());
		
	}
	
}
