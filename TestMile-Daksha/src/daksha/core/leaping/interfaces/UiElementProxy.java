/*******************************************************************************
 * Copyright 2015-18 Test Mile Software Testing Pvt Ltd
 * 
 * Website: www.TestMile.com
 * Email: support [at] testmile.com
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package daksha.core.leaping.interfaces;

import java.io.File;

import daksha.core.leaping.interfaces.actions.AttributesInquirer;
import daksha.core.leaping.interfaces.actions.BasicActionHandler;
import daksha.core.leaping.interfaces.actions.ChainActionHandler;
import daksha.core.leaping.interfaces.actions.CheckBoxActionHandler;
import daksha.core.leaping.interfaces.actions.ElementNestedActionHandler;
import daksha.core.leaping.interfaces.actions.ImageBasedActionHandler;
import daksha.core.leaping.interfaces.actions.InstanceGetter;
import daksha.core.leaping.interfaces.actions.SelectAndRadioActionHandler;
import daksha.core.leaping.interfaces.actions.WebActionHandler;
import daksha.tpi.leaping.enums.UiElementType;
import daksha.tpi.leaping.interfaces.UiElement;

public interface UiElementProxy extends 	AttributesInquirer,
									BasicActionHandler,
									ChainActionHandler,
									CheckBoxActionHandler,
									ImageBasedActionHandler,
									SelectAndRadioActionHandler,
									WebActionHandler,
									InstanceGetter,
									ElementNestedActionHandler{
	String getAutomatorName();
	void setAutomatorName(String name);
	
	Object getToolFindersQueueObject();
	void identify() throws Exception;
	void identifyAll() throws Exception;
	
	int getWaitTime() throws Exception;
	File takeScreenshot() throws Exception;
	
	void setRawToolElement(Object toolElementObject) throws Exception;
	void setRawToolElements(Object toolElementsObject);
	int getRandomElementIndex() throws Exception;
	int getLastIndex() throws Exception;
	
	boolean isCompositeElementIdentified() throws Exception;
	boolean isSingularElementIdentified() throws Exception;
	int getElementCountForCompositeElement() throws Exception;
	void assignElementAtIndexFromMatches(int index) throws Exception;
	UiElement getInstanceAtIndex(int index) throws Exception;
	UiElementType getElementType();
}