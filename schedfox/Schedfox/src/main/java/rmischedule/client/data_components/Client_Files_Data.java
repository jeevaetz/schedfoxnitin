/**
 *  Filename:  Client_Files_Data.java
 *  Author:  Jeffrey N. Davis
 *  Date Created:  10/01/2010
 *  Revisions:
 *  Purpose of File:  contains a data object for Client_Files
 */

//  package declaration
package rmischedule.client.data_components;

//  import declarations
import java.io.File;
import java.net.HttpURLConnection;

/**
 *  Class Name:  Client_Files_Data
 *  Purpose of Class:  a data object for Client_Files
 */
public class Client_Files_Data
{
    //  private variable declarations
    private StringBuffer fileName;
    private StringBuffer localFilePath;
    private File file;
    private HttpURLConnection urlConn = null;

    //  public method implementations
    /**
     *  Method Name:  Client_Files_Data
     *  Purpose of Method:  creates a default instance of this object, calls
     *      secondary constructor to initialize
     *  Arguments:  none
     *  Returns:  none
     *  Preconditions:  object DNE
     *  Postconditions:  object exists, variables initialized
     */
    public Client_Files_Data()
    {
        //  constructor chaining
        this(null, null, null);
    }

    /**
     *  Method Name:  Client_Files_Data
     *  Purpose of Method:  creates an instance of this object, initializes
     *      and sets member variables
     *  Arguments:  two strings representing fileName, localFilePath
     *  Returns:  none
     *  Preconditions:  object DNE
     *  Postconditions:  object exists, variables intitialized and set
     */
    public Client_Files_Data(String argFileName, String argLocalFilePath,
            File argFile)
    {
        //  initialize class variables
        this.fileName = new StringBuffer(0);
        this.localFilePath = new StringBuffer(0);
        this.file = null;
        
        //  set fileName
        if(argFileName != null)
        {
            try
            {
                this.fileName.append(argFileName);
            }
            catch(IllegalArgumentException ex)
            {
                ex.printStackTrace();
            }
        }

        //  set localFilePath
        if(argLocalFilePath != null)
        {
            try
            {
                this.localFilePath.append(argLocalFilePath);
            }
            catch(IllegalArgumentException ex)
            {
                ex.printStackTrace();
            }
        }

        //  set file
        if(argFile != null)
        {
            try
            {
                this.file = argFile;
            }
            catch(IllegalArgumentException ex)
            {
                ex.printStackTrace();
            }   //  end try/catch
        }   //  end if
    }   //  end method

    //  getters/setters
    public String getFileName()
    {
        return this.fileName.toString();
    }

    public String getLocalFilePath()
    {
        return this.localFilePath.toString();
    }

    public File getFile()
    {
        return this.file;
    }

    public void setFileName(String argFileName)
    {
        //  ensure nothing in stringbuffer
        this.fileName.setLength(0);

        //  set
        try
        {
            this.fileName.append(argFileName);
        }
        catch(IllegalArgumentException ex)
        {
            ex.printStackTrace();
        }
    }

    public void setLocalFilePath(String argLocalFilePath)
    {
        //  ensure nothing in stringbuffer
        this.localFilePath.setLength(0);

        //  set
        try
        {
            this.localFilePath.append(argLocalFilePath);
        }
        catch(IllegalArgumentException ex)
        {
            ex.printStackTrace();
        }
    }

    public void setFile(File argFile)
    {
        //  ensure no duplicate file
        this.file = null;

        //  set
        if(argFile != null)
        {
            try
            {
                this.file = argFile;
            }
            catch(IllegalArgumentException ex)
            {
                ex.printStackTrace();
            }   //  end try/catch
        }   //  end if
    }   //  end method

    public String getShortName(){

    StringBuilder strBld=new StringBuilder(getFileName());

    if(strBld.length()==0)
        return("");

    int i=0;
    for(i=strBld.length()-1;i>-1;i--)
        if((strBld.charAt(i)=='/')||(strBld.charAt(i)=='\\'))
            break;
    String name=new String(strBld.substring(i+1, strBld.length()));

    return(name);
    
}

};  // end class