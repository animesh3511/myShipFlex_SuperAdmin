package com.example.user_verification.serviceimpl;

import com.example.user_verification.mapper.MsfCompanyViewMapper;
import com.example.user_verification.model.MsfCompany;
import com.example.user_verification.model.User;
import com.example.user_verification.model.request.MsfCompanyRequest;
import com.example.user_verification.model.request.UserRequest;
import com.example.user_verification.repository.MsfCompanyRepository;
import com.example.user_verification.repository.UserRepository;
import com.example.user_verification.service.MsfCompanyService;
import com.example.user_verification.service.TokenService;
import com.example.user_verification.utils.JwtTokenUtil;
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
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.ArrayList;
import java.util.List;

@Service
public class MsfCompanyServiceImpl implements MsfCompanyService {

    @Autowired
    private MsfCompanyRepository msfCompanyRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OtpService otpService;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private UserToken userToken;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private MsfCompanyViewMapper msfCompanyViewMapper;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JavaMailSender javaMailSender;
    @Value("${spring.mail.username}")
    private String fromEmail;

    @Override
    public Object signUp(MsfCompanyRequest msfCompanyRequest) {

        MsfCompany msfCompany = new MsfCompany();
        msfCompany.setName(msfCompanyRequest.getName());
        if (msfCompanyRepository.existsByEmail(msfCompanyRequest.getEmail())) {
            return "Email already exists";
        } else {
            msfCompany.setEmail(msfCompanyRequest.getEmail());
        }
        msfCompany.setLocation(msfCompanyRequest.getLocation());
        if (isMobNoValid(msfCompanyRequest.getMobNumber())) {
            if (msfCompanyRepository.existsByMobNumber(msfCompanyRequest.getMobNumber())) {
                return "Mobile number already exists";
            } else {
                msfCompany.setMobNumber(msfCompanyRequest.getMobNumber());
            }
        } else {
            return "Mobile Number must be of exactly 10 digits";
        }
        //   msfCompany.setMobNumber(msfCompanyRequest.getMobNumber());
        msfCompany.setPassword(hashPassword(msfCompanyRequest.getPassword()));
        msfCompany.setIsActive(true);
        msfCompany.setIsDeleted(false);
        MsfCompany msfCompany1 = msfCompanyRepository.save(msfCompany);
        msfCompany.setCompanyId(msfCompany1.getId());
        msfCompanyRepository.save(msfCompany);
        sendCredentialEmail(msfCompanyRequest.getEmail(), msfCompanyRequest.getPassword());
        return "Please check your Email";
    }

    @Override
    public Object login(String email, String password) {

        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        UserDetailsImpl userDetails = (UserDetailsImpl) authenticate.getPrincipal();
        MsfCompany msfCompany = userDetails.getMsfCompany();
        System.out.println("Email after authentication: " + msfCompany.getEmail());
        System.out.println("Is Authenticated: " + authenticate.isAuthenticated());

        if (msfCompanyRepository.existsByEmail(email)) {
            MsfCompany msfCompany1 = msfCompanyRepository.findByEmail(email).get();
            if (passwordEncoder.matches(password, msfCompany1.getPassword())) {
                sendEmailWithOTP(msfCompany1.getEmail());
                return "Login succesfull";
            } else {
                return "Incorrect Password";
            }
        } else {
            return "Email does not exists";
        }
    }

    String token;

    public String getToken() {
        return token;
    }

    @Override
    public Object verifyOtpWithLogin(String email, int otp) throws Exception {

        if (msfCompanyRepository.existsByEmail(email)) {
            int cacheOtp = otpService.getOtp(email);
            //MsfCompany msfCompany = msfCompanyRepository.findByEmail(email).get();
            if (cacheOtp == otp) {
                otpService.clearOTP(email);
                //return "OTP verification succesfull JWT Token "+jwtTokenUtil.generateToken(email);
                token = jwtTokenUtil.generateToken(email);
                //Long companyId = userToken.getCompanyFromToken().getCompanyId();
                //tokenService.saveToken(companyId, token);
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

    @Override
    public Object findByCompanyId(Long companyId) {
        if (msfCompanyRepository.existsByCompanyId(companyId)) {
            MsfCompany msfCompany = msfCompanyRepository.findByCompanyId(companyId);
            if (!msfCompany.getIsDeleted()) {
                return msfCompany;
            } else {
                return "Company details are deleted";
            }
        } else {
            return "Company not found";
        }
    }

    @Override
    public Object saveOrUpdateUser(UserRequest userRequest) {

        if (userRepository.existsById(userRequest.getUserId())) {

            User user = userRepository.findById(userRequest.getUserId()).get();
            user.setUserId(userRequest.getUserId());
            user.setIndustry(userRequest.getIndustry());
            user.setLastName(userRequest.getLastName());
            user.setFirstName(userRequest.getFirstName());
            user.setCompanyName(userRequest.getCompanyName());
            user.setCompanyId(userToken.getCompanyFromToken().getCompanyId());
            user.setLocation(userRequest.getLocation());
            List<Long> userId = new ArrayList<>();
            userId.add(userRequest.getUserId());
            if (userRepository.existsByPhoneNumberAndUserIdNotIn(userRequest.getPhoneNumber(), userId)) {
                return "Phone Number already exists";
            } else {
                user.setPhoneNumber(userRequest.getPhoneNumber());
            }
            userRepository.save(user);
            return "User details are updated";
        } else {
            if (msfCompanyRepository.existsByCompanyId(userToken.getCompanyFromToken().getCompanyId())) {

                User user = new User();
                user.setLocation(userRequest.getLocation());
                user.setCompanyId(userToken.getCompanyFromToken().getCompanyId());
                user.setCompanyName(userRequest.getCompanyName());
                user.setFirstName(userRequest.getFirstName());
                user.setLastName(userRequest.getLastName());
                user.setIndustry(userRequest.getIndustry());
                if (isMobNoValid(userRequest.getPhoneNumber())) {
                    if (userRepository.existsByPhoneNumber(userRequest.getPhoneNumber())) {
                        return "Phone number already exists";
                    } else {
                        user.setPhoneNumber(userRequest.getPhoneNumber());
                    }
                } else {
                    return "Phone number must contain exactly 10 digits";
                }
                userRepository.save(user);
            }
            return "User Details are saved";
        }
    }

   /* @Override
    @Cacheable(value = "cacheList", key = "#name + #Email + #Location")
    public Object getUserByNameEmailLocation(String name, String email, String location) {

            if (name.isEmpty()) {
                name = null;
        }
        if (email.isEmpty()) {
            email = null;
        }
        if (location.isEmpty()) {
            location = null;
        }

        System.out.println("name " + name);
        System.out.println("email " + email);
        System.out.println("location " + location);
        List<MsfCompany> userList = userRepository.findByNameEmailLocation(name, email, location);
        return userList;

    }*/

   /* @Override
    @Cacheable(value = "userCache", key = "#location")
    public Object findByLocation(String location) {
        if (userRepository.existsByLocation(location)) {
            List<MsfCompany> userList = userRepository.findByLocation(location);
            return userList;
        } else {
            return userRepository.findAll();
        }
    }*/

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

        String body = "Welcome to Msf Company.Your signUp was succesfull " +
                "Your credentials are Email: " + email + " and password: " + password +
                " Thank You";

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setFrom(fromEmail);
        message.setSubject("SignUp: Msf Company");
        message.setText(body);
        javaMailSender.send(message);
    }

    public MsfCompany findByEmail(String email) {
        return msfCompanyRepository.findByEmail(email).get();
    }

    public Boolean isMobNoValid(String mobNo) {
        String regex = "^[0-9]{10}$";
        return mobNo != null && mobNo.matches(regex);
    }

    public String hashPassword(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt());
    }
//class ends here
}
