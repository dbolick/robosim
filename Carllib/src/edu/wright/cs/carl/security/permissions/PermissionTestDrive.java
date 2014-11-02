
package edu.wright.cs.carl.security.permissions;

/**
 * 
 *
 * @author Duane
 */
public class PermissionTestDrive
{
    public static void main(String[] args)
    {
        ServerPermission s1 = new ServerPermission("login");
        System.out.println(s1.toString());
        
        ServerPermission  s2 = new ServerPermission("sendMessage");
        System.out.println(s2.toString());
        
        System.out.println(s1.equals(s2));
    }
}
