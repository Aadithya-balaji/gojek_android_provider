package com.gox.foodservice.model

import java.io.Serializable

data class FoodieCheckRequestModel(
        var error: List<Any> = listOf(),
        var message: String = "",
        var responseData: ResponseData = ResponseData(),
        var statusCode: String = "",
        var title: String = ""
) : Serializable

data class ResponseData(
        var account_status: String = "",
        var provider_details: ProviderDetails = ProviderDetails(),
        var reasons: List<Reason> = listOf(),
        var referral_amount: String = "",
        var referral_count: String = "",
        var referral_total_amount: String = "",
        var referral_total_count: String = "",
        var requests: Request = Request(),
        var ride_otp: String = "",
        var serve_otp: String = "",
        var service_status: String = ""
) : Serializable

data class Reason(
        var created_by: String = "",
        var created_type: String = "",
        var deleted_by: Any = Any(),
        var deleted_type: Any = Any(),
        var id: Int = 0,
        var modified_by: String = "",
        var modified_type: String = "",
        var reason: String = "",
        var service: String = "",
        var status: String = "",
        var type: String = ""
) : Serializable

data class ProviderDetails(
        var activation_status: String = "",
        var admin_id: Any = Any(),
        var city_id: String = "",
        var country_code: String = "",
        var country_id: String = "",
        var currency: Any = Any(),
        var currency_symbol: String = "",
        var device_id: Any = Any(),
        var device_token: Any = Any(),
        var device_type: Any = Any(),
        var email: String = "",
        var first_name: String = "",
        var gender: Any = Any(),
        var id: Int = 0,
        var is_assigned: String = "",
        var is_bankdetail: String = "",
        var is_document: String = "",
        var is_online: String = "",
        var is_service: String = "",
        var language: String = "",
        var last_name: String = "",
        var latitude: String = "",
        var login_by: String = "",
        var longitude: String = "",
        var mobile: String = "",
        var otp: Any = Any(),
        var payment_gateway_id: Any = Any(),
        var payment_mode: String = "",
        var picture: Any = Any(),
        var qrcode_url: String = "",
        var rating: Double = 0.0,
        var referal_count: String = "",
        var referral_unique_id: String = "",
        var social_unique_id: Any = Any(),
        var state_id: Any = Any(),
        var status: String = "",
        var stripe_cust_id: Any = Any(),
        var wallet_balance: String = "",
        var zone_id: Any = Any()
) : Serializable

data class Request(
        var admin_service_id: Int = 0,
        var company_id: String = "",
        var created_at: String = "",
        var id: Int = 0,
        var provider_id: Int = 0,
//        var request: RequestX = RequestX(),
        var request_id: String = "",
        var schedule_at: Any = Any(),
        var service: Service = Service(),
        var status: String = "",
        var time_left_to_respond: Int = 0,
        var user: User = User(),
        var user_id: Int = 0,
        var store_order_invoice_id: String = "",
        var user_address_id: String = "",
        var promocode_id: String = "",
        var store_id: String = "",
        var provider_vehicle_id: String = "",
        var note: String = "",
        var delivery_date: String = "",
        var pickup_addressd: String = "",
        var delivery_address: String = "",
        var order_type: String = "",
        var schedule_datetime: String = "",
        var order_otp: String = "",
        var order_ready_time: String = "",
        var order_ready_status: String = "",
        var paid: String = "",
        var provider_rated: String = "",
        var cancelled_by: String = "",
        var user_rated: String = "",
        var cancel_reason: String = "",
        var schedule_status: String = "",
        var request_type: String = "",
        var delivery: Delivery = Delivery(),
        var pickup: Pickup = Pickup(),
        var payment: Payment = Payment(),
        var stores_details: StoreDetails = StoreDetails(),
        var order_invoice: OrderInvoice = OrderInvoice(),
        var leave_at_door:Int=0
) : Serializable

