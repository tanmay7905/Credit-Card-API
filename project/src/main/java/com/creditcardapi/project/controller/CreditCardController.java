package com.shepherdmoney.interviewproject.controller;

import com.shepherdmoney.interviewproject.model.CreditCard;
import com.shepherdmoney.interviewproject.repository.CreditCardRepository;
import com.shepherdmoney.interviewproject.repository.UserRepository;
import com.shepherdmoney.interviewproject.vo.request.AddCreditCardToUserPayload;
import com.shepherdmoney.interviewproject.vo.request.UpdateBalancePayload;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.shepherdmoney.interviewproject.model.User;
import com.shepherdmoney.interviewproject.model.BalanceHistory;
import com.shepherdmoney.interviewproject.repository.CreditCardRepository;
import com.shepherdmoney.interviewproject.repository.UserRepository;
import com.shepherdmoney.interviewproject.vo.response.CreditCardView;


@RestController
public class CreditCardController {

    // TODO: wire in CreditCard repository here (~1 line)
    private final CreditCardRepository creditCardRepository;
    private final UserRepository userRepository;

    public CreditCardController(CreditCardRepository creditCardRepository, UserRepository userRepository) {
        this.creditCardRepository = creditCardRepository;
        this.userRepository = userRepository;
    }

    @PostMapping("/credit-card")
    public ResponseEntity<Integer> addCreditCardToUser(@RequestBody AddCreditCardToUserPayload payload) {
        //       Create a credit card entity, and then associate that credit card with user with given userId
        //       Return 200 OK with the credit card id if the user exists and credit card is successfully associated with the user
        //       Return other appropriate response code for other exception cases
        //       Do not worry about validating the card number, assume card number could be any arbitrary format and length
        
        // Retrieve the user from the repository based on the provided userId
        User user = userRepository.findById(payload.getUserId()).orElse(null);

        // Check if the user exists
        if (user == null) {
            // Return a bad request response if the user is not found
            return ResponseEntity.badRequest().build();
        }

        // Create a new CreditCard object
        CreditCard creditCard = new CreditCard();
        creditCard.setIssuanceBank(payload.getCardIssuanceBank());
        creditCard.setNumber(payload.getCardNumber());
        creditCard.setOwner(user);

        // Save the newly created credit card to the repository
        creditCardRepository.save(creditCard);

        // Return a response with the ID of the newly created credit card
        return ResponseEntity.ok(creditCard.getId());
    }
        

    @GetMapping("/credit-card:all")
    public ResponseEntity<List<CreditCardView>> getAllCardOfUser(@RequestParam int userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return ResponseEntity.badRequest().build();
        }

        List<CreditCardView> creditCardViews = user.getCreditCards().stream()
                .map(creditCard -> new CreditCardView(creditCard.getId(), creditCard.getIssuanceBank(), creditCard.getNumber()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(creditCardViews);
    }

    @GetMapping("/credit-card:user-id")
    public ResponseEntity<Integer> getUserIdForCreditCard(@RequestParam String creditCardNumber) {
        // Convert credit card number from String to Integer
        Integer int_creditCardNumber = Integer.parseInt(creditCardNumber);

        // Retrieve the credit card associated with the given credit card number
        Optional<CreditCard> optionalCreditCard = creditCardRepository.findById(int_creditCardNumber);

        // Check if the credit card exists
        if (optionalCreditCard.isEmpty()) {
            // Return a bad request response if the credit card is not found
            return ResponseEntity.badRequest().build();
        }

        // Get the credit card object
        CreditCard creditCard = optionalCreditCard.get();

        // Return a response with the owner's ID associated with the credit card
        return ResponseEntity.ok(creditCard.getOwner().getId());
    }

    @PostMapping("/credit-card:update-balance")
    public ResponseEntity<Void> postMethodName(@RequestBody UpdateBalancePayload[] payload) {
        //    Given a list of transactions, update credit cards' balance history.
        //      1. For the balance history in the credit card
        //      2. If there are gaps between two balance dates, fill the empty date with the balance of the previous date
        //      3. Given the payload `payload`, calculate the balance different between the payload and the actual balance stored in the database
        //      4. If the different is not 0, update all the following budget with the difference
        //      For example: if today is 4/12, a credit card's balanceHistory is [{date: 4/12, balance: 110}, {date: 4/10, balance: 100}],
        //      Given a balance amount of {date: 4/11, amount: 110}, the new balanceHistory is
        //      [{date: 4/12, balance: 120}, {date: 4/11, balance: 110}, {date: 4/10, balance: 100}]
        //      Return 200 OK if update is done and successful, 400 Bad Request if the given card number
        //        is not associated with a card.
    
       // Iterate over the list of transactions in the payload
        for (UpdateBalancePayload transaction : payload) {
            // Retrieve the credit card associated with the transaction from the repository
            Optional<CreditCard> optionalCreditCard = creditCardRepository.findById(Integer.parseInt(transaction.getCreditCardNumber()));
            
            // Check if the credit card exists
            if (!optionalCreditCard.isPresent()) {
                // Return a bad request response if the credit card is not found
                return ResponseEntity.badRequest().build();
            }
            
            // Get the credit card object
            CreditCard creditCard = optionalCreditCard.get();
            
            // Extract the transaction date and amount from the payload
            LocalDate transactionDate = transaction.getBalanceDate();
            double transactionAmount = transaction.getBalanceAmount();

            // Get the balance history of the credit card
            List<BalanceHistory> balanceHistory = creditCard.getBalanceHistory();
            
            // Initialize an index for inserting the transaction into the sorted list
            int index = 0;
            
            // Find the correct position to insert the transaction based on the transaction date
            while (index < balanceHistory.size() && balanceHistory.get(index).getDate().isAfter(transactionDate)) {
                index++;
            }

            // If the transaction date is not found in the balance history, add a new entry
            if (index == balanceHistory.size() || !balanceHistory.get(index).getDate().isEqual(transactionDate)) {
                // Create a new balance history entry for the transaction
                BalanceHistory newBalanceHistory = new BalanceHistory();
                newBalanceHistory.setDate(transactionDate);
                newBalanceHistory.setBalance(transactionAmount);
                
                // Insert the new balance history entry at the correct index
                balanceHistory.add(index, newBalanceHistory);
            }

            // Calculate the difference between the transaction amount and the existing balance
            double difference = transactionAmount - balanceHistory.get(index).getBalance();
            
            // Update the balances starting from the index where the transaction was inserted
            for (int i = index; i < balanceHistory.size(); i++) {
                balanceHistory.get(i).setBalance(balanceHistory.get(i).getBalance() + difference);
            }
        }

        // Save all credit cards with updated balance history
        creditCardRepository.saveAll(creditCardRepository.findAll());

        // Return a success response
        return ResponseEntity.ok().build();
    }
}
