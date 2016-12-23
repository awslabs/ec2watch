/*
 * Copyright 2016 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * A copy of the License is located at
 *
 *  http://aws.amazon.com/apache2.0
 *
 * or in the "license" file accompanying this file. This file is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */

package ec2watch;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.ec2.AmazonEC2Client;
import com.amazonaws.services.ec2.model.GetConsoleScreenshotRequest;
import com.amazonaws.services.ec2.model.GetConsoleScreenshotResult;
import java.util.Base64;

class EC2Watch {

  private final ClientConfiguration clientConfiguration;
  private final String instanceId;
  private final Integer interval;
  private final String region;

  public EC2Watch(String instanceId, String interval, String region) {
    clientConfiguration = getClientConfig();

    this.instanceId = instanceId;
    this.region = region;

    if (interval != null) {
      this.interval = Integer.parseInt(interval);
    } else {
      this.interval = 1;
    }
  }

  public void run() {

    ImageViewer viewer = null;
    AmazonEC2Client ec2Client = new AmazonEC2Client(clientConfiguration);

    if (region != null) {
      ec2Client.setRegion(Region.getRegion(Regions.fromName(region)));
    }

    while (true) {
      try {
        GetConsoleScreenshotResult result = ec2Client.getConsoleScreenshot(
          new GetConsoleScreenshotRequest()
            .withInstanceId(instanceId)
            .withWakeUp(Boolean.TRUE)
        );
        byte[] imageData = Base64.getDecoder().decode(result.getImageData());

        if (viewer == null) {
          viewer = new ImageViewer(instanceId, imageData);
        } else {
          viewer.setImageData(imageData);
          System.out.print(".");
        }

        Thread.sleep(interval * 1000);
      } catch (Exception e) {
        // Ignore it
        System.out.print("x");
      }

    }
  }

  private ClientConfiguration getClientConfig() {
    ClientConfiguration config = new ClientConfiguration();
    String proxyHost = System.getenv("PROXY_HOST");
    String proxyPort = System.getenv("PROXY_PORT");
    String proxyUser = System.getenv("PROXY_USER");
    String proxyPass = System.getenv("PROXY_PASSWORD");
    if (proxyHost != null && proxyPort != null) {
      config.setProxyHost(proxyHost);
      config.setProxyPort(Integer.valueOf(proxyPort));
      if (proxyUser != null) {
        config.setProxyUsername(proxyUser);
      }
      if (proxyPass != null) {
        config.setProxyPassword(proxyPass);
      }
    }
    return config;
  }
}
