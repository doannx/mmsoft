package dto;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Implementation class for the User data transfer object.
 * 
 * @author NX.Doan, @version 1.0
 * @date 20160602
 */
public class User {
    /**
     * Generate ID.
     */
    private static final AtomicInteger COUNTER = new AtomicInteger();
    // properties
    private final int id;
    private String name;
    private String email;
    private List<Message> messages;

    // constructor methods
    public User(String name, String email) {
        this.id = COUNTER.getAndIncrement();
        this.name = name;
        this.email = email;
        this.messages = new ArrayList<Message>();
    }

    public User() {
        this.id = COUNTER.getAndIncrement();
    }
    
    // accessor methods
    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}