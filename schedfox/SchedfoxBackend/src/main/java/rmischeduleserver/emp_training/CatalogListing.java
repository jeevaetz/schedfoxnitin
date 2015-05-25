/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rmischeduleserver.emp_training;

import java.util.ArrayList;

/**
 *
 * @author user
 */
public class CatalogListing {
    private ArrayList<Catalog> catalogs;

    public CatalogListing() {
        catalogs = new ArrayList<Catalog>();
    }

    public void addCatalog(Catalog catalog) {
        this.catalogs.add(catalog);
    }

    public ArrayList<Catalog> getCatalogs() {
        return this.catalogs;
    }
}
