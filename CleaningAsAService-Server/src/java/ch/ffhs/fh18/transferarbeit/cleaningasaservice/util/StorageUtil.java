/*
 * Fernfachhochschule Schweiz  
 * Transferarbeit Innovationen & Technologien
 * Cleaning as a Service
 * 2018
 */
package ch.ffhs.fh18.transferarbeit.cleaningasaservice.util;

import ch.ffhs.fh18.transferarbeit.cleaningasaservice.model.Account;
import ch.ffhs.fh18.transferarbeit.cleaningasaservice.model.Customer;
import ch.ffhs.fh18.transferarbeit.cleaningasaservice.model.Employee;
import ch.ffhs.fh18.transferarbeit.cleaningasaservice.model.Login;
import ch.ffhs.fh18.transferarbeit.cleaningasaservice.model.MyOrder;
import ch.ffhs.fh18.transferarbeit.cleaningasaservice.model.Person;
import ch.ffhs.fh18.transferarbeit.cleaningasaservice.model.AccountRole;
import ch.ffhs.fh18.transferarbeit.cleaningasaservice.model.MyKey;
import ch.ffhs.fh18.transferarbeit.cleaningasaservice.model.Stamp;
import ch.ffhs.fh18.transferarbeit.cleaningasaservice.model.Work;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

/**
 *
 * @author Djan Sarwari
 */
public class StorageUtil {

    public static Account getAccountByCredentials(EntityManager entityManager, String email, String password) {
        if (email == null) {
            return null;
        }
        Account ac = null;

        TypedQuery<Account> query = entityManager.createQuery(
                "SELECT a FROM Account a "
                + "WHERE a.email= :email "
                + "AND a.password = :password",
                Account.class);
        query.setParameter("email", email);
        String encryptedPassword = EncryptedPassword.forRawPassword(password).getEncrypted();
        query.setParameter("password", encryptedPassword);
        List<Account> list = query.getResultList();
        if (list != null && list.size() > 0) {
            ac = list.get(0);
        }
        return ac;
    }

    public static Account getAccountById(EntityManager entityManager, String id) {
        if (id == null) {
            return null;
        }
        Account ac = null;

        TypedQuery<Account> query = entityManager.createQuery(
                "SELECT a FROM Account a "
                + "WHERE a.id= :id ",
                Account.class);
        query.setParameter("id", id);
        List<Account> list = query.getResultList();
        if (list != null && list.size() > 0) {
            ac = list.get(0);
        }
        return ac;
    }

    public static Account getAccountByToken(EntityManager entityManager, String token) {

        Login login = getLoginByToken(entityManager, token);

        if (login == null) {
            return null;
        }

        TypedQuery<Account> query = entityManager.createQuery(
                "SELECT a FROM Account a "
                + "WHERE a.id= :id ",
                Account.class);
        query.setParameter("id", login.getAccountId());

        List<Account> list = query.getResultList();
        Account ac = null;
        if (list != null && list.size() > 0) {
            ac = list.get(0);
        }

        return ac;
    }

    public static Account getAccountIfCustomerOrAdmin(EntityManager entityManager, String token) {
        Account ac = getAccountByToken(entityManager, token);
        if (ac.getAccountRole() == AccountRole.ADMIN || ac.getAccountRole() == AccountRole.CUSTOMER) {
            return ac;
        }
        return null;
    }

    public static boolean isAvailable(EntityManager entityManager, String email) {
        if (email == null) {
            return false;
        }
        Account ac = null;

        TypedQuery<Account> query = entityManager.createQuery(
                "SELECT a FROM Account a "
                + "WHERE a.email= :email ",
                Account.class);
        query.setParameter("email", email);
        List<Account> list = query.getResultList();
        if (list != null && list.size() > 0) {
            ac = list.get(0);
        }
        return ac == null;
    }

    public static Login getLoginByToken(EntityManager entityManager, String token) {
        if (token == null) {
            return null;
        }
        Login login = null;

        TypedQuery<Login> query = entityManager.createQuery(
                "SELECT l FROM Login l "
                + "WHERE l.token= :token ",
                Login.class);
        query.setParameter("token", token);

        List<Login> list = query.getResultList();
        if (list != null && list.size() > 0) {
            login = list.get(0);
        }
        return login;
    }

    public static boolean isAdmin(EntityManager entityManager, String token) {
        Account ac = getAccountByToken(entityManager, token);
        return ac != null && ac.getAccountRole() == AccountRole.ADMIN;
    }

    public static Customer findCustomerById(EntityManager entityManager, String customerId) {
        Customer c = null;
        TypedQuery<Customer> query = entityManager.createQuery(
                "SELECT c FROM Customer c "
                + "WHERE c.id= :id ",
                Customer.class);
        query.setParameter("id", customerId);

        List<Customer> list = query.getResultList();
        if (list != null && list.size() > 0) {
            c = list.get(0);
        }
        return c;
    }

    public static Employee findEmployeeById(EntityManager entityManager, String employeeId) {
        Employee e = null;
        TypedQuery<Employee> query = entityManager.createQuery(
                "SELECT e FROM Employee e "
                + "WHERE e.id= :id ",
                Employee.class);
        query.setParameter("id", employeeId);

        List<Employee> list = query.getResultList();
        if (list != null && list.size() > 0) {
            e = list.get(0);
        }
        return e;
    }

