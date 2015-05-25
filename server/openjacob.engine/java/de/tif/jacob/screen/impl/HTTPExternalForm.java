/*******************************************************************************
 *    This file is part of Open-jACOB
 *    Copyright (C) 2005-2006 Tarragon GmbH
 * 
 *    This program is free software; you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation; version 2 of the License.
 * 
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 * 
 *    You should have received a copy of the GNU General Public License     
 *    along with this program; if not, write to the Free Software
 *    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  
 *    USA
 *******************************************************************************/

package de.tif.jacob.screen.impl;

import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IDomain;
import de.tif.jacob.screen.IExternalForm;
import de.tif.jacob.screen.IForm;
import de.tif.jacob.screen.IFormGroup;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.event.IDomainEventHandler.INavigationEntry;

/**
 * Muss IForm implementieren, damit die Engine mit dieser besonderen Form zurecht kommt.
 * Dieses Interface darf der normale user aber nicht sehen, da die Methode von IForm nur
 * leere Hüllen sind und keinerlei Funktion bieten.
 */
public interface HTTPExternalForm extends IExternalForm, HTTPForm
{
}
