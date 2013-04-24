/*******************************************************************************
 * Copyright (c) 2013 WPI-Suite
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Team Rolling Thunder
 ******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.requirementmanager.view.requirements;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.FontMetrics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import edu.wpi.cs.wpisuitetng.modules.requirementmanager.models.Requirement;

/**
 */
public class RequirementButtonPanel extends JPanel implements RequirementPanelListener
{
	private final RequirementPanel parentPanel;
	private final RequirementViewMode viewMode;
	private final List<String> errorList;
	private final JButton buttonOK;
	private final JButton buttonCancel;
	private final JButton buttonClear;
	private final JButton buttonDelete;
	private final JLabel errorMessage;
	private boolean changes;
	private boolean valid;
	
	/**
	 * Constructor for the requirement button panel
	 * @param parentPanel the panel this reports to
	 * @param mode viewmode for the panel
	 * @param curr current requirement
	 */
	public RequirementButtonPanel(RequirementPanel parentPanel, RequirementViewMode mode, Requirement curr)
	{
		this.setLayout(new FlowLayout(FlowLayout.LEFT));
		changes = false;
		valid = false;
		errorList = new ArrayList<String>();
		this.parentPanel = parentPanel;
		viewMode = mode;
		errorMessage = new JLabel("");
		errorMessage.setForeground(Color.RED);
		String okString;
		final String cancelString = "Cancel";
		String clearString;
		final String deleteString = "Delete";
		
		buttonOK = new JButton();
		buttonCancel = new JButton(cancelString);
		buttonClear = new JButton();
		buttonDelete = new JButton(deleteString);
		
		this.add(buttonOK);
		this.add(buttonClear);
		if(viewMode == RequirementViewMode.CREATING)
		{
			okString = "Create";
			clearString = "Clear";
		}
		else
		{
			okString = "Update";
			clearString = "Undo Changes";
			this.add(buttonDelete);
		}
		
		buttonOK.setText(okString);
		buttonClear.setText(clearString);
		this.add(buttonCancel);
		buttonOK.setEnabled(false);
		buttonClear.setEnabled(false);
		this.add(errorMessage);
		setupListeners();
	}
	
	/**
	 * Sets up the listeners for the buttons in the requirement button panel.
	 */
	private void setupListeners()
	{
		buttonOK.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				parentPanel.OKPressed();
			}
		});

		buttonClear.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				parentPanel.clearPressed();
			}

		});
		
		buttonCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				parentPanel.cancelPressed();
			}
		});
		
		buttonDelete.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e)
			{
				parentPanel.deletePressed();
			}
		});
	}

	/**
	 * Method fireDeleted.
	 * @param b boolean
	 * @see edu.wpi.cs.wpisuitetng.modules.requirementmanager.view.requirements.RequirementPanelListener#fireDeleted(boolean)
	 */
	@Override
	public void fireDeleted(boolean b) 
	{
		this.buttonDelete.setEnabled(!b);
	}

	/**
	 * Method fireValid.
	 * @param b boolean
	 * @see edu.wpi.cs.wpisuitetng.modules.requirementmanager.view.requirements.RequirementPanelListener#fireValid(boolean)
	 */
	@Override
	public void fireValid(boolean b) {
		valid = b;
		this.buttonOK.setEnabled(b && changes);
	}

	/**
	 * Method fireChanges.
	 * @param b boolean
	 * @see edu.wpi.cs.wpisuitetng.modules.requirementmanager.view.requirements.RequirementPanelListener#fireChanges(boolean)
	 */
	@Override
	public void fireChanges(boolean b) {
		changes = b;
		this.buttonOK.setEnabled(b && valid);
		this.buttonClear.setEnabled(b);
	}
	
	/**
	 * Method readyToRemove.
	 * @return boolean
	 * @see edu.wpi.cs.wpisuitetng.modules.requirementmanager.view.requirements.RequirementPanelListener#readyToRemove()
	 */
	@Override
	public boolean readyToRemove() {
		return true;
	}

	/**
	 * Method fireRefresh.
	 * @see edu.wpi.cs.wpisuitetng.modules.requirementmanager.view.requirements.RequirementPanelListener#fireRefresh()
	 */
	@Override
	public void fireRefresh() {}
	
	/**
	 * Shows the error message in the button panel.
	 * @param msg the error message
	 */
	public void displayError(String msg){
		if(msg.trim().length() == 0) return;
		if(errorList.contains(msg)) return;
		
		errorList.add(msg);
		refreshError();
	}
	
	/**
	 * Removes the given error message
	 * @param msg the error messgae
	 */
	public void removeError(String msg)
	{
		errorList.remove(msg);
		refreshError();
	}
	
	/**
	 * Refreshes the error display.
	 */
	private void refreshError()
	{
		String errorString = "";
		int width = getWidth();
		//FontMetrics m = errorMessage.getGraphics().getFontMetrics();
		
		//int currWidth = 0;
		for(String err : errorList)
		{
			//if(currWidth + m.stringWidth(err) > width) break;
			//currWidth += m.stringWidth(err);
			errorString += err;
			errorString += " ";
		}
		
		errorMessage.setText(errorString);
	}
	
	/**
	 * 
	
	 * @return the clear button */
	public JButton getButtonClear() {
		return buttonClear;
	}
	
	/**
	 * 
	
	 * @return the delete button */
	public JButton getButtonDelete() {
		return buttonDelete;
	}
	
	/**
	 * 
	
	 * @return the ok button */
	public JButton getButtonOK() {
		return buttonOK;
	}
	
	/**
	 * 
	
	 * @return the cancel button */
	public JButton getButtonCancel() {
		return buttonCancel;
	}
}
