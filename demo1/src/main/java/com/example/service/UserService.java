package com.example.service;

import com.example.bean.User;
import com.example.dto.UserCreateDto;
import com.example.dto.UserDto;
import com.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {
    private UserRepository userRepository;
    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;

    }
    public List<UserDto> findAll() {
       return  userRepository.findAll().stream()
               .map(user -> new UserDto(user.getId(),user.getName(), user.getEmail(), user.getPhoneNumber())).sorted((new Comparator<UserDto>() {
                   @Override
                   public int compare(UserDto o1, UserDto o2) {
                       return o1.getId().compareTo(o2.getId());
                   }
               }))
               .collect(Collectors.toList());
    }
    public UserDto findById(Long id) {
        Optional<User> optionalUser=  userRepository.findById(id);
        if (optionalUser.isEmpty()){
            return null ;
        }
        User u = optionalUser.get();
        return new UserDto(u.getId(),u.getName(),u.getEmail(),u.getPhoneNumber());
    }
    public UserDto findById(long id){
        Optional<User> optionalUser=  userRepository.findById(id);
        if (optionalUser.isEmpty()){
            return null ;

        }
        User u = optionalUser.get();
        return new UserDto(u.getId(),u.getName(),u.getEmail(),u.getPhoneNumber());

    }
    public int create(UserCreateDto userCreateDto) {
        User user = new User();
        user.setName(userCreateDto.getName());
        user.setEmail(userCreateDto.getEmail());
        user.setPhoneNumber(userCreateDto.getPhoneNumber());

        User savedUser =userRepository.save(user);
        if (savedUser.getId()==null){
            System.out.println("user hasn't been created : " + savedUser);
            return 0;
        }
            System.out.println("user  been created : " + savedUser);
        return 1;
    }
    public int update(UserCreateDto userCreateDto, Long id) {
        User user = new User();
        user.setName(userCreateDto.getName());
        user.setId(id);
        user.setEmail(userCreateDto.getEmail());
        user.setPhoneNumber(userCreateDto.getPhoneNumber());
        User updatedUser =userRepository.save(user);
        if (updatedUser.getId()==null){
            System.out.println("user hasn't been updated : " + updatedUser);
            return 0;
        }
        System.out.println("user  been updated : " + updatedUser);
        return 1;
    }
}
