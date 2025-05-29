package com.devstat.blog.utility;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.EntityPathBase;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class QueryDslUtils {

    public static OrderSpecifier<?>[] getAllOrderSpecifiers(Pageable pageable, EntityPathBase entityType) {
        List<OrderSpecifier<?>> orders = new ArrayList<>();

        if (ItemCheck.isNotEmpty(pageable.getSort())) {
            for (Sort.Order order : pageable.getSort()) {
                Order direction = order.getDirection().isAscending() ? Order.ASC : Order.DESC;

                OrderSpecifier<?> orderSpecifier;

//                if (entityType.equals(Qmember)) {
//                    orderSpecifier = resolveNoticeOrder(order, direction);
//                } else {
//                    throw new IllegalArgumentException("정의되지 않은 entityType입니다: " + entityType);
//                }
//
//                if (orderSpecifier != null) {
//                    orders.add(orderSpecifier);
//                }
            }
        }

        return orders.toArray(new OrderSpecifier[0]);
    }

    // notice 필드 처리
//    private static OrderSpecifier<?> resolveNoticeOrder(Sort.Order order, Order direction) {
//        return switch (order.getProperty()) {
//            case "createDate" -> new OrderSpecifier<>(direction, Qmember.member.createDate);
//            case "title" -> new OrderSpecifier<>(direction, Qmember.member.title);
//            case "view" -> new OrderSpecifier<>(direction, Qmember.member.view);
//            default -> throw new CmmnException(StatusCode.DO_NOT_ORDER_FIELD);
//        };
//    }

}
