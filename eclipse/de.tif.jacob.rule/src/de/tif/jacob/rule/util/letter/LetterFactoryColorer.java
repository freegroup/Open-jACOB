package de.tif.jacob.rule.util.letter;

import org.apache.oro.text.regex.MalformedPatternException;
import org.apache.oro.text.regex.MatchResult;
import org.apache.oro.text.regex.Pattern;
import org.apache.oro.text.regex.PatternCompiler;
import org.apache.oro.text.regex.PatternMatcher;
import org.apache.oro.text.regex.PatternMatcherInput;
import org.apache.oro.text.regex.Perl5Compiler;
import org.apache.oro.text.regex.Perl5Matcher;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ExtendedModifyEvent;
import org.eclipse.swt.custom.ExtendedModifyListener;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import de.tif.jacob.letter.AbstractTextLetter;

public class LetterFactoryColorer implements ExtendedModifyListener
{
  PatternMatcher matcher = new Perl5Matcher();
  PatternCompiler compiler = new Perl5Compiler();
  Pattern pattern;


	public LetterFactoryColorer()
	{
		super();
		try {
			pattern = compiler.compile(AbstractTextLetter.PATTERN_DB_FIELD);
		} catch (MalformedPatternException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// TODO Auto-generated constructor stub
	}

	public void modifyText(ExtendedModifyEvent event) 
	{
		StyledText styledText = (StyledText)event.getSource(); 

		String text = styledText.getText();
    PatternMatcherInput input = new PatternMatcherInput(text);

		// Create a collection to hold the StyleRanges
		java.util.List ranges = new java.util.ArrayList();

		MatchResult result;
    while (matcher.contains(input, pattern))
    {
        result = matcher.getMatch();
				ranges.add(new StyleRange(result.beginOffset(0),result.length(), ColorConstants.blue, null, SWT.ITALIC));
    }

		styledText.setStyleRanges((StyleRange[]) ranges.toArray(new StyleRange[0]));
	}
	
}
