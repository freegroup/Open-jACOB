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
 * Created on Jul 14, 2004
 */
package de.tif.jacob.designer.editor.relationset.layout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import org.eclipse.draw2d.graph.DirectedGraph;
import org.eclipse.draw2d.graph.Edge;
import org.eclipse.draw2d.graph.EdgeList;
import org.eclipse.draw2d.graph.Node;
import org.eclipse.draw2d.graph.NodeList;
import org.eclipse.draw2d.graph.*;

/**
 * Creates dummy edges between nodes, to be used with NodeJoiningDirectedGraphLayout
 * @author Phil Zoio
 */
public class DummyEdgeCreator extends DirectedGraphLayout

{

	NodeList nodeList;
	EdgeList edgeList;
	DirectedGraph graph;

	List edgesAdded;
	List candidateList;
	int targetNodeIndex;

	boolean cleanNextTime = false;

	public void visit(DirectedGraph g)
	 {
		cleanNextTime = true;
		init(g);
		setDummyEdges();
	}

	private void init(DirectedGraph graph)
	{

		this.graph = graph;
		this.nodeList = graph.nodes;
		this.edgeList = graph.edges;
		edgesAdded = new ArrayList();

	}

	protected void setDummyEdges()
	{

		Node targetNode = null;

		int nodeCount = nodeList.size();

		boolean addedEdge = false;

		//if node count is only one then we don't have to worry about whether
		// the nodes are connected
		if (nodeCount > 1)
		{
			for (Iterator iter = nodeList.iterator(); iter.hasNext();)
			{
				Node sourceNode = (Node) iter.next();

				//we will need to set up a dummy relationship for any table not
				// in one already
				if (sourceNode.outgoing.size() == 0 && sourceNode.incoming.size() == 0)
				{

					targetNode = findTargetNode(sourceNode);
					Edge edge = newDummyEdge(targetNode, sourceNode);
					edgesAdded.add(edge);

				}
			}
		}
	}

	private Edge newDummyEdge(Node targetNode, Node sourceNode)
	{
	  System.out.println("DummyEdgeCreator.newDummyEdge()");
		boolean addedEdge;
		DummyEdgePart edgePart = new DummyEdgePart();
		Edge edge = new Edge(edgePart, sourceNode, targetNode);
		edge.weight = 2;
		edgeList.add(edge);

		targetNode = sourceNode;
		addedEdge = true;
		return edge;
	}

	private Node findTargetNode(Node cantBeThis)
	{

		if (candidateList == null)
		{

			candidateList = new NodeList();

			boolean relationshipFound = false;

			//first look for set of targets which are already in relationships
			for (Iterator iter = nodeList.iterator(); iter.hasNext();)
			{
				Node element = (Node) iter.next();
				if ((element.incoming.size() + element.outgoing.size()) >= 1)
				{
					candidateList.add(element);
					relationshipFound = true;
				}
			}

			//if none found, then just use the existing set
			if (!relationshipFound)
			{
				candidateList = nodeList;
			}
			// sort the target set with those in fewest relationships coming
			// first
			else
			{

				Comparator comparator = new Comparator()
				{

					public int compare(Object o1, Object o2)
					{
						Node t1 = (Node) o1;
						Node t2 = (Node) o2;
						return t1.incoming.size() - (t2.incoming.size());
					}

				};

				try
				{
					Collections.sort(candidateList, comparator);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
				//System.out.println("Sorted set: " + candidateList);

			}

		}

		//handle situation where first table is the top of the set - we will
		// want the next one then
		Node toReturn = getNext();
		if (toReturn == cantBeThis)
		{
			toReturn = getNext();
		}
		return toReturn;

	}

	private Node getNext()
	{
		if (targetNodeIndex == candidateList.size() - 1)
			targetNodeIndex = 0;
		else
			targetNodeIndex++;

		return (Node) candidateList.get(targetNodeIndex);
	}

	protected void removeDummyEdges()
	{
		for (Iterator iter = edgesAdded.iterator(); iter.hasNext();)
		{
			Edge edge = (Edge) iter.next();
			edgeList.remove(edge);

		}
	}

}