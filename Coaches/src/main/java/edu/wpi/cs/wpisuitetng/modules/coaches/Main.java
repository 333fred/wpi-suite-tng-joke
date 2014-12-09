/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * Contributors:
 * Chris Casola
 ******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.coaches;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.List;

import javafx.application.Platform;
import javafx.concurrent.Worker.State;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;

import edu.wpi.cs.wpisuitetng.janeway.modules.IJanewayModule;
import edu.wpi.cs.wpisuitetng.janeway.modules.JanewayTabModel;

/**
 * @author 333fr_000
 */
public class Main implements IJanewayModule {

	/**
	 * A list of tabs owned by this module
	 */
	List<JanewayTabModel> tabs;

	/**
	 * Variables, used for tests
	 */
	JanewayTabModel tab1;

	private final JFXPanel mainPanel = new JFXPanel();
	private WebEngine engine;
	private WebView view;

	private final JPanel panel;
	private final JPanel toolbar;
	private final JProgressBar progressBar = new JProgressBar();
	private final JButton trelloBtn = new JButton("Trello");
	private final JButton jiraBtn = new JButton("Jira");
	private final JButton vsoBtn = new JButton("Visual Studio Online");
	private final JButton fcBtn = new JButton("Freedcamp");
	private final JButton gdBtn = new JButton("Google Drive");
	private final JButton paycheckBtn = new JButton("Paycheck");

	/**
	 * Constructs the main views for this module. Namely one tab, containing a
	 * toolbar and a main content panel.
	 */
	public Main() {

		// Initialize the list of tabs (however, this module has only one tab)
		this.tabs = new ArrayList<JanewayTabModel>();

		panel = new JPanel(new BorderLayout());
		panel.add(mainPanel, BorderLayout.CENTER);
		Platform.runLater(() -> {
			view = new WebView();
			engine = view.getEngine();
			engine.setUserAgent("Mozilla/5.0 (Windows NT 6.3; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/37.0.2049.0 Safari/537.36");
			engine.load("http://trello.com");
			mainPanel.setScene(new Scene(view));

			// The progress bar
			engine.getLoadWorker()
					.workDoneProperty()
					.addListener(
							(observableValue, oldValue, newValue) -> {
								SwingUtilities.invokeLater(() -> progressBar
										.setValue(newValue.intValue()));
							});

			// If an error occurs
			engine.getLoadWorker()
					.exceptionProperty()
					.addListener(
							(o, old, value) -> {
								if (engine.getLoadWorker().getState() == State.FAILED) {
									SwingUtilities.invokeLater(() -> {
										JOptionPane
												.showMessageDialog(
														panel,
														(value != null) ? engine
																.getLocation()
																+ "\n"
																+ value.getMessage()
																: engine.getLocation()
																		+ "\nUnexpected error.",
														"Loading error...",
														JOptionPane.ERROR_MESSAGE);
									});
								}
								mainPanel.setScene(new Scene(view));
							});
		});

		toolbar = new JPanel(new BorderLayout());
		JPanel buttonPanel = new JPanel();
		toolbar.add(buttonPanel, BorderLayout.CENTER);
		toolbar.add(progressBar, BorderLayout.SOUTH);
		buttonPanel.add(trelloBtn);
		buttonPanel.add(jiraBtn);
		buttonPanel.add(vsoBtn);
		buttonPanel.add(fcBtn);
		buttonPanel.add(gdBtn);
		buttonPanel.add(paycheckBtn);

		trelloBtn.addActionListener((action) -> Platform.runLater(() -> engine
				.load("http://trello.com")));

		jiraBtn.addActionListener((action) -> Platform.runLater(() -> engine
				.load("https://jira.atlassian.com/secure/Dashboard.jspa")));

		vsoBtn.addActionListener((action) -> Platform.runLater(() -> engine
				.load("https://fsilberberg.visualstudio.com/")));

		fcBtn.addActionListener((action) -> Platform.runLater(() -> engine
				.load("https://freedcamp.com/")));

		gdBtn.addActionListener((action) -> Platform.runLater(() -> engine
				.load("https://drive.google.com/drive/#folders/0B61x-pCgzYaaeGNtTTNORFlCTHc/0B0f75QqLd9z9dUhuZ01TcS1nd2s")));

		paycheckBtn.addActionListener((action) -> Platform.runLater(() -> engine
				.load("http://randomish.org")));

		tab1 = new JanewayTabModel("Coaches Task Manager", null, toolbar, panel);

		// Add the tab to the list of tabs owned by this module
		this.tabs.add(tab1);
	}

	/*
	 * @see edu.wpi.cs.wpisuitetng.janeway.modules.IJanewayModule#getName()
	 */
	@Override
	public String getName() {
		return "Coaches";
	}

	/*
	 * @see edu.wpi.cs.wpisuitetng.janeway.modules.IJanewayModule#getTabs()
	 */
	@Override
	public List<JanewayTabModel> getTabs() {
		return tabs;
	}
}
