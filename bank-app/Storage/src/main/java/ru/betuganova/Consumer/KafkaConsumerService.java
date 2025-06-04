package ru.betuganova.Consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.betuganova.Dao.Entity.BankAccountEvent;
import ru.betuganova.Dao.Entity.ClientEvent;
import ru.betuganova.Dao.Repository.BankAccountEventRepository;
import ru.betuganova.Dao.Repository.ClientEventRepository;

@Service
public class KafkaConsumerService {
    @Autowired
    private ClientEventRepository clientEventRepository;

    @Autowired
    private BankAccountEventRepository accountEventRepository;

    @KafkaListener(topics = "client-topic", groupId = "storage-group")
    public void listenClientEvents(ConsumerRecord<String, String> record) {
        Long clientId = Long.parseLong(record.key());
        String json = record.value();
        clientEventRepository.save(new ClientEvent(null, clientId, json));
    }

    @KafkaListener(topics = "account-topic", groupId = "storage-group")
    public void listenBankAccountEvents(ConsumerRecord<String, String> record) {
        Long accountId = Long.parseLong(record.key());
        String json = record.value();
        accountEventRepository.save(new BankAccountEvent(null, accountId, json));
    }
}
