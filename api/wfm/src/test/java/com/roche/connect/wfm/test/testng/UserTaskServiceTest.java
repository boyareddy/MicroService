package com.roche.connect.wfm.test.testng;

import java.util.List;

import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.IObjectFactory;
import org.testng.annotations.ObjectFactory;
import org.testng.annotations.Test;

import com.hcl.hmtp.common.server.ThreadSessionManager;
import com.hcl.hmtp.common.server.logger.HMTPLoggerImpl;
import com.hcl.hmtp.common.server.rest.RestClientUtil;
import com.roche.connect.wfm.service.UserTaskService;

@PrepareForTest({ RestClientUtil.class, HMTPLoggerImpl.class, ThreadSessionManager.class }) @PowerMockIgnore({
"sun.misc.Launcher.*", "com.sun.*", "javax.*", "javax.ws.*", "org.mockito.*", "javax.management.*",
"javax.inject.Inject", "org.w3c.dom.*", "org.apache.logging.*", "org.slf4j.*" }) public class UserTaskServiceTest
    extends AbstractTestNGSpringContextTests {
    @InjectMocks private UserTaskService userTaskService;
    @Mock HMTPLoggerImpl hmtpLoggerImpl;
    @Mock TaskService taskService;
    @Mock List<Task> ll;
    @Mock Task task;
    @Mock TaskQuery taskQuery;
    
    @ObjectFactory public IObjectFactory getObjectFactory() {
        return new org.powermock.modules.testng.PowerMockObjectFactory();
    }
    
    public void config() {
        MockitoAnnotations.initMocks(this);
        PowerMockito.mockStatic(RestClientUtil.class);
        Mockito.doNothing().when(hmtpLoggerImpl).info(Mockito.anyString(), Mockito.any(Object.class));
    }
    
    @Test
    public void userTaskCheck(){
        Mockito.when(taskService.createTaskQuery()).thenReturn(taskQuery);
        Mockito.when(taskService.createTaskQuery().taskAssignee("123456")).thenReturn(taskQuery);
        Mockito.when(taskService.createTaskQuery().taskAssignee("123456").orderByTaskId()).thenReturn(taskQuery);
        Mockito.when(taskService.createTaskQuery().taskAssignee("123456").orderByTaskId().desc()).thenReturn(taskQuery);
        Mockito.when(taskService.createTaskQuery().taskAssignee("123456").orderByTaskId().desc().list()).thenReturn(ll);
        Mockito.when(ll.get(0)).thenReturn(task);
        Mockito.when(ll.get(0).getId()).thenReturn("1");
        userTaskService.userTaskCheck("123456");
    }
}
