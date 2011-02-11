/* Viewer.java
 * =========================================================================
 * This file is part of PregraphViewer - http://caagt.ugent.be/pregraphs
 * 
 * Copyright (C) 2010-2011 Universiteit Gent
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or (at
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

import be.ugent.caagt.nvcleemp.graphio.pregraph.PregraphReader;
import be.ugent.caagt.nvcleemp.pregraph.viewer.embedder.EmbedderRunner;
import be.ugent.caagt.nvcleemp.pregraph.viewer.embedder.ForceEmbedder;
import be.ugent.caagt.nvcleemp.pregraph.viewer.embedder.RandomEmbedder;
import be.ugent.caagt.nvcleemp.pregraph.viewer.embedder.ScaleToFitEmbedder;
import be.ugent.caagt.nvcleemp.pregraph.viewer.io.EmbeddedPregraphXmlReader;
import be.ugent.caagt.nvcleemp.pregraph.viewer.list.DefaultEmbeddedPregraphListModel;
import be.ugent.caagt.nvcleemp.pregraph.viewer.list.EmbeddedPregraphListModel;
import be.ugent.caagt.nvcleemp.pregraph.viewer.preferences.PregraphViewerPreferences;
import be.ugent.caagt.nvcleemp.pregraph.viewer.preferences.PregraphViewerPreferences.Preference;
import be.ugent.caagt.nvcleemp.pregraph.viewer.preferences.PregraphViewerPreferencesListener;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author nvcleemp
 */
public class Viewer extends JFrame {

    private final JMenu windowsMenu;

    public Viewer(){
        super("Pregraph Viewer (c) NVC 2010-2011");
        setLayout(new GridLayout(1,1));
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JMenuBar bar = new JMenuBar();
        JMenu menu = new JMenu("File");
        menu.add(new AbstractAction("Open") {

            JFileChooser chooser = new JFileChooser();

            private PregraphViewerPreferencesListener preferencesListener =
                    new PregraphViewerPreferencesListener() {

                public void preferenceChanged(Preference preference) {
                    String dir = PregraphViewerPreferences.getInstance()
                            .getStringPreference(Preference.CURRENT_DIRECTORY);
                    if (chooser != null && dir != null) {
                        chooser.setCurrentDirectory(new File(dir));
                    }
                }
            };

            {
                String dir = PregraphViewerPreferences.getInstance()
                            .getStringPreference(Preference.CURRENT_DIRECTORY);
                if (dir != null) {
                    chooser.setCurrentDirectory(new File(dir));
                }
                PregraphViewerPreferences.getInstance().addListener(preferencesListener);
            }

            public void actionPerformed(ActionEvent e) {
                if(chooser.showOpenDialog(Viewer.this)==JFileChooser.APPROVE_OPTION){
                    PregraphViewerPreferences.getInstance().setStringPreference(
                            Preference.CURRENT_DIRECTORY,
                            chooser.getSelectedFile().getParent());
                    PregraphReader pregraphReader = null;
                    try {
                        pregraphReader = new PregraphReader(new FileInputStream(chooser.getSelectedFile()));
                    } catch (FileNotFoundException ex) {
                        Logger.getLogger(Viewer.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    if(pregraphReader!=null){
                        EmbeddedPregraphListModel listModel = new DefaultEmbeddedPregraphListModel(pregraphReader);
                        EmbedderRunner.singleRunEmbedder(new RandomEmbedder(), listModel);
                        EmbedderRunner.repeatedRunEmbedder(50, new ForceEmbedder(), listModel);
                        EmbedderRunner.singleRunEmbedder(new ScaleToFitEmbedder(500,400), listModel);
                        ViewerFrame frame = new ViewerFrame(chooser.getSelectedFile().getName(), listModel);
                        registerFrame(frame);
                        frame.setVisible(true);
                    }
                }
            }
        });
        menu.add(new AbstractAction("Open embedded pregraph list") {

            JFileChooser chooser = new JFileChooser();

            private PregraphViewerPreferencesListener preferencesListener =
                    new PregraphViewerPreferencesListener() {

                public void preferenceChanged(Preference preference) {
                    String dir = PregraphViewerPreferences.getInstance()
                            .getStringPreference(Preference.CURRENT_DIRECTORY);
                    if (chooser != null && dir != null) {
                        chooser.setCurrentDirectory(new File(dir));
                    }
                }
            };

            {
                chooser.setFileFilter(new FileNameExtensionFilter("Embedded Pregraph XML", "epxml"));
                String dir = PregraphViewerPreferences.getInstance()
                            .getStringPreference(Preference.CURRENT_DIRECTORY);
                if (dir != null) {
                    chooser.setCurrentDirectory(new File(dir));
                }
                PregraphViewerPreferences.getInstance().addListener(preferencesListener);
            }

            public void actionPerformed(ActionEvent e) {
                if(chooser.showOpenDialog(Viewer.this)==JFileChooser.APPROVE_OPTION){
                    PregraphViewerPreferences.getInstance().setStringPreference(
                            Preference.CURRENT_DIRECTORY,
                            chooser.getSelectedFile().getParent());
                    EmbeddedPregraphXmlReader pregraphReader = new EmbeddedPregraphXmlReader(chooser.getSelectedFile());
                    EmbeddedPregraphListModel listModel = new DefaultEmbeddedPregraphListModel(pregraphReader);
                    ViewerFrame frame = new ViewerFrame(chooser.getSelectedFile().getName(), listModel);
                    registerFrame(frame);
                    frame.setVisible(true);
                }
            }
        });
        windowsMenu = new JMenu("Windows"){

            @Override
            public JMenuItem add(Action a) {
                setEnabled(true);
                return super.add(a);
            }
        };
        windowsMenu.setEnabled(false);
        bar.add(menu);
        bar.add(windowsMenu);
        setJMenuBar(bar);

        pack();
        setVisible(true);
    }

    private void registerFrame(JFrame frame){
        windowsMenu.add(new WindowAction(frame));
    }

    public static void main(String[] args) {
        new Viewer();
    }

    private static class WindowAction extends AbstractAction implements WindowListener{

        private JFrame frame;

        public WindowAction(JFrame frame) {
            super(frame.getTitle());
            this.frame = frame;
            frame.addWindowListener(this);
        }

        public void actionPerformed(ActionEvent e) {
            frame.setVisible(true);
        }

        public void windowOpened(WindowEvent e) {
            setEnabled(false);
        }

        public void windowClosing(WindowEvent e) {
            setEnabled(true);
        }

        public void windowClosed(WindowEvent e) {
        }

        public void windowIconified(WindowEvent e) {
        }

        public void windowDeiconified(WindowEvent e) {
        }

        public void windowActivated(WindowEvent e) {
        }

        public void windowDeactivated(WindowEvent e) {
        }

    }
}
