/*******************************************************************************
 * Copyright (c) 2013 WPI-Suite
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Team Rolling Thunder
 ******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.requirementmanager.view.overview;

import java.awt.BorderLayout;
import java.awt.Graphics;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import edu.wpi.cs.wpisuitetng.modules.requirementmanager.view.ViewEventController;

/**
 * @author justinhess
 * @version $Revision: 1.0 $
 */
public class OverviewPanel extends JPanel {
	
	/**
	 * Sets up directory table of requirements in system
	 */
	public OverviewPanel()
	{
		this.setLayout(new BorderLayout());

		OverviewTreePanel filterPanel = new OverviewTreePanel();
		
		String[] columnNames = {"ID", "Name", "Release #", "Iteration", "Type", "Status", "Priority", "Estimate"};
				
		Object[][] data = {};
		
		OverviewTable table = new OverviewTable(data, columnNames);
		
		JScrollPane tablePanel = new JScrollPane(table);
		
		table.getColumnModel().getColumn(0).setMaxWidth(40); // ID
		
		table.getColumnModel().getColumn(1).setMaxWidth(200); // Name

		table.getColumnModel().getColumn(2).setMinWidth(90); // Release #
		table.getColumnModel().getColumn(2).setMaxWidth(90); // Release #
		
		table.getColumnModel().getColumn(3).setMaxWidth(90); // Iteration
		
		table.getColumnModel().getColumn(4).setMinWidth(105); // Type
		table.getColumnModel().getColumn(4).setMaxWidth(105); // Type
		
		table.getColumnModel().getColumn(5).setMinWidth(85); // Status
		table.getColumnModel().getColumn(5).setMaxWidth(85); // Status
		
		table.getColumnModel().getColumn(6).setMinWidth(75); // Priority
		table.getColumnModel().getColumn(6).setMaxWidth(75); // Priority
		
		table.getColumnModel().getColumn(7).setMinWidth(75); // Estimate
		table.getColumnModel().getColumn(7).setMaxWidth(75); // Estimate
		
		this.add(tablePanel, BorderLayout.CENTER);
		this.add(filterPanel, BorderLayout.WEST);
	}
}
