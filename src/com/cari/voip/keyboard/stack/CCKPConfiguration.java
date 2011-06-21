package com.cari.voip.keyboard.stack;

import java.util.ArrayList;
import java.util.List;

public final class CCKPConfiguration {

	private static final String CCKP_VERSION = "1.0.0";
	
	private static  int keepAliveInterval = 30000;
	private static  int packetReplyTimeout = 10000;
	private static int invalidPacketsNumber2shutdown = 1;
	private static long queryLoopInterval = 10000*100;//10 s
	static {
		try{
			//ClassLoader[] classLoaders = getClassLoaders();
			keepAliveInterval = 30000;
			packetReplyTimeout = 10000;
			invalidPacketsNumber2shutdown = 1;
			queryLoopInterval = 10000*100;
			
		} catch (Exception e) {
            e.printStackTrace();
        }
	}

	public static int getKeepAliveInterval(){
		return keepAliveInterval;
		
	}
	
	public static int getPacketReplyTimeout(){
		return packetReplyTimeout;
	}
	
	public static int getInvalidPacketsNumber2shutdown(){
		return invalidPacketsNumber2shutdown;
	}
	public static long getQueryLoopInterval(){
		return queryLoopInterval;
	}
	private static ClassLoader[] getClassLoaders() {

        ClassLoader[] classLoaders = new ClassLoader[2];
        classLoaders[0] = CCKPConfiguration.class.getClassLoader();
        classLoaders[1] = Thread.currentThread().getContextClassLoader();
        // Clean up possible null values. Note that #getClassLoader may return a null value.
        List<ClassLoader> loaders = new ArrayList<ClassLoader>();
        for (ClassLoader classLoader : classLoaders) {
            if (classLoader != null) {
                loaders.add(classLoader);
            }
        }
        return loaders.toArray(new ClassLoader[loaders.size()]);
	}
}
