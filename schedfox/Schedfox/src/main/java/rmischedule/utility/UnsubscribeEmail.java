/*  package declaration */
package rmischedule.utility;

/*  import declarations */

/**
 *  This utility class contains static methods for appending the appropriate
 *      unsubscribe email links to various emails.
 *  @author Jeffrey N. Davis
 *  @since 05/17/2011
 */
public class UnsubscribeEmail 
{
    private static final String GLASSFISH_LOCATION = "http://192.168.1.80:8080/";
    private static final String CONTEXT_PATH = "EmailSubcriberServices/unsubscribe";
    private static final String CHAMPION_NATIONAL_SECURITY = "schedfox";
    
    public static String getClientContactUnsubscribeLink ( String id )
    {
        StringBuilder link = new StringBuilder();
        
        link.append("<a href=\"");
        link.append(GLASSFISH_LOCATION );
        link.append( CONTEXT_PATH );
        link.append( "?company=");
        link.append( CHAMPION_NATIONAL_SECURITY );
        link.append( "&id=");
        link.append( id );
        link.append("\" > ");
        link.append("Click this link to remove your email address from the send link.  ");
        link.append("</a>");
        
        return link.toString();
    }
    
};
