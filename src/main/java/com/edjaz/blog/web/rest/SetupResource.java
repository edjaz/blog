package com.edjaz.blog.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.edjaz.blog.domain.User;
import com.edjaz.blog.repository.UserRepository;
import com.edjaz.blog.security.AuthoritiesConstants;
import com.edjaz.blog.service.UserService;
import com.edjaz.blog.service.dto.UserDTO;
import com.edjaz.blog.web.rest.errors.EmailAlreadyUsedException;
import com.edjaz.blog.web.rest.errors.LoginAlreadyUsedException;
import com.edjaz.blog.web.rest.util.HeaderUtil;
import com.edjaz.blog.web.rest.vm.ManagedUserVM;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("/api")
public class SetupResource {

    private final Logger log = LoggerFactory.getLogger(SetupResource.class);

    private final UserRepository userRepository;

    private final UserService userService;


    public SetupResource(UserRepository userRepository, UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
    }


    @PreAuthorize("isFirstSetup()")
    @GetMapping("/setup")
    @Timed
    public ResponseEntity<Boolean> getSetup() {
        log.debug("REST request to get is the first setup");
        return ResponseEntity.ok(true);
    }


    @PreAuthorize("isFirstSetup()")
    @GetMapping("/setup/user")
    @Timed
    public ResponseEntity<Long> getIdAdmin() {
        log.debug("REST request to get id admin");
        return ResponseEntity.ok(
            userRepository.findOneByLogin("admin")
                .orElseThrow(() -> new UsernameNotFoundException("User admin not found"))
                .getId()
        );
    }


    @PutMapping("/setup/user")
    @Timed
    @PreAuthorize("isFirstSetup()")
    public ResponseEntity<UserDTO> updateUser(@Valid @RequestBody ManagedUserVM userDTO) {
        log.debug("REST request to update User : {}", userDTO);
        Optional<User> existingUser = userRepository.findOneByEmailIgnoreCase(userDTO.getEmail());
        if (existingUser.isPresent() && (!existingUser.get().getId().equals(userDTO.getId()))) {
            throw new EmailAlreadyUsedException();
        }
        existingUser = userRepository.findOneByLogin(userDTO.getLogin().toLowerCase());
        if (existingUser.isPresent() && (!existingUser.get().getId().equals(userDTO.getId()))) {
            throw new LoginAlreadyUsedException();
        }

        //Met les droits admin par defaut
        userDTO.setAuthorities(Stream.of(AuthoritiesConstants.ADMIN, AuthoritiesConstants.USER).collect(Collectors.toSet()));
        userDTO.setActivated(true);
        Optional<UserDTO> updatedUser = userService.updateUser(userDTO);
        userService.changePasswordForUser(userDTO.getLogin(), userDTO.getPassword());

        return ResponseUtil.wrapOrNotFound(updatedUser,
            HeaderUtil.createAlert("userManagement.updated", userDTO.getLogin()));

    }


}
