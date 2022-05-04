package com.codedidier.paymybuddy.TestIT;

import static org.hamcrest.Matchers.iterableWithSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.math.BigDecimal;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import com.codedidier.paymybuddy.Repository.TestIT.TransferRepo;
import com.codedidier.paymybuddy.dto.NewTransferDto;
import com.codedidier.paymybuddy.entity.Transfer;

@SpringBootTest
@AutoConfigureMockMvc
public class TransferIT {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    TransferRepo transferRepo;

    private final String addMoneyUrl = "/home/balance/add";
    private final String removeMoneyUrl = "/home/balance/remove";
    private final String createTransferUrl = "/home/transfer";
    private final String readTransferUrl = "/home/transfer";

    @Test
    public void requestWhenNotAuthenticated() throws Exception {
        mockMvc.perform(post(addMoneyUrl)).andExpect(status().isForbidden());
        mockMvc
                .perform(post(addMoneyUrl).with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isUnauthorized());
        mockMvc.perform(post(removeMoneyUrl)).andExpect(status().isForbidden());
        mockMvc
                .perform(post(removeMoneyUrl).with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isUnauthorized());
        mockMvc.perform(post(createTransferUrl)).andExpect(status().isForbidden());
        mockMvc
                .perform(post(createTransferUrl).with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isUnauthorized());
        mockMvc.perform(get(readTransferUrl)).andExpect(status().isUnauthorized());
    }

    @Test
    public void getTransfers() throws Exception {

        // When Valid
        mockMvc
                .perform(
                        get(readTransferUrl).with(SecurityMockMvcRequestPostProcessors.user("mail2@mail.com")))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.model().attribute("transfers", iterableWithSize(4)));

        // When there are no transfers should return emptyList
        mockMvc
                .perform(
                        get(readTransferUrl).with(SecurityMockMvcRequestPostProcessors.user("mail3@mail.com")))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.model().attribute("transfers", iterableWithSize(0)));
    }

    @Transactional
    @Test
    public void addCash() throws Exception {
        // When valid
        mockMvc
                .perform(
                        post(addMoneyUrl)
                                .param("amount", "50")
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                                .with(SecurityMockMvcRequestPostProcessors.user("mail3@mail.com"))
                                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isAccepted())
                .andExpect(view().name("success"));

        // When amount is not valid
        mockMvc
                .perform(
                        post(addMoneyUrl)
                                .param("amount", "-5")
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                                .with(SecurityMockMvcRequestPostProcessors.user("mail3@mail.com"))
                                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isBadRequest());
    }

    @Transactional
    @Test
    public void removeCash() throws Exception {
        // When valid
        mockMvc
                .perform(
                        post(removeMoneyUrl)
                                .param("amount", "19.99")
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                                .with(SecurityMockMvcRequestPostProcessors.user("mail3@mail.com"))
                                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isAccepted())
                .andExpect(view().name("success"));

        // When valid but amount>balance
        mockMvc
                .perform(
                        post(removeMoneyUrl)
                                .param("amount", "1")
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                                .with(SecurityMockMvcRequestPostProcessors.user("mail3@mail.com"))
                                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isBadRequest());

        // When amount is not valid
        mockMvc
                .perform(
                        post(removeMoneyUrl)
                                .param("amount", "-5")
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                                .with(SecurityMockMvcRequestPostProcessors.user("mail3@mail.com"))
                                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isBadRequest());
    }

    @Transactional
    @Test
    public void createTransfer() throws Exception {

        // When valid
        NewTransferDto transferDto1 = new NewTransferDto("mail3@mail.com", 21.3, "bordures");

        String urlEncoded1 = "creditorEmail="
                + transferDto1.getCreditorEmail()
                + "&amount="
                + transferDto1.getAmount()
                + "&description="
                + transferDto1.getDescription();

        mockMvc
                .perform(
                        post(createTransferUrl)
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                                .content(urlEncoded1)
                                .with(SecurityMockMvcRequestPostProcessors.csrf())
                                .with(SecurityMockMvcRequestPostProcessors.user("mail2@mail.com")))
                .andExpect(status().isCreated())
                .andExpect(view().name("success"));

        Transfer check = transferRepo.getTransferByDescription("bordures");
        Assertions.assertThat(check.getAmount()).isEqualTo(new BigDecimal("21.3"));
        Assertions.assertThat(check.getDebtorId()).isEqualTo(2);

        // When amount>debtorBalance
        NewTransferDto transferDto2 = new NewTransferDto("mail3@mail.com", 9999999, "erreur");

        String urlEncoded2 = "creditorEmail="
                + transferDto2.getCreditorEmail()
                + "&amount="
                + transferDto2.getAmount()
                + "&description="
                + transferDto2.getDescription();

        mockMvc
                .perform(
                        post(createTransferUrl)
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                                .content(urlEncoded2)
                                .with(SecurityMockMvcRequestPostProcessors.csrf())
                                .with(SecurityMockMvcRequestPostProcessors.user("mail2@mail.com")))
                .andExpect(status().isBadRequest());

        Assertions.assertThat(transferRepo.getTransferByDescription("erreur")).isNull();

    }
}
