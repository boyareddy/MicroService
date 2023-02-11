package com.roche.nipt.dpcr;

import org.testng.annotations.Test;
import org.testng.annotations.BeforeTest;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

import javax.sql.DataSource;

import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.testng.annotations.AfterTest;

public class ApplicationBootTest {
	
	ApplicationBoot app = new ApplicationBoot();
	
  @BeforeTest
  public void beforeTest() {
  }

  @AfterTest
  public void afterTest() {
  }


  @Test
  public void context() {
app.context();
  }

  @Test
  public void entityManagerFactory() {
	  
	  }

  @Test
  public void getRootConfigClasses() {
  }

  @Test
  public void getServletConfigClasses() {
  }

  @Test
  public void getServletMappings() {
  }

  @Test
  public void main() {
	  
  }
}
