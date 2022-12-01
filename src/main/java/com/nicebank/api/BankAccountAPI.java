package com.nicebank.api;

import java.math.BigDecimal;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.nicebank.api.dto.BankAccountRequest;
import com.nicebank.api.dto.BankAccountResponse;
import com.nicebank.exception.AccountNotFoundException;
import com.nicebank.exception.InvalidValueException;
import com.nicebank.exception.NotEnoughBalanceException;
import com.nicebank.exception.UserNotFoundException;
import com.nicebank.service.BankAccountService;

@RestController
@RequestMapping("/api")
public class BankAccountAPI {
	
	@Autowired
	private BankAccountService bankAccountService;
	
	@PostMapping("/withdraw")
	public BankAccountResponse withdraw(@Valid @RequestBody BankAccountRequest bankAccountRequest) {
		
		BankAccountResponse response = null;
		try {
			BigDecimal newBalance = bankAccountService.withdraw(bankAccountRequest.getUsername(), bankAccountRequest.getAccountId(), bankAccountRequest.getValue());
			response = new BankAccountResponse("withdraw", bankAccountRequest.getUsername(), bankAccountRequest.getValue(), newBalance.doubleValue());
		} catch (InvalidValueException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Invalid value: "+bankAccountRequest.getValue());
		} catch (UserNotFoundException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"User not found: "+bankAccountRequest.getUsername());
		} catch (AccountNotFoundException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Account not found: "+bankAccountRequest.getAccountId());
		} catch (NotEnoughBalanceException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Not enough balance !");
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal error");
		}
		return response;
 	}
	
	@PostMapping("/deposit")
	public BankAccountResponse deposit(@Valid @RequestBody BankAccountRequest bankAccountRequest) {
		
		BankAccountResponse response = null;
		BigDecimal newBalance;
		try {
			newBalance = bankAccountService.deposit(bankAccountRequest.getUsername(), bankAccountRequest.getAccountId(), bankAccountRequest.getValue());
			response = new BankAccountResponse("deposit", bankAccountRequest.getUsername(), bankAccountRequest.getValue(), newBalance.doubleValue());
		} catch (InvalidValueException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Invalid value: "+bankAccountRequest.getValue());
		} catch (UserNotFoundException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"User not found: "+bankAccountRequest.getUsername());
		} catch (AccountNotFoundException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Account not found: "+bankAccountRequest.getAccountId());
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal error");
		}
		return response;
 	}
	
}