class OrderInvoice(
        var id: Int = 0,
        var store_order_id: String = "",
        var payment_mode: String = "",
        var gross: String = "",
        var discount: Double = 0.0,
        var promocode_amount: Double = 0.0,
        var wallet_amount: Double = 0.0,
        var tax_amount: Double = 0.0,
        var delivery_amount: Double = 0.0,
        var store_package_amount: Double = 0.0,
        var total_amount: Double = 0.0,
        var cash: Double = 0.0,
        var payable: Double = 0.0,
        var cart_details: String = "",
        var items: List<Item> = listOf()

) : Serializable

class Item(
        var id: Int = 0,
        var user_id: String = "",
        var store_item_id: String = "",
        var store_id: String = "",
        var store_order_id: String = "",
        var company_id: String = "",
        var quantity: String = "",
        var item_price: Double = 0.0,
        var total_item_price: Double = 0.0,
        var tot_addon_price: Double = 0.0,
        var note: String = "",
        var product_data: String = "",
        var product: Product = Product(),
//        var store: Store = Store(),
        var cartaddon: List<CartAddon> = listOf()
) : Serializable

class CartAddon(
        var id: Int = 0,
        var store_cart_id: String = "",
        var store_cart_item_id: String = "",
        var store_item_addons_id: String = "",
        var store_addon_id: String = "",
        var company_id: String = "",
        var addon_price: Double = 0.0,
        var addon_name: String = ""
) : Serializable

//class Store(
//        var store_name: String = "",
//        var store_packing_charges: String = "",
//        var store_gst: String = "",
//        var offer_min_amount: String = "",
//        var commission: String = "",
//        var offer_percent: String = "",
//        var free_delivery: String = "",
//        var id: Int = 0,
//        var store_type_id: String = "",
//        var storetype: FoodieStoretype = FoodieStoretype()
//) : Serializable

class StoreType(
        var id: Int = 0,
        var company_id: String = "",
        var name: String = "",
        var category: String = "",
        var status: String = ""
) : Serializable

class Product(
        var item_name: String = "",
        var item_price: Double = 0.0,
        var id: String = "") : Serializable

class StoreDetails(
        var id: Int = 0,
        var store_name: String = "",
        var store_location: String = "",
        var store_zipcode: String = "",
        var city_id: String = "",
        var rating: String = "",
        var latitude: String = "",
        var longitude: String = "",
        var picture: String = "",
        var is_veg: String = ""
) : Serializable

class Pickup(
        var id: Int = 0,
        var picture: String = "",
        var contact_number: String = "",
        var store_type_idd: String = "",
        var latitude: String = "",
        var longitude: String = "",
        var store_location: String = "",
        var store_name: String = "",
        var storetype: FoodieStoretype = FoodieStoretype()
) : Serializable

class FoodieStoretype(
        var id: Int = 0,
        var company_id: String = "",
        var name: String = "",
        var category: String = "",
        var status: String = ""
) : Serializable

class Delivery(
        var id: Int = 0,
        var latitude: String = "",
        var longitude: String = "",
        var map_address: String = "",
        var flat_no: String = "",
        var street: String = ""
) : Serializable

data class User(
        var city_id: String = "",
        var country_code: String = "",
        var country_id: String = "",
        var created_at: String = "",
        var currency_symbol: String = "",
        var email: String = "",
        var first_name: String = "",
        var gender: String = "",
        var id: Int = 0,
        var language: String = "",
        var last_name: String = "",
        var latitude: Any = Any(),
        var login_by: String = "",
        var longitude: Any = Any(),
        var mobile: String = "",
        var payment_mode: String = "",
        var picture: String = "",
        var rating: String = "",
        var state_id: String = "",
        var status: String = "",
        var user_type: String = "",
        var wallet_balance: String = ""
) : Serializable

