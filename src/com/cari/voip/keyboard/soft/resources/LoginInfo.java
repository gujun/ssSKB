package com.cari.voip.keyboard.soft.resources;

import com.ibm.icu.text.MessageFormat;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Utility class which helps managing messages
 */
public  class LoginInfo {

    private static final String RESOURCE_BUNDLE = "com.cari.voip.keyboard.soft.resources.loginfo"; //$NON-NLS-1$
    private static final String RESOURCE_FILE = "configuration/loginfo.ini";
    private static ResourceBundle bundle = ResourceBundle
            .getBundle(RESOURCE_BUNDLE);
    
    private static LoginInfo singleton;
    private String filename = null;
    private File file = null;
    
   // private URL url = null;
    private final Map<String,String> properties = new HashMap<String, String>();
	
    public LoginInfo(String filename){
    	this.filename = filename;
    	if(this.filename != null){
    		//this.url = this.getClass().getClassLoader().getResource(this.file);
    		this.file = new File(this.filename);
    		this.loadProperty();
    	}
    	
    }
    public boolean exists(){
    	if(this.file == null || !this.file.exists() ||
    			!this.file.isFile()){
    		return false;
    	}
    	return true;
    }
    public synchronized String getProperty(String name) {
        if (properties == null) {
            return null;
        }
        return properties.get(name);
 }
 public synchronized void setProperty(String name, String value) {
        if (name == null) {
            return;
        }
        properties.put(name, value);
 }
 
    public synchronized boolean loadProperty(){
		boolean ok = true;
		if(this.file == null || 
				!this.file.exists() || 
				!this.file.isFile()){
			return false;
		}
		try {
			int k = -1;
			InputStream in = new FileInputStream(this.file);
			
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(in,"UTF-8")
					);
			
			
			String line = reader.readLine();
			while(line != null){
				if(line.length() == 0 || line.startsWith("#")){
					line = reader.readLine();
					continue;
				}
				k = -1;
				k = line.indexOf('=');
				if(k > 0){
					String name = line.substring(0, k).trim().toLowerCase();
					String value = line.substring(k+1).trim();
					this.setProperty(name, value);
				}
				line = reader.readLine();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			ok = false;
		}
		return ok;
	}
    
    public synchronized boolean storeProperty(){
		boolean ok = true;
		if(this.filename == null){
			return false;
		}
		if(this.file == null){
			this.file = new File(this.filename);
			if(this.file == null){
				return false;
			}
		}
		if(this.file.exists()){
			
			this.file.delete();
			if(this.file.exists()){
				return false;
			}
			
		}
		
		try {
			if(!this.file.createNewFile()){
				return false;
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			return false;
		}
		
		StringBuilder sb = new StringBuilder(128);
		int props = properties.size();
		if(props > 0){
			for(String key:properties.keySet()){
				if(key != null){
					String val = (String)properties.get(key);
					if(val != null){
						sb.append(key+"="+val+"\n");
					}
				}
			}
		}
		
		try {
			file.setWritable(true);
			OutputStream out = new FileOutputStream(this.file,false);
			
			BufferedWriter writer = new BufferedWriter(
					new OutputStreamWriter(out,"UTF-8")
					);
			writer.write(sb.toString());
			writer.flush();
			writer.close();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			ok = false;
		}
		return ok;
	}
    public static LoginInfo getSingleton(){
    	if(singleton == null){
    		singleton = new LoginInfo(RESOURCE_FILE);
    	}
    	return singleton;
    }
    /**
     * Returns the formatted message for the given key in the resource bundle.
     * 
     * @param key
     *            the resource name
     * @param args
     *            the message arguments
     * @return the string
     */
    public static String format(String key, Object[] args) {
        return MessageFormat.format(getString(key), args);
    }

    /**
     * Returns the resource object with the given key in the resource bundle. If
     * there isn't any value under the given key, the key is returned.
     * 
     * @param key
     *            the resource name
     * @return the string
     */
    public static String getString(String key) {
        try {
            return bundle.getString(key);
        } catch (MissingResourceException e) {
            return key;
        }
    }

    /**
     * Returns the resource object with the given key in the resource bundle. If
     * there isn't any value under the given key, the default value is returned.
     * 
     * @param key
     *            the resource name
     * @param def
     *            the default value
     * @return the string
     */
    public static String getString(String key, String def) {
        try {
        	
            return bundle.getString(key);
        } catch (MissingResourceException e) {
            return def;
        }
    }
}
