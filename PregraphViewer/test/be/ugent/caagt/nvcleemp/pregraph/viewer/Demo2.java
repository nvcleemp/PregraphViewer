/* Demo2.java
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

import be.ugent.caagt.nvcleemp.pregraph.viewer.embedder.RandomEmbedder;
import be.ugent.caagt.nvcleemp.graphio.pregraph.PregraphReader;
import be.ugent.caagt.nvcleemp.pregraph.viewer.embedder.EmbedderControl;
import be.ugent.caagt.nvcleemp.pregraph.viewer.embedder.EmbedderRunner;
import be.ugent.caagt.nvcleemp.pregraph.viewer.embedder.ForceEmbedder;
import be.ugent.caagt.nvcleemp.pregraph.viewer.list.DefaultEmbeddedPregraphListModel;
import be.ugent.caagt.nvcleemp.pregraph.viewer.list.EmbeddedPregraphListModel;
import be.ugent.caagt.nvcleemp.pregraph.viewer.list.ListViewer;
import java.awt.BorderLayout;
import java.io.IOException;
import javax.swing.JFrame;

/**
 *
 * @author nvcleemp
 */
public class Demo2 {

    public static void main(String[] args) throws IOException {
        //PregraphReader reader = new PregraphReader(Demo.class.getResourceAsStream("test_without_loops.code"));
        PregraphReader reader = new PregraphReader(Demo2.class.getResourceAsStream("pregraph.code"));
        JFrame frame = new JFrame();
        EmbeddedPregraphListModel listModel = new DefaultEmbeddedPregraphListModel(reader);
        EmbedderRunner.singleRunEmbedder(new RandomEmbedder(), listModel);
        EmbedderRunner.repeatedRunEmbedder(50, new ForceEmbedder(), listModel);
        frame.setLayout(new BorderLayout());
        frame.add(new ListViewer(listModel), BorderLayout.CENTER);
        frame.add(new EmbedderControl(listModel), BorderLayout.SOUTH);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
