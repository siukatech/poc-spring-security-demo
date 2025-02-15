package com.siukatech.poc.spring.security.demo.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.siukatech.poc.spring.security.demo.config.WebSecurityConfig;
import com.siukatech.poc.spring.security.demo.model.SimpleReq;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.anonymous;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * WebSecurityConfig.class is required to include to the WebMvcTest
 * or implement the WebMvcConfigurer interface
 */
@Slf4j
@WebMvcTest(controllers = {
        WebSecurityConfig.class,
        GeneralController.class, UserController.class, AdminController.class
})
@WebAppConfiguration
public class WebSecurityIntegrationTests {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    private ObjectMapper objectMapperForTest;

    @BeforeEach
    public void setup(TestInfo testInfo) {
        if (this.mockMvc == null) {
            this.mockMvc = MockMvcBuilders
                    .webAppContextSetup(webApplicationContext)
                    .apply(springSecurity())
                    .build();
        }
        if (this.objectMapperForTest == null) {
            this.objectMapperForTest = JsonMapper.builder().build();
        }
    }

    private SimpleReq prepare_SimpleReq() {
        SimpleReq simpleReq = new SimpleReq("1");
        return simpleReq;
    }

    private String convert_SimpleReqToStr(SimpleReq simpleReq) throws JsonProcessingException {
        String simpleReqStr = this.objectMapperForTest.writeValueAsString(simpleReq);
        return simpleReqStr;
    }

    // /general/welcome
    @Test
    public void test_getGeneralWelcome() throws Exception {
        // given

        // when
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/general/welcome")
                .accept(MediaType.APPLICATION_JSON)
                ;

        // then
        MvcResult mvcResult = this.mockMvc
                .perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

    }

    // /user/info
    @Test
    public void test_postUserInfo_anonymous() throws Exception {
        // given
        SimpleReq simpleReq = this.prepare_SimpleReq();
        String simpleReqStr = this.convert_SimpleReqToStr(simpleReq);

        // when
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/user/info")
                .content(simpleReqStr)
                .contentType(MediaType.APPLICATION_JSON)
                //
                .with(anonymous())
                //
                ;

        // then
        MvcResult mvcResult = this.mockMvc
                .perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andReturn();

    }

    // /user/info
    @WithMockUser(value = "user1"
//            , roles = {"USER_01"}
            , authorities = {"USER_01"}
    )
    @Test
    public void test_getUserInfo_user1_USER_01() throws Exception {
        // given

        // when
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/user/info")
                .accept(MediaType.APPLICATION_JSON)
                ;

        // then
        MvcResult mvcResult = this.mockMvc
                .perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

    }

    // /user/info
    @WithMockUser(value = "admin1", authorities = {"ADMIN_01"})
    @Test
    public void test_getUserInfo_admin1_ADMIN_01() throws Exception {
        // given

        // when
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/user/info")
                .accept(MediaType.APPLICATION_JSON)
                ;

        // then
        MvcResult mvcResult = this.mockMvc
                .perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isForbidden())
                .andReturn();

    }

    // /user/info
    @WithMockUser(value = "user1", authorities = {"USER_01"}
//            , roles = {"USER_02"}
    )
    @Test
    public void test_postUserInfo_user1_USER_01() throws Exception {
        // given
        SimpleReq simpleReq = this.prepare_SimpleReq();
        String simpleReqStr = this.convert_SimpleReqToStr(simpleReq);

        // when
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/user/info")
                .content(simpleReqStr)
                .contentType(MediaType.APPLICATION_JSON)
                ;

        // then
        MvcResult mvcResult = this.mockMvc
                .perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

    }

    // /admin/info
    @WithMockUser(value = "admin1", authorities = {"ADMIN_01"})
    @Test
    public void test_postUserInfo_admin1_ADMIN_01() throws Exception {
        // given
        SimpleReq simpleReq = this.prepare_SimpleReq();
        String simpleReqStr = this.convert_SimpleReqToStr(simpleReq);

        // when
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/user/info")
                .content(simpleReqStr)
                .contentType(MediaType.APPLICATION_JSON)
                ;

        // then
        MvcResult mvcResult = this.mockMvc
                .perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isForbidden())
                .andReturn();

    }

    // /admin/info
    @WithMockUser(value = "admin2"
//            , roles = {"ADMIN_02"}
            , authorities = {"ADMIN_02"}
    )
    @Test
    public void test_getAdminInfo_admin2_ADMIN_02() throws Exception {
        // given

        // when
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/admin/info")
                .accept(MediaType.APPLICATION_JSON)
                ;

        // then
        MvcResult mvcResult = this.mockMvc
                .perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

    }

    // /admin/info
    @WithMockUser(value = "user2", authorities = {"USER_02"})
    @Test
    public void test_getAdminInfo_user2_USER_02() throws Exception {
        // given

        // when
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/admin/info")
                .accept(MediaType.APPLICATION_JSON)
                ;

        // then
        MvcResult mvcResult = this.mockMvc
                .perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isForbidden())
                .andReturn();

    }

    // /admin/info
    @WithMockUser(value = "admin2", authorities = {"ADMIN_02"})
    @Test
    public void test_postAdminInfo_admin2_ADMIN_02() throws Exception {
        // given
        SimpleReq simpleReq = this.prepare_SimpleReq();
        String simpleReqStr = this.convert_SimpleReqToStr(simpleReq);

        // when
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/admin/info")
                .content(simpleReqStr)
                .contentType(MediaType.APPLICATION_JSON)
                ;

        // then
        MvcResult mvcResult = this.mockMvc
                .perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

    }

    // /admin/info
    @WithMockUser(value = "admin3", authorities = {"ADMIN_03"})
    @Test
    public void test_postAdminInfo_admin3_ADMIN_03() throws Exception {
        // given
        SimpleReq simpleReq = this.prepare_SimpleReq();
        String simpleReqStr = this.convert_SimpleReqToStr(simpleReq);

        // when
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/admin/info")
                .content(simpleReqStr)
                .contentType(MediaType.APPLICATION_JSON)
                ;

        // then
        MvcResult mvcResult = this.mockMvc
                .perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isForbidden())
                .andReturn();

    }

}
