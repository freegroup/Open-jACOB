package de.tif.jacob.rule.util;

import java.util.Iterator;
import java.util.List;

import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.ITypeParameter;
import org.eclipse.jdt.core.Signature;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Javadoc;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SimpleType;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.TagElement;
import org.eclipse.jdt.core.dom.TextElement;

public class ASTHelper 
{

	public static String getJavadoc(final IMethod method)
	{
    final StringBuffer comment=new StringBuffer();
    ASTParser parser = ASTParser.newParser(AST.JLS3);
    parser.setSource(method.getCompilationUnit());
    CompilationUnit unit = (CompilationUnit) parser.createAST(null);
    unit.accept( new ASTVisitor()
    {
      public boolean visit(MethodDeclaration node)
      {
        if(node.getName().getIdentifier().equals(method.getElementName()))
        {
          String types[]=method.getParameterTypes();
          Iterator iter = node.parameters().iterator();
          int counter=0;
          boolean found=true;
          while(iter.hasNext())
          {
            SingleVariableDeclaration variable = (SingleVariableDeclaration)iter.next();
            String varType = ((SimpleType)variable.getType()).getName().getFullyQualifiedName();
            found = found && varType.equals(Signature.toString( types[counter++]));
          }
          if(found)
          {
            if(node.getJavadoc()==null)
              return false;
            
            List tags = node.getJavadoc().tags();

            iter = tags.iterator();
            while(iter.hasNext())
            {
              TagElement tag = (TagElement)iter.next();
              Iterator fragIter =tag.fragments().iterator();
              while(fragIter.hasNext())
              {
                Object frag = fragIter.next();
                if(frag instanceof TextElement)
                {
                  TextElement text = (TextElement)frag;
                  comment.append(text.getText()+"\n");
                }
              }
            }
          }
            
        }
        return true;
      }
    });
		return comment.toString();
	}
}
