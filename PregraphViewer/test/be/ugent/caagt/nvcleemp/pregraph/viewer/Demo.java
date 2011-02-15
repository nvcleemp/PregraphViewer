/* Demo.java
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

import be.ugent.caagt.nvcleemp.pregraph.viewer.embedder.EmbeddedPregraph;
import be.ugent.caagt.nvcleemp.pregraph.viewer.embedder.Embedding2D;
import be.ugent.caagt.nvcleemp.pregraph.viewer.embedder.RandomEmbedder;
import be.ugent.caagt.nvcleemp.pregraph.viewer.embedder.Embedder;
import be.ugent.caagt.nvcleemp.graphio.pregraph.Pregraph;
import be.ugent.caagt.nvcleemp.graphio.pregraph.PregraphReader;
import be.ugent.caagt.nvcleemp.graphio.pregraph.Vertex;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;

/**
 *
 * @author nvcleemp
 */
public class Demo {

    private static int position = 0;
    private static Embedder<Embedding2D> embedder = new RandomEmbedder();

    public static void main(String[] args) throws IOException {
        //PregraphReader reader = new PregraphReader(Demo.class.getResourceAsStream("test_without_loops.code"));
        PregraphReader reader = new PregraphReader(Demo.class.getResourceAsStream("pregraph.code"));
        final List<EmbeddedPregraph> embeddedGraphs = new ArrayList<EmbeddedPregraph>();
        for (Pregraph pregraph : reader.readAllGraphs()) {
            EmbeddedPregraph graph = new EmbeddedPregraph(pregraph);
            embeddedGraphs.add(graph);
            embedder.setGraph(graph);
            embedder.embed();
        }
        JFrame frame = new JFrame();
        final ViewerPanel viewer = new ViewerPanel();
        viewer.setGraph(embeddedGraphs.get(position));
        viewer.addMouseListener(new MouseAdapter() {

            private Vertex current;

            @Override
            public void mouseClicked(MouseEvent e) {
                if(e.isControlDown()){
                    if(e.isShiftDown()){
                        if(position>0)
                            viewer.setGraph(embeddedGraphs.get(--position));
                    } else {
                        if(position<embeddedGraphs.size()-1)
                            viewer.setGraph(embeddedGraphs.get(++position));
                    }
                    viewer.repaint();
                }
            }
        });
        frame.add(viewer);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
