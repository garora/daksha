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
package daksha.core.guiauto.element;

import daksha.core.guiauto.actions.element.AttributesInquirer;
import daksha.core.guiauto.actions.element.BasicActionHandler;
import daksha.core.guiauto.actions.element.ChainActionHandler;
import daksha.core.guiauto.actions.element.CheckBoxActionHandler;
import daksha.core.guiauto.actions.element.DropdownActionsHandler;
import daksha.core.guiauto.actions.element.ImageBasedActionHandler;
import daksha.core.guiauto.actions.element.State;
import daksha.core.guiauto.actions.element.WebActionHandler;
import daksha.tpi.guiauto.enums.GuiElementType;

public interface ConcreteGuiElement<D,E> extends ManagedConcreteGuiElement,
									AttributesInquirer,
									BasicActionHandler,
									ChainActionHandler,
									CheckBoxActionHandler,
									ImageBasedActionHandler,
									DropdownActionsHandler,
									WebActionHandler,
									State{

	E getToolElement();
	void setToolElement(E element);
	
	int getWaitTime() throws Exception;
	GuiElementType getElementType();
	
}
