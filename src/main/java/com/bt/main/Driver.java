package com.bt.main;

import com.bt.dao.UserRoleDao;
import com.bt.dao.UserRoleDaoImpl;
import com.bt.domain.User;
import com.bt.domain.UserRole;
import com.bt.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class Driver {

    public static void main(String[] args){
//        init();
        testURDao();

    }
    static void init() {

        Session s = HibernateUtil.getSession();
        Transaction tx = s.beginTransaction();

		/* create transient objects */
        UserRole ur1 = new UserRole("Admin");
        UserRole ur2 = new UserRole("User");

        User u1 = new User("Admin","password","John","Doe","Admin@Somewhere.com",ur1);
        User u2 = new User("User","password","Mary","Sue","User@Somewhere.com",ur2);


        System.out.println(s.save(ur1));
        System.out.println(s.save(ur2));
        System.out.println(s.save(u1));
        System.out.println(s.save(u2));

        tx.commit();
        s.close();

    }
    static void testURDao(){
        UserRoleDao urDao = new UserRoleDaoImpl();
        for(UserRole ur: urDao.getUserRoles()){
            System.out.println("hello");
            for(User x : ur.getMembers()){
                System.out.println(x.toString());
            }
        }
    }

}
