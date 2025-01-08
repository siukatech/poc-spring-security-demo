# Spring Security Demo

## WebSecurityConfig
The `WebSecurityConfig` could also implement `WebMvcConfigurer`.  
Then we will combine the security config and web mvc config.  

In this demo, we dont do that.  

The `WebMvcConfigurer` will be included when the `WebMvcTest` is used.  
So if we want the `WebSecurityConfig` being included, we could either implement the `WebMvcConfigurer` or add it to `WebMvcTest`'s classes list as below.  
```java
@Slf4j
@WebMvcTest(controllers = {
        WebSecurityConfig.class,
        GeneralController.class, UserController.class, AdminController.class
})
@WebAppConfiguration
public class WebSecurityIntegrationTests {
    ...
}
```

Once `WebSecurityConfig` is loaded, the `HttpSecurity.authorizeHttpRequests` could be tested with following configuration.
```java
...
http.authorizeHttpRequests(requests -> requests
    ...
    //
    .requestMatchers(AntPathRequestMatcher.antMatcher("/user/**"))
    //.hasAnyAuthority("USER_01", "USER_02")
    .hasAnyRole("USER_01", "USER_02")
    //
    ...
```
```java
@WithMockUser(value = "user1", roles = {"USER_01"})
@Test
public void test_getUserInfo_user1_USER_01() throws Exception {
    ...
}
```

