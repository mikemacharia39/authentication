package com.mikehenry.authentication.domain.service;

import java.time.Instant;
import java.util.Optional;

import com.mikehenry.authentication.api.dto.ForgotPasswordRequest;
import com.mikehenry.authentication.api.dto.InvitationRequest;
import com.mikehenry.authentication.api.dto.InvitationResponse;
import com.mikehenry.authentication.api.dto.ResetPasswordRequest;
import com.mikehenry.authentication.domain.entity.User;
import com.mikehenry.authentication.domain.entity.UserCode;
import com.mikehenry.authentication.domain.repository.UserCodeRepository;
import com.mikehenry.authentication.domain.repository.UserRepository;
import com.mikehenry.authentication.domain.util.RandomReferenceGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserCodeRepository userCodeRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public InvitationResponse inviteUser(final InvitationRequest invitationRequest) {
        final Optional<User> user = userRepository.findByEmail(invitationRequest.email());

        if (user.isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User with email " + invitationRequest.email() + " already exists.");
        } else {
            final String invitationCode = RandomReferenceGenerator.generateReference();
            final String defaultPassword = passwordEncoder.encode(invitationCode);

            final User newUser = User.builder()
                    .email(invitationRequest.email())
                    .fullName(invitationRequest.fullName())
                    .password(defaultPassword)
                    .hasVerifiedEmail(false)
                    .build();

            final User savedUser = userRepository.save(newUser);

            createUserCode(savedUser, invitationCode);

            log.info("Invited new user with email: {} and invitation code: {}", invitationRequest.email(), invitationCode);
            return new InvitationResponse(
                    invitationRequest.email(),
                    invitationCode,
                    "Invitation sent successfully. Use the invitation code to reset your password."
            );
        }
    }

    @Transactional
    public void forgotPassword(final ForgotPasswordRequest forgotPasswordRequest) {
        final User user = findUser(forgotPasswordRequest.email());

        final String resetCode = RandomReferenceGenerator.generateReference();

        createUserCode(user, resetCode);

        log.info("Password reset code generated for user with email: {}. Reset code: {}", forgotPasswordRequest.email(), resetCode);
    }

    @Transactional
    public void resetPassword(final ResetPasswordRequest resetPasswordRequest) {
        final User user = findUser(resetPasswordRequest.email());

        verifyInvitationCode(resetPasswordRequest.email(), resetPasswordRequest.resetCode());

        final String newPassword = passwordEncoder.encode(resetPasswordRequest.newPassword());

        user.updatePassword(newPassword);

        userRepository.save(user);

        log.info("Password reset successfully for user with email: {}", resetPasswordRequest.email());
    }

    private User findUser(final String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User with email " + email + " not found."));
    }

    private void createUserCode(final User user, final String code) {
        final UserCode userCode = UserCode.builder()
                .user(user)
                .code(code)
                .build();
        userCodeRepository.save(userCode);
    }

    private void verifyInvitationCode(final String email, final String code) {
        final UserCode userCode = userCodeRepository.findLatestUserCode(email, code)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid invitation code."));

        if (Instant.now().isAfter(userCode.getExpiresAt())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invitation code has expired.");
        }

        if (userCode.isUsed()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invitation code has already been used.");
        }

        userCode.markAsUsed();
        userCodeRepository.save(userCode);
    }
}
