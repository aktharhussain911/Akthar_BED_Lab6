package org.ems.debateregistration.services;

import org.ems.debateregistration.models.User;
import org.ems.debateregistration.web.dto.UserRegistrationDto;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService {

    User save(UserRegistrationDto registrationDto);
}
