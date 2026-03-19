package com.teamsolution.lab.service;

import com.teamsolution.lab.dto.CartDto;
import java.util.UUID;

public interface CartService {

  CartDto getCartByAccountId(UUID accountId);
}
