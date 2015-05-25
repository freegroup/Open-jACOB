/*******************************************************************************
 *    This file is part of Open-jACOB
 *    Copyright (C) 2005-2006 Tarragon GmbH
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

package de.tif.jacob.core.data.impl.qbe;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.tif.jacob.core.data.impl.misc.InvalidTableAliasException;
import de.tif.jacob.core.definition.IManyToManyRelation;
import de.tif.jacob.core.definition.IOneToManyRelation;
import de.tif.jacob.core.definition.IRelation;
import de.tif.jacob.core.definition.IRelationSet;
import de.tif.jacob.core.definition.ITableAlias;

/**
 * @author Andreas
 * 
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public final class QBERelationGraph
{
	// IBIS: Diese Klasse non-mutual machen. Im Moment steht dem noch die veränderbare Klasse QBERelationConstraint
	// entgegen. Außerdem sollte es dann möglich sein Instanzen wiederzuverwenden, da die Berechnung recht teuer ist.
	static public transient final String RCS_ID = "$Id: QBERelationGraph.java,v 1.4 2009/08/20 21:19:45 ibissw Exp $";
	static public transient final String RCS_REV = "$Revision: 1.4 $";

	static private final transient Log logger = LogFactory.getLog(QBERelationGraph.class);

  // Map<ITableAlias, Node>
	private final Map connectedTableAliases;
  
	private final IRelationSet relationSet;
	private final ITableAlias initialTableAlias;
	private final List unusedRelations;
	private final Node initialNode;

	public QBERelationGraph(IRelationSet relationSet, ITableAlias initialTableAlias)
	{
    if (null == relationSet)
      throw new NullPointerException("relationSet is null");
    
    if (null == initialTableAlias)
      throw new NullPointerException("initialTableAlias is null");
    
    if (logger.isTraceEnabled())
			logger.trace("QBERelationGraph(): relationSet=" + relationSet + ", initialTableAlias=" + initialTableAlias);

    this.relationSet = relationSet;
    this.initialTableAlias = initialTableAlias; 
    
		// init graph
		this.connectedTableAliases = new HashMap();

		// create initial node
		this.initialNode = new Node(initialTableAlias);
		this.connectedTableAliases.put(initialNode.alias, initialNode);
		//		this.initialNode.constrained = true;

		List relationContraints = getRelationConstraints(relationSet);
		
		// collect all aliases which stand in direct backward hierarchy
		// by means of beginning with the initial node
		//
		Map backwardAliasesAndRelationConstraints = new HashMap();
    {
      // add initial table alias
      backwardAliasesAndRelationConstraints.put(initialTableAlias, null);
      
      // create copy of relations
      List relationContraints2 = new LinkedList(relationContraints);

      boolean aliasesAdded;
      do
      {
        aliasesAdded = false;
        ListIterator iter = relationContraints2.listIterator();
        while (iter.hasNext())
        {
          QBERelationConstraint relation = (QBERelationConstraint) iter.next();

          if (backwardAliasesAndRelationConstraints.containsKey(relation.getToTableAlias()))
          {
            // Backward-Relation setzen sofern vorhanden
            relation.setPreviousBackwardRelation((QBERelationConstraint) backwardAliasesAndRelationConstraints.get(relation.getToTableAlias()));
            
            if (!backwardAliasesAndRelationConstraints.containsKey(relation.getFromTableAlias()))
            {
              // neuer Alias
              
              backwardAliasesAndRelationConstraints.put(relation.getFromTableAlias(), relation);
              aliasesAdded = true;
            }

            // remove relation from list
            iter.remove();
          }
        }
      }
      while (aliasesAdded);
    }
		
		// temporary list to add nodes at end of each loop
		// reason: nodes should be connected as close as possible to the initial
		// node, but nodes in direct backward hierarchy have a higher priority!
    //
		List newNodes = new ArrayList();

		// process relation list until graph does not extend anymore
		boolean graphModified;
		int loopCount = 0;
		do
		{
			loopCount++;
			graphModified = false;
			ListIterator iter = relationContraints.listIterator();
			while (iter.hasNext())
			{
				QBERelationConstraint relation = (QBERelationConstraint) iter.next();

				// the backward node is the table containing the primary key
				Node backwardNode = (Node) this.connectedTableAliases.get(relation.getFromTableAlias());

				// the forward node is the table containing the foreign key
				Node forwardNode = (Node) this.connectedTableAliases.get(relation.getToTableAlias());
				if (null != backwardNode)
				{
					if (null != forwardNode)
					{
						// both nodes of relation are already connected -> ignore relation
						if (logger.isDebugEnabled())
							logger.debug("QBERelationGraph: ignored relation=" + relation);

						iter.remove();
						continue;
					}
					Node newNode = new Node(relation.getToTableAlias());
					newNodes.add(newNode);

          relation.setForwardFlag();
					backwardNode.addForwardNode(newNode, relation);
				}
				else
				{
					if (null == forwardNode)
					{
						continue;
					}
					
					// ensure that nodes in direct backward hierarchy have a
					// higher priority
					//
					if (!backwardAliasesAndRelationConstraints.containsKey(relation.getToTableAlias()) && 
					     backwardAliasesAndRelationConstraints.containsKey(relation.getFromTableAlias()))
					{
						// both nodes of relation are already connected -> ignore relation
						if (logger.isDebugEnabled())
							logger.debug("QBERelationGraph: ignored relation=" + relation+" (node will be added later)");

						iter.remove();
						continue;
					}
					
					Node newNode = new Node(relation.getFromTableAlias());
					newNodes.add(newNode);

					forwardNode.addBackwardNode(newNode, relation);
				}

				if (logger.isTraceEnabled())
					logger.trace("Relation " + relation + " added");
				
				// mark graph as modified
				graphModified = true;

				// remove relation from list
				iter.remove();
			}

			// and add new nodes for the next loop
			for (int i = 0; i < newNodes.size(); i++)
			{
				Node node = (Node) newNodes.get(i);
				this.connectedTableAliases.put(node.alias, node);
			}
			newNodes.clear();
		}
		while (graphModified);

		// the remaining relations are the unused ones, i.e. no connection to any
		// node (table alias) of the graph
		this.unusedRelations = relationContraints;

		if (logger.isDebugEnabled())
		{
			logger.debug("QBERelationGraph(): initialTableAlias=" + initialTableAlias + ", loopCount=" + loopCount + ", unusedRelations=" + unusedRelations.size());
			this.printGraph(System.out);
		}
	}

	/**
	 * @param tableAlias
	 * @return
	 */
	public QBERelationConstraint getRelationToInitial(ITableAlias tableAlias)
	{
		return getNode(tableAlias, true).relationToInitial;
	}

	/**
	 * @param tableAliasName
	 * @return Iterator
	 *         <QBERelationConstraint>
	 */
	public Iterator markAsConstrained(ITableAlias tableAlias)
	{
		return markAsConstrained(tableAlias, false);
	}

	/**
	 * @param tableAliasName
	 * @param isOptional
	 * @return Iterator
	 *         <QBERelationConstraint>
	 */
	public Iterator markAsConstrained(ITableAlias tableAlias, boolean isOptional)
	{
		return new TraverseToInitialIterator(getNode(tableAlias, true), !isOptional);
	}

	/**
	 * @param tableAliasName
	 * @return Iterator
	 *         <QBERelationConstraint>
	 */
	public Iterator markAsResult(ITableAlias tableAlias)
	{
		Node node = getNode(tableAlias, false);
		return node == null ? null : new TraverseToInitialIterator(node, false);
	}

	/**
	 * @param tableAliasName
	 * @return Iterator
	 *         <QBERelationConstraint>
	 */
	public Iterator getBackwardRelations(ITableAlias tableAlias)
	{
		Node node = getNode(tableAlias, true);
		return node.linkedBackwardNodes.values().iterator();
	}

	/**
	 * @param tableAliasName
	 * @return Iterator
	 *         <QBERelationConstraint>
	 */
	public Iterator getForwardRelations(ITableAlias tableAlias)
	{
		Node node = getNode(tableAlias, true);
		return node.linkedForwardNodes.values().iterator();
	}

	/**
   * Returns a list of all relations to get from the initial alias to the given
   * alias. The first relation will be the relation starting at the initial node
   * (if <code>traverseToAlias</code> is not already the initial alias) and so
   * further.
   * 
   * @param tableAliasName
   *          the table alias to traverse to.
   * @param directBackwardOnly
   *          if set to <code>true</code> the given alias must be in direct
   *          backward hierarchy of the initial alias. If not, <code>null</code>
   *          will be returned.
   * @return List of {@link QBERelationConstraint}or <code>null</code> if no
   *         such traversal path exits.
   */
	public List getTraverseFromInitialRelations(ITableAlias traverseToAlias, boolean directBackwardOnly)
	{
		Node node = getNode(traverseToAlias, false);
		if (null == node)
			return null;

		// avoid unnecessary allocation
		if (node.relationToInitial == null)
			return Collections.EMPTY_LIST;
		
		// IBIS: cache traversal list for alias
		LinkedList result = new LinkedList();
		while (node.relationToInitial != null)
		{
		  // check backward only
		  if (directBackwardOnly && node.relationToInitial.isForwardFlag())
		    return null;
		  
		  // add always at first, since we start from traverseToAlias 
			result.addFirst(node.relationToInitial);
			node = node.nextNodeToInitial;
		}

		return result;
	}

	/**
	 * @return Set<ITableAlias>
	 */
	public Set getConnectedTableAliases()
	{
		return Collections.unmodifiableSet(this.connectedTableAliases.keySet());
	}

	private Node getNode(ITableAlias alias, boolean throwException)
	{
		Node node = (Node) this.connectedTableAliases.get(alias);
		if (null == node)
		{
			if (throwException)
				throw new InvalidTableAliasException(alias, this.relationSet);
			
			return null;
		}
		return node;
	}

	private static List getRelationConstraints(IRelationSet relationSet)
	{
		List relationContraints = new LinkedList();
		Iterator iter = relationSet.getRelations().iterator();
		while (iter.hasNext())
		{
			IRelation relation = (IRelation) iter.next();

			if (relation instanceof IManyToManyRelation)
			{
				IManyToManyRelation rel = (IManyToManyRelation) relation;
				relationContraints.add(new QBERelationConstraint(rel.getFromRelation()));
				relationContraints.add(new QBERelationConstraint(rel.getToRelation()));
			}
			else
			{
				IOneToManyRelation rel = (IOneToManyRelation) relation;
				relationContraints.add(new QBERelationConstraint(rel));
			}
		}
		return relationContraints;
	}

	public void printGraph(PrintStream out)
	{
		this.initialNode.printRelations(out);
	}

	/**
	 * @author Andreas
	 * 
	 * To change the template for this generated type comment go to
	 * Window>Preferences>Java>Code Generation>Code and Comments
	 */
	private static final class TraverseToInitialIterator implements Iterator
	{
		private Node nextNode;
		private final boolean enforceInnerJoin;
		
		private TraverseToInitialIterator(Node startNode, boolean enforceInnerJoin)
		{
			this.nextNode = startNode;
			this.enforceInnerJoin = enforceInnerJoin;
		}

		public Object next()
		{
			QBERelationConstraint result = nextNode.relationToInitial;
			if (this.enforceInnerJoin)
			{
				result.setEnforceInnerJoin();
			}
			nextNode = this.nextNode.nextNodeToInitial;
			return result;
		}

		public boolean hasNext()
		{
			return !this.nextNode.isInitial();
		}

		public void remove()
		{
			throw new UnsupportedOperationException();
		}
	}

	/**
	 * @author Andreas
	 * 
	 * To change the template for this generated type comment go to
	 * Window>Preferences>Java>Code Generation>Code and Comments
	 */
	private static final class Node
	{
		private Node nextNodeToInitial = null;
		private QBERelationConstraint relationToInitial = null;
		//		boolean constrained = false;
		//		boolean resultNode = false;
		private final ITableAlias alias;
		private final Map linkedBackwardNodes = new HashMap();
		private final Map linkedForwardNodes = new HashMap();
		
		Node(ITableAlias alias)
		{
			this.alias = alias;
		}

		void addBackwardNode(Node backwardNode, QBERelationConstraint relation)
		{
			this.linkedBackwardNodes.put(backwardNode, relation);
			//			backwardNode.linkedForwardNodes.put(this, relation);
			backwardNode.nextNodeToInitial = this;
			backwardNode.relationToInitial = relation;
		}

		void addForwardNode(Node forwardNode, QBERelationConstraint relation)
		{
			this.linkedForwardNodes.put(forwardNode, relation);
			//			forwardNode.linkedBackwardNodes.put(this, relation);
			forwardNode.nextNodeToInitial = this;
			forwardNode.relationToInitial = relation;
		}

		void printRelations(PrintStream out)
		{
			out.println(this.alias);
			printForwardRelations(out, 0, true);
			printBackwardRelations(out, 0, true);
		}

		private void printBackwardRelations(PrintStream out, int level, boolean recursive)
		{
			Iterator iter = this.linkedBackwardNodes.keySet().iterator();
			while (iter.hasNext())
			{
				Node node = (Node) iter.next();
				for (int i = 0; i < level; i++)
					out.print("    ");
				out.println("--> " + node.alias);
				node.printForwardRelations(out, level + 1, false);
				if (recursive)
				{
					node.printBackwardRelations(out, level + 1, recursive);
				}
			}
		}

		private void printForwardRelations(PrintStream out, int level, boolean recursive)
		{
			Iterator iter = this.linkedForwardNodes.keySet().iterator();
			while (iter.hasNext())
			{
				Node node = (Node) iter.next();
				for (int i = 0; i < level; i++)
					out.print("    ");
				out.println("<-- " + node.alias);
				if (recursive)
				{
					node.printForwardRelations(out, level + 1, recursive);
				}
				node.printBackwardRelations(out, level + 1, recursive);
			}
		}

		boolean isInitial()
		{
			return this.nextNodeToInitial == null;
		}

		public boolean equals(Object anObject)
		{
			if (this == anObject)
			{
				return true;
			}
			if (anObject instanceof Node)
			{
				Node another = (Node) anObject;
				return this.alias.equals(another.alias);
			}
			return false;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Object#hashCode()
		 */
		public int hashCode()
		{
			return this.alias.hashCode();
		}

	}
	
	/**
	 * @return Returns the relationSet.
	 */
	public IRelationSet getRelationSet()
	{
		return relationSet;
	}

	/**
	 * @return Returns the initialTableAlias.
	 */
	public ITableAlias getInitialTableAlias()
	{
		return initialTableAlias;
	}

}
