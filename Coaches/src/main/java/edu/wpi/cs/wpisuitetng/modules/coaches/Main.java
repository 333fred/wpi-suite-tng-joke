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
import java.util.ArrayList;
import java.util.List;



import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;

import javafx.scene.web.WebView;


import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;



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
    
    /**
     * Constructs the main views for this module. Namely one tab, containing
     * a toolbar and a main content panel.
     */
    public Main() {
        
        // Initialize the list of tabs (however, this module has only one tab)
        this.tabs = new ArrayList<JanewayTabModel>();
        
        panel = new JPanel(new BorderLayout());
        panel.add(mainPanel, BorderLayout.CENTER);
        Platform.runLater(() -> {
        	view = new WebView();
        	engine = view.getEngine();
        	engine.load("http://trello.com");
        	mainPanel.setScene(new Scene(view));
        });
        
        tab1 = new JanewayTabModel("Coaches", null, new JLabel("Test 1"), panel);
        
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
