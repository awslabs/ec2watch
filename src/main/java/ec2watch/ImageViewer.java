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

import java.awt.Image;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

class ImageViewer extends JFrame {

  private final JLabel imageLabel;
  private final ImageIcon imageIcon;
  private byte[] imageData;

  public ImageViewer(String title, byte[] imageData) {
    

    imageIcon = new ImageIcon();
    imageLabel = new JLabel(imageIcon);

    this.setTitle(title);
    this.getContentPane().add(imageLabel);
    this.setSize(720, 400);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setVisible(true);

    setImageData(imageData);

    this.getRootPane().addComponentListener(new ComponentAdapter() {
      @Override
      public void componentResized(ComponentEvent e) {
        updateImage();
      }
    });

  }
  
  public final void setImageData(byte[] imageData) {
    this.imageData = imageData;
    updateImage();
  }

  public void updateImage() {
    try {
      ByteArrayInputStream bais = new ByteArrayInputStream(imageData);
      Image image = ImageIO.read(bais);
      Image scaledImage = image.getScaledInstance(this.getWidth(), this.getHeight(), Image.SCALE_SMOOTH);
      imageIcon.setImage(scaledImage);
      imageIcon.getImage().flush();
      imageLabel.repaint();

    } catch (IOException ioe) {
      System.err.println("Caught exception: " + ioe.getMessage());
    }
  }

}
