package com.kc.fmj;

import javax.swing.JFrame;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Hashtable;

import javax.media.Manager;
import javax.media.MediaLocator;
import javax.media.Player;
import javax.swing.JPanel;
//import java.io.InputStream;
//import javazoom.jl.player.Player;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;

import net.sf.fmj.ui.application.CaptureDeviceBrowser;
import net.sf.fmj.ui.application.ContainerPlayer;
import net.sf.fmj.ui.application.PlayerPanelPrefs;

/**
 * 描述：
 * 
 * Copyright (c) 2012 k.c.Studio All right reserved.
 *
 * @author k.c.
 * @version v1.0
 * 
 * 2012-9-27
 */
public class CameraFrame extends JFrame{

	/**
	 * @param args
	 * @throws  
	 */
	public static void main(String[] args)throws Exception {
		//InputStream is = new FileInputStream("E:\\Epay_wordspace\\qrcode\\beep.mp3");
		//System.out.println("InputStream:"+is);
		CameraFrame camera = new CameraFrame();
		camera.setVisible(true);
	}
	
    private static int num = 0;
    
    public CameraFrame() throws Exception{
        this.setTitle("摄像头读取二维码应用");
        this.setSize(480, 500);
        //this.setSize(250, 280);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        final JPanel cameraPanel = new JPanel();
        this.getContentPane().setLayout(new BorderLayout());
        this.getContentPane().add(cameraPanel, BorderLayout.CENTER);
        ContainerPlayer containerPlayer = new ContainerPlayer(cameraPanel);
        MediaLocator locator = CaptureDeviceBrowser.run(null);   //弹出摄像头设备选择

        PlayerPanelPrefs prefs = new PlayerPanelPrefs();
        containerPlayer.setMediaLocation(locator.toExternalForm(), prefs.autoPlay);
        
        new Thread() {
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(1000);
                        Dimension imageSize = cameraPanel.getSize();
                        BufferedImage image = new BufferedImage(
                                imageSize.width, imageSize.height,
                                BufferedImage.TYPE_INT_ARGB);
                        Graphics2D g = image.createGraphics();
                        cameraPanel.paint(g);
                        g.dispose();
                        LuminanceSource source = new BufferedImageLuminanceSource(image);
                        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
                        
            			Hashtable hints = new Hashtable();
            			hints.put(DecodeHintType.CHARACTER_SET, "GBK"); //指定编码，否则中文会乱码
                        
                        Result result = new MultiFormatReader().decode(bitmap, hints);
                        System.out.println("二维码解析结果:" + result.getText());
                        
                    } catch (Exception re) {
                    	System.out.println("扫描不到...");
                        re.printStackTrace();
                    }
                }
            }
        }.start();
    }
    
}
