/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rmischeduleserver.emp_training;

/**
 *
 * @author user
 */
public class Catalog {
    private int catalog_id;
    private String catalog_name;
    private String catalog_test_url;
    private String catalog_manage_url;

    /**
     * @return the catalog_name
     */
    public String getCatalog_name() {
        return catalog_name;
    }

    /**
     * @param catalog_name the catalog_name to set
     */
    public void setCatalog_name(String catalog_name) {
        this.catalog_name = catalog_name;
    }

    /**
     * @return the catalog_test_url
     */
    public String getCatalog_test_url() {
        return catalog_test_url;
    }

    /**
     * @param catalog_test_url the catalog_test_url to set
     */
    public void setCatalog_test_url(String catalog_test_url) {
        this.catalog_test_url = catalog_test_url;
    }

    /**
     * @return the catalog_manage_url
     */
    public String getCatalog_manage_url() {
        return catalog_manage_url;
    }

    /**
     * @param catalog_manage_url the catalog_manage_url to set
     */
    public void setCatalog_manage_url(String catalog_manage_url) {
        this.catalog_manage_url = catalog_manage_url;
    }

    /**
     * @return the catalog_id
     */
    public int getCatalog_id() {
        return catalog_id;
    }

    /**
     * @param catalog_id the catalog_id to set
     */
    public void setCatalog_id(int catalog_id) {
        this.catalog_id = catalog_id;
    }
}
