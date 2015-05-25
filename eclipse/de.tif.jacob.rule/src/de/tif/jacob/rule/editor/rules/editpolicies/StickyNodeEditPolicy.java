/*
 * Created on Jul 15, 2004
 */
package de.tif.jacob.rule.editor.rules.editpolicies;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.ComponentEditPolicy;
import org.eclipse.gef.requests.GroupRequest;

import de.tif.jacob.rule.editor.rules.commands.RemoveStickyNoteCommand;
import de.tif.jacob.rule.editor.rules.editpart.AnnotationModelEditPart;
import de.tif.jacob.rule.editor.rules.model.AnnotationModel;
import de.tif.jacob.rule.editor.rules.model.RulesetModel;

/**
 * Handles deletion of tables
 * @author Phil Zoio
 */
public class StickyNodeEditPolicy extends ComponentEditPolicy
{

	protected Command createDeleteCommand(GroupRequest request)
	{
		AnnotationModelEditPart annotation = (AnnotationModelEditPart) getHost();
		RulesetModel parent = (RulesetModel) (annotation.getParent().getModel());
		return new RemoveStickyNoteCommand(parent,(AnnotationModel) annotation.getModel());
	}
}