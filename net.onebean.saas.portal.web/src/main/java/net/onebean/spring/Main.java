package net.onebean.spring;

import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig;
import com.eakay.core.extend.ApolloConfInitializer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.ImportResource;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

@ComponentScan(
		basePackages = {
				"net.onebean.saas.portal.service",
				"net.onebean.saas.portal.consumer",
				"net.onebean.saas.portal.provider",
				"net.onebean.saas.portal.common",
				"net.onebean.saas.portal.security",
				"com.eakay.*.**.api.**",
				"com.eakay.core",
				"com.eakay.config",
				"com.eakay.component"
		},
		includeFilters = {
				@ComponentScan.Filter(value = Service.class, type = FilterType.ANNOTATION),
				@ComponentScan.Filter(value = Component.class, type = FilterType.ANNOTATION)
		})
@ComponentScan(
		basePackages = {"net.onebean.saas.portal.web.**"},
		includeFilters = {
				@ComponentScan.Filter(value = Controller.class, type = FilterType.ANNOTATION),
		})
@ImportResource(locations={"classpath*:META-INF/spring/*.xml"})
@SpringBootApplication
@EnableApolloConfig
@EnableDiscoveryClient
@EnableHystrix
@EnableFeignClients(basePackages = "com.eakay.*.**.api.**")
public class Main {

	public static void main(String[] args) {
		/*在apollo之前加载logback配置 初始化spring boot logger*/
		ApolloConfInitializer.init();
		SpringApplication.run(Main.class, args);
	}


}


