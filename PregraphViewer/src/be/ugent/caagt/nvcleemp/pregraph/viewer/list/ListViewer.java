/* ListViewer.java
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

package be.ugent.caagt.nvcleemp.pregraph.viewer.list;

import be.ugent.caagt.nvcleemp.graphio.pregraph.PregraphReader;
import be.ugent.caagt.nvcleemp.pregraph.viewer.ViewerPanel;
import be.ugent.caagt.nvcleemp.pregraph.viewer.util.ListSelectionNavigator;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.image.RenderedImage;
import javax.swing.JPanel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 *
 * @author nvcleemp
 */
public class ListViewer extends JPanel {

    private ViewerPanel viewerPanel = new ViewerPanel();
    private EmbeddedPregraphListModel listModel;

    public ListViewer(PregraphReader reader) {
        this(new DefaultEmbeddedPregraphListModel(reader));
    }

    public ListViewer(EmbeddedPregraphListModel listModel) {
        super(new BorderLayout());
        this.listModel = listModel;
        add(new ListSelectionNavigator(listModel.getSelectionModel(), listModel), BorderLayout.NORTH);
        add(viewerPanel, BorderLayout.CENTER);
        viewerPanel.setGraph(listModel.getSelectedGraph());
        listModel.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            public void valueChanged(ListSelectionEvent e) {
                viewerPanel.setGraph(ListViewer.this.listModel.getSelectedGraph());
            }
        });
    }
    private Dimension d = new Dimension(600, 450);

    @Override
    public Dimension getPreferredSize() {
        return d;
    }

    public RenderedImage getImage() {
        return viewerPanel.getImage();
    }

    
}
