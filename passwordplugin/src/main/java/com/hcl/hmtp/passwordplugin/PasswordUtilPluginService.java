package com.hcl.hmtp.passwordplugin;

import java.util.List;

public interface PasswordUtilPluginService {
    public List<Integer> validate(String userName, String password) throws Exception;    
}
