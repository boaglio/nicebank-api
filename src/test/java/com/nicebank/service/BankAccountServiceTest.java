package com.nicebank.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.nicebank.domain.BankAccount;
import com.nicebank.domain.UserAccount;
import com.nicebank.exception.AccountNotFoundException;
import com.nicebank.exception.InvalidValueException;
import com.nicebank.exception.NotEnoughBalanceException;
import com.nicebank.exception.UserNotFoundException;
import com.nicebank.repository.BankAccountRepository;
import com.nicebank.repository.UserAccountRepository;
 
@ExtendWith(SpringExtension.class)
@SpringBootTest
class BankAccountServiceTest {

	@Autowired
	BankAccountService  service;
	
	@Autowired
	UserAccountRepository userAccountRepository;

	@Autowired
	BankAccountRepository bankAccountRepository;

	// dummy accounts
	UserAccount ua1 = new UserAccount(10001l, "Machado de Assis");
	UserAccount ua2 = new UserAccount(10002l, "Ariano Suassuna");
	UserAccount ua3 = new UserAccount(10003l, "Graciliano Ramos");
	BankAccount ba1 = new BankAccount(50001l,ua1);
	BankAccount ba2 = new BankAccount(50002l,ua2);
	BankAccount ba3 = new BankAccount(50003l,ua3);
	
	@BeforeEach
	void setupAccounts() {
				
		Optional<UserAccount> findUA1 = userAccountRepository.findById(ua1.getId());
		if (findUA1.isEmpty()) userAccountRepository.save(ua1);
		
		Optional<UserAccount> findUA2 = userAccountRepository.findById(ua2.getId());
		if (findUA2.isEmpty()) userAccountRepository.save(ua2);
		
		Optional<UserAccount> findUA3 = userAccountRepository.findById(ua3.getId());
		if (findUA3.isEmpty()) userAccountRepository.save(ua3);
		
		Optional<BankAccount> findBA1 = bankAccountRepository.findById(ba1.getAccountId());
		if (findBA1.isEmpty()) bankAccountRepository.save(ba1);
		
		Optional<BankAccount> findBA2 = bankAccountRepository.findById(ba2.getAccountId());
		if (findBA2.isEmpty()) bankAccountRepository.save(ba2);
		
		Optional<BankAccount> findBA3 = bankAccountRepository.findById(ba3.getAccountId());
		if (findBA3.isEmpty()) bankAccountRepository.save(ba3);
 
	}

	@Test 
	void testUnknownUserAccount() throws Exception {
	
		Exception exception = assertThrows(UserNotFoundException.class, () ->
		service.withdraw("Unknown User Account", ba1.getAccountId(), 100l));
		
	    assertNotNull(exception);
	     
	}
 
	@Test 
	void testUnknownBankAccount() throws Exception {
	
		Exception exception = assertThrows(AccountNotFoundException.class, () ->
		service.withdraw(ua1.getName(), 999999l, 100l));
		
	    assertNotNull(exception);
	     
	}
	
	 
	@Test 
	void testNotEnoughBalance() throws Exception {
		
		Exception exception = assertThrows(NotEnoughBalanceException.class, () ->
		service.withdraw(ua1.getName(),  ba1.getAccountId() , 1000000l));
		
	    assertNotNull(exception);
		     
	}
	
	@Test 
	void testDeposit() throws  Exception {
		
		BigDecimal balance1 = service.deposit(ua1.getName(), ba1.getAccountId(), 100l);
		BigDecimal balance2 = service.deposit(ua2.getName(), ba2.getAccountId(), 100l);
		BigDecimal balance3 = service.deposit(ua3.getName(), ba3.getAccountId(), 100l);
		
		assertThat(balance1.doubleValue()).isPositive();
		assertThat(balance2.doubleValue()).isPositive();
		assertThat(balance3.doubleValue()).isPositive();
	}

	@Test 
	void testWithdraw() throws Exception {
		
		long withdrawValue = 20l;
		Optional<BankAccount> ba1Stored = bankAccountRepository.findById(ba1.getAccountId());
		BigDecimal oldBalance = ba1Stored.get().getBalance();
		BigDecimal newBalance = service.withdraw(ua1.getName(), ba1.getAccountId(), withdrawValue);
		
		assertThat(oldBalance.doubleValue()-withdrawValue).isEqualTo(newBalance.doubleValue());
	}

	@Test 
	void testWithdrawMoreThanExistingBalance() throws Exception {
		
		Optional<BankAccount> ba2Stored = bankAccountRepository.findById(ba2.getAccountId());
 
		double withdrawValue = ba2Stored.get().getBalance().doubleValue()+1;
		  
		Exception exception = assertThrows(NotEnoughBalanceException.class, () ->
		service.withdraw(ua1.getName(), ba2.getAccountId(), withdrawValue));
		 
		assertNotNull(exception);
	}
	
	@Test 
	void testDepositWithInvalidValue() throws Exception {
		
		Exception exception = assertThrows(InvalidValueException.class, () ->
		service.deposit(ua1.getName(), ba1.getAccountId(), -100l));
		 
		assertNotNull(exception);
	}

}