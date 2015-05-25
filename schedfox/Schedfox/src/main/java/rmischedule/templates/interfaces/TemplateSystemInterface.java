//  package declaration
package rmischedule.templates.interfaces;

//  import declarations
import rmischedule.templates.controllers.TemplateController;
import rmischedule.templates.models.InitializeTemplateSystem;


public interface TemplateSystemInterface
{

    public final TemplateController templateController = TemplateController.getInstance();

    
    public void viewController();

    
    public InitializeTemplateSystem initTemplateSystem();


    public int getTemplateType();

    public void loadTemplates();

    /**
     *  Implementations of this method will kick off <i>independent template system</i>,
     *      which is used to <b>save, delete, update</b> templates for general use.
     *      This method should be called by whatever graphical component the
     *      implementing developer has chosen to provide access to the the <i>independent template system</i>.
     *      All future implementations should be virtually identical:
     *  <p>{@code TemplateDiagForm templateForm = new TemplateDiagForm()}
     *  <p>{@code templateForm.init( this.initTemplateSystem() );   //  initTemplateSystem is defined in this interface}
     *  <p>{@code templateForm.pack();}
     *  <p>{@code templateForm.setVisible( true );}
     *  <p>{@code this.viewController;    //  viewController is defined in this interface}
     */
    public void editTemplateAction();

    /**
     *  Implementations of this method will be the most independent in this
     *      interface.  This method allows the implementing class to handle
     *      displaying the data contained in {@code ParentTemplateController}
     *      when a GUI action occurs.
     *  <p>Typical display components involve drop down menus, as is Jim's preference.
     */
    public void templateSelectionAction();
};
