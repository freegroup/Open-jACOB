package de.tif.jacob.rule.editor.rules.editpart;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;

import de.tif.jacob.rule.editor.rules.model.*;

public class RuleModelEditPartFactory implements EditPartFactory
{
  public EditPart createEditPart(EditPart context, Object modelElement)
  {
    EditPart part=null;
    if (modelElement instanceof RulesetModel)
      part=new RulesetModelEditPart();
    else if (modelElement instanceof AnnotationModel)
      part = new AnnotationModelEditPart(); 
    else if (modelElement instanceof StartModel)
      part = new StartEditPart(); 
    else if (modelElement instanceof FunnelModel)
      part = new FunnelModelEditPart();
    else if (modelElement instanceof ExceptionModel)
      part = new ExceptionModelEditPart();
    else if (modelElement instanceof MessageEMailModel)
      part = new MessageEmailModelEditPart();
    else if (modelElement instanceof MessageDialogModel)
      part = new MessageDialogModelEditPart();
    else if (modelElement instanceof MessageAlertModel)
      part = new MessageAlertModelEditPart();
    else if (modelElement instanceof BusinessObjectModel)
      part = new BusinessObjectModelEditPart();
    else if (modelElement instanceof TransitionModel)
      part = new TransitionEditPart();
    else if (modelElement instanceof FalseConditionalExitModel)
      part = new OnFalseModelEditPart();
    else if (modelElement instanceof TrueConditionalExitModel)
      part = new OnTrueModelEditPart();
    else if (modelElement instanceof ConditionalExitModel)
      part = new ConditionalExitModelEditPart();
    else if (modelElement instanceof BooleanDecisionModel)
      part = new BooleanDecisionModelEditPart();
    else if (modelElement instanceof EnumDecisionModel)
      part = new EnumDecisionModelEditPart();
    else if (modelElement instanceof CommentModel)
      part = new CommentEditPart();

    if(part!=null)
      part.setModel(modelElement);
    return part;
  }

}