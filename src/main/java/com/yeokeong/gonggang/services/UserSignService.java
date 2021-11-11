package com.yeokeong.gonggang.services;

import com.yeokeong.gonggang.model.UserAccount;
import com.yeokeong.gonggang.model.entity.User;
import com.yeokeong.gonggang.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserSignService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByUserName(username);

        if(user == null) {
            return null;
        }

        return new UserAccount(user);
    }
}
