/* DefaultEmbeddedPregraphListModel.java
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

package be.ugent.caagt.nvcleemp.pregraph.viewer.list;

import be.ugent.caagt.nvcleemp.graphio.pregraph.Pregraph;
import be.ugent.caagt.nvcleemp.graphio.pregraph.PregraphReader;
import be.ugent.caagt.nvcleemp.pregraph.viewer.embedder.EmbeddedPregraph;
import be.ugent.caagt.nvcleemp.pregraph.viewer.io.DelaneyDressSymbolReader;
import be.ugent.caagt.nvcleemp.pregraph.viewer.io.EmbeddedPregraphXmlReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractListModel;
import javax.swing.DefaultListSelectionModel;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class DefaultEmbeddedPregraphListModel extends AbstractListModel implements ListSelectionListener, EmbeddedPregraphListModel {

    private List<EmbeddedPregraph> list = new ArrayList<EmbeddedPregraph>();
    private ListSelectionModel selectionModel;

    public DefaultEmbeddedPregraphListModel(PregraphReader reader) {
        try {
            for (Pregraph pregraph : reader.readAllGraphs()) {
                list.add(new EmbeddedPregraph(pregraph));
            }
        } catch (IOException ex) {
            Logger.getLogger(DefaultEmbeddedPregraphListModel.class.getName()).log(Level.SEVERE, null, ex);
        }
        selectionModel = new DefaultListSelectionModel();
        selectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        selectionModel.addListSelectionListener(this);

    }

    public DefaultEmbeddedPregraphListModel(DelaneyDressSymbolReader reader) {
        //TODO: provide common interface for PregraphReader and DelaneyDressSymbolReader
        try {
            for (Pregraph pregraph : reader.readAllGraphs()) {
                list.add(new EmbeddedPregraph(pregraph));
            }
        } catch (IOException ex) {
            Logger.getLogger(DefaultEmbeddedPregraphListModel.class.getName()).log(Level.SEVERE, null, ex);
        }
        selectionModel = new DefaultListSelectionModel();
        selectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        selectionModel.addListSelectionListener(this);

    }

    public DefaultEmbeddedPregraphListModel(EmbeddedPregraphXmlReader reader) {
        try {
            for (EmbeddedPregraph embeddedPregraph : reader.readAllGraphs()) {
                list.add(embeddedPregraph);
            }
        } catch (IOException ex) {
            Logger.getLogger(DefaultEmbeddedPregraphListModel.class.getName()).log(Level.SEVERE, null, ex);
        }
        selectionModel = new DefaultListSelectionModel();
        selectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        selectionModel.addListSelectionListener(this);
    }
    
    public EmbeddedPregraph getGraph(int index) {
        return list.get(index);
    }

    public int getSize() {
        return list.size();
    }

    public Object getElementAt(int index) {
        return getGraph(index);
    }

    public EmbeddedPregraph getSelectedGraph(){
        int selectedIndex = selectionModel.getMinSelectionIndex();
        if(selectedIndex < 0 || selectedIndex>= list.size())
            return null;
        else
            return getGraph(selectedIndex);
    }

    public void valueChanged(ListSelectionEvent e) {
        //fireSelectedGraphChanged();
    }
    
    public void addListSelectionListener(ListSelectionListener l){
        selectionModel.addListSelectionListener(l);
    }

    public void removeListSelectionListener(ListSelectionListener l){
        selectionModel.removeListSelectionListener(l);
    }
    
    public ListSelectionModel getSelectionModel(){
        return selectionModel;
    }
}
