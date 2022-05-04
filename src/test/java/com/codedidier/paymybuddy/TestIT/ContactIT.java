package com.codedidier.paymybuddy.TestIT;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import com.codedidier.paymybuddy.dto.ContactDto;
import com.codedidier.paymybuddy.repository.UserRepository;

@SpringBootTest
@AutoConfigureMockMvc
public class ContactIT {

    @Autowired
    UserRepository userRepo;
    @Autowired
    MockMvc mockMvc;

    private final String createUrl = "/newContact";
    private final String deleteUrl = "/deleteContact";
    private final String readUrl = "/home/contact";

    @Test
    public void requestWhenNotAuthenticated() throws Exception {

        // POST:/newContact DELETE:/deleteContact GET:/home/contact
        mockMvc
                .perform(post(createUrl).with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isUnauthorized());
        mockMvc
                .perform(delete(deleteUrl).with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isUnauthorized());
        mockMvc.perform(get(readUrl)).andExpect(status().isUnauthorized());
    }

    @Test
    public void getContactValid() throws Exception {

        // When no contact -> should return an empty list
        mockMvc
                .perform(get(readUrl).with(user("mailtest@mail.com")))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.model().attribute("contacts", new ArrayList<>()));

        // When there are contact, should return all of them

        ContactDto contactDto1 = new ContactDto("Prenom3", "Nom3", "mail3@mail.com");
        ContactDto contactDto2 = new ContactDto("Prenom4", "Nom4", "mail4@mail.com");
        ContactDto contactDto3 = new ContactDto("Prenom5", "Nom5", "mail5@mail.com");

        mockMvc
                .perform(get(readUrl).with(user("mail2@mail.com")))
                .andExpect(status().isOk())
                .andExpect(
                        MockMvcResultMatchers.model()
                                .attribute("contacts", containsInAnyOrder(contactDto1, contactDto2, contactDto3)));
    }

    @Transactional
    @Test
    public void deleteContact() throws Exception {

        // When valid
        mockMvc
                .perform(
                        delete(deleteUrl)
                                .param("email", "mail3@mail.com")
                                .with(SecurityMockMvcRequestPostProcessors.user("mail2@mail.com"))
                                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isAccepted())
                .andExpect(view().name("success"));

        // When contact doesn't exist
        mockMvc
                .perform(
                        delete(deleteUrl)
                                .param("email", "mail3@mail.com")
                                .with(SecurityMockMvcRequestPostProcessors.user("mail2@mail.com"))
                                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isNotFound());
    }

    @Transactional
    @Test
    public void createContact() throws Exception {

        // When valid
        String friendMail = "mailvalid@mail.com";
        mockMvc
                .perform(
                        post(createUrl)
                                .param("email", friendMail)
                                .with(SecurityMockMvcRequestPostProcessors.user("mail2@mail.com"))
                                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isCreated())
                .andExpect(view().name("success"));

        // When contact already exist
        mockMvc
                .perform(
                        post(createUrl)
                                .param("email", friendMail)
                                .with(SecurityMockMvcRequestPostProcessors.user("mail2@mail.com"))
                                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isConflict());

        // When contact is not found
        mockMvc
                .perform(
                        post(createUrl)
                                .param("email", "mailnotfound@mail.com")
                                .with(SecurityMockMvcRequestPostProcessors.user("mail2@mail.com"))
                                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isNotFound());
    }
}
