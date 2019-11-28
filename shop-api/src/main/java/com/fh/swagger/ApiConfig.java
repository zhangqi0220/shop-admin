package com.fh.swagger;

import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

@EnableWebMvc
@EnableSwagger2
public class ApiConfig {

    @Bean
    public Docket customDocket() {
        /**
         * 设置参数token
         */
        ParameterBuilder ticketPar = new ParameterBuilder();
        List<Parameter> pars = new ArrayList<Parameter>();
        ticketPar.name("token").description("令牌").modelRef(new ModelRef("string")).parameterType("header")
                .required(false).build(); // header中的token参数非必填，传空也可以
        pars.add(ticketPar.build()); // 根据每个方法名也知道当前方法在设置什么参数

        /**
         * 这里有包含正则
         */
        return new Docket(DocumentationType.SWAGGER_2).select().
                //指定扫描的包路径来定义指定要建立API的目录
                        apis(RequestHandlerSelectors.basePackage("com.fh")).
                        paths(PathSelectors.any()).
                        build()
                /**
                 * 这里把token加进去了。
                 */
                .globalOperationParameters(pars).
                        apiInfo(apiInfo());
    }
    /**
     * 这个是设置大标题小标题
     * @return
     */
    ApiInfo apiInfo() {
        return new ApiInfoBuilder().title("飞狐商城接口api").description("前后端联调api 文档").version("0.1.0")
                .build();
    }
}
