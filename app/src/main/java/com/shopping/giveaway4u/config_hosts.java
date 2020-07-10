package com.shopping.giveaway4u;

public class config_hosts {

    public  final String hostname = "http://192.168.1.108/";

    public final String home = hostname+"index.php?route=api/home";

    public final String cart = hostname+"index.php?route=api/cart";

    public final String wishlist = hostname+"index.php?route=api/wishlist/add";

    public final  String wishlist_remove = hostname+"index.php?route=api/wishlist&remove="; //GET METHOD




    public final String login = hostname+"index.php?route=api/signin"; //POST AND SET TOKEN

    public final String logout = hostname+"index.php?route=api/signin/logout"; //POST WITH TOKEN


    // WISHLIST

    public  final String wishlists = hostname+"index.php?route=api/wishlist";

    public  final String wishlist_rm = hostname+"index.php?route=api/wishlist&remove=";



    //CART

    public   final String carts =  hostname+"index.php?route=api/cart";

    public final String cart_add = hostname+"index.php?route=api/cart/add";

    public final String removeCart = hostname+"index.php?route=api/cart/remove";


    //coupon POST

    public final String coupon = hostname+"index.php?route=extension/total/coupon/coupon";


    //Payment Address
    public final String billingAdress = hostname+"index.php?route=api/payment";


    //Store Billing Address IN db
    public final String billingAdresSave = hostname+"index.php?route=api/payment_address/save"; //POST


    //Store Billing address as Cookie

    public final String billingAdreesCookie = hostname+"index.php?route=api/payment_address"; //POST


    //save Shipping address
    public final String shippingAddressSave = hostname+"index.php?route=api/shipping_address/save";

    //Retreive current user Shipping Address

    public final String shippingAddress = hostname+"index.php?route=api/shipping_address";




    //Save Shipping method
    public final String shippingSave = hostname+"index.php?route=api/shipping_method/save";


    //Retrieve and Store Shipping Method as Cookie

    public final String shippingURL = hostname+"index.php?route=api/shipping_method";



    //Payment methods
    public final String paymentMethod = hostname+"index.php?route=api/payment_method";

    //set Payment Method

    public final String setPayment = hostname+"index.php?route=api/payment_method/save";



    //Confirm Orders
    public final  String confirmOrder = hostname+"index.php?route=api/confirm";



    //Order COD LINK
    public final  String OrderCOD = hostname+"index.php?route=extension/payment/cod/confirm";



    //Order Success
    public final String successOrder = hostname+"index.php?route=checkout/success";





    //Instamojo Payment Gateway Link
    public final String instamojo = hostname+"index.php?route=api/instamojo/start";


    //Orders Link
    public final String orders = hostname+"index.php?route=api/order";


    //Search Product link
    //Accepts Param : search , sort , order ,
    //Ex search=iphone , sort=p.price , order=ASC OR DESC <- Must be capital letter
    //METHOD = GET
    public final String searchBy = hostname+"index.php?route=api/search&";




}
