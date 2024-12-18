package com.example.demo1.service.item;

import com.example.demo1.domain.item.Item;
import com.example.demo1.domain.item.ItemCart;
import com.example.demo1.domain.user.User;
import com.example.demo1.dto.item.ItemCartAddDto;
import com.example.demo1.dto.item.ItemCartEaUpdateDto;
import com.example.demo1.dto.item.ItemCartStatusUpdateDto;
import com.example.demo1.exception.Item.item.ItemCartNotFoundException;
import com.example.demo1.exception.Item.item.ItemNotFoundException;
import com.example.demo1.repository.item.ItemCartRepository;
import com.example.demo1.repository.item.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.example.demo1.util.constant.Constants.*;

@Service
@RequiredArgsConstructor
@Transactional
public class ItemCartService {

    private final ItemCartRepository itemCartRepository;
    private final ItemRepository itemRepository;

    public ItemCart save(ItemCartAddDto dto, User user) {
        // 이미 있으면 수량 추가
        Optional<ItemCart> findItemCart = itemCartRepository.findByUserIdAndItemIdxAndStatusZero(user.getId(), dto.getItemIdx());
        if (findItemCart.isPresent()) {
            ItemCart itemCart = findItemCart.get();
            itemCart.updateEa(itemCart.getEa() + dto.getEa());
            return itemCart;
        }

        Item item = itemRepository.findById(dto.getItemIdx())
                .orElseThrow(ItemNotFoundException::new);
        ItemCart itemCart = ItemCart.builder()
                .user(user)
                .item(item)
                .ea(dto.getEa())
                .build();
        return itemCartRepository.save(itemCart);
    }

    public ItemCart updateEa(ItemCartEaUpdateDto dto, User user) {
        // 유저검증

        ItemCart itemCart = itemCartRepository.findById(dto.getCartIdx())
                .orElseThrow(ItemNotFoundException::new);
        itemCart.updateEa(dto.getEa());
        return itemCart;
    }

    // 장바구니 불러오기
    public List<ItemCart> getCart(User user) {
        return itemCartRepository.findByUserAndStatusIsLike(user, CART_ADD)
                .orElseThrow(() -> new ItemCartNotFoundException("장바구니를 찾을 수 없습니다."));
    }

    public void deleteCart(Long cartIdx, User user) {

        ItemCart itemCart = itemCartRepository.findById(cartIdx)
                .orElseThrow(ItemNotFoundException::new);
        itemCart.updateStatus(CART_DELETED);
    }

    public void orderCart(Long itemCartIdx, User user) {

        ItemCart itemCart = itemCartRepository.findById(itemCartIdx)
                .orElseThrow(ItemNotFoundException::new);
        itemCart.updateStatus(CART_COMP);
    }

    public void emptyCart(User user) {
        List<ItemCart> itemCarts = itemCartRepository.findByUserAndStatusIsLike(user, CART_ADD)
                .orElseThrow(() -> new ItemCartNotFoundException("장바구니를 찾을 수 없습니다."));
        for (ItemCart itemCart : itemCarts) {
            itemCart.updateStatus(CART_DELETED);
        }
    }
}
