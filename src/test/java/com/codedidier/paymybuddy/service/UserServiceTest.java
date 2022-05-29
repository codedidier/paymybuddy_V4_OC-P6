package com.codedidier.paymybuddy.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.codedidier.paymybuddy.dto.ContactDto;
import com.codedidier.paymybuddy.entity.User;
import com.codedidier.paymybuddy.exception.DataAlreadyExistException;
import com.codedidier.paymybuddy.exception.DataNotFindException;
import com.codedidier.paymybuddy.model.UserModel;
import com.codedidier.paymybuddy.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    UserRepository userRepositoryMock;
    @Mock
    BCryptPasswordEncoder bCryptPasswordEncoderMock;
    @InjectMocks
    UserServiceImpl userService;

    private final int id = 1;
    private final String email = "testmail@gmail.com";
    private final String lastName = "Nom";
    private final String firstName = "Prenom";
    private final String password = "testpassword";
    private final int balance = 0;

    @Test
    void saveUserValid() {

//        GIVEN
        UserModel user = new UserModel();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setPassword(password);

//        WHEN

        userService.save(user);
//        THEN

        verify(userRepositoryMock, times(1)).save(any());
    }

    @Test
    public void updateUserValid() {

        // GIVEN
        UserModel userToUpdate = new UserModel();
        userToUpdate.setFirstName(firstName);
        userToUpdate.setLastName(lastName);
        userToUpdate.setId(id);
        userToUpdate.setEmail(email);
        User user = new User();

        // WHEN
        when(userRepositoryMock.existsById(anyInt())).thenReturn(true);
        when(userRepositoryMock.getById(anyInt())).thenReturn(user);
        userService.update(userToUpdate);
        // THEN

        verify(userRepositoryMock, times(1)).save(any());
        verify(userRepositoryMock, times(1)).existsById(anyInt());

    }

    @Test
    public void updateUserWhenUserDontExist_ShouldThrowDataNotFindException() {

        // GIVEN
        UserModel userToUpdate = new UserModel();
        userToUpdate.setFirstName(firstName);
        userToUpdate.setLastName(lastName);
        userToUpdate.setId(id);

        // WHEN
        when(userRepositoryMock.existsById(anyInt())).thenReturn(false);
        // THEN
        Assertions.assertThrows(DataNotFindException.class, () -> userService.update(userToUpdate));

    }

    @Test
    public void deleteUserValid() {

        // GIVEN
        int validUserId = 598743;

        // WHEN
        when(userRepositoryMock.existsById(anyInt())).thenReturn(true);
        userService.deleteUserById(validUserId);
        // THEN

        verify(userRepositoryMock, times(1)).deleteById(validUserId);
        verify(userRepositoryMock, times(1)).existsById(validUserId);

    }

    @Test
    public void deleteUserValidWhenUserDontExist_ShouldThrowDataNotFindException() {

        // GIVEN
        int validUserId = 598743;

        // WHEN
        when(userRepositoryMock.existsById(anyInt())).thenReturn(false);
        // THEN
        Assertions.assertThrows(DataNotFindException.class, () -> userService.deleteUserById(validUserId));

    }

    @Test
    public void addNewContactValid() {

        // GIVEN
        User contact = new User(email, lastName, firstName, password);
        contact.setId(1);
        Optional<User> contactToAdd = Optional.of(contact);
        User user = new User(email + 1, lastName, firstName, password);
        user.setId(2);
        Optional<User> userAccount = Optional.of(user);

        // WHEN
        when(userRepositoryMock.findByEmail(email)).thenReturn(contactToAdd);
        when(userRepositoryMock.findByEmail(email + 1)).thenReturn(userAccount);

        userService.addNewContact(email, email + 1);

        user.getContacts().add(contact);
        // THEN
        Mockito.verify(userRepositoryMock, times(1)).save(user);
    }

    @Test
    public void addNewContactWhenContactEmailDontExist_ShouldThrowDataNotFindException() {

        // GIVEN
        Optional<User> userToAdd = Optional.empty();

        // WHEN
        when(userRepositoryMock.findByEmail(email)).thenReturn(userToAdd);

        // THEN
        Assertions.assertThrows(DataNotFindException.class, () -> userService.addNewContact(email, email + 1));

    }

    @Test
    public void addNewContactWhenContactAlreadyExist_ShouldThrowDataAlreadyExist() {

        // GIVEN
        User user1 = new User(email, lastName, firstName, password);
        user1.setId(1);
        Optional<User> userToAdd = Optional.of(user1);
        User user2 = new User(email + 1, lastName, firstName, password);
        user2.setId(2);
        user2.getContacts().add(user1);
        Optional<User> userAccount = Optional.of(user2);

        // WHEN
        when(userRepositoryMock.findByEmail(email)).thenReturn(userToAdd);
        when(userRepositoryMock.findByEmail(email + 1)).thenReturn(userAccount);

        // THEN
        Assertions.assertThrows(DataAlreadyExistException.class, () -> userService.addNewContact(email, email + 1));
    }

    @Test
    public void deleteContactValid() {

        // GIVEN
        User user = new User("userEmail", "userLastName", "userFirstName", "test");
        User contact = new User(email, lastName, firstName, password);
        contact.setId(id);
        user.getContacts().add(contact);
        // WHEN
        when(userRepositoryMock.findByEmail("userEmail")).thenReturn(Optional.of(user));

        userService.deleteContact(email, "userEmail");
        user.getContacts().clear();
        // THEN
        Mockito.verify(userRepositoryMock, times(1)).save(user);

    }

    @Test
    public void deleteContactWhenContactDontExist_ShouldThrowDataNotFindException() {

        // GIVEN
        User user = new User(email, "userLastName", "userFirstName", "test");

        // WHEN
        when(userRepositoryMock.findByEmail(email)).thenReturn(Optional.of(user));

        // THEN
        Assertions.assertThrows(DataNotFindException.class, () -> userService.deleteContact("email", email));

    }

    @Test
    public void getContactValid_ShouldReturnAListOfTwoDto() {

        // GIVEN
        User user = new User(email, lastName, firstName, password);
        User userContact1 = new User(email + 1, lastName + 1, firstName + 1, password + 1);
        User userContact2 = new User(email + 2, lastName + 2, firstName + 2, password + 2);
        user.getContacts().add(userContact1);
        user.getContacts().add(userContact2);

        ContactDto contactDto1 = new ContactDto(firstName + 1, lastName + 1, email + 1);
        ContactDto contactDto2 = new ContactDto(firstName + 2, lastName + 2, email + 2);
        // WHEN
        when(userRepositoryMock.findByEmail(email)).thenReturn(Optional.of(user));
        Collection<ContactDto> expected = Arrays.asList(contactDto1, contactDto2);
        Collection<ContactDto> actual = userService.getAllContact(email);

        // THEN
        org.assertj.core.api.Assertions.assertThat(actual.containsAll(expected)).isTrue();

    }

    @Test
    public void getContactValidWhenNoContactFound_ShouldReturnAnEmptyList() {

        // GIVEN
        User user = new User(email, lastName, firstName, password);

        // WHEN
        when(userRepositoryMock.findByEmail(email)).thenReturn(Optional.of(user));
        Collection<ContactDto> expected = new ArrayList<>();
        Collection<ContactDto> actual = userService.getAllContact(email);

        // THEN
        org.assertj.core.api.Assertions.assertThat(actual).isEqualTo(expected);

    }

    public int getBalance() {
        return balance;
    }

}
