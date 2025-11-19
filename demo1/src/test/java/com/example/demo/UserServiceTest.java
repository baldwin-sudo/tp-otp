package com.example.demo;
import static org.junit.jupiter.api.Assertions.*;
import com.example.bean.User;
import com.example.dto.UserCreateDto;
import com.example.dto.UserDto;
import com.example.repository.UserRepository;
import com.example.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.assertj.core.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    public void testFindUserById(){
        // Configure le mock juste pour ce test
        User user = new User(1L, "John", "john@example.com","0611111111");
        // la repository return ce user apres appel de findByid avec ces args .
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        UserDto expected=new UserDto(1L, "John", "john@example.com","0611111111");
        UserDto actual = userService.findById(1L);
        assertEquals(expected,actual);
    }

    @Test
    public void testFindAllUsers(){
        User user = new User(1L, "John", "john@example.com","0611111111");
        when(userRepository.findAll()).thenReturn(List.of(user));

        List<UserDto> expected= new ArrayList<>();
        expected.add(new UserDto(1L, "John", "john@example.com","0611111111"));
        List<UserDto> actual= userService.findAll();
        assertEquals(expected,actual);
    }
    // this  method was done with the help of ai
    /**
     * Scénario 1 : Données valides - User créé avec succès
     */
    @Test
    public void testCreateUserWithCleanData_Success() {
        // Créer un user sauvegardé avec un ID
        User savedUser = new User(1L, "John", "john@example.com", "0611111111");

        // Configurer le mock pour retourner l'user sauvegardé
        when(userRepository.save(any(User.class))).thenReturn(savedUser);


        //  Préparer les données de test
        UserCreateDto userCreateDto = new UserCreateDto("John", "john@example.com", "0611111111");

        int result = userService.create(userCreateDto);

        // Assert - Vérifier les résultats
        assertEquals(1, result);  // Doit retourner 1 (succès)

        // Vérifier que save() a été appelé une fois
        verify(userRepository, times(1)).save(any(User.class));

        // Vérifier les données sauvegardées
        // ArgumentCaptor capture les arguments passés à une méthode mockée pour les vérifier après.
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());
        User capturedUser = userCaptor.getValue();

        assertEquals("John", capturedUser.getName());
        assertEquals("john@example.com", capturedUser.getEmail());
        assertEquals("0611111111", capturedUser.getPhoneNumber());
    }

    // this  method was done with the help of ai
    /**
     * Scénario 2 : Données invalides - User n'a pas d'ID après sauvegarde
     */
    @Test
    public void testCreateUserWithInvalidData_Failure() {
        // Arrange - Préparer les données
        UserCreateDto userCreateDto = new UserCreateDto("Jane", "jane@example.com", "0622222222");

        // Créer un user sans ID (erreur de sauvegarde)
        User unsavedUser = new User(null, "Jane", "jane@example.com", "0622222222");

        // Configurer le mock pour retourner un user sans ID
        when(userRepository.save(any(User.class))).thenReturn(unsavedUser);

        // appel de la méthode
        int result = userService.create(userCreateDto);

        // Assert - Vérifier les résultats
        assertEquals(0, result);  // Doit retourner 0 (échec)

        // Vérifier que save() a été appelé
        verify(userRepository, times(1)).save(any(User.class));
    }
}