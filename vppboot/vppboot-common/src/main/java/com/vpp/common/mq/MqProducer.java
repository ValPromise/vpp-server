package com.vpp.common.mq;

import java.util.Map;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.Session;

import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.command.ActiveMQTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * 消息生产者
 * 
 * @author Lxl
 * @version V1.0 2017年12月22日
 */
@Component
public class MqProducer {

    @Autowired
    private JmsTemplate jmsTemplate;

    // private Queue queue = new ActiveMQQueue("queue");
    //
    // private Topic topic = new ActiveMQTopic("topic");

    public void sendQueue(String queueName, final Map<String, Object> map) {
        MessageCreator msg = new MessageCreator() {
            public Message createMessage(Session session) throws JMSException {
                MapMessage mapMessage = session.createMapMessage();
                for (Map.Entry<String, Object> entry : map.entrySet()) {
                    mapMessage.setObject(entry.getKey(), entry.getValue());
                }
                return mapMessage;
            }
        };

        jmsTemplate.send(new ActiveMQQueue(queueName), msg);
    }

    @Async
    public void sendQueueText(String topicName, final String content) {
        MessageCreator msg = new MessageCreator() {
            public Message createMessage(Session session) throws JMSException {
                return session.createTextMessage(content);
            }
        };
        jmsTemplate.send(new ActiveMQQueue(topicName), msg);
    }

    public void sendTopic(String topicName, final Map<String, Object> map) {
        MessageCreator msg = new MessageCreator() {
            public Message createMessage(Session session) throws JMSException {
                MapMessage mapMessage = session.createMapMessage();
                for (Map.Entry<String, Object> entry : map.entrySet()) {
                    mapMessage.setObject(entry.getKey(), entry.getValue());
                }
                return mapMessage;
            }
        };

        jmsTemplate.send(new ActiveMQTopic(topicName), msg);
    }

    @Async
    public void sendTopicText(String topicName, final String content) {
        MessageCreator msg = new MessageCreator() {
            public Message createMessage(Session session) throws JMSException {
                return session.createTextMessage(content);
            }
        };
        jmsTemplate.send(new ActiveMQTopic(topicName), msg);
    }
}