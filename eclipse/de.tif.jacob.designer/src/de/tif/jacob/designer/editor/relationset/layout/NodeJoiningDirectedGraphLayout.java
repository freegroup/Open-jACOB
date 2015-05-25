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
/*
 * Created on Jul 16, 2004
 */
package de.tif.jacob.designer.editor.relationset.layout;

import org.eclipse.draw2d.graph.DirectedGraph;
import org.eclipse.draw2d.graph.DirectedGraphLayout;

/**
 * Extended version of DirectedGraphLayout which allows DirectedGraphLayout
 * functionality to be used even when graph nodes either have no edges, or when part
 * of clusters isolated from other clusters of Nodes
 * 
 * @author Phil Zoio
 */
public class NodeJoiningDirectedGraphLayout extends DirectedGraphLayout
{

	/**
	 * @param graph public method called to handle layout task
	 */
	
	public void visit(DirectedGraph graph)
	{
	  System.out.println("NodeJoiningDirectedGraphLayout.visit(DirectedGraph graph)");
		
		//System.out.println("Before Populate: Graph nodes: " + graph.nodes);
		//System.out.println("Before Populate: Graph edges: " + graph.edges);				
		
		//add dummy edges so that graph does not fall over because some nodes
		// are not in relationships
		new DummyEdgeCreator().visit(graph);
		
		// create edges to join any isolated clusters
		new ClusterEdgeCreator().visit(graph);	
		
		//System.out.println("After Populate: Graph nodes: " + graph.nodes);
		//System.out.println("After Populate: Graph edges: " + graph.edges);	
		
		
		super.visit(graph);
	}
	
}