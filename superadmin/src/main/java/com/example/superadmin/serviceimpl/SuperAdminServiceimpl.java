package com.example.superadmin.serviceimpl;

import com.example.superadmin.model.SuperAdmin;
import com.example.superadmin.model.request.SuperAdminRequest;
import com.example.superadmin.repository.SuperAdminRepository;
import com.example.superadmin.service.SuperAdminService;
import com.example.superadmin.util.JwtTokenUtil;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class SuperAdminServiceimpl implements SuperAdminService {

    @Autowired
    private SuperAdminRepository superAdminRepository;
    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private OtpService otpService;
    @Value("${spring.mail.username}")
    private String fromEmail;


    @Override
    public Object signUp(SuperAdminRequest superAdminRequest) {

        SuperAdmin superAdmin = new SuperAdmin();
        superAdmin.setSuperAdminId(superAdminRequest.getSuperAdminId());
        superAdmin.setEmail(superAdminRequest.getEmail());
        superAdmin.setFirstName(superAdminRequest.getFirstName());
        superAdmin.setLastName(superAdminRequest.getLastName());
        superAdmin.setGender(superAdminRequest.getGender());
        superAdmin.setMobNumber(superAdminRequest.getMobNumber());
        superAdmin.setPassword(hashPassword(superAdminRequest.getPassword()));
        SuperAdmin superAdmin1 = superAdminRepository.save(superAdmin);
        sendCredentialEmail(superAdminRequest.getEmail(), superAdminRequest.getPassword());
        return "SuperAdmin details are saved";
    }

    @Override
    public Object login(String email, String password) {

        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        UserDetailsImpl userDetails = (UserDetailsImpl) authenticate.getPrincipal();
        SuperAdmin superAdmin = userDetails.getSuperAdmin();
        System.out.println("Email after authentication: " + superAdmin.getEmail());
        System.out.println("Is Authenticated: " + authenticate.isAuthenticated());

        if (superAdminRepository.existsByEmail(email)) {
            SuperAdmin superAdmin1 = superAdminRepository.findByEmail(email).get();
            if (passwordEncoder.matches(password, superAdmin1.getPassword())) {
                sendEmailWithOTP(superAdmin1.getEmail());
                return "Login succesfull";
            } else {
                return "Incorrect Password";
            }
        } else {
            return "Email does not exists";
        }
    }

    @Override
    public Object verifyOtpWithLogin(String email, int otp) {

        if (superAdminRepository.existsByEmail(email)) {
            int cacheOtp = otpService.getOtp(email);
            if (cacheOtp == otp) {
                otpService.clearOTP(email);
                String token = jwtTokenUtil.generateToken(email);
                return ResponseEntity.ok()
                        .header(HttpHeaders.AUTHORIZATION, token)
                        .body("OTP verification succesfull");
            } else {
                return "OTP verification failed due to incorrect otp";
            }
        } else {
            return "Email does not exist";
        }
    }

    public void sendEmailWithOTP(String email) {

        int otp = otpService.generateOTP(email);
        String body = "OTP for verification :" + otp;
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setFrom(fromEmail);
        message.setSubject("OTP VERIFICATION");
        message.setText(body);
        javaMailSender.send(message);
    }

    public void sendCredentialEmail(String email, String password) {

        String body = "Welcome !!!.Your signUp was succesfull " +
                "Your credentials are Email: " + email + " and password: " + password +
                " Thank You";

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setFrom(fromEmail);
        message.setSubject("SignUp: Super Admin");
        message.setText(body);
        javaMailSender.send(message);
    }

    public String hashPassword(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt());
    }

//class ends here
}
