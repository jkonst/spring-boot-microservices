package jk.codechallenge.btc.currency.service;

import jk.codechallenge.btc.currency.exceptions.BtcCurrencyNotFoundException;
import jk.codechallenge.btc.currency.model.BtcCurrency;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class BtcCurrencyService {

	private final RestTemplate restTemplate;
	private final String BTC_CURRENCY_URL = "http://localhost:5000/btc-price";

	public BtcCurrencyService(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	public BtcCurrency fetchBtcCurrency() throws BtcCurrencyNotFoundException {
		try {
			ResponseEntity<BtcCurrency> entity = restTemplate.getForEntity(BTC_CURRENCY_URL, BtcCurrency.class);
			return entity.getBody();
		} catch (RuntimeException e) {
			throw new BtcCurrencyNotFoundException("Failed to communicate with " + BTC_CURRENCY_URL);
		}
	}

}
