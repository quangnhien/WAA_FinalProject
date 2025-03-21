package waa.miu.finalproject.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import waa.miu.finalproject.entity.Role;
import waa.miu.finalproject.entity.User;
import waa.miu.finalproject.entity.dto.TokenDto;
import waa.miu.finalproject.entity.dto.input.InputUserDto;
import waa.miu.finalproject.entity.dto.output.PropertyDetailDto;
import waa.miu.finalproject.entity.dto.output.PropertyDto;
import waa.miu.finalproject.entity.dto.output.UserDetailDto;
import waa.miu.finalproject.entity.dto.output.UserDto;
import waa.miu.finalproject.entity.dto.output.UserLoginInfo;
import waa.miu.finalproject.entity.request.LoginRequest;
import waa.miu.finalproject.entity.request.RefreshTokenRequest;
import waa.miu.finalproject.entity.request.ResetPasswordRequest;
import waa.miu.finalproject.entity.response.LoginResponse;
import waa.miu.finalproject.enums.OwnerStatusEnum;
import waa.miu.finalproject.enums.RoleEnum;
import waa.miu.finalproject.repository.RoleRepo;
import waa.miu.finalproject.repository.UserRepo;
import waa.miu.finalproject.service.AuthService;
import waa.miu.finalproject.helper.JwtUtil;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;

    @Autowired
    public UserRepo userRepo;

    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private RoleRepo roleRepo;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Override
    public LoginResponse authenticate(LoginRequest loginRequest) {
        Authentication result = null;
        try {
            result = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException(e.getMessage());
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(result.getName());
        Long userId = ((CustomUserDetails) userDetails).getUserId();
        User userData = userRepo.findById(userId).orElse(null);
        final String accessToken = jwtUtil.generateToken(userDetails, userId);
        final String refreshToken = jwtUtil.generateRefreshToken(loginRequest.getEmail());
        UserLoginInfo user = modelMapper.map(userData, UserLoginInfo.class);
        user.setRole(userData.getRoles().get(0).getRole());
        return new LoginResponse(accessToken, refreshToken, user);
    }

    @Override
    public LoginResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
        if (jwtUtil.validateToken(refreshTokenRequest.getRefreshToken())
                && jwtUtil.isRefreshToken(refreshTokenRequest.getRefreshToken())) {
            if (jwtUtil.isTokenExpired(refreshTokenRequest.getAccessToken())) {
                final String accessToken = jwtUtil
                        .doGenerateToken(jwtUtil.getSubject(refreshTokenRequest.getRefreshToken()));
                TokenDto tokenDto = jwtUtil.getUserDtoFromClaims(accessToken);
                User userdata = userRepo.findById(tokenDto.getUserId()).orElse(null);
                UserLoginInfo user = modelMapper.map(userdata, UserLoginInfo.class);
                user.setRole(userdata.getRoles().get(0).getRole());
                return new LoginResponse(accessToken, refreshTokenRequest.getRefreshToken(), user);
            } else {
                log.info("Refresh token expired");
                TokenDto tokenDto = jwtUtil.getUserDtoFromClaims(refreshTokenRequest.getAccessToken());
                User userData = userRepo.findById(tokenDto.getUserId()).orElse(null);
                UserLoginInfo user = modelMapper.map(userData, UserLoginInfo.class);
                user.setRole(userData.getRoles().get(0).getRole());
                return new LoginResponse(refreshTokenRequest.getAccessToken(), refreshTokenRequest.getRefreshToken(),
                        user);
            }
        } else {
            log.warn("Refresh token expired");
            return new LoginResponse();
        }
    }

    @Override
    public UserDetailDto signup(InputUserDto userDto) {
        User user = new User();
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setPhone(userDto.getPhone());
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String hashedPassword = passwordEncoder.encode(userDto.getPassword());
        user.setPassword(hashedPassword);
        Role role = roleRepo.findByRole(userDto.getRole());
        user.setRoles(List.of(role));
        if (userDto.getRole().equals(RoleEnum.OWNER)) {
            user.setStatus(OwnerStatusEnum.DEACTIVATED);
        } else {
            user.setStatus(OwnerStatusEnum.ACTIVE);
        }
        user = userRepo.save(user);
        return modelMapper.map(user, UserDetailDto.class);
    }

    @Override
    public String resetPassword(ResetPasswordRequest resetPasswordRequest) {
        try {
            Authentication result = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(resetPasswordRequest.getEmail(), resetPasswordRequest.getOldPassword())
            );
            UserDetails userDetails = userDetailsService.loadUserByUsername(result.getName());
            Long userId = ((CustomUserDetails) userDetails).getUserId();
            User userData = userRepo.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

            userData.setPassword(passwordEncoder.encode(resetPasswordRequest.getNewPassword()));
            userRepo.save(userData); // Save updated user

            return "Password successfully updated!";
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Invalid old password!");
        }
    }
}
