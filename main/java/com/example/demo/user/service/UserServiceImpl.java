package com.example.demo.user.service;


import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserRepository;
import com.example.demo.user.dto.SignupReq;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

import static com.example.demo.utils.ValidationRegex.isRegexNickName;
import static com.example.demo.utils.ValidationRegex.isRegexPassword;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    public void register(SignupReq signupReq) throws Exception {

        if (!isRegexNickName(signupReq.getNickname())){
            throw new Exception("닉네임은 최소 3자 이상, 알파벳 대소문자(a~z, A~Z), 숫자(0~9)로 구성하기");

        }
        if (!isRegexPassword(signupReq.getPassword())){
            throw new Exception("비밀번호는 최소 4자 이상");

        }
        if(signupReq.getPassword().contains(signupReq.getNickname())){
            throw new Exception("비밀번호는 닉네임과 같은 값이 포함되어서는 안된다");

        }



        //중복확인
        Optional<User> found = userRepository.findByUserId(signupReq.getUser_id());
        if (found.isPresent()) {
            throw new IllegalArgumentException("중복된 사용자 ID 가 존재합니다.");
        }

        //new AES128(Secret.USER_INFO_PASSWORD_KEY)
        User user = User.builder()
                        .userId(signupReq.getUser_id())
                        .nickname(signupReq.getNickname())
                        .userPwd(passwordEncoder.encode(signupReq.getPassword()))
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .build();
        userRepository.save(user);
    }

//    public void Login(LoginReq loginReq) throws BaseException {
//        Optional<User> user = userRepository.findByUserId(loginReq.getUserId());
//        if (user == null){
//            throw new BaseException(CANNOT_FIND_USERID);
//        }
//        if (!passwordEncoder.matches(loginReq.getPassword(), user.get().getUserPwd())){
//            throw new BaseException(CANNOT_FIND_PASSWORD);
//        }
//    }
}
