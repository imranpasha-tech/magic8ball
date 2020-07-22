/*
 * (C) Copyright 1999-2019 iCIMS, Inc. Proprietary and Confidential. All rights reserved.
 * This software is the intellectual property of iCIMS. The program may be used only in
 * accordance with the terms of the license agreement you entered into with iCIMS.
 */

/*
 * (C) Copyright 1999-2019 iCIMS, Inc. Proprietary and Confidential. All rights reserved.
 * This software is the intellectual property of iCIMS. The program may be used only in
 * accordance with the terms of the license agreement you entered into with iCIMS.
 */

package com.icims.labs.services.eightball;

import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import org.hamcrest.Matchers;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Smoke Tests simple evaluates the actuator endpoint and validates the expected configuration.
 *
 * @author Jeffrey Mildenberg {@literal <jeffrey.mildenberg@icims.com>}
 */
public class SmokeTests {
  private static Integer servicePort;
  private static String serviceUrl;
  private static String serviceVersion;
  private static String gitHash;

  @BeforeClass
  public static void setUpClass() {
    servicePort = Integer.parseInt(System.getenv().getOrDefault("SERVICE_PORT", "8080"));
    serviceUrl = System.getenv().getOrDefault("SERVICE_URL", "http://localhost");
    serviceVersion = System.getenv().getOrDefault("SERVICE_VERSION", "");
    gitHash = System.getenv().getOrDefault("GIT_COMMIT_ID", "");
    if (gitHash.startsWith("@")) {
      gitHash = "";
    }
  }

  @Test
  public void infoEndpointMatchesExpectedVersionInformation() {
    getRequestSpecification().get("info")
        .then()
        .log().all(true)
        .assertThat()
        .statusCode(200)
        .body("application.version", serviceVersion.isEmpty()
            ? Matchers.not(Matchers.nullValue())
            : Matchers.equalTo(serviceVersion))
        .body("git.commit.id", gitHash.isEmpty()
            ? Matchers.nullValue()
            : Matchers.equalTo(gitHash)
        );
  }

  @Test
  public void healthEndpointIsUp() {
    getRequestSpecification().get("health")
        .then()
        .log().all(true)
        .assertThat()
        .statusCode(200)
        .body("status", Matchers.equalTo("UP"));
  }

  //
  // Helper Methods
  //
  private RequestSpecification getRequestSpecification() {
    return RestAssured.given().relaxedHTTPSValidation().baseUri(serviceUrl).port(servicePort).basePath("/actuator");
  }
}
