package com.hcl.hmtp.passwordplugin;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

@Service("pwdPluginManager")
public class PasswordUtilPluginServiceImpl implements PasswordUtilPluginService {

    public List<Integer> validate(String userName, String password) throws Exception {
        List<Integer> errorList = new ArrayList<Integer>();
        int uppercase = 0;
        int lowercase = 0;
        int count =0;
        for (int k = 0; k < password.length(); k++) {
            if (Character.isUpperCase(password.charAt(k)))
                uppercase++;
            else if (Character.isLowerCase(password.charAt(k)))
                lowercase++;
        }
        for (int i = 1; i < password.length(); i++){
            if(password.charAt(i)==password.charAt(i-1)){
                count++;
                if(count>=4)
                    errorList.add (13);
            }
            
            if(password.charAt(i)!=password.charAt(i-1))
                count=0;
        }
        
        //Validate if password not having At least 1 uppercase letter (error-code -11) ,At least 1 lower case letter(12), At least 1 numeric digit(13) and At least 1 special character (14) 
        if (uppercase <= 0)
            errorList.add (11);
        if (lowercase <= 0)
            errorList.add (12);
        
        return errorList;
    }


}
