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
package de.tif.jacob.designer.editor.externalform;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.jface.contentassist.IContentAssistSubjectControl;
import org.eclipse.jface.contentassist.ISubjectControlContentAssistProcessor;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.contentassist.CompletionProposal;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.jface.text.contentassist.IContextInformationValidator;
import org.eclipse.swt.graphics.Point;
import de.tif.jacob.designer.model.TableAliasModel;

public class ContentAssistProcessors implements ISubjectControlContentAssistProcessor
{
  //  Proposal part before cursor
  private final String[] keyWords={"{APPLICATION_WEBPATH}"};
  
  public ContentAssistProcessors()
  {
   
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.jface.text.contentassist.IContentAssistProcessor#computeCompletionProposals(org.eclipse.jface.text.ITextViewer,
   *      int)
   */
  public ICompletionProposal[] computeCompletionProposals(IContentAssistSubjectControl viewer, int documentOffset)
  {
    // Retrieve current document
    IDocument doc = viewer.getDocument();

    // Retrieve current selection range
    Point selectedRange = viewer.getSelectedRange();
    List propList = new ArrayList();

    String qualifier = getQualifier(doc, documentOffset);

    // Compute completion proposals
    computeStructureProposals(qualifier, documentOffset, propList);

    // Create completion proposal array
    ICompletionProposal[] proposals = new ICompletionProposal[propList.size()];

    // and fill with list elements
    propList.toArray(proposals);

    // Return the proposals
    return proposals;

  }

  public ICompletionProposal[] computeCompletionProposals(ITextViewer viewer, int documentOffset)
  {
    // Retrieve current document
    IDocument doc = viewer.getDocument();

    // Retrieve current selection range
    Point selectedRange = viewer.getSelectedRange();
    List<ICompletionProposal> propList = new ArrayList<ICompletionProposal>();

    String qualifier = getQualifier(doc, documentOffset);

    // Compute completion proposals
    computeStructureProposals(qualifier, documentOffset, propList);

    // Create completion proposal array
    ICompletionProposal[] proposals = new ICompletionProposal[propList.size()];

    // and fill with list elements
    propList.toArray(proposals);

    // Return the proposals
    return proposals;

  }

  private String getQualifier(IDocument doc, int documentOffset)
  {
    // Use string buffer to collect characters
    StringBuffer buf = new StringBuffer();
    while (true)
    {
      try
      {
        // Read character backwards
        char c = doc.getChar(--documentOffset);
        
        if(c==' ')
          return buf.reverse().toString(); 
        if(c=='=')
          return buf.reverse().toString(); 
        if(c=='\'')
          return buf.reverse().toString(); 

        buf.append(c);

      }
      catch (BadLocationException e)
      {

        // Document start reached, no tag found
        return buf.reverse().toString();
      }
    }
  }

  private void computeStructureProposals(String qualifier, int documentOffset, List<ICompletionProposal> propList)
  {
    int qlen = qualifier.length();

    // Loop through all proposals
    for (int i = 0; i < keyWords.length; i++)
    {
      String startTag = keyWords[i];
      String text = startTag;
      // Yes -- compute whole proposal text
      if (text.startsWith(qualifier))
      {
        // Derive cursor position
        int cursor = startTag.length();
        // Construct proposal
        CompletionProposal proposal = new CompletionProposal(text, documentOffset - qlen, qlen, cursor);
        // and add to result list
        propList.add(proposal);
      }
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.jface.text.contentassist.IContentAssistProcessor#getCompletionProposalAutoActivationCharacters()
   */
  public char[] getCompletionProposalAutoActivationCharacters()
  {
    return new char[] { '{' };
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.jface.text.contentassist.IContentAssistProcessor#computeContextInformation(org.eclipse.jface.text.ITextViewer,
   *      int)
   */
  public IContextInformation[] computeContextInformation(ITextViewer viewer, int offset)
  {
    return null;
  }

  public IContextInformation[] computeContextInformation(IContentAssistSubjectControl viewer, int offset)
  {
    return null;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.jface.text.contentassist.IContentAssistProcessor#getContextInformationAutoActivationCharacters()
   */
  public char[] getContextInformationAutoActivationCharacters()
  {
    return new char[] { '{' };
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.jface.text.contentassist.IContentAssistProcessor#getErrorMessage()
   */
  public String getErrorMessage()
  {
    return null;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.jface.text.contentassist.IContentAssistProcessor#getContextInformationValidator()
   */
  public IContextInformationValidator getContextInformationValidator()
  {
    return null;
  }
}
