/*
 * (C) Copyright 1999-2019 iCIMS, Inc. Proprietary and Confidential. All rights reserved.
 * This software is the intellectual property of iCIMS. The program may be used only in
 * accordance with the terms of the license agreement you entered into with iCIMS.
 */

package com.icims.labs.services.eightball;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Entry point class for Service.
 *
 * <p>This class is ignored by code-coverage by default, as it is a simple application context loading class, and
 * cannot easily be covered by unit tests.
 *
 * <p><b>DO NOT</b> add any other methods into this class, otherwise you will have to remove the code coverage
 * exclusion, and take the hit for the main method.
 *
 * @author iCIMS Labs {@literal <labs@icims.com>}
 */
@SpringBootApplication
public class Application {
  /**
   * Entry point method for Service.
   *
   * @param args the command line arguments
   */
  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }
}
