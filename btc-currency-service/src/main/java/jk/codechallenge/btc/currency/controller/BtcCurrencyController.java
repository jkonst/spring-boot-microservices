package jk.codechallenge.btc.currency.controller;

import jk.codechallenge.btc.currency.model.BtcCurrency;
import jk.codechallenge.btc.currency.service.BtcCurrencyService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/btcCurrency")
public class BtcCurrencyController {

	private final BtcCurrencyService btcCurrencyService;

	public BtcCurrencyController(BtcCurrencyService btcCurrencyService) {
		this.btcCurrencyService = btcCurrencyService;
	}

	@GetMapping("/")
	public BtcCurrency fetchBtcCurrency() {
		return this.btcCurrencyService.fetchBtcCurrency();
	}

}
