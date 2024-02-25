package com.example.Config;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.apache.catalina.connector.Connector;
import org.apache.coyote.ajp.AbstractAjpProtocol;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


// 設定クラス
@Configuration
public class AJPConfig {
  // application.propertiesから設定値を受け取る。
  @Value("${tomcat.ajp.port}")
  private int port;
  // bean設定
  @Bean
  public ServletWebServerFactory servletContainer() throws UnknownHostException {
	
	System.out.println("■■servletContainer,02 処理開始!!!!!!");
    
	// AJP/1.3プロトコールのConnectorを生成
    var ajpConnector = new Connector("AJP/1.3");
    // ポート設定をapplication.propertiesで設定
    ajpConnector.setPort(port);
    // ajpログ、以前のserver.xmlのallowTraceアトリビュート設定
    ajpConnector.setAllowTrace(false);
    
    //
    // httpとhttps処理
    //ajpConnector.setScheme("http");
    // SSL連結する時に使う
    //ajpConnector.setSecure(false);
    
    //server.xmlからsecretRequired設定
    ((AbstractAjpProtocol<?>)ajpConnector.getProtocolHandler()).setSecretRequired(false);
    
    //コレを設定しないとmod_jkで「Tomcat is probably not started or is listening on the wrong port (errno=111)」のエラーになる可能性あり
    ((AbstractAjpProtocol<?>)ajpConnector.getProtocolHandler()).setAddress(InetAddress.getByName("0.0.0.0"));
    
    // tomcat設定
    var tomcat = new TomcatServletWebServerFactory();
    // 追加
    tomcat.addAdditionalTomcatConnectors(ajpConnector);

	System.out.println("■■servletContainer,02 処理終了");
	
    return tomcat;
  }
}