    public static MyOrder findOrderById(EntityManager entityManager, String orderId) {
        MyOrder o = null;

        TypedQuery<MyOrder> query = entityManager.createQuery(
                "SELECT o FROM MyOrder o "
                + "WHERE o.id= :id ",
                MyOrder.class);
        query.setParameter("id", orderId);

        List<MyOrder> list = query.getResultList();
        if (list != null && list.size() > 0) {
            o = list.get(0);
        }
        return o;
    }

    public static boolean isAdminOrCustomer(EntityManager entityManager, String token) {
        return getAccountIfCustomerOrAdmin(entityManager, token) != null;
    }

    public static Person getPersonByLogin(EntityManager entityManager, Login login) {
        Person p = null;
        TypedQuery<Person> query = entityManager.createQuery(
                "SELECT p FROM Person p "
                + "WHERE p.account.id= :acid ",
                Person.class);
        query.setParameter("acid", login.getAccountId());

        List<Person> list = query.getResultList();

        if (list != null && list.size() > 0) {
            p = list.get(0);
        }

        return p;
    }

    public static Person getPersonByAccount(EntityManager entityManager, Account account) {
        Person p = null;
        TypedQuery<Person> query = entityManager.createQuery(
                "SELECT p FROM Person p "
                + "WHERE p.account.id= :acid ",
                Person.class);
        query.setParameter("acid", account.getId());

        List<Person> list = query.getResultList();

        if (list != null && list.size() > 0) {
            p = list.get(0);
        }

        return p;
    }

    public static Employee getEmployeeByLogin(EntityManager entityManager, Login login) {
        Employee e = null;
        Person p = getPersonByLogin(entityManager, login);
        if (p == null) {
            return null;
        }
        TypedQuery<Employee> query = entityManager.createQuery(
                "SELECT e FROM Employee e "
                + "WHERE e.person.id= :pid ",
                Employee.class);
        query.setParameter("pid", p.getId());

        List<Employee> list = query.getResultList();

        if (list != null && list.size() > 0) {
            e = list.get(0);
        }
        return e;
    }

    public static Stamp getOpenStamp(EntityManager entityManager, Employee employee) {
        Stamp st = null;
        TypedQuery<Stamp> query = entityManager.createQuery(
                "SELECT s FROM Stamp s "
                + "WHERE s.employee.id= :eid "
                + "AND s.endTime = null",
                Stamp.class);
        query.setParameter("eid", employee.getId());

        List<Stamp> list = query.getResultList();

        if (list != null && list.size() > 0) {
            st = list.get(0);
        }
        return st;
    }

    public static List<Stamp> getStamps(EntityManager entityManager, Employee employee) {
        TypedQuery<Stamp> query = entityManager.createQuery(
                "SELECT s FROM Stamp s "
                + "WHERE s.employee.id= :eid ",
                Stamp.class);
        query.setParameter("eid", employee.getId());

        List<Stamp> list = query.getResultList();

        return list;
    }

    public static List<MyOrder> getMyOrders(EntityManager entityManager, Customer customer) {
        TypedQuery<MyOrder> query = entityManager.createQuery(
                "SELECT o FROM MyOrder o "
                + "WHERE o.customer.id= :cid ",
                MyOrder.class);
        query.setParameter("cid", customer.getId());

        List<MyOrder> list = query.getResultList();

        return list;
    }

    public static List<MyKey> getKeys(EntityManager entityManager, Customer customer) {
        TypedQuery<MyKey> query = entityManager.createQuery(
                "SELECT k FROM MyKey k "
                + "WHERE k.customer.id= :cid ",
                MyKey.class);
        query.setParameter("cid", customer.getId());

        List<MyKey> list = query.getResultList();

        return list;
    }

    public static List<Work> getWorks(EntityManager entityManager, Employee employee) {
        TypedQuery<Work> query = entityManager.createQuery(
                "SELECT w FROM Work w "
                + "WHERE w.employee.id= :eid ",
                Work.class);
        query.setParameter("eid", employee.getId());

        List<Work> list = query.getResultList();

        return list;
    }

    public static Customer getCustomerByToken(EntityManager entityManager, String token) {
        Customer c = null;
        Login login = getLoginByToken(entityManager, token);

        if (login == null) {
            return null;
        }

        Person p = getPersonByLogin(entityManager, login);
        if (p == null) {
            return null;
        }

        TypedQuery<Customer> query = entityManager.createQuery(
                "SELECT c FROM Customer c "
                + "WHERE c.person.id= :pid ",
                Customer.class);
        query.setParameter("pid", p.getId());

        List<Customer> list = query.getResultList();

        if (list != null && list.size() > 0) {
            c = list.get(0);
        }

        return c;
    }

    public static Customer getCustomerByPerson(EntityManager entityManager, Person person) {
        Customer c = null;

        TypedQuery<Customer> query = entityManager.createQuery(
                "SELECT c FROM Customer c "
                + "WHERE c.person.id= :id ",
                Customer.class);
        query.setParameter("id", person.getId());

        List<Customer> list = query.getResultList();
        if (list != null && list.size() > 0) {
            c = list.get(0);
        }
        return c;
    }

    public static Customer getCustomerByAccountId(EntityManager entityManager, String accountId) {
        Account ac = getAccountById(entityManager, accountId);

        if (ac == null) {
            return null;
        }
        Person p = getPersonByAccount(entityManager, ac);
        if (p == null) {
            return null;
        }

        return getCustomerByPerson(entityManager, p);
    }

    public static Customer getCustomerByAccount(EntityManager entityManager, Account account) {

        if (account == null) {
            return null;
        }
        Person p = getPersonByAccount(entityManager, account);
        if (p == null) {
            return null;
        }

        return getCustomerByPerson(entityManager, p);
    }

}
