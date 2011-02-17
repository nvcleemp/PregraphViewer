/* PregraphViewerPreferences.java
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

package be.ugent.caagt.nvcleemp.pregraph.viewer.preferences;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

/**
 * Class to handle PregraphViewer preferences management. When the preferences
 * are not set by the user, a default value is taken.
 */
public class PregraphViewerPreferences {
    
    //This class is based upon GrinvinPreferences from the GrInvIn-project
    
    //
    public enum Preference {
        CURRENT_DIRECTORY("gui.currentdir");
        
        private String id;
        
        Preference(String id) {
            this.id = id;
        }
        
        protected String getId() {
            return id;
        }
    }
    
    //
    private static final String OUR_NODE_NAME = "/be/ugent/caagt/nvcleemp/pregraph/viewer/preferences";
    
    //
    private final Preferences userPreferences = Preferences.userRoot().node(OUR_NODE_NAME);
            
    //
    private List<PregraphViewerPreferencesListener> listeners;
        
    //
    private final static PregraphViewerPreferences INSTANCE = new PregraphViewerPreferences();
    
    //
    private PregraphViewerPreferences() {
        listeners = new ArrayList<PregraphViewerPreferencesListener>();
    }
    
    public static PregraphViewerPreferences getInstance() {
        return INSTANCE;
    }
    
    //
    public int getIntPreference(Preference key) {
        throw new RuntimeException("No default value available for: " + key);
    }
    
    //
    public void setIntPreference(Preference key, int value) {
        userPreferences.putInt(key.getId(), value);
        firePreferenceChanged(key);
    }
    
    private String getDefaultDir() {
        return null;
    }
    
    //
    public String getStringPreference(Preference key) {
        if (key == Preference.CURRENT_DIRECTORY) {
            String defaultValue = getDefaultDir();
            return userPreferences.get(key.getId(), defaultValue);
        } else {
            throw new RuntimeException("No default value available for: " + key);
        }
    }
    
    //
    public void setStringPreference(Preference key, String value) {
        userPreferences.put(key.getId(), value);
        firePreferenceChanged(key);
    }
    
    //
    public void addListener(PregraphViewerPreferencesListener listener) {
        listeners.add(listener);
    }
    
    //
    private void firePreferenceChanged(Preference preference) {
        for(PregraphViewerPreferencesListener listener : listeners)
            listener.preferenceChanged(preference);
    }

    public void reset() throws BackingStoreException{
        userPreferences.clear();
    }

    public void delete() throws BackingStoreException{
        userPreferences.removeNode();
        userPreferences.flush();
    }

    public static void main(String[] args) {
        //set default values for the preferences
        File graphfiles = new File(".", "graphfiles");
        
        try {
            INSTANCE.setStringPreference(Preference.CURRENT_DIRECTORY, graphfiles.getCanonicalPath());
        } catch (IOException ex) {
            Logger.getLogger(PregraphViewerPreferences.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(1);
        }

        System.exit(0);

    }
    
}
