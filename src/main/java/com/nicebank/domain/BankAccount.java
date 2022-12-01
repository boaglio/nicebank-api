package com.nicebank.domain;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;

import com.nicebank.exception.NotEnoughBalanceException;

@Entity
public class BankAccount {

	private static final int BALANCE_ACCOUNT_LIMIT = 0;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_id_bank_account")
	@SequenceGenerator(name = "seq_id_bank_account", sequenceName = "seq_id_bank_account", allocationSize = 1)
	private Long accountId;
	
	@OneToOne(optional = true)
	private UserAccount userAccount;
	
	private BigDecimal balance;
	
	public BankAccount() {}
	
	public BankAccount(Long accountId, UserAccount userAccount) {
		super();
		this.accountId = accountId;
		this.userAccount = userAccount;
		this.balance = BigDecimal.ZERO;;
	}

	public void withdraw(double withdrawValue) throws NotEnoughBalanceException  {
		
		BigDecimal withdrawbd = new BigDecimal(withdrawValue);
		BigDecimal balanceValidation = new BigDecimal(balance.doubleValue());
		
		if (balanceValidation.subtract(withdrawbd).doubleValue() < BALANCE_ACCOUNT_LIMIT)
			throw new NotEnoughBalanceException();
		
		balance = balance.subtract(withdrawbd);
	}

	public void deposit(double depositValue)  {
		
		BigDecimal depositbd = new BigDecimal(depositValue);
		balance = balance.add(depositbd);
		
	}

	public Long getAccountId() {
		return accountId;
	}

	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}

	public UserAccount getUserAccount() {
		return userAccount;
	}

	public void setUserAccount(UserAccount userAccount) {
		this.userAccount = userAccount;
	}

	public BigDecimal getBalance() {
		return balance;
	}
	
}
