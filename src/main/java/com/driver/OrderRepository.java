package com.driver;

import java.util.*;

import io.swagger.models.auth.In;
import org.springframework.stereotype.Repository;

@Repository
public class OrderRepository {

    private HashMap<String, Order> orderMap;
    private HashMap<String, DeliveryPartner> partnerMap;
    private HashMap<String, HashSet<String>> partnerToOrderMap;
    private HashMap<String, String> orderToPartnerMap;

    public OrderRepository(){
        this.orderMap = new HashMap<String, Order>();
        this.partnerMap = new HashMap<String, DeliveryPartner>();
        this.partnerToOrderMap = new HashMap<String, HashSet<String>>();
        this.orderToPartnerMap = new HashMap<String, String>();
    }

    public void saveOrder(Order order){
        // your code here
        this.orderMap.put(order.getId(), order);
    }

    public void savePartner(String partnerId){
        // your code here
        // create a new partner with given partnerId and save it
        DeliveryPartner deliveryPartner = new DeliveryPartner(partnerId);
        this.partnerToOrderMap.put(partnerId, new HashSet<>());
        this.partnerMap.put(partnerId, deliveryPartner);
    }

    public void saveOrderPartnerMap(String orderId, String partnerId){
        if(orderMap.containsKey(orderId) && partnerMap.containsKey(partnerId)){
            // your code here
            //add order to given partner's order list
            //increase order count of partner
            //assign partner to this order
//            HashSet<String> hs = partnerToOrderMap.get(partnerId);
//            hs.add(orderId);
//            this.partnerToOrderMap.put(partnerId, partnerToOrderMap.get(partnerId).add(orderId));
            this.partnerToOrderMap.get(partnerId).add(orderId);
            DeliveryPartner partner = partnerMap.get(partnerId);
            partner.setNumberOfOrders(partner.getNumberOfOrders()+1);
            this.partnerMap.put(partnerId, partner);
            this.orderToPartnerMap.put(orderId, partnerId);
        }
    }

    public Order findOrderById(String orderId){
        // your code here
        if (this.orderMap.containsKey(orderId)){
            return this.orderMap.get(orderId);
        }

        return null;
    }

    public DeliveryPartner findPartnerById(String partnerId){
        // your code here
        if (this.partnerMap.containsKey(partnerId)){
            return this.partnerMap.get(partnerId);
        }

        return null;
    }

    public Integer findOrderCountByPartnerId(String partnerId){
        // your code here
        if (this.partnerToOrderMap.containsKey(partnerId)){
            return this.partnerToOrderMap.get(partnerId).size();
        }

        return 0;
    }

    public List<String> findOrdersByPartnerId(String partnerId){
        // your code here
        List<String> list = new ArrayList<>();
        if (this.partnerToOrderMap.containsKey(partnerId)){
            list.addAll(this.partnerToOrderMap.get(partnerId));
        }

        return list;
    }

    public List<String> findAllOrders(){
        // your code here
        // return list of all orders
        return new ArrayList<>(orderMap.keySet());
    }

    public void deletePartner(String partnerId){
        // your code here
        // delete partner by ID
        this.partnerMap.remove(partnerId);
//        this.partnerToOrderMap.remove(partnerId);
//        for (String s : this.orderToPartnerMap.keySet()){
//            if (orderToPartnerMap.get(s).equals(partnerId)){
//                orderToPartnerMap.remove(s);
//            }
//        }
    }

    public void deleteOrder(String orderId){
        // your code here
        // delete order by ID
        this.orderMap.remove(orderId);
        for (String s : this.partnerToOrderMap.keySet()){
            this.partnerToOrderMap.get(s).remove(orderId);
        }
//        for (String s : this.orderToPartnerMap.keySet()){
//            if (s.equals(orderId)){
//                this.orderToPartnerMap.remove(s);
//            }
//        }
        this.orderToPartnerMap.remove(orderId);
    }

    public Integer findCountOfUnassignedOrders(){
        // your code here
        int ans = 0;
        for (String s : this.orderMap.keySet()){
            if (!this.orderToPartnerMap.containsKey(s)){
                ans++;
            }
        }

        return ans;
    }

    public Integer findOrdersLeftAfterGivenTimeByPartnerId(String timeString, String partnerId){
        // your code here
        String[] arr = timeString.split(":");
        int hr = Integer.parseInt(arr[0]);
        int mm = Integer.parseInt(arr[1]);
        hr = hr *  60 + mm;
//        int hr1 = hr * 60 + mm;
        HashSet<String> hs = this.partnerToOrderMap.get(partnerId);
        int ans = 0;

        for (String s : hs){
            Order order = this.orderMap.get(s);
            if (order != null && order.getDeliveryTime() > hr){
                ans++;
            }
        }

        return ans;
    }

    public String findLastDeliveryTimeByPartnerId(String partnerId){
        // your code here
        // code should return string in format HH:MM
        int max = Integer.MIN_VALUE;
        String ans = "";
        int time = 0;
        HashSet<String> hs = partnerToOrderMap.get(partnerId);

        for (String s : hs){
            Order order = orderMap.get(s);
            if (order.getDeliveryTime() > max){
                max = order.getDeliveryTime();
                time = order.getDeliveryTime();
            }
        }

        int hr = time / 60;
        int minute = time % 60;
        ans += hr + ":" + minute;
        return ans;
    }
}