//data class RequestX(
//        var admin_id: Any = Any(),
//        var assigned_at: String = "",
//        var assigned_time: String = "",
//        var booking_id: String = "",
//        var calculator: String = "",
//        var cancel_reason: Any = Any(),
//        var cancelled_by: Any = Any(),
//        var city_id: String = "",
//        var company_id: String = "",
//        var country_id: Any = Any(),
//        var created_at: String = "",
//        var currency: Any = Any(),
//        var d_address: String = "",
//        var d_latitude: Double = 0.0,
//        var d_longitude: Double = 0.0,
//        var destination_log: String = "",
//        var distance: Double = 0.0,
//        var finished_at: String = "",
//        var finished_time: String = "",
//        var id: Int = 0,
//        var invoice: Invoice = Invoice(),
//        var is_scheduled: String = "",
//        var is_track: String = "",
//        var otp: String = "",
//        var paid: Any = Any(),
//        var payment: Payment = Payment(),
//        var payment_mode: String = "",
//        var peak_hour_id: Any = Any(),
//        var promocode_id: String = "",
//        var provider_id: String = "",
//        var provider_rated: String = "",
//        var provider_service_id: String = "",
//        var provider_vehicle_id: String = "",
//        var request_type: String = "",
//        var ride_delivery_id: String = "",
//        var ride_type_id: String = "",
//        var route_key: String = "",
//        var s_address: String = "",
//        var s_latitude: Double = 0.0,
//        var s_longitude: Double = 0.0,
//        var schedule_at: Any = Any(),
//        var schedule_time: String = "",
//        var started_at: String = "",
//        var started_time: String = "",
//        var status: String = "",
//        var surge: String = "",
//        var timezone: String = "",
//        var track_distance: Double = 0.0,
//        var track_latitude: Double = 0.0,
//        var track_longitude: Double = 0.0,
//        var travel_time: String = "",
//        var unit: String = "",
//        var use_wallet: String = "",
//        var user: User = User(),
//        var user_id: String = "",
//        var user_rated: String = "",
//        var vehicle_type: String = ""
//) : Serializable

//data class Invoice(
//        var commision: String = "",
//        var commision_percent: String = "",
//        var company_id: String = "",
//        var discount: String = "",
//        var discount_percent: String = "",
//        var distance: Double = 0.0,
//        var fixed: String = "",
//        var fleet_id: Any = Any(),
//        var hour: String = "",
//        var id: Int = 0,
//        var minute: String = "",
//        var payable: String = "",
//        var payment_mode: String = "",
//        var peak_amount: String = "",
//        var peak_comm_amount: String = "",
//        var provider_id: String = "",
//        var provider_pay: Double = 0.0,
//        var ride_request_id: String = "",
//        var round_of: Double = 0.0,
//        var tax: String = "",
//        var tax_percent: String = "",
//        var toll_charge: String = "",
//        var total: Double = 0.0,
//        var total_waiting_time: String = "",
//        var user_id: String = "",
//        var waiting_amount: String = "",
//        var waiting_comm_amount: String = ""
//) : Serializable

data class Payment(
        var card: String = "",
        var cash: Double = 0.0,
        var commision: Double = 0.0,
        var commision_percent: String = "",
        var company_id: String = "",
        var discount: String = "",
        var discount_percent: String = "",
        var distance: Double = 0.0,
        var fixed: Double = 0.0,
        var fleet: Double = 0.0,
        var fleet_id: Any = Any(),
        var fleet_percent: String = "",
        var hour: String = "",
        var id: Int = 0,
        var is_partial: Any = Any(),
        var minute: String = "",
        var payable: Double = 0.0,
        var payment_id: Any = Any(),
        var payment_mode: String = "",
        var peak_amount: String = "",
        var peak_comm_amount: String = "",
        var promocode_id: Any = Any(),
        var provider_id: String = "",
        var provider_pay: Double = 0.0,
        var ride_request_id: String = "",
        var round_of: Double = 0.0,
        var tax: Double = 0.0,
        var tax_percent: String = "",
        var tips: String = "",
        var toll_charge: Double = 0.0,
        var total: Double = 0.0,
        var total_waiting_time: String = "",
        var user_id: String = "",
        var waiting_amount: String = "",
        var waiting_comm_amount: Double = 0.0,
        var wallet: String = ""
) : Serializable

data class Service(
        var admin_service: String = "",
        var base_url: String = "",
        var display_name: String = "",
        var id: Int = 0,
        var status: String = ""
) : Serializable
