package com.teamsolution.lab.service;

import java.util.UUID;

public interface CartService {

  CartDto getCartByAccountId(UUID accountId);
}
