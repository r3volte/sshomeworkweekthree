package sshomeworkweekthree.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sshomeworkweekthree.demo.model.VerificationToken;
import sshomeworkweekthree.demo.model.WebUser;
import sshomeworkweekthree.demo.repository.VerificationTokenRepo;

import java.util.UUID;

@Service
public class VerificationTokenService {

    private VerificationTokenRepo verificationTokenRepo;

    @Autowired
    public VerificationTokenService(VerificationTokenRepo verificationTokenRepo) {
        this.verificationTokenRepo = verificationTokenRepo;
    }

    public void addToken(WebUser user, String token) {
        verificationTokenRepo.save(new VerificationToken(user, token));
    }

    public VerificationToken findByValue(String value) {
        return verificationTokenRepo.findByValue(value);
    }

    public String initToken() {
        return UUID.randomUUID().toString();
    }

    public void deleteTokenAfterVerify(String token) {
        VerificationToken verificationToken = findByValue(token);
        verificationTokenRepo.delete(verificationToken);
    }
}
