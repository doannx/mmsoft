package dao;

import java.util.LinkedHashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import dto.Message;
import dto.User;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

/**
 * Implementation class for the DatabaseMock service.
 * 
 * @author NX.Doan, @version 1.0
 * @date 20160602
 */
public class DatabaseMock extends AbstractVerticle {
    /**
     * Store the simulator users.
     */
    private Map<Integer, User> users = new LinkedHashMap<>();
    /**
     * Log.
     */
    private final Logger log = LoggerFactory.getLogger(getClass());

    /**
     * Start the service.
     */
    @Override
    public void start() throws Exception {
        this.initializeUserData();
        getVertx().eventBus().consumer("ping-pong", message -> {
            log.info(String.format("call function: %s()", message.body()));
            JsonObject jParam = (JsonObject) message.body();
            if ("getAll".equals(jParam.getString("action"))) {
                message.reply(this.getAllUsers());
            } else if ("addMessageById".equals(jParam.getString("action"))) {
                this.addMessageById(jParam.getString("id"), jParam.getString("title"), jParam.getString("content"));
            } else {
                message.reply(this.getMessagesById(jParam.getString("id")));
            }
        }).completionHandler(event -> {
            if (event.succeeded()) {
                log.info("complete handler");
            } else {
                log.info("failed");
            }
        });
        log.info("DatabaseMock started");
    }

    /**
     * Initialize some users.
     */
    private void initializeUserData() {
        User u1 = new User("nx.doan", "doannx@gmail.com");
        Message mess1 = new Message("title1", "content1");
        u1.getMessages().add(mess1);
        this.users.put(u1.getId(), u1);

        User u2 = new User("nk.thy", "thykt@gmail.com");
        Message mess2 = new Message("title1", "content1");
        u1.getMessages().add(mess2);
        this.users.put(u2.getId(), u2);

        User u3 = new User("mmsoft", "mmsoft@gmail.com");
        Message mess3 = new Message("title1", "content1");
        u1.getMessages().add(mess3);
        this.users.put(u3.getId(), u3);
    }

    /**
     * Get all users.
     * 
     * @return JSON string stands for list of users.
     */
    private String getAllUsers() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        try {
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(users.values());
        } catch (JsonProcessingException e) {
            return users.values().toString();
        }
    }

    /**
     * Add once more message to specific [user].
     * 
     * @param id user id
     * @param title
     * @param content
     */
    private void addMessageById(String id, String title, String content) {
        Message newMess = new Message(title, content);
        this.users.get(id).getMessages().add(newMess);
    }

    /**
     * Get messages from specific [user].
     * 
     * @param id user id
     * @return JSON string stands for message list
     */
    private String getMessagesById(String id) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        try {
            return mapper.writerWithDefaultPrettyPrinter()
                    .writeValueAsString(this.users.get(Integer.parseInt(id)).getMessages());
        } catch (JsonProcessingException e) {
            return this.users.get(id).getMessages().toString();
        }
    }
}
