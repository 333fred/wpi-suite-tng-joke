package edu.wpi.cs.wpisuitetng.modules.RequirementManager.view.Requirements;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import edu.wpi.cs.wpisuitetng.modules.RequirementManager.controller.UpdateRequirementController;
import edu.wpi.cs.wpisuitetng.modules.RequirementManager.models.Requirement;
import edu.wpi.cs.wpisuitetng.modules.RequirementManager.models.characteristics.Iteration;
import edu.wpi.cs.wpisuitetng.modules.RequirementManager.models.characteristics.RequirementPriority;
import edu.wpi.cs.wpisuitetng.modules.RequirementManager.models.characteristics.RequirementStatus;
import edu.wpi.cs.wpisuitetng.modules.RequirementManager.models.characteristics.RequirementType;
import edu.wpi.cs.wpisuitetng.modules.RequirementManager.models.characteristics.TransactionHistory;
import edu.wpi.cs.wpisuitetng.modules.RequirementManager.view.ViewEventController;
/**
 * 
 * @author Pi 
 * @author Chris
 * @author Brian
 *
 */
public class EditRequirementPanel extends RequirementPanel 
{	
	private Requirement requirementBeingEdited;
	
	/**
	 * Constructor for a new requirement panel
	 * @param reqModel Local requirement model for containing data
	 */
	public EditRequirementPanel(Requirement req) {
		super();
		
		GridBagLayout layout = new GridBagLayout();
		contentPanel = new JPanel(layout);
		GridBagConstraints c = new GridBagConstraints();
		
		
		JPanel left = buildLeftPanel();
		JPanel right = buildRightPanel();
		
		JTabbedPane tabs = new JTabbedPane();
		JPanel notes = buildNotePanel();
		JPanel history = buildHistoryPanel();
		tabs.add("Notes", notes);
		tabs.add("Transaction History", history);
		
		JPanel bottom = buildBottom();
		
		c.gridx = 0; // Column 0
		c.gridy = 0; // Row 0
		c.weighty = 1; // Row is elastic
		contentPanel.add(left,c); //add left panel
		
		c.gridx = 1; // Column 1
		contentPanel.add(right,c); //add right panel
		
		c.gridx = 2; //Column 2
		c.weightx = 1; //Column is elastic
		c.fill = GridBagConstraints.BOTH; // Stretch contents
		contentPanel.add(tabs,c); // add tabs
		
		c.fill = GridBagConstraints.NONE;
		c.gridy = 1; // Row 1
		c.gridx = 1; // Column 1
		c.gridwidth = 2; // Fill the rest of the row
		c.weighty = 0; // Row is not elastic
		c.weightx = 0; // Column is not elastic
		c.anchor = GridBagConstraints.LINE_END;
		contentPanel.add(bottom,c); // Add bottom
		
		contentPanel.setMinimumSize(new Dimension(500,465));
		contentPanel.setPreferredSize(new Dimension(500,465));

		this.setViewportView(contentPanel);
		
		requirementBeingEdited = req;
		fillFieldsForRequirement();
	}
	
	/**
	 * Fills the fields of the edit requirement panel based on the current settings of the
	 * edited requirement.
	 */
	private void fillFieldsForRequirement()
	{
		boxName.setText(requirementBeingEdited.getName());
		boxDescription.setText(requirementBeingEdited.getDescription());
		boxEstimate.setText(String.valueOf(requirementBeingEdited.getEstimate()));
		boxReleaseNum.setText(requirementBeingEdited.getRelease());
		dropdownStatus.setSelectedItem(requirementBeingEdited.getStatus());
		dropdownType.setSelectedItem(requirementBeingEdited.getType());
		boxIteration.setText(requirementBeingEdited.getIteration().toString());
		
		switch(requirementBeingEdited.getPriority())
		{
		case BLANK:
			priorityBlank.setSelected(true);
			break;
		case LOW:
			priorityLow.setSelected(true);
			break;
		case MEDIUM:
			priorityMedium.setSelected(true);
			break;
		case HIGH:
			priorityHigh.setSelected(true);
			break;
		}
		
		if(requirementBeingEdited.getStatus() == RequirementStatus.INPROGRESS || requirementBeingEdited.getStatus() == RequirementStatus.COMPLETE)
		{
			boxEstimate.setEnabled(false);
		}
		else
		{
			boxEstimate.setEnabled(true);
		}
		
		if(!(requirementBeingEdited.getEstimate() > 0)) boxIteration.setEnabled(false);
		
		//reset the error messages.
		this.errorEstimate.setText("");
		boxEstimate.setBorder(defaultBorder);
		this.errorDescription.setText("");
		boxDescription.setBorder(defaultBorder);
		this.errorName.setText("");
		boxName.setBorder(defaultBorder);
		
		repaint();
	}
	
