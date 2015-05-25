package de.tif.jacob.rule.editor.rules;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.InputStream;
import java.io.StringBufferInputStream;
import java.util.EventObject;
import org.apache.axis.utils.XMLUtils;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.gef.AutoexposeHelper;
import org.eclipse.gef.ContextMenuProvider;
import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.GEFPlugin;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.RootEditPart;
import org.eclipse.gef.SnapToGrid;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.gef.editparts.ScalableFreeformRootEditPart;
import org.eclipse.gef.editparts.ViewportAutoexposeHelper;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.ui.actions.GEFActionConstants;
import org.eclipse.gef.ui.parts.GraphicalEditorWithPalette;
import org.eclipse.gef.ui.parts.GraphicalViewerKeyHandler;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;
import org.eclipse.ui.views.properties.IPropertySheetPage;
import org.eclipse.ui.views.properties.PropertySheetPage;
import org.exolab.castor.xml.Marshaller;
import org.w3c.dom.Document;
import de.tif.jacob.designer.JacobDesigner;
import de.tif.jacob.rule.Constants;
import de.tif.jacob.rule.editor.rules.editpart.RuleModelEditPartFactory;
import de.tif.jacob.rule.editor.rules.model.RulesetModel;
import de.tif.jacob.util.FastStringWriter;

/**
 * A graphical editor with flyout palette that can edit .shapes files. The
 * binding between the .shapes file extension and this editor is done in
 * plugin.xml
 * 
 */
public class RulesetEditor extends  GraphicalEditorWithPalette  implements PropertyChangeListener
{
  public static final String ID = "de.tif.jacob.designer.editor.rules.RulesetEditor";

  private RulesetModel      model;
  private PaletteRoot       palette;
  private PropertySheetPage propPage;

  private boolean additionalDirty=false;
  
  /** Create a new ShapesEditor instance. This is called by the Workspace. */
  public RulesetEditor()
  {
    setEditDomain(new DefaultEditDomain(this));
  }

