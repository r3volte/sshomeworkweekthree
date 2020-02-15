package sshomeworkweekthree.demo.model;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

@Entity
public class VerificationToken extends BaseEntity {

    private String value;

    @OneToOne
    private WebUser webUser;

    public VerificationToken(WebUser appUser, String value) {
        this.webUser = appUser;
        this.value = value;
    }

    public VerificationToken() {
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public WebUser getWebUser() {
        return webUser;
    }

    public void setWebUser(WebUser webUser) {
        this.webUser = webUser;
    }
}
