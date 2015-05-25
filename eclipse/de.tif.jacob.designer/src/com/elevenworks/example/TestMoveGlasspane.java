package com.elevenworks.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;


public class TestMoveGlasspane
{
	public static void main(String[] args)
	{
		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch (Exception exc)
		{
			// Do nothing...
		}

		JFrame vFrame = new JFrame();

		JMenuBar vMenuBar = new JMenuBar();
		vFrame.setJMenuBar(vMenuBar);

		JMenu vFileMenu = new JMenu("File");
		vMenuBar.add(vFileMenu);
		addMenuItem(vFileMenu,"Exit");

		JMenu vEditMenu = new JMenu("Edit");
		vMenuBar.add(vEditMenu);
		addMenuItem(vEditMenu,"Undo");
		vEditMenu.addSeparator();
		addMenuItem(vEditMenu,"Cut");
		addMenuItem(vEditMenu,"Copy");
		addMenuItem(vEditMenu,"Paste");

		JMenu vHelpMenu = new JMenu("Help");
		vMenuBar.add(vHelpMenu);
		addMenuItem(vHelpMenu,"Help...");
		vHelpMenu.addSeparator();
		addMenuItem(vHelpMenu,"About...");

		JEditorPane vEditor = new JEditorPane("text/html","<html><head></head><body><h2>Move Glasspane Example</h2><p>Copyright &copy; 2005 by Jon Lipsky</p><p>Licensed under the Apache License, Version 2.0 (the \"License\"); you may not use this file except in compliance with the License. You may obtain a copy of the License at</p><p> http://www.apache.org/licenses/LICENSE-2.0.</p><p>Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an \"AS IS\" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.</p></body></html>");
		vFrame.getContentPane().add(vEditor,BorderLayout.CENTER);

		vFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		vFrame.setSize(400, 400);

		MoveGlassPane.registerFrame(vFrame);

		vFrame.show();
	}

	private static void addMenuItem(JMenu aParent, String aTitle)
	{
		addMenuItem(aParent,aTitle,null);
	}

	private static void addMenuItem(JMenu aParent, String aTitle, ActionListener aListener)
	{
		JMenuItem vMenuItem = new JMenuItem(aTitle);
		aParent.add(vMenuItem);

		if (aListener != null)
		{
			vMenuItem.addActionListener(aListener);
		}
	}
}
