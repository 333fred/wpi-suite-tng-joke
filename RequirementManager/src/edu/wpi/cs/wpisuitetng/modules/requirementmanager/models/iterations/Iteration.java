/*******************************************************************************
 * Copyright (c) 2013 WPI-Suite
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Team Rolling Thunder
 ******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.requirementmanager.models.iterations;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import com.google.gson.Gson;

import edu.wpi.cs.wpisuitetng.modules.AbstractModel;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.iterationcontroller.UpdateIterationController;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.models.Requirement;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.models.RequirementModel;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.models.characteristics.IterationDate;

/**
 * An iteration in a project. Requirements can be assigned to an iteration.
 * 
 * @author Gabriel McCormick, David Iglesias, Nick Mollica, Chris Botaish, Arianna
 * @version $Revision: 1.0 $
 */
public class Iteration extends AbstractModel {
	/** the ID of the iteration */
	private int id; // TODO: move ID stuff to server side? (copied from
					// requirement impl.)

	/** the name of the iteration */
	private String name;

	/** the estimated amount of effort to complete the iteration */
	private int estimate;
		
	/** start and end date associated with the iteration */
	private IterationDate start;
	private IterationDate end;

	public Iteration() {
	}

	/**
	 * Construct an Iteration with required properties provided and others set
	 * to default
	 * 
	 * @param id
	 *            The ID number of the iteration
	 * @param name
	 *            The name of the iteration
	 * @param start
	 * 			  The start of the iteration
	 * @param end
	 * 			  The end of the iteration
	 */
	public Iteration(int id, String name, IterationDate start, IterationDate end) {
		this.id = id;
		this.name = name;
		if (name.trim().length() == 0)
			this.name = "Backlog";
		this.estimate = 0;
		this.start = start;
		this.end = end;
	}
	
	/**
	 * Constructor for Iteration.
	 * @param id int
	 * @param name String
	 */
	public Iteration(int id, String name) {
		this.id = id;
		this.name = name;
		if (name.trim().length() == 0)
			this.name = "Backlog";
		this.estimate = 0;
		this.start = null;
		this.end = null;
	}

	/**
	 * Getter for the id
	 * 
	
	 * @return the id */
	public int getId() {
		return id;
	}

	/**
	 * Setter for the id
	 * 
	 * @param id
	 *            the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * getter for the name
	 * 
	
	 * @return the name */
	public String getName() {
		return name;
	}

	/**
	 * setter for the name
	 * 
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
		if (name.trim().length() == 0)
			this.name = "Backlog";
		UpdateIterationController.getInstance().updateIteration(this);
	}

	/**
	 * Getter for the estimate
	 * 
	
	 * @return the estimate */
	public int getEstimate() {
		return estimate;
	}

	/**
	 * Setter for the estimate
	 * 
	 * @param estimate
	 *            the estimate to set
	 */
	public void setEstimate(int estimate) {
		this.estimate = estimate;
		UpdateIterationController.getInstance().updateIteration(this);
	}
	
	/**
	 * Getter for the requirements
	 * 
	 * @return list of requirements
	 */
	public List<Requirement> getRequirements() {
		return RequirementModel.getInstance().getRequirementsForIteration(name);
	}
	

	/**
	
	 * @return the start of the iteration */
	public IterationDate getStart() {
		return start;
	}

	/**
	 * @param start the start of the iteration
	 */
	public void setStart(IterationDate start) {
		this.start = start;
		UpdateIterationController.getInstance().updateIteration(this);
	}

	/**
	
	 * @return the end of the iteration */
	public IterationDate getEnd() {
		return end;
	}

	/**
	 * @param end the end of the iteration
	 */
	public void setEnd(IterationDate end) {
		this.end = end;
		UpdateIterationController.getInstance().updateIteration(this);
	}

	/**
	 * represents iteration as a string which is currently just the name
	 * 
	
	 * @return the name * @see edu.wpi.cs.wpisuitetng.modules.Model#toString()
	 */
	// should this be extended for the other fields?
	public String toString() {
		return name;
	}

	/**
	 * compares two iterations for equality based on name
	 * 
	 * @param that
	 *            iteration to compare to
	
	 * @return boolean for equality */
	// should this also be extended for the other fields?
	public boolean equals(Iteration that) {
		if (this.name.equals(that.getName()))
			return true;
		else
			return false;
	}

	/**
	 * Copies all of the values from the given iteration to this iteration
	 * excluding the Id.
	 * 
	 * @param toCopyFrom
	 *            the iteration to copy from.
	 */
	public void copyFrom(Iteration toCopyFrom) {
		this.name = toCopyFrom.name;
		this.estimate = toCopyFrom.estimate;
	}

	/**
	 * Returns an array of Iterations parsed from the given JSON-encoded
	 * string.
	 * 
	 * @param body	string containing a JSON-encoded array of Iteration
	
	 * @return an array of Requirement deserialized from the given JSON string */
	public static Iteration[] fromJsonArray(String body) {
		final Gson parser = new Gson();
		return parser.fromJson(body, Iteration[].class);
	}

	/**
	 * Method toJSON.
	 * @return String
	 * @see edu.wpi.cs.wpisuitetng.modules.Model#toJSON()
	 */
	public String toJSON() {
		return new Gson().toJson(this, Iteration.class);
	}

	/**
	 * Returns an instance of Iteration constructed using the given
	 * Iteration encoded as a JSON string.
	 * 
	 * @param body the
	 *            JSON-encoded Iteration to deserialize
	
	 * @return the Iteration contained in the given JSON */
	public static Iteration fromJson(String body) {
		final Gson parser = new Gson();
		Iteration test = parser.fromJson(body, Iteration.class);

		return test;
	}

	/**
	 * Method identify.
	 * @param o Object
	 * @return Boolean
	 * @see edu.wpi.cs.wpisuitetng.modules.Model#identify(Object)
	 */
	@Override
	public Boolean identify(Object o) {
		return null;
	}

	/**
	 * Method save.
	 * @see edu.wpi.cs.wpisuitetng.modules.Model#save()
	 */
	@Override
	public void save() {

	}

	/**
	 * Method delete.
	 * @see edu.wpi.cs.wpisuitetng.modules.Model#delete()
	 */
	@Override
	public void delete() {

	}

}
