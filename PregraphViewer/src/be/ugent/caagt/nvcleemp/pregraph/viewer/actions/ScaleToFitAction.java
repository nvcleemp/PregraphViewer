/* ScaleToFitAction.java
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

import be.ugent.caagt.nvcleemp.pregraph.viewer.embedder.ScaleToFitEmbedder;
import be.ugent.caagt.nvcleemp.pregraph.viewer.list.EmbeddedPregraphListModel;
import be.ugent.caagt.nvcleemp.pregraph.viewer.list.ListViewer;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;

/**
 *
 * @author nvcleemp
 */
public class ScaleToFitAction extends AbstractAction{

    private EmbeddedPregraphListModel listModel;
    private ListViewer listViewer;
    private ScaleToFitEmbedder embedder = new ScaleToFitEmbedder();

    public ScaleToFitAction(EmbeddedPregraphListModel listModel, ListViewer listViewer) {
        super("Scale graph to window");
        this.listModel = listModel;
        this.listViewer = listViewer;
    }

    public void actionPerformed(ActionEvent e) {
        Dimension d = listViewer.getViewerSize();
        embedder.setDimensions(d.width, d.height);
        embedder.setGraph(listModel.getSelectedGraph());
        embedder.embed();
    }


}
