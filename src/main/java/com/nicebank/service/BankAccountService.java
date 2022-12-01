package com.nicebank.service;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nicebank.domain.BankAccount;
import com.nicebank.domain.UserAccount;
import com.nicebank.exception.AccountNotFoundException;
import com.nicebank.exception.InvalidValueException;
import com.nicebank.exception.NotEnoughBalanceException;
import com.nicebank.exception.UserNotFoundException;
import com.nicebank.repository.BankAccountRepository;
import com.nicebank.repository.UserAccountRepository;

@Service
public class BankAccountService {

	@Autowired
	private BankAccountRepository bankAccountRepository;
	@Autowired
	private UserAccountRepository userAccountRepository;

	@Transactional
	public BigDecimal withdraw(String userAccountName, Long bankAccountId, double withdrawValue)
			throws UserNotFoundException, AccountNotFoundException, NotEnoughBalanceException, InvalidValueException {

		if (withdrawValue<=0) throw new InvalidValueException();
		
		Optional<UserAccount> userAccountOpt = userAccountRepository.findByName(userAccountName);
		if (userAccountOpt.isEmpty())
			throw new UserNotFoundException();

		Optional<BankAccount> bankAccountOpt = bankAccountRepository.findById(bankAccountId);
		if (bankAccountOpt.isEmpty())
			throw new AccountNotFoundException();

		bankAccountOpt.get().withdraw(withdrawValue);		
		bankAccountRepository.save(bankAccountOpt.get());
		
		return bankAccountOpt.get().getBalance();
	}

	@Transactional
	public BigDecimal deposit(String userAccountName, Long bankAccountId, double depositValue)
			throws UserNotFoundException, AccountNotFoundException, InvalidValueException {

		if (depositValue<=0) throw new InvalidValueException();
		
		Optional<UserAccount> userAccountOpt = userAccountRepository.findByName(userAccountName);
		if (userAccountOpt.isEmpty())
			throw new UserNotFoundException();

		Optional<BankAccount> bankAccountOpt = bankAccountRepository.findById(bankAccountId);
		if (bankAccountOpt.isEmpty())
			throw new AccountNotFoundException();

		bankAccountOpt.get().deposit(depositValue);
		bankAccountRepository.save(bankAccountOpt.get());
		
		return bankAccountOpt.get().getBalance();
	}

}
