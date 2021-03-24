// 
// Decompiled by Procyon v0.5.36
// 

package me.travis.wurstplus.util;

import java.util.Arrays;
import java.util.UUID;
import java.io.IOException;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

public class CapeManager
{
    private static final List<String> staticUsers;
    private static HashMap<String, Boolean> capeUsers;
    
    public CapeManager() {
        CapeManager.capeUsers = new HashMap<String, Boolean>();
    }
    
    public void initializeCapes() {
        final Boolean b;
        CapeManager.staticUsers.forEach(uuid -> b = CapeManager.capeUsers.put(uuid, true));
    }
    
    private List<String> getFromPastebin(final String urlString) {
        URL url;
        try {
            url = new URL(urlString);
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
            return new ArrayList<String>();
        }
        BufferedReader bufferedReader;
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(url.openStream()));
        }
        catch (IOException e2) {
            e2.printStackTrace();
            return new ArrayList<String>();
        }
        final ArrayList<String> uuidList = new ArrayList<String>();
        while (true) {
            String line;
            try {
                line = bufferedReader.readLine();
                if (line == null) {
                    break;
                }
            }
            catch (IOException e3) {
                e3.printStackTrace();
                return new ArrayList<String>();
            }
            uuidList.add(line);
        }
        try {
            bufferedReader.close();
        }
        catch (IOException e4) {
            e4.printStackTrace();
            return new ArrayList<String>();
        }
        return uuidList;
    }
    
    public static boolean hasCape(final UUID uuid) {
        return CapeManager.capeUsers.containsKey(uuid.toString());
    }
    
    public static boolean isOg(final UUID uuid) {
        return CapeManager.capeUsers.containsKey(uuid.toString()) && CapeManager.capeUsers.get(uuid.toString());
    }
    
    static {
        staticUsers = Arrays.asList("");
    }
}
