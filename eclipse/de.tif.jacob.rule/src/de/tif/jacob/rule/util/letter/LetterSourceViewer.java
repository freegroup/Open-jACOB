package de.tif.jacob.rule.util.letter;

import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.contentassist.ContentAssistant;
import org.eclipse.jface.text.source.IOverviewRuler;
import org.eclipse.jface.text.source.IVerticalRuler;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.widgets.Composite;
import de.tif.jacob.designer.model.JacobModel;

public class LetterSourceViewer extends SourceViewer 
{

	public LetterSourceViewer(JacobModel model, Composite parent, IVerticalRuler ruler, int styles) 
	{
		super(parent, ruler, styles);
		
    setDocument(new Document());

    final ContentAssistant assistant = new ContentAssistant();
    assistant.setContentAssistProcessor(new LetterFactoryContentAssistProcessor(model), IDocument.DEFAULT_CONTENT_TYPE);

    assistant.install(this);

    getTextWidget().addExtendedModifyListener(new LetterFactoryColorer());
    
    getControl().addKeyListener(new KeyAdapter() 
    {
      public void keyPressed(KeyEvent e) 
      {
        if (e.stateMask == SWT.CTRL && e.character == ' ') 
          assistant.showPossibleCompletions();
      }
    });
	}

  public String getText()
  {
    return getTextWidget().getText();
  }
  
  public void setText(String text)
  {
    getTextWidget().setText(text);
  }
}
