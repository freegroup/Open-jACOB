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
 * Created on Jul 15, 2004
 */
package de.tif.jacob.designer.editor.relationset.layout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.eclipse.draw2d.AbsoluteBendpoint;
import org.eclipse.draw2d.PolygonDecoration;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.draw2d.graph.DirectedGraph;
import org.eclipse.draw2d.graph.Edge;
import org.eclipse.draw2d.graph.Node;
import org.eclipse.draw2d.graph.NodeList;
import de.tif.jacob.designer.editor.relationset.editpart.RelationEditPart;
import de.tif.jacob.designer.editor.relationset.editpart.RelationsetEditPart;
import de.tif.jacob.designer.editor.relationset.editpart.TableAliasEditPart;
import de.tif.jacob.designer.editor.relationset.figures.TableAliasFigure;


/**
 * Visitor with support for populating nodes and edges of DirectedGraph
 * from model objects
 * @author Phil Zoio
 */
public class DirectedGraphLayoutVisitor
{
	Map           partToNodesMap;
	DirectedGraph graph;
	
	/**
	 * Public method for reading graph nodes
	 */
	public void layoutDiagram(RelationsetEditPart diagram)
	{
	  System.out.println("DirectedGraphLayoutVisitor.layoutDiagram("+diagram.getRelationsetModel().getName()+")");
		partToNodesMap = new HashMap();
		
		graph = new DirectedGraph();
		// insert all nodes of the relation set
		//
		for (int i = 0; i < diagram.getChildren().size(); i++)
		{
      Object part =  diagram.getChildren().get(i);
      if(part instanceof TableAliasEditPart)
      {
  			TableAliasEditPart tp = (TableAliasEditPart)part;
  			Node n = new Node(tp);
  			n.width = tp.getFigure().getPreferredSize(400, 300).width;
  			n.height = tp.getFigure().getPreferredSize(400, 300).height;
  			n.setPadding(new Insets(10, 8, 10, 12));
  			partToNodesMap.put(tp, n);
      }
		}
		
		if (graph.nodes.size() > 0)
		{	
			for (int i = 0; i < diagram.getChildren().size(); i++)
			{
				TableAliasEditPart tablePart = (TableAliasEditPart) diagram.getChildren().get(i);
				List outgoing = tablePart.getSourceConnections();
				
				for (int j = 0; j < outgoing.size(); j++)
				{
					RelationEditPart relationshipPart = (RelationEditPart) tablePart.getSourceConnections().get(j);
					if(relationshipPart.getTarget()!=null)
					{
						Node source = (Node) partToNodesMap.get(relationshipPart.getSource());
						Node target = (Node) partToNodesMap.get(relationshipPart.getTarget());

						Edge e = new Edge(relationshipPart, source, target);
						e.weight = 2;
						graph.edges.add(e);
						partToNodesMap.put(relationshipPart, e);
					}
				}
			}
			new NodeJoiningDirectedGraphLayout().visit(graph);
			applyResults(diagram);
		}
		
	}

	//******************* SchemaDiagramPart apply methods **********/

	protected void applyResults(RelationsetEditPart diagram)
	{
	  System.out.println("DirectedGraphLayoutVisitor.applyResults(RelationsetEditPart "+diagram.getRelationsetModel().getName()+")");
		applyChildrenResults(diagram);
	}

	protected void applyChildrenResults(RelationsetEditPart diagram)
	{
	  System.out.println("DirectedGraphLayoutVisitor.applyChildrenResults(RelationsetEditPart "+diagram.getRelationsetModel().getName()+")");
		for (int i = 0; i < diagram.getChildren().size(); i++)
		{
			TableAliasEditPart tablePart = (TableAliasEditPart) diagram.getChildren().get(i);
			applyResults(tablePart);
		}
	}

	protected void applyOwnResults(RelationsetEditPart diagram)
	{
	}

	//******************* TablePart apply methods **********/

	public void applyResults(TableAliasEditPart tablePart)
	{
	  System.out.println("DirectedGraphLayoutVisitor.applyResults(TableAliasEditPart "+tablePart.getTableAliasModel().getName()+")");

		Node n = (Node) partToNodesMap.get(tablePart);
		TableAliasFigure tableFigure = (TableAliasFigure) tablePart.getFigure();

		Rectangle bounds = new Rectangle(n.x, n.y, tableFigure.getPreferredSize().width,
				tableFigure.getPreferredSize().height);

		tableFigure.setBounds(bounds);

		for (int i = 0; i < tablePart.getSourceConnections().size(); i++)
		{
			RelationEditPart relationship = (RelationEditPart) tablePart.getSourceConnections().get(i);
			applyResults(relationship);
		}
	}

	//******************* RelationshipPart apply methods **********/

	protected void applyResults(RelationEditPart relationshipPart)
	{
	  System.out.println("DirectedGraphLayoutVisitor.applyResults(RelationEditPart "+relationshipPart.getRelationModel().getName()+")");
		Edge e = (Edge) partToNodesMap.get(relationshipPart);
		NodeList nodes = e.vNodes;

		PolylineConnection conn = (PolylineConnection) relationshipPart.getConnectionFigure();
		conn.setTargetDecoration(new PolygonDecoration());
		if (nodes != null)
		{
			List bends = new ArrayList();
			for (int i = 0; i < nodes.size(); i++)
			{
				Node vn = nodes.getNode(i);
				int x = vn.x;
				int y = vn.y;
				if (e.isFeedback)
				{
					bends.add(new AbsoluteBendpoint(x, y + vn.height));
					bends.add(new AbsoluteBendpoint(x, y));

				}
				else
				{
					bends.add(new AbsoluteBendpoint(x, y));
					bends.add(new AbsoluteBendpoint(x, y + vn.height));
				}
			}
			conn.setRoutingConstraint(bends);
		}
		else
		{
			conn.setRoutingConstraint(Collections.EMPTY_LIST);
		}
	}
}