package controllers;

import models.User;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.query.AuditEntity;
import play.Play;
import play.data.validation.Required;
import play.db.jpa.JPA;
import play.mvc.Controller;

import java.util.*;

public class Application extends Controller {

    public static void index() {
        List<User> users = User.all().fetch();

        Play.configuration.list(System.out);

        render(users);
    }

    public static void showRevisions(String userId) {
        // Retrieve the AuditReader from the EntityManager
        AuditReader reader = AuditReaderFactory.get(JPA.em());

        // Retrieves all the versions of User which id is userId
        List<User> revisions = reader.createQuery().forRevisionsOfEntity(User.class, true, true)
                                                   .add(AuditEntity.id().eq(Long.valueOf(userId)))
                                                   .addOrder(AuditEntity.property("id").asc())
                                                   .getResultList();

        render(revisions);
   }
    
    public static void add(@Required User user) {
        User existingUser = User.find("byLogin", user.login).first();
        if (existingUser != null) {
            existingUser.firstName = user.firstName;
            existingUser.lastName = user.lastName;
            existingUser.save();
        } else {
            user.save();
        }
        index();
    }
}