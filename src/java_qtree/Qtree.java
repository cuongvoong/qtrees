/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package java_qtree;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import java.io.BufferedInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author cuong
 */
public class Qtree {

    public String readFile(String host, String username, String password, String filename) {

        String SFTPHOST = host;
        int SFTPPORT = 22;
        String SFTPUSER = username;
        String SFTPPASS = password;

        Session session = null;
        Channel channel = null;
        ChannelSftp channelSftp = null;
        String file = null;

        try {
            JSch jsch = new JSch();
            session = jsch.getSession(SFTPUSER, SFTPHOST, SFTPPORT);
            session.setPassword(SFTPPASS);
            java.util.Properties config = new java.util.Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);
            session.connect();
            channel = session.openChannel("sftp");
            channel.connect();
            channelSftp = (ChannelSftp) channel;
            byte[] buffer = new byte[1024];
            BufferedInputStream bis = new BufferedInputStream(channelSftp.get(filename));

            Scanner s = new java.util.Scanner(bis).useDelimiter("\\A");
            while (s.hasNext()) {
                file += s.next();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return file;
    }

    public List readCifsShares(String cifsShareFile) {
        
        List<String> list = new ArrayList<>();
        List<cifsShareDefinition> volumes = new ArrayList<>();
        
        Scanner scanner = new Scanner(cifsShareFile);
        
        while (scanner.hasNextLine()) {
            list.add(scanner.nextLine());
        }
        scanner.close();

        int line_number = 0;

        for (String i : list) {
            line_number++;

            Pattern pattern = Pattern.compile(".*cifs shares -add\\s\"([^\"]*)\"\\s\"([^\"]*)\"");
            Matcher matcher = pattern.matcher(i);

            if (matcher.find()) {

                pattern = Pattern.compile("/vol/([^/]+)/([^/]+)(/*)([^\r\n]*)");
                matcher = pattern.matcher(matcher.group(2));

                if (matcher.find()) {
                    cifsShareDefinition definition = new cifsShareDefinition();
                    definition.volume = matcher.group(1);
                    definition.qtree = matcher.group(2);
                    definition.subdirectories = matcher.group(4);
                    definition.line_number = line_number;
                    volumes.add(definition);
                }
            }
        }
        
        return volumes;
    }
    
    public List readCifsPermissions(String cifsShareFile) {
        
        List<String> list = new ArrayList<>();
        List<cifsSharePermissions> permissions = new ArrayList<>();
        
        Scanner scanner = new Scanner(cifsShareFile);
        
        while (scanner.hasNextLine()) {
            list.add(scanner.nextLine());
        }
        scanner.close();
        
        for(String i : list)
        {
            Pattern pattern = Pattern.compile(".*cifs access\\s\"([^\"]*)\"\\s([^\\s]*|[eE]veryone)\\s([^\n]+)");
            Matcher matcher = pattern.matcher(i);
        
            if(matcher.find())
            {
                cifsSharePermissions permission = new cifsSharePermissions();
                permission.share = matcher.group(1);
                permission.sid = matcher.group(2);
                permission.access = matcher.group(3).replace("\"", "");
                permissions.add(permission);
            }
        }
        
        return permissions;
    }
    
    public HashMap getVolumesQtreesMapping(String volumeQtreeMappingFile)
    {
        List<String> list = new ArrayList<>();
        List<String> qtreeList;
        HashMap<String, HashSet<String>> volumeQtrees = new HashMap<>();
        
        Scanner scanner = new Scanner(volumeQtreeMappingFile);
        
        while (scanner.hasNextLine()) {
            list.add(scanner.nextLine());
        }
        scanner.close();
        
        for(String i : list)
        {
            qtreeList = Arrays.asList(i.split(","));

            if(volumeQtrees.get(qtreeList.get(0)) != null && !volumeQtrees.get(qtreeList.get(0)).isEmpty())
            {
                HashSet<String> tempQtree = new HashSet<>();
                tempQtree = volumeQtrees.get(qtreeList.get(0));
                tempQtree.add(qtreeList.get(1));
                volumeQtrees.put(qtreeList.get(0), tempQtree);
            }
            else
            {
                HashSet<String> qtrees = new HashSet<>();
                qtrees.add(qtreeList.get(1));
                volumeQtrees.put(qtreeList.get(0), qtrees);
            }

        }
        return volumeQtrees;
    }
}
