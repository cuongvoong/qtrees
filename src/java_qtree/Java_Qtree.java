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
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
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
public class Java_Qtree {

    /**
     * @param args the command line arguments
     */
    
    public static void main(String[] args) throws FileNotFoundException {
        
        String SFTPHOST = "192.168.37.128";
        int SFTPPORT = 22;
        String SFTPUSER = "cuong";
        String SFTPPASS = "";
        String SFTPWORKINGDIR = "/home/cuong/";

        Session session = null;
        Channel channel = null;
        ChannelSftp channelSftp = null;

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
            channelSftp.cd(SFTPWORKINGDIR);
            byte[] buffer = new byte[1024];
            BufferedInputStream bis = new BufferedInputStream(channelSftp.get("mysql"));
            
            Scanner s = new java.util.Scanner(bis).useDelimiter("\\A");
            //while (s.hasNext()) {
                System.out.println(s.next());
            //}
            //s.close();
            
            System.exit(0);
            File newFile = new File("C:\\Users\\cuong\\Documents\\NetBeansProjects\\Java_Qtree\\mysql");
            OutputStream os = new FileOutputStream(newFile);
            BufferedOutputStream bos = new BufferedOutputStream(os);
            int readCount;
//System.out.println("Getting: " + theLine);
            while ((readCount = bis.read(buffer)) > 0) {
                System.out.println("Writing: ");
                bos.write(buffer, 0, readCount);
            }
            bis.close();
            bos.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
        System.exit(0);
        
        String cifsconfig_file = "C:\\Users\\cuong\\Documents\\NetBeansProjects\\Java_Qtree\\f6_cifsconfig_share.cfg.txt";
        String qtrees_file = "C:\\Users\\cuong\\Documents\\NetBeansProjects\\Java_Qtree\\f6_volume_qtrees.txt";
        
        List<String> list = new ArrayList<>();
        List<cifsShareDefinition> volumes = new ArrayList<>();
        List<cifsSharePermissions> permissions = new ArrayList<>();
        List<String> qtreeList;
        HashSet<String> volumeNames = new HashSet<>();
        HashMap<String, HashSet<String>> volumeQtrees = new HashMap<>();
        
        Scanner s = new Scanner(new File(cifsconfig_file));
        s.useDelimiter("\n");
        while (s.hasNext()) {
            list.add(s.next());
        }
        s.close();

        int line_number = 0;
        
        for(String i : list)
        {
            line_number++;
            
            Pattern pattern = Pattern.compile(".*cifs shares -add\\s\"([^\"]*)\"\\s\"([^\"]*)\"");
            Matcher matcher = pattern.matcher(i);
            
            if(matcher.find())
            {
                //System.out.println(matcher.group(2));

                pattern = Pattern.compile("/vol/([^/]+)/([^/]+)(/*)([^\r\n]*)");
                matcher = pattern.matcher(matcher.group(2));
                
                if(matcher.find())
                {
                    //System.out.println(matcher.group(3));
                    cifsShareDefinition definition = new cifsShareDefinition();
                    definition.volume = matcher.group(1);
                    definition.qtree = matcher.group(2);
                    definition.subdirectories = matcher.group(4);
                    definition.line_number = line_number;
                    volumes.add(definition);
                }
            }
        }
        
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
        
        for(cifsSharePermissions i : permissions)
        {
            System.out.println(i.access);
        }
        
        System.exit(0);
        
        for(cifsShareDefinition i : volumes)
        {
            if(i.subdirectories != null && !i.subdirectories.isEmpty())
                System.out.print("/"+i.subdirectories+"\n");
        }
        
        list.clear();
        s = new Scanner(new File(qtrees_file));
        s.useDelimiter("\n");
        while (s.hasNext()) {
            list.add(s.next());
        }
        s.close();
        
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
        
        System.out.println(volumeQtrees.get("MA_DevIS"));
    }
}
