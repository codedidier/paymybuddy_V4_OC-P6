package com.codedidier.paymybuddy.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.codedidier.paymybuddy.dto.GetUserAccountDto;
import com.codedidier.paymybuddy.entity.User;
import com.codedidier.paymybuddy.exception.DataNotFindException;
import com.codedidier.paymybuddy.service.UserServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;

@WebMvcTest(controllers = UserControllerImpl.class)
@AutoConfigureMockMvc(addFilters = false)
public class UserControllerTest {

    @Autowired
    MockMvc mockMvc;
    @MockBean
    UserServiceImpl userServiceMock;
    @Mock
    Principal principalMock;
    @InjectMocks
    UserControllerImpl userController;

    private final String getUserAccountUrl = "/home";
    private final String getUserProfileUrl = "/home/profile";
    private final int id = 1;
    private final String email = "mailtest@mail.com";
    private final String lastName = "PrenomTest";
    private final String firstName = "testPaiement";
    private final String password = "0000test";
    private final int balance = 0;

    public User getUserTest() {

        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setPassword(password);
        return user;
    }

    @Test
    public void updateUserValid() throws Exception {
        // GIVEN
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode jsonNodes = objectMapper.createObjectNode();
        jsonNodes.set("id", TextNode.valueOf("1"));
        jsonNodes.set("firstName", TextNode.valueOf(firstName));
        jsonNodes.set("lastName", TextNode.valueOf(lastName));
        jsonNodes.set("email", TextNode.valueOf(email));

        // WHEN

        // THEN
        mockMvc
                .perform(
                        MockMvcRequestBuilders.put("/home/user")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonNodes.toString()))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(view().name("success"));
    }

    @Test
    public void updateUserValidWhenUserDontExist_ShouldThrowDataNotFindException() throws Exception {
        // GIVEN
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode jsonNodes = objectMapper.createObjectNode();
        jsonNodes.set("id", TextNode.valueOf("1"));
        jsonNodes.set("firstName", TextNode.valueOf(firstName));
        jsonNodes.set("lastName", TextNode.valueOf(lastName));
        jsonNodes.set("email", TextNode.valueOf(email));

        // WHEN
        Mockito.doThrow(DataNotFindException.class).when(userServiceMock).update(Mockito.any());
        // THEN
        mockMvc
                .perform(
                        MockMvcRequestBuilders.put("/home/user")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonNodes.toString()))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void deleteUserValid() throws Exception {

        // GIVEN
        String validParam = String.valueOf(id);
        // WHEN

        // THEN
        mockMvc
                .perform(MockMvcRequestBuilders.delete("/home/user").param("id", validParam))
                .andExpect(MockMvcResultMatchers.status().isAccepted())
                .andExpect(view().name("plain-login"));
    }

    @Test
    public void deleteUserInvalid() throws Exception {

        // GIVEN
        String invalidParam = null;

        // WHEN

        // THEN
        mockMvc
                .perform(MockMvcRequestBuilders.delete("/home/user").param("id", invalidParam))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void deleteUserValidWhenUserDontExist_ShouldThrowDataNotFindException() throws Exception {

        // GIVEN
        String validParam = "1";
        // WHEN
        Mockito.doThrow(DataNotFindException.class)
                .when(userServiceMock)
                .deleteUserById(Mockito.anyInt());
        // THEN
        mockMvc
                .perform(MockMvcRequestBuilders.delete("/home/user").param("id", validParam))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void getUserAccountValid() throws Exception {

        // GIVEN
        GetUserAccountDto user = new GetUserAccountDto();
        user.setEmail(email);
        user.setLastName(lastName);
        user.setFirstName(firstName);
        user.setBalance(new BigDecimal(0));
        // WHEN
        Mockito.when(principalMock.getName()).thenReturn(email);
        Mockito.when(userServiceMock.findByEmail(email)).thenReturn(Optional.of(this.getUserTest()));

        // THEN
        mockMvc
                .perform(MockMvcRequestBuilders.get(getUserAccountUrl).principal(principalMock))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(view().name("home"))
                .andExpect(MockMvcResultMatchers.model().attribute("theUser", user));
    }

    @Test
    public void getUserProfileValid() throws Exception {

        // GIVEN
        GetUserAccountDto user = new GetUserAccountDto();
        user.setEmail(email);
        user.setLastName(lastName);
        user.setFirstName(firstName);
        user.setBalance(new BigDecimal(0));
        // WHEN
        Mockito.when(principalMock.getName()).thenReturn(email);
        Mockito.when(userServiceMock.findByEmail(email)).thenReturn(Optional.of(this.getUserTest()));

        // THEN
        mockMvc
                .perform(MockMvcRequestBuilders.get(getUserProfileUrl).principal(principalMock))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(view().name("edit-user-profile"))
                .andExpect(MockMvcResultMatchers.model().attribute("theUser", user));
    }
}
