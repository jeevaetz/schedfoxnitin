/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rmischeduleserver.control.model;

/**
 *
 * @author user
 */
public class BoundMappings {

    private String unusedMapping;
    private String unrecognizedMapping;

    public BoundMappings(String unusedMapping, String unrecognizedMapping) {
        this.unusedMapping = unusedMapping;
        this.unrecognizedMapping = unrecognizedMapping;
    }

    public String getUnusedMapping() {
        return this.unusedMapping;
    }

    public String getUnrecognizedMapping() {
        return this.unrecognizedMapping;
    }

    @Override
    public String toString() {
        return this.unrecognizedMapping + " - " + this.unusedMapping;
    }
}
