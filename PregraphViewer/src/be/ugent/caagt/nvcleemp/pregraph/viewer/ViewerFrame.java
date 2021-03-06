/* ViewerFrame.java
 * =========================================================================
 * This file is part of PregraphViewer - http://caagt.ugent.be/pregraphs
 * 
 * Copyright (C) 2010-2011 Universiteit Gent
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or (at
 * your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 * 
 * A copy of the GNU General Public License can be found in the file
 * LICENSE.txt provided with the source distribution of this program (see
 * the META-INF directory in the source jar). This license can also be
 * found on the GNU website at http://www.gnu.org/licenses/gpl.html.
 * 
 * If you did not receive a copy of the GNU General Public License along
 * with this program, contact the lead developer, or write to the Free
 * Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA
 * 02110-1301, USA.
 */

package be.ugent.caagt.nvcleemp.pregraph.viewer;

import be.ugent.caagt.nvcleemp.pregraph.viewer.ViewerSettings.Setting;
import be.ugent.caagt.nvcleemp.pregraph.viewer.actions.ExportAllToLatex;
import be.ugent.caagt.nvcleemp.pregraph.viewer.actions.ExportToLatex;
import be.ugent.caagt.nvcleemp.pregraph.viewer.actions.SaveEmbeddedPregraphListAction;
import be.ugent.caagt.nvcleemp.pregraph.viewer.actions.SaveImageAction;
import be.ugent.caagt.nvcleemp.pregraph.viewer.actions.ScaleToFitAction;
import be.ugent.caagt.nvcleemp.pregraph.viewer.embedder.EmbedderControl;
import be.ugent.caagt.nvcleemp.pregraph.viewer.list.EmbeddedPregraphListModel;
import be.ugent.caagt.nvcleemp.pregraph.viewer.list.ListViewer;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * @author nvcleemp
 */
public class ViewerFrame extends JFrame {

    public ViewerFrame(String name, EmbeddedPregraphListModel listModel) {
        super(name);
        setLayout(new GridLayout(1,1));
        setDefaultCloseOperation(HIDE_ON_CLOSE);
        JPanel panel = new JPanel(new BorderLayout());
        final ViewerSettings settings = new ViewerSettings();
        final ViewerPanel viewerPanel = new ViewerPanel(settings);
        ListViewer listViewer = new ListViewer(viewerPanel, listModel);
        panel.add(listViewer, BorderLayout.CENTER);
        panel.add(new EmbedderControl(listModel), BorderLayout.SOUTH);
        add(panel);
        JMenuBar bar = new JMenuBar();
        JMenu saveMenu = new JMenu("Save");
        saveMenu.add(new SaveImageAction(listViewer));
        saveMenu.add(new SaveEmbeddedPregraphListAction(listModel));
        saveMenu.add(new ExportToLatex(listModel));
        saveMenu.add(new ExportAllToLatex(listModel));
        bar.add(saveMenu);
        JMenu settingsMenu = createSettingsMenu(settings);
        bar.add(settingsMenu);
        JMenu viewMenu = new JMenu("View");
        viewMenu.add(new ScaleToFitAction(listModel, listViewer));
        bar.add(viewMenu);
        setJMenuBar(bar);
        pack();
    }

    private JMenu createSettingsMenu(final ViewerSettings settings){
        final JMenu settingsMenu = new JMenu("Settings");
        final JCheckBoxMenuItem showLegendItem = new JCheckBoxMenuItem("Show legend", settings.showLegend());
        showLegendItem.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                settings.setShowLegend(showLegendItem.isSelected());
            }
        });
        settingsMenu.add(showLegendItem);

        settings.addViewerSettingsListener(new ViewerSettingsListener() {

            public void settingChanged(Setting setting) {
                if(Setting.SHOW_LEGEND.equals(setting)){
                    showLegendItem.setSelected(settings.showLegend());
                }
            }
        });
        return settingsMenu;
    }
}
