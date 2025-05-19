package com.example.payment_stab.service;

import com.example.payment_stab.model.AddCardRequest;
import com.example.payment_stab.model.Card;
import com.example.payment_stab.model.PaymentRequest;
import com.example.payment_stab.model.TopUpRequest;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PaymentService {
    private List<Card> cards = new ArrayList<>();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final ResourceLoader resourceLoader;

    @Value("${cards.file.path:cards.json}")
    private String cardsFilePath;

    public PaymentService(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @PostConstruct
    public void init() {
        try {
            System.out.println("Инициализация PaymentService. Путь к файлу: " + cardsFilePath);

            File externalFile = new File(cardsFilePath);

            if (externalFile.exists()) {
                System.out.println("Загрузка данных из внешнего файла");
                cards = objectMapper.readValue(externalFile, new TypeReference<>() {});
                System.out.println("Загружено карт: " + cards.size());
                return;
            }

            System.out.println("Попытка загрузки из ресурсов");
            Resource resource = resourceLoader.getResource("classpath:cards.json");

            if (resource.exists()) {
                System.out.println("Файл ресурсов найден");
                try (InputStream inputStream = resource.getInputStream()) {
                    cards = objectMapper.readValue(inputStream, new TypeReference<>() {});
                    System.out.println("Загружено карт из ресурсов: " + cards.size());

                    Files.createDirectories(Paths.get(cardsFilePath).getParent());

                    objectMapper.writeValue(externalFile, cards);
                    System.out.println("Данные сохранены во внешний файл");
                }
            } else {
                System.out.println("Файл не найден, создаем пустой список карт");
                cards = new ArrayList<>();
                saveCardsToFile();
            }
        } catch (IOException e) {
            System.err.println("Ошибка инициализации: " + e.getMessage());
            throw new RuntimeException("Failed to initialize payment service", e);
        }
    }

    public List<Card> getAllCards() {
        return new ArrayList<>(cards);
    }

    public String addCard(AddCardRequest request) {
        if (findCardByNumber(request.cardNumber()).isPresent()) {
            return "Ошибка: Карта с таким номером уже существует";
        }
        Card newCard = new Card();
        newCard.setCardNumber(request.cardNumber());
        newCard.setCardHolder(request.cardHolder());
        newCard.setExpiryDate(request.expiryDate());
        newCard.setCvv(request.cvv());
        newCard.setBalance(request.balance());

        cards.add(newCard);
        saveCardsToFile();
        return "Карта успешно добавлена. Баланс: " + request.balance();
    }

    public String topUpBalance(TopUpRequest request) {
        Optional<Card> cardOpt = findCardByNumber(request.cardNumber());

        if (cardOpt.isEmpty()) {
            return "Ошибка: Карта не найдена";
        }

        Card card = cardOpt.get();
        card.setBalance(card.getBalance() + request.amount());
        saveCardsToFile();
        return "Баланс успешно пополнен. Новый баланс: " + card.getBalance();
    }

    private Optional<Card> findCardByNumber(String cardNumber) {
        return cards.stream()
                .filter(card -> cardNumber.equals(card.getCardNumber()))
                .findFirst();
    }

    private void saveCardsToFile() {
        try {
            objectMapper.writeValue(new File(cardsFilePath), cards);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save cards data", e);
        }
    }

    public String processPayment(PaymentRequest request) {
        Optional<Card> cardOpt = findCardByNumber(request.cardNumber());
        if (cardOpt.isEmpty()) {
            return "Ошибка: Карта не найдена";
        }
        Card card = cardOpt.get();
        if (!card.getCardHolder().equalsIgnoreCase(request.cardHolder())) {
            return "Ошибка: Неверное имя владельца карты";
        }
        if (!card.getExpiryDate().equals(request.expiryDate())) {
            return "Ошибка: Неверная дата истечения срока";
        }
        if (!card.getCvv().equals(request.cvv())) {
            return "Ошибка: Неверный CVV код";
        }
        if (card.getBalance() < request.amount()) {
            return "Ошибка: Недостаточно средств на карте";
        }

        card.setBalance(card.getBalance() - request.amount());
        saveCardsToFile();
        return "Оплата успешно проведена. Остаток на карте: " + card.getBalance();
    }
}