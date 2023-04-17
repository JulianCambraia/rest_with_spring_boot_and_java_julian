package br.com.juliancambraia;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Startup {

    public static void main(String[] args) {
        SpringApplication.run(Startup.class, args);

        // gerando senha Encriptada JWT - poderá ser usada no cadastro de users na hora de setar o password.
        /*Pbkdf2PasswordEncoder pbkdf2Encoder = new Pbkdf2PasswordEncoder("", 8, 185000,
                Pbkdf2PasswordEncoder.SecretKeyFactoryAlgorithm.PBKDF2WithHmacSHA256);
        Map<String, PasswordEncoder> encoders = new HashMap<>();

        encoders.put("pbkdf2", pbkdf2Encoder);
        DelegatingPasswordEncoder passwordEncoder = new DelegatingPasswordEncoder("pbkdf2", encoders);
        passwordEncoder.setDefaultPasswordEncoderForMatches(pbkdf2Encoder);

        String result1 = passwordEncoder.encode("admin123"); // setadas nas migrations
        String result2 = passwordEncoder.encode("admin234");
        System.out.println("My hash result1 " + result1);
        System.out.println("My hash result2 " + result2);*/
    }


}
