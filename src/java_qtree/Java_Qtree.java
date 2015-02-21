/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package java_qtree;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/**
 *
 * @author cuong
 */
public class Java_Qtree {

    /**
     * @param args the command line arguments
     */
    
    public static void main(String[] args) throws FileNotFoundException {
        
        String cifsconfig_file = "/home/cuong/cifsconfig_share.cfg";
        Qtree stuff = new Qtree();
        String test = stuff.readFile("192.168.37.128", "cuong", "voong", cifsconfig_file);
        
        //System.out.println(test);

        //String cifsconfig_file = "C:\\Users\\cuong\\Documents\\NetBeansProjects\\Java_Qtree\\f6_cifsconfig_share.cfg.txt";
        String qtrees_file = "C:\\Users\\cuong\\Documents\\NetBeansProjects\\Java_Qtree\\f6_volume_qtrees.txt";
        
        List<String> list = new ArrayList<>();
        List<cifsShareDefinition> volumes = new ArrayList<>();
        List<cifsSharePermissions> permissions = new ArrayList<>();
        List<String> qtreeList;
        HashSet<String> volumeNames = new HashSet<>();
        HashMap<String, HashSet<String>> volumeQtrees = new HashMap<>();

        volumes = stuff.readCifsShares(test);
        permissions = stuff.readCifsPermissions(test);

    }
}