  /**
   * Editor muss im JacobDesigner.Plugin immer das aktuelle Projekt anziehen,
   * da sonst die Modelle nicht �bereinstimmen (Table, Alias,...
   */
  public void setFocus()
  {
    IEditorInput input  = getEditorInput();
    
    if(input instanceof IFileEditorInput)
    {
        IProject project = ((IFileEditorInput)input).getFile().getProject();
        if(JacobDesigner.getPlugin().getSelectedProject()==null || JacobDesigner.getPlugin().getModel()==null || !JacobDesigner.getPlugin().getSelectedProject().equals(project))
        {
          JacobDesigner.getPlugin().setModel(project);
        }
    }   
    super.setFocus();
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.gef.ui.parts.GraphicalEditorWithFlyoutPalette#getPaletteRoot()
   */
  protected PaletteRoot getPaletteRoot()
  {
    if (palette == null)
    {
      palette = ShapesEditorPaletteFactory.createPalette();
    }
    return palette;
  }

  /**
   * Configure the graphical viewer before it receives contents.
   * <p>
   * This is the place to choose an appropriate RootEditPart and EditPartFactory
   * for your editor. The RootEditPart determines the behavior of the editor's
   * "work-area". For example, GEF includes zoomable and scrollable root edit
   * parts. The EditPartFactory maps model elements to edit parts (controllers).
   * </p>
   * 
   * @see org.eclipse.gef.ui.parts.GraphicalEditor#configureGraphicalViewer()
   */
  protected void configureGraphicalViewer()
  {
  	GraphicalViewer viewer = getGraphicalViewer();

     viewer.setEditPartFactory(new RuleModelEditPartFactory());
    viewer.setKeyHandler(new GraphicalViewerKeyHandler(viewer));

    ScalableFreeformRootEditPart rootEditPart =  new ScalableFreeformRootEditPart() {
      public Object getAdapter(Class key) {
       if (key == AutoexposeHelper.class)
        return new ViewportAutoexposeHelper(this);
       return super.getAdapter(key);
      }
     };
    viewer.setRootEditPart(rootEditPart);
    rootEditPart.getContentPane().setBackgroundColor(Constants.COLOR_WORKAREA);


   
    // configure the context menu provider
    ContextMenuProvider cmProvider = new ShapesEditorContextMenuProvider(viewer, getActionRegistry());
    viewer.setContextMenu(cmProvider);
    getSite().registerContextMenu(cmProvider, viewer);

    // Grid properties
    getGraphicalViewer().setProperty(SnapToGrid.PROPERTY_GRID_ENABLED,  Boolean.TRUE);
    getGraphicalViewer().setProperty(SnapToGrid.PROPERTY_GRID_VISIBLE,  Boolean.TRUE);
    getGraphicalViewer().setProperty(SnapToGrid.PROPERTY_GRID_SPACING,  new Dimension(Constants.RULE_GRID_WIDTH,Constants.RULE_GRID_HEIGHT));
  }

  
  public boolean isDirty() 
  {
		return additionalDirty || getCommandStack().isDirty();
	}

	protected void initializeGraphicalViewer()
  {
    getGraphicalViewer().setContents(getContents());
  }

  protected RulesetModel getContents()
  {
    if (model == null)
      model = createContents();
    return model;
  }

  protected RulesetModel createContents()
  {
    IEditorInput input  = getEditorInput();
    
    if(input instanceof IFileEditorInput)
    {
      try
      {
        IFileEditorInput fileInput = (IFileEditorInput)input;
        InputStream is = fileInput.getFile().getContents();
        RulesetModel model = new RulesetModel(is);
        is.close();
        model.addPropertyChangeListener(this);
        
        IProject project = fileInput.getFile().getProject();
        if(JacobDesigner.getPlugin().getSelectedProject()==null || JacobDesigner.getPlugin().getModel()==null || !JacobDesigner.getPlugin().getSelectedProject().equals(project))
        {
          JacobDesigner.getPlugin().setModel(project);
        }
        
        setPartName(fileInput.getName());
        return model;
      }
      catch (Exception e)
      {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
    return null;
  }

  public void createPartControl(Composite parent)
  {
    super.createPartControl(parent);
    getEditorSite().getActionBars().setGlobalActionHandler(ActionFactory.UNDO.getId(), getActionRegistry().getAction(GEFActionConstants.UNDO));
    getEditorSite().getActionBars().setGlobalActionHandler(ActionFactory.REDO.getId(), getActionRegistry().getAction(GEFActionConstants.REDO));
    getEditorSite().getActionBars().setGlobalActionHandler(ActionFactory.DELETE.getId(), getActionRegistry().getAction(GEFActionConstants.DELETE));
  }



  public Object getAdapter(Class aClass)
  {
    if (aClass == IContentOutlinePage.class)
      return getOverviewOutlinePage();
      if (aClass == CommandStack.class)
        return getCommandStack();
      else if (aClass == IPropertySheetPage.class)
        return getPropertySheetPage();
      return super.getAdapter(aClass);
  }

  /** the overview outline page */
  private OverviewOutlinePage overviewOutlinePage;

  /**
   * Returns the overview for the outline view.
   * 
   * @return the overview
   */
  protected OverviewOutlinePage getOverviewOutlinePage()
  {
      if (null == overviewOutlinePage && null != getGraphicalViewer())
      {
          RootEditPart rootEditPart = getGraphicalViewer().getRootEditPart();
          if (rootEditPart instanceof ScalableFreeformRootEditPart)
          {
              overviewOutlinePage = new OverviewOutlinePage((ScalableFreeformRootEditPart) rootEditPart);
          }
      }

      return overviewOutlinePage;
  }

  public PropertySheetPage getPropertySheetPage()
  {
    if (propPage == null)
    {
      propPage = new PropertySheetPage();
      propPage.setRootEntry(GEFPlugin.createUndoablePropertySheetEntry(getCommandStack()));
    }
    return propPage;
  }



  public void doSave(IProgressMonitor monitor)
  {
    try
    {
      IEditorInput input  = getEditorInput();
      
      if(input instanceof IFileEditorInput)
      {
          IFileEditorInput fileInput = (IFileEditorInput)input;

          Document doc = XMLUtils.newDocument();
          Marshaller.marshal(model.getCastor(), doc);
          
          FastStringWriter writer = new FastStringWriter(1024*100);
          org.apache.xml.serialize.OutputFormat outFormat = new org.apache.xml.serialize.OutputFormat();
          outFormat.setIndenting(true);
          outFormat.setIndent(2);
          outFormat.setLineWidth(200);
          outFormat.setEncoding("ISO-8859-1");
          org.apache.xml.serialize.XMLSerializer xmlser = new org.apache.xml.serialize.XMLSerializer(writer, outFormat);
    
          xmlser.serialize(doc); //replace your_document with reference to document you want to serialize
    
          IFile modelFile = fileInput.getFile();
          modelFile.setContents( new StringBufferInputStream(new String(writer.toCharArray())), true,true, null);
      }
      getCommandStack().markSaveLocation();
      additionalDirty=false;
			firePropertyChange(IEditorPart.PROP_DIRTY);
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }
  
  public void doSaveAs()
  {
  }

	public boolean isSaveAsAllowed()
  {
    return true;
  }

	public void commandStackChanged(EventObject event) 
  {
		if (isDirty())
    {
				firePropertyChange(IEditorPart.PROP_DIRTY);
    }
		super.commandStackChanged(event);
	}
  
  public void propertyChange(PropertyChangeEvent ev)
	{
		additionalDirty=true;
		firePropertyChange(IEditorPart.PROP_DIRTY);
	}
}


