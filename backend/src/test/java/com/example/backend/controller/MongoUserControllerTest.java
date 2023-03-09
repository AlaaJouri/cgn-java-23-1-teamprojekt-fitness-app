package com.example.backend.controller;

import com.example.backend.model.MongoUser;
import com.example.backend.repo.MongoUserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class MongoUserControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    MongoUserRepository mongoUserRepository;


    @Test
    @DirtiesContext
    @WithMockUser(username = "user", password = "password")
    void getMe_whenAuthenticated_thenUsername() throws Exception {
        mongoUserRepository.save(new MongoUser("1", "user", "password", "BASIC"));
        mockMvc.perform(MockMvcRequestBuilders.get("/api/user/me")
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("user"))
                .andExpect(jsonPath("$.password").isEmpty());


    }


    @Test
    @DirtiesContext
    void create_whenValid_then200() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "username": "test",
                                    "password": "test"
                                }
                                """)
                        .with(csrf()))
                .andExpect(status().isOk());
    }


    @Test
    void getStatus401ifUserIsNotAuthenticatedAdmin() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/user/admin")
                        .with(csrf()))
                .andExpect(status().isUnauthorized());
    }


    @Test
    void createUserWithoutUsername_ThenBadRequest400() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "username": "",
                                    "password": "test"
                                }
                                """)
                        .with(csrf()))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createUserWithoutPassword_ThenBadRequest400() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "username": "test",
                                    "password": ""
                                }
                                """)
                        .with(csrf()))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createUser_UserAlreadyExists() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "username": "test",
                                    "password": "test"
                                }
                                """)
                        .with(csrf()))
                .andExpect(status().isOk());
        mockMvc.perform(MockMvcRequestBuilders.post("/api/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "username": "test",
                                    "password": "test"
                                }
                                """)
                        .with(csrf()))
                .andExpect(status().isConflict());
    }

    @Test
    @WithMockUser(username = "user", password = "password")
    void loginUserWithValidUsernameAndPassword_Then200() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/user/login")
                        .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                                {
                                    "username": "user",
                                    "password": "password"
                                }
                                """));
        mockMvc.perform(MockMvcRequestBuilders.get("/api/user")
                .with(csrf()))
                .andExpect(status().isOk());
    }

    @Test
    void loginUserWithInvalidUsernameAndPassword_Then401() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/user/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "username": "lolll",
                                    "password": "passworddd"
                                }
                                """))

                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "user", password = "password")
    void logoutUser_Then200() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/user/logout")
                        .with(csrf()))
                .andExpect(status().isOk());

    }

    @Test
    @WithMockUser(username = "user", password = "password")
    public void testGetMe2() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/user/me2")
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().string("user"));

    }

    @Test
    @WithMockUser(username = "user", password = "password")
    public void testGetAdminStatus() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/user/admin"))
                .andExpect(status().isOk())
                .andExpect(content().string("Admin OK"));
    }

    @Test
    @WithMockUser(username = "user", password = "password")
    public void testGetStatus() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/user"))

                .andExpect(status().isOk())
                .andExpect(content().string("OK"));
    }

}

