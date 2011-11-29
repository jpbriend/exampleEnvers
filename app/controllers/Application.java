package controllers;

import models.User;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.query.AuditEntity;
import org.hibernate.envers.query.AuditQuery;
import play.Logger;
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
       User revision1 = null;
       AuditReader reader = AuditReaderFactory.get(JPA.em());
       List<Number> revs = reader.getRevisions(User.class, Long.valueOf(userId));

       if (revs.size() >= 1) {
           revision1 = (User) reader.createQuery().forEntitiesAtRevision(User.class, revs.get(0)).getSingleResult();
       }

       render(revision1);
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