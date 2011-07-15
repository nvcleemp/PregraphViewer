/* PregraphColourProvider.java
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

package be.ugent.caagt.nvcleemp.pregraph.viewer.rendering;

import be.ugent.caagt.nvcleemp.graphio.pregraph.Edge;
import java.awt.Color;

/**
 *
 * @author nvcleemp
 */
public class PregraphColourProvider {

    private static final Color[] COLOURS_1 = {Color.BLACK};
    private static final Color[] COLOURS_2 = {Color.BLACK, Color.RED};
    private static final Color[] COLOURS_3 = {Color.BLACK, Color.RED, Color.GREEN};
    private static final Color[] COLOURS_4 = {Color.BLACK, Color.RED, Color.GREEN, Color.PINK};

    private Color defaultColour = Color.BLACK;
    private Color[] colours = null;

    public PregraphColourProvider(int count) {
        if(count==1){
            colours = COLOURS_1;
        } else if(count==2){
            colours = COLOURS_2;
        } else if(count==3){
            colours = COLOURS_3;
        } else if(count==4){
            colours = COLOURS_4;
        } else if(count!=0){
            colours = getGradient(count);
        }
    }

    public int getColourCount(){
        return colours==null ? 1 : colours.length;
    }

    public Color getColour(int colourNumber){
        if(colours == null){
            return defaultColour;
        } else {
            return colours[colourNumber];
        }
    }

    public Color getColour(Edge edge, int colourIndex){
        if(colours == null || edge.getColours().isEmpty() || colourIndex >= edge.getColours().size()){
            return defaultColour;
        } else {
            return colours[edge.getColours().get(colourIndex)-1];
        }
    }

    private static final int WHITE_RGB = 255 + (255*256) + (255*256)*256;

    private static Color[] getGradient(int colours){
        Color[] gradient = new Color[colours];
        for (int i = 0; i < gradient.length; i++) {
            gradient[i] = new Color(i*(WHITE_RGB/(colours+1)));
        }
        return gradient;
    }

    public static void main(String[] args) {
        System.out.println(Color.WHITE.getRGB());
        System.out.println(255);
        System.out.println(255 << 8);
        System.out.println((255 << 8)<<8);
        System.out.println(255 + (255 << 8) + (255 << 8)<<8);
        System.out.println(255 + (255*256) + (255*256)*256);
    }
}
