package com.epamlearning.services;

import com.epamlearning.models.User;
import com.epamlearning.storage.InMemoryStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class UserServiceTest {
    @InjectMocks
    private UserService userService;

    @Mock
    private InMemoryStorage inMemoryStorage;

    private Map<Long, User> storageUsers;

    @BeforeEach
    public void init() {
        storageUsers = new HashMap<>();
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateUser() {
        when(inMemoryStorage.getStorageUsers()).thenReturn(storageUsers);

        User user = userService.createUser("John", "Smith");

        assertNotNull(user.getId());
        assertEquals("John", user.getFirstName());
        assertEquals("Smith", user.getLastName());
        assertTrue(user.isActive());
        assertNotNull(user.getUsername());
        assertNotNull(user.getPassword());
    }


    @Test
    public void testGenerateUserName() {
        // Mocking
        when(inMemoryStorage.getStorageUsers()).thenReturn(storageUsers);

        // Test
        String username = userService.generateUserName("John", "Smith");

        // Assertions
        assertNotNull(username);
        assertTrue(username.matches("John\\.Smith\\d*"));
    }

    @Test
    void generateRandomPassword() {
        // Test
        String password = userService.generateRandomPassword();

        // Assertions
        assertNotNull(password);
        assertEquals(10, password.length());
    }

}
