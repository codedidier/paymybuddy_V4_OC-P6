package com.codedidier.paymybuddy.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.codedidier.paymybuddy.dto.NewUserDto;
import com.codedidier.paymybuddy.exception.DataAlreadyExistException;
import com.codedidier.paymybuddy.service.UserServiceImpl;

@WebMvcTest(controllers = RegistrationControllerImpl.class)
@AutoConfigureMockMvc(addFilters = false)
public class RegistrationControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserServiceImpl userServiceMock;

    private final String createUserUrl = "/registration/createNewUser";
    private final int id = 1;
    private final String email = "testmail@mail.com";
    private final String lastName = "NomTest";
    private final String firstName = "PrenomTest";
    private final String password = "0000test";

    public NewUserDto getUserTest() {

        NewUserDto user = new NewUserDto();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setPassword(password);
        return user;
    }

    @Test
    public void createUserValid() throws Exception {
        // GIVEN
        NewUserDto newUserDto = this.getUserTest();
        // WHEN

        // THEN
        mockMvc
                .perform(
                        post(createUserUrl)
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                                .content(
                                        "firstName="
                                                + newUserDto.getFirstName()
                                                + "&lastName="
                                                + newUserDto.getLastName()
                                                + "&email="
                                                + newUserDto.getEmail()
                                                + "&password="
                                                + newUserDto.getPassword()))
                .andExpect(status().isCreated())
                .andExpect(view().name("registration-confirmation"));
    }

    @Test
    public void createUserInvalid() throws Exception {

        // GIVEN
        NewUserDto newUserDto = this.getUserTest();
        newUserDto.setPassword(" ");
        // WHEN

        // THEN
        mockMvc
                .perform(
                        post(createUserUrl)
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                                .content(
                                        "firstName="
                                                + newUserDto.getFirstName()
                                                + "&lastName="
                                                + newUserDto.getLastName()
                                                + "&email="
                                                + newUserDto.getEmail()
                                                + "&password="
                                                + newUserDto.getPassword()))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void createUserWhen_UserAlreadyExist() throws Exception {

        // GIVEN
        NewUserDto newUserDto = this.getUserTest();

        // WHEN
        Mockito.doThrow(DataAlreadyExistException.class).when(userServiceMock).save(Mockito.any());

        // THEN

        mockMvc
                .perform(
                        post(createUserUrl)
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                                .content(
                                        "firstName="
                                                + newUserDto.getFirstName()
                                                + "&lastName="
                                                + newUserDto.getLastName()
                                                + "&email="
                                                + newUserDto.getEmail()
                                                + "&password="
                                                + newUserDto.getPassword()))
                .andExpect(status().isConflict());
    }
}
