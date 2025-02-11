package az.developia.shop.controller;

import az.developia.shop.entity.BasketItem;
import az.developia.shop.service.BasketService;
import az.developia.shop.security.JwtUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/basket")
public class BasketController {
@Autowired
    private  BasketService basketService;
@Autowired
    private  JwtUtil jwtUtil;

    public BasketController(BasketService basketService, JwtUtil jwtUtil) {
        this.basketService = basketService;
        this.jwtUtil = jwtUtil;
    }

    private Long getUserIdFromToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName(); // Получаем username (обычно email)
        return jwtUtil.getUserIdFromUsername(username);
    }

    @GetMapping
    public List<BasketItem> getBasket() {
        Long userId = getUserIdFromToken();
        return basketService.getBasketItems(userId);
    }

    @PostMapping("/add")
    public void addToBasket(@RequestParam Long productId, @RequestParam int quantity) {
        Long userId = getUserIdFromToken();
        basketService.addProductToBasket(userId, productId, quantity);
    }

    @DeleteMapping("/clear")
    public void clearBasket() {
        Long userId = getUserIdFromToken();
        basketService.clearBasket(userId);
    }
}
