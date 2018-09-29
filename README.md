#### 模块设计和说明
* sso-server
> TokenManager：令牌生成、校验、失效时间管理；
>> LocalTokenManager:认证中心系统内存中维护一个tokenMap，用来存储生成的token和登录信息，作为sso-client过滤器校验客户系统是否登录的依据；<br>
>> RedisTokenManager:认证中心将生成的token和用户信息存储在redis中，作为sso-client过滤器校验客户系统是否登录的依据；<br>
> LoginController:统一登录、登录后根据backUrl跳转到访问的目标页面；
>> 将登录生成的token存在认证中心系统的cookie中，在backUrl中携带token，在sso-client过滤器通过TokenManager校验token是否失效，token有效，则该请求就不会被拦截，然后在过滤器中将该token对应的loginUser和token信息存储在当前系统的session中，之后请求当前系统拦截器中从session中获取登录信息即可；

* sso-client
> 作为sso-server、sso-demo依赖模块，定义了SsoFilter、SsoLogoutFilter，统一控制请求的登录校验；

* sso-demo
> 集成单点登录的子项目，需要在web.xml中配置sso-client配置的过滤器，配置认证中心的访问链接；

#### 使用说明
* sso-server模块配置（需要依赖sso-client）
> web.xml中配置sso-client过滤器(注意：将认证中心登录相关请求放开，不拦截)
```
  <!-- sso配置过滤器核心配置 -->
	<filter>
		<filter-name>smartFilter</filter-name>
		<filter-class>org.springframework.web.filter.DelegatingFilterProxy</filter-class>
		<init-param>
			<param-name>targetFilterLifecycle</param-name>
			<param-value>true</param-value>
		</init-param>
	</filter>
	<filter-mapping>
		<filter-name>smartFilter</filter-name>
		<!-- 这里只要不拦截认证中心登录请求即可 -->
		<url-pattern>/admin/*</url-pattern>
	</filter-mapping>
```
> applicationContext.xml中配置ssoFilter,作为认证中心内部登录校验过滤器
```
    <!-- 过滤器 -->
    <bean id="sharpSsoFilter" class="com.sharp.sso.client.SsoFilterContainer">
        <property name="isServer" value="true"/>
        <property name="authenticationRpcService" ref="authenticationRpcService"/>
        <property name="filters">
            <list>
                <bean class="com.sharp.sso.client.filter.SsoFilter"/>
            </list>
        </property>
    </bean>

```
>> 关于tokenManager注入，提供两种方式，分别如下：

>> 本地方式
```
      <!-- SSO令牌管理(cookie+session) -->
    <bean id="tokenManager" class="com.sharp.sso.server.common.token.LocalTokenManager">
        <property name="tokenTimeout" value="1800"/>
    </bean>
```

>> redis方式存储
```
    <!-- SSO令牌管理（cookie+redis实现） -->
    <!-- redis模版，提供对string、set、list、hash类型的redis操作 -->
    <bean id="redisTemplate" class="org.springframework.data.redis.core.RedisTemplate">
        <property name="connectionFactory" ref="jedisConnectionFactory"/>
    </bean>
    <!-- 自定义redisCache，支持泛型，封装redisTemplate对redis的常用操作 -->
    <bean id="redisCache" class="com.sharp.sso.server.common.RedisCache">
        <property name="redisTemplate" ref="redisTemplate"/>
    </bean>
    <!--SSO令牌管理(分布式) : 用于部署多JVM实例时，RedisSession覆盖HttpSession实现Session共享-->
    <bean id="tokenManager" class="com.sharp.sso.server.common.token.RedisTokenManager">
        <property name="tokenTimeout" value="${sso.timeout}"/>
    </bean>
```

* sso-demo配置（需要依赖sso-client）
> web.xml中配置sso-client提供的核心过滤器
```
   <!-- Smart核心 -->
	<filter>
		<filter-name>smartFilter</filter-name>
		<!-- 通过设置过滤器代理，可以将自定义过滤器纳入spring管理，这样就可以在spring配置文件中直接注入 -->
		<filter-class>org.springframework.web.filter.DelegatingFilterProxy</filter-class>
		<init-param>
			<param-name>targetFilterLifecycle</param-name>
			<param-value>true</param-value>
		</init-param>
	</filter>
	<filter-mapping>
		<filter-name>smartFilter</filter-name>
		<url-pattern>/*</url-pattern>
	</filter-mapping>
```
> applicationContext.xml注入Filter相关bean
```
   <!-- sso核心filter注入 -->
	<bean id="smartFilter" class="com.smart.sso.client.SmartContainer">
		<property name="ssoServerUrl" value="${sso.server.url}" />
		<!-- Dubbo服务开启时，打开注释 -->
		<property name="authenticationRpcService" ref="authenticationRpcService" />
		<property name="filters">
			<list>
				<!-- 单点登录 -->
				<bean class="com.smart.sso.client.SsoFilter"/>
				<!-- 单点退出(选配) -->
				<bean class="com.smart.sso.client.LogoutFilter">
					<property name="pattern" value="/logout" />
					<property name="ssoBackUrl" value="/index" />
				</bean>
				<!-- 权限控制(选配) -->
				<bean class="com.smart.sso.client.PermissionFilter">
					<property name="ssoAppCode" value="${sso.app.code}" />
				</bean>
			</list>
		</property>
	</bean>
```

