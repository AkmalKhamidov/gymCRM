package com.epamlearning.services;

import com.epamlearning.daos.UserDAOImpl;
import com.epamlearning.exceptions.NotAuthenticated;
import com.epamlearning.exceptions.NotFoundException;
import com.epamlearning.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserDAOImpl userDAO;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void saveUser() {
        User user = new User();
        when(userDAO.save(user)).thenReturn(Optional.of(user));

        Optional<User> savedUser = userService.save(user);

        assertTrue(savedUser.isPresent());
        assertEquals(user, savedUser.get());
    }

    @Test
    void updateUser() {
        long userId = 1L;
        User updatedUser = new User();
        when(userDAO.update(eq(userId), any(User.class))).thenReturn(Optional.of(updatedUser));

        Optional<User> user = userService.update(userId, updatedUser);

        assertTrue(user.isPresent());
        assertEquals(updatedUser, user.get());
    }

    @Test
    void updateUserPassword() {
        long userId = 1L;
        String newPassword = "newPassword";
        User updatedUser = new User();
        when(userDAO.updatePassword(eq(userId), eq(newPassword))).thenReturn(Optional.of(updatedUser));

        Optional<User> user = userService.updatePassword(userId, newPassword);

        assertTrue(user.isPresent());
        assertEquals(updatedUser, user.get());
    }

    @Test
    void updateActiveStatus() {
        long userId = 1L;
        boolean newActiveStatus = true;
        User updatedUser = new User();
        when(userDAO.updateActive(eq(userId), eq(newActiveStatus))).thenReturn(Optional.of(updatedUser));

        Optional<User> user = userService.updateActive(userId, newActiveStatus);

        assertTrue(user.isPresent());
        assertEquals(updatedUser, user.get());
    }

    @Test
    void findUserById() {
        long userId = 1L;
        User user = new User();
        when(userDAO.findById(userId)).thenReturn(Optional.of(user));

        Optional<User> foundUser = userService.findById(userId);

        assertTrue(foundUser.isPresent());
        assertEquals(user, foundUser.get());
    }

    @Test
    void findAllUsers() {
        List<Optional<User>> users = new ArrayList<>();
        when(userDAO.findAll()).thenReturn(users);

        List<Optional<User>> foundUsers = userService.findAll();

        assertEquals(users, foundUsers);
    }

    @Test
    void deleteUserById() {
        long userId = 1L;
        when(userDAO.findById(userId)).thenReturn(Optional.of(new User()));

        assertDoesNotThrow(() -> userService.deleteById(userId));
        verify(userDAO, times(1)).deleteById(userId);
    }

    @Test
    void deleteUserByIdNotFound() {
        long userId = 1L;
        when(userDAO.findById(userId)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> userService.deleteById(userId));
        assertEquals("User with ID " + userId + " not found for delete.", exception.getMessage());
        verify(userDAO, never()).deleteById(anyLong());
    }

    @Test
    void findByUsername() {
        String username = "testUser";
        User user = new User();
        when(userDAO.findByUserName(username)).thenReturn(Optional.of(user));

        Optional<User> foundUser = userService.findByUsername(username);

        assertTrue(foundUser.isPresent());
        assertEquals(user, foundUser.get());
    }

    @Test
    void findByUsernameNotFound() {
        String username = "nonExistentUser";
        when(userDAO.findByUserName(username)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> userService.findByUsername(username));
        assertEquals("User with username " + username + " not found.", exception.getMessage());
    }

    @Test
    void authenticateUser() {
        String username = "testUser";
        String password = "testPassword";
        User user = new User();
        user.setPassword(password);
        when(userDAO.findByUserName(username)).thenReturn(Optional.of(user));

        assertTrue(userService.authenticate(username, password));
    }

    @Test
    void authenticateUserNotFound() {
        String username = "nonExistentUser";
        String password = "testPassword";
        when(userDAO.findByUserName(username)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> userService.authenticate(username, password));
        assertEquals("User with username " + username + " not found.", exception.getMessage());
    }

    @Test
    void authenticateUserWrongPassword() {
        String username = "testUser";
        String correctPassword = "correctPassword";
        String wrongPassword = "wrongPassword";
        User user = new User();
        user.setPassword(correctPassword);
        when(userDAO.findByUserName(username)).thenReturn(Optional.of(user));

        NotAuthenticated exception = assertThrows(NotAuthenticated.class, () -> userService.authenticate(username, wrongPassword));
        assertEquals("Wrong password. Username: " + username, exception.getMessage());
    }

    @Test
    void createUser() {
        String firstName = "John";
        String lastName = "Doe";
        User generatedUser = userService.createUser(firstName, lastName);

        assertNotNull(generatedUser);
        assertEquals(firstName, generatedUser.getFirstName());
        assertEquals(lastName, generatedUser.getLastName());
        assertTrue(generatedUser.isActive());
    }

    @Test
    void generateUserName() {
        String firstName = "John";
        String lastName = "Doe";
        String generatedUsername = userService.generateUserName(firstName, lastName);

        assertNotNull(generatedUsername);
        assertTrue(generatedUsername.startsWith(firstName + "." + lastName));
    }

    @Test
    void generateRandomPassword() {
        String generatedPassword = userService.generateRandomPassword();

        assertNotNull(generatedPassword);
        assertEquals(10, generatedPassword.length());
    }
}
