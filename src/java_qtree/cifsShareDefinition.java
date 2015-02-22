/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package java_qtree;

/**
 *
 * @author cuong
 */
public class cifsShareDefinition {
    private String volume;
    private String qtree;
    private String subdirectories;
    private int line_number;

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public String getQtree() {
        return qtree;
    }

    public void setQtree(String qtree) {
        this.qtree = qtree;
    }

    public String getSubdirectories() {
        return subdirectories;
    }

    public void setSubdirectories(String subdirectories) {
        this.subdirectories = subdirectories;
    }

    public int getLine_number() {
        return line_number;
    }

    public void setLine_number(int line_number) {
        this.line_number = line_number;
    }
}
