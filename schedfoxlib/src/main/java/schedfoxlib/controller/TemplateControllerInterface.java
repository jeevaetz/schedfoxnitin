/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package schedfoxlib.controller;

import java.util.ArrayList;
import schedfoxlib.controller.exceptions.RetrieveDataException;
import schedfoxlib.model.Template;

/**
 *
 * @author ira
 */
public interface TemplateControllerInterface {
    public ArrayList<Template> getTemplatesByName(String name) throws RetrieveDataException;
}
