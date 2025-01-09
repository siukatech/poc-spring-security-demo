# Spring Security Demo

## WebSecurityConfig
The `WebSecurityConfig` is a configuration class that attaches the following annotations to enable the `Spring Security`.  
- @EnableWebSecurity
- @EnableMethodSecurity
- @Configuration (optional)

However, it is not initiated when `WebMvcTest` is used.  
The configuration of `HttpSecurity` then cannot be tested.  

There are 2 ways to initiate the `WebSecurityConfig`.
- Implement the interface `WebMvcConfigurer` as `WebMvcTest` uses this.
- Add to `WebMvcTest.controllers`.

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

Once `WebSecurityConfig` is loaded, the `HttpSecurity.authorizeHttpRequests` could be tested with `WithMockUser`.  
```java
// HttpSecurity.authorizeHttpRequests
...
http.authorizeHttpRequests(requests -> requests
    ...
    //
    .requestMatchers(AntPathRequestMatcher.antMatcher("/user/**"))
    //.hasAnyRole("USER_01", "USER_02")
    .hasAnyAuthority("USER_01", "USER_02")
    //
    ...
```
```java
@WithMockUser(value = "user1"
//    , roles = {"USER_01"}
    , authorities = {"USER_01"}
)
@Test
public void test_getUserInfo_user1_USER_01() throws Exception {
    ...
}
```


## Role and Authority
Detail explanation can be found on baeldung.com.  
https://baeldung.com/spring-security-granted-authority-vs-role  

They are the `GrantedAuthority` class in `Spring Security`, technically they are same.  
From my view, the main difference between `Role` and `Authority` is role having the prefix `ROLE_`.  

In configuration `HttpSecurity` and unit-test `WithMockUser`, we need to align them.  
If `hasRole` or `hasAnyRole` are configured, then `WithMockUser.roles` will be used.  
Otherwise, `WithMockUser.authorities` will be used if `hasAuthority` or `hasAnyAuthority` are configured.  


