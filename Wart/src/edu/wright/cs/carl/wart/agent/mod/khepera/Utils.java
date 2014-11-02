
package edu.wright.cs.carl.wart.agent.mod.khepera;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;


/**
 * This class defines static utility methods for working with the Khepera.
 *
 * @author  Duane Bolick
 */
public class Utils
{
    
    public static List<Integer> ParseResultList(String result, String resultChar, int expectedNumElements)
    {
        result = result.trim();      
        
        List<String> resultStringList = new ArrayList<String>(Arrays.asList(result.split(",")));
        List<Integer> resultValues = new ArrayList<Integer>();
        
        if(resultStringList.remove(resultChar) == false) {
            return null;
        }
        
        Iterator<String> it = resultStringList.iterator();
        String current = null;
        while(it.hasNext()) {
            current = it.next();           
           resultValues.add(Integer.valueOf(current));
        }
                
        if(resultValues.size() != expectedNumElements) {
            return null;
        }
        
        return resultValues;
    }

}
