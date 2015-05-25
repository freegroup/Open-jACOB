package de.shorti.contextdesigner;

/**
 * Title:        short-i
 * Description:  Information retrival System with human language interface
 * Copyright:    Copyright (c) 2001
 * Company:      short-i
 * @author Andreas Herz
 * @version 1.0
 */
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.util.Vector;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import org.apache.oro.text.regex.MatchResult;
import org.apache.oro.text.regex.Pattern;
import org.apache.oro.text.regex.Perl5Compiler;
import org.apache.oro.text.regex.Perl5Matcher;

public class Perl5PatternRenderer implements TableCellRenderer{
    String question   = "";
    JPanel errorCell  = new JPanel();
    JLabel errorLabel = new JLabel();
    JPanel okCell     = new JPanel();
    JLabel okLabel    = new JLabel();
    JPanel hitCell    = new JPanel();
    JLabel hitLabel   = new JLabel();

    FlowLayout layout = new FlowLayout(FlowLayout.LEFT);
	protected static Perl5Compiler   compiler   = new Perl5Compiler();
	protected static Perl5Matcher    matcher    = new Perl5Matcher();

    public Perl5PatternRenderer ()
    {
        errorCell.setBackground(Color.orange);
        errorCell.add(errorLabel);
        errorCell.setLayout(layout);

        okCell.setBackground(Color.lightGray);
        okCell.add(okLabel);
        okCell.setLayout(layout);

        hitCell.setBackground(Color.green);
        hitCell.add(hitLabel);
        hitCell.setLayout(layout);
    }

    public void setQuestion(String _question)
    {
        question = _question;
    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col)
    {
        if(col ==0)
        {
            try
            {
                if(value!=null && !value.toString().equals("") && !question.equals(""))
                {
                    Pattern pattern= compiler.compile(value.toString());
                    if(matcher.matches(question,pattern))
                    {
                        hitLabel.setText(value.toString());
                        return hitCell;
                    }
                }
            }
            catch (Exception ex)
            {
                System.out.println(ex);
                errorLabel.setText(value.toString());
                return errorCell;
            }
        }
        else if(col ==1)
        {
            try
            {
                String p = (String)table.getValueAt(row,0);
                if(p!=null && !p.equals("") && !question.equals(""))
                {
                    Pattern pattern= compiler.compile(p);
                    if(matcher.matches(question,pattern))
                    {
                        Vector result = new Vector();
                        MatchResult match = matcher.getMatch();
                        int groupCount = match.groups();
                        for(int loop=0 ; loop<(groupCount-1);loop++)
                            result.add(match.group(loop+1));
                        hitLabel.setText(result.toString());
                        return hitCell;
                    }
                }
            }
            catch (Exception ex)
            {
                errorLabel.setText(value.toString());
                return errorCell;
            }
        }

        okLabel.setText(value.toString());
        return okCell;
    }
}

