/*******************************************************************************
 *    This file is part of Open-jACOB
 *    Copyright (C) 2005-2010 Andreas Herz | FreeGroup
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

package de.tif.jacob.designer.editor.util;

import org.eclipse.jface.contentassist.SubjectControlContentAssistant;
import org.eclipse.jface.contentassist.TextContentAssistSubjectAdapter;
import org.eclipse.jface.text.DefaultInformationControl;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IInformationControl;
import org.eclipse.jface.text.IInformationControlCreator;
import org.eclipse.jface.text.TextPresentation;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.contentassist.IContentAssistant;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.VerifyKeyListener;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import de.tif.jacob.designer.model.JacobModel;

public class LabelContentAssistant 
{
    public static SubjectControlContentAssistant createContentAssistant(Text input, JacobModel jacob) 
    {
      	TextContentAssistSubjectAdapter adapter = new TextContentAssistSubjectAdapter(input);
        final SubjectControlContentAssistant contentAssistant = new SubjectControlContentAssistant();
      

        IContentAssistProcessor processor = new LabelContentAssistProcessors(jacob);
        contentAssistant.setContentAssistProcessor(processor, IDocument.DEFAULT_CONTENT_TYPE);

        contentAssistant.enableAutoActivation(true);
        contentAssistant.setAutoActivationDelay(500);

        contentAssistant.setContextInformationPopupOrientation(IContentAssistant.CONTEXT_INFO_ABOVE);
        contentAssistant.setInformationControlCreator(new IInformationControlCreator() {
                    public IInformationControl createInformationControl( Shell parent) 
                    {
                        return new DefaultInformationControl(parent, presenter);
                    }
                });

        contentAssistant.install(adapter);
        ILabelProvider labelProvider = new LabelProvider() {
          /**
           * @inheritDoc
           */
          public String getText(Object element) 
          {
              return "";
          }
      };
      adapter.setContentAssistCueProvider(labelProvider);
      adapter.appendVerifyKeyListener(
          new VerifyKeyListener() {
              public void verifyKey(VerifyEvent event) {

              // Check for Ctrl+Spacebar
              if (event.stateMask == SWT.CTRL && event.character == ' ') 
              {
                contentAssistant.showPossibleCompletions();              
                event.doit = false;
              }
           }
        });
        return contentAssistant;
    }

    private static final DefaultInformationControl.IInformationPresenter presenter = new DefaultInformationControl.IInformationPresenter() 
    {
        public String updatePresentation(Display display, String infoText,  TextPresentation presentation, int maxWidth, int maxHeight) 
        {

            StyleRange range = new StyleRange(0, infoText.length(), null, null, SWT.BOLD);

            // Add this style range to the presentation
            presentation.addStyleRange(range);

            // Return the information text
            return infoText;
        }
    };

}