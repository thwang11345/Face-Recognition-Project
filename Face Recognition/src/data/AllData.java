package data;
import java.io.File;

public class AllData {
    
    Dataset Train = null;
    Dataset Test = null;
    
    public AllData(File folder, boolean faceOnly) {

        Init(folder, faceOnly);
        
    }
    
    public Dataset getTrain()
    {
    	return Train;
    }
    
    public Dataset getTest()
    {
    	return Test;
    }
    
    private void Init(File f, boolean faceOnly) {
        
        for (final File fileEntry : f.listFiles()) {
            if (fileEntry.isDirectory()) {
                String path = fileEntry.getAbsolutePath();
                int faceIdx = path.toLowerCase().indexOf("face");
                
                if ( faceOnly && faceIdx >= 0 ) 
                {
	                if ( path.toLowerCase().indexOf("train") >= 0 )
	                {                	
	                    Train = new Dataset(fileEntry, faceOnly);
	                }
	                else if (path.toLowerCase().indexOf("test") >= 0 )
	                {
	                    Test = new Dataset(fileEntry, faceOnly);
	                }
                } else if (!faceOnly && faceIdx <0)
                {
                	if ( path.toLowerCase().indexOf("train") >= 0 )
	                {                	
	                    Train = new Dataset(fileEntry, faceOnly);
	                }
	                else if (path.toLowerCase().indexOf("test") >= 0 )
	                {
	                    Test = new Dataset(fileEntry, faceOnly);
	                } 	
                }
            }    
        }
    }
}
