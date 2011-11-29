package models;

import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import play.db.jpa.Model;

import javax.persistence.Entity;

@Entity
@Audited
public class User extends Model {

    public String firstName;

    public String lastName;

    public String login;

    @NotAudited
    public String password;
}
