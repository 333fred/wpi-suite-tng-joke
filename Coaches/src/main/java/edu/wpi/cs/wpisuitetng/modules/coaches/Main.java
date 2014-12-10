package edu.wpi.cs.wpisuitetng.modules.coaches;

import java.awt.BorderLayout;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javafx.application.Platform;
import javafx.concurrent.Worker.State;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;

import edu.wpi.cs.wpisuitetng.janeway.modules.IJanewayModule;
import edu.wpi.cs.wpisuitetng.janeway.modules.JanewayTabModel;

public class Main implements IJanewayModule {

	List<JanewayTabModel> tabs;

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
	private final JButton distractBtn = new JButton("Distraction");

	public Main() {

		this.tabs = new ArrayList<JanewayTabModel>();

		panel = new JPanel(new BorderLayout());
		panel.add(mainPanel, BorderLayout.CENTER);
		Platform.runLater(() -> {
			view = new WebView();
			engine = view.getEngine();
			engine.setUserAgent("Mozilla/5.0 (Windows NT 6.3; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/37.0.2049.0 Safari/537.36");
			engine.load("http://trello.com");
			mainPanel.setScene(new Scene(view));

			engine.getLoadWorker()
					.workDoneProperty()
					.addListener(
							(observableValue, oldValue, newValue) -> {
								SwingUtilities.invokeLater(() -> progressBar
										.setValue(newValue.intValue()));
							});

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
		buttonPanel.add(distractBtn);

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

		paycheckBtn.addActionListener((action) -> Platform
				.runLater(() -> engine.load("http://randomish.org")));

		distractBtn.addActionListener((action) -> {
			try {
				Process proc = Runtime.getRuntime().exec(
						"java -jar " + System.getProperty("user.home")
								+ File.separator + "Minecraft.jar");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});

		tab1 = new JanewayTabModel("Coaches Task Manager", null, toolbar, panel);

		this.tabs.add(tab1);
	}

	@Override
	public String getName() {
		return "Coaches";
	}

	@Override
	public List<JanewayTabModel> getTabs() {
		return tabs;
	}
}