	/**
	 * Builds the right panel
	 */
	@Override
	protected JPanel buildRightPanel()
	{
		super.buildRightPanel();
		
		return rightPanel;
	}
	public JPanel buildBottom()
	{
		//setup the buttons
		JPanel buttonPanel = new JPanel();
		JButton buttonUpdate = new JButton("Update");
		JButton buttonCancel = new JButton("Cancel");
		JButton buttonClear = new JButton("Undo Changes");
		
		// Construct the add requirement controller and add it to the update button
		buttonUpdate.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e)
			{
				if(validateFields()) update();
			}
		});
		
		buttonClear.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				fillFieldsForRequirement();
			}
		
		});
		
		buttonCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cancel();
			}
		});

		buttonPanel.add(buttonUpdate);
		buttonPanel.add(buttonClear);
		buttonPanel.add(buttonCancel);
		
		return buttonPanel;
	}
	
	/**
	 * Updates the requirement.
	 */
	private void update()
	{
		// Extract the name, release number, and description from the GUI fields
		String stringName = this.boxName.getText();
		String stringReleaseNum = this.boxReleaseNum.getText();
		String stringDescription = this.boxDescription.getText();
		String stringEstimate = this.boxEstimate.getText();
		String stringIteration = this.boxIteration.getText();


		RequirementPriority priority;
		RequirementStatus status;
		RequirementType type = (RequirementType)dropdownType.getSelectedItem();
		
		int estimate = stringEstimate.trim().length() == 0 ? 0 : Integer.parseInt(stringEstimate);
		// Extract the status from the GUI
		status = (RequirementStatus)this.dropdownStatus.getSelectedItem();
		Iteration iteration = new Iteration(stringIteration);
		// Extract which radio is selected for the priority
		boolean stateHigh = priorityHigh.isSelected();
		boolean stateMedium = priorityMedium.isSelected();
		boolean stateLow = priorityLow.isSelected();

		// Convert the priority string to its corresponding enum
		if (stateHigh)
			priority = RequirementPriority.HIGH;
		else if (stateMedium)
			priority = RequirementPriority.MEDIUM;
		else if (stateLow)
			priority = RequirementPriority.LOW;
		else
			priority = RequirementPriority.BLANK;

		// Set to false to indicate the requirement is being newly created
		boolean created = false;
		
		// Set the time stamp so that all transaction messages from this update 
		// will have the same time stamp
		TransactionHistory requirementHistory = requirementBeingEdited.getHistory();	
		requirementHistory.setTimestamp(System.currentTimeMillis());
				
		// Create a new requirement object based on the extracted info
		requirementBeingEdited.setName(stringName);
		requirementBeingEdited.setRelease(stringReleaseNum);
		requirementBeingEdited.setDescription(stringDescription);
		requirementBeingEdited.setStatus(status, created);
		requirementBeingEdited.setPriority(priority, created);
		requirementBeingEdited.setEstimate(estimate);
		requirementBeingEdited.setIteration(iteration, created);
		requirementBeingEdited.setType(type);		
		UpdateRequirementController.getInstance().updateRequirement(requirementBeingEdited);
		ViewEventController.getInstance().refreshTable();
		ViewEventController.getInstance().removeTab(this);
	}
	
	private JPanel buildNotePanel()
	{
		GridBagLayout layout = new GridBagLayout();
		JPanel panel = new JPanel(layout);
		
		JScrollPane scroll = new JScrollPane();
		
		panel.add(scroll);
		
		return panel;
	}
	
	private JPanel buildHistoryPanel()
	{
		return new JPanel();
	}
	
	/**
	 * Cancels the editing of the requirement.
	 */
	private void cancel()
	{
		ViewEventController.getInstance().removeTab(this);
	}
}
