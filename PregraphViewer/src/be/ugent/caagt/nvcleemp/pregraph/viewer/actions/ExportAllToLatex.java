/* ExportAllToLatex.java
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

package be.ugent.caagt.nvcleemp.pregraph.viewer.actions;

import be.ugent.caagt.nvcleemp.pregraph.viewer.io.EmbeddedPregraphLatexExport;
import be.ugent.caagt.nvcleemp.pregraph.viewer.list.EmbeddedPregraphListModel;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 *
 * @author nvcleemp
 */
public class ExportAllToLatex extends AbstractAction {

    private final LatexExportFrame latexExportFrame = new LatexExportFrame();
    private EmbeddedPregraphListModel listModel;

    public ExportAllToLatex(EmbeddedPregraphListModel listModel) {
        super("Export all to LaTeX");
        this.listModel = listModel;
    }

    public void actionPerformed(ActionEvent e) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                StringBuilder builder = new StringBuilder();
                for (int i = 0; i<listModel.getSize(); i++) {
                    builder.append(EmbeddedPregraphLatexExport.export(listModel.getGraph(i)));
                    builder.append("\n\n");
                }
                latexExportFrame.setText(builder.toString());
                latexExportFrame.setVisible(true);
            }
        });
    }

    private static class LatexExportFrame extends JFrame implements ClipboardOwner {

        private final JTextArea textArea;

        public LatexExportFrame() {
            super("LaTeX export");
            textArea = new JTextArea(20, 80);
            EventQueue.invokeLater(new Runnable() {
                public void run() {
                    installGui();
                }
            });
        }

        public void setText(final String text) {
            textArea.setText(text);
        }

        private void installGui() {
            setLayout(new BorderLayout());
            textArea.setLineWrap(true);
            textArea.setWrapStyleWord(true);
            textArea.setEditable(false);
            textArea.setHighlighter(null);
            add(new JScrollPane(textArea), BorderLayout.CENTER);
            add(new JButton(new AbstractAction("Copy to clipboard") {

                public void actionPerformed(ActionEvent e) {
                    copyToClipboard();
                }
            }), BorderLayout.SOUTH);
            pack();
        }

        private void copyToClipboard() {
            StringSelection stringSelection = new StringSelection(textArea.getText());
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(stringSelection, this);
        }

        public void lostOwnership(Clipboard clipboard, Transferable contents) {
            //don't care about that
        }
    }
}
