package com.gox.partner.models

import java.io.Serializable

data class CheckRequestModel(
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
        var referral_total_amount: Double? = 0.0,
        var referral_total_count: String = "",
        var requests: List<Request> = listOf(),
        var ride_otp: String = "",
        var service_status: String = ""
) : Serializable

data class Reason(
        var created_by: Int? = 0,
        var created_type: String = "",
        var deleted_by: Any = Any(),
        var deleted_type: Any = Any(),
        var id: Int? = 0,
        var modified_by: Int? = 0,
        var modified_type: String = "",
        var reason: String = "",
        var service: String = "",
        var status: String = "",
        var type: String = ""
) : Serializable

data class ProviderDetails(
        var activation_status: Int? = 0,
        var admin_id: Any = Any(),
        var city_id: Int? = 0,
        var country_code: String = "",
        var country_id: Int? = 0,
        var currency: Any = Any(),
        var currency_symbol: String = "",
        var device_id: Any = Any(),
        var device_token: Any = Any(),
        var device_type: Any = Any(),
        var email: String = "",
        var first_name: String = "",
        var gender: Any = Any(),
        var id: Int? = 0,
        var is_assigned: Int? = 0,
        var is_bankdetail: Int? = 0,
        var is_document: Int? = 0,
        var is_online: Int? = 0,
        var is_service: Int? = 0,
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
        var rating: Double? = 0.0,
        var referal_count: Int? = 0,
        var referral_unique_id: String = "",
        var social_unique_id: Any = Any(),
        var state_id: Any = Any(),
        var status: String = "",
        var stripe_cust_id: Any = Any(),
        var wallet_balance: Double? = 0.0,
        var zone_id: Any = Any()
) : Serializable

data class Request(
        var admin_service: String? = "",
        var company_id: Int? = 0,
        var created_at: String = "",
        var id: Int? = 0,
        var provider_id: Int? = 0,
        var request: RequestX = RequestX(),
        var request_id: Int? = 0,
        var schedule_at: Any = Any(),
        var service: Service = Service(),
        var status: String = "",
        var time_left_to_respond: Int? = 0,
        var user: User = User(),
        var user_id: Int = 0
) : Serializable

data class User(
        var city_id: Int? = 0,
        var country_code: String = "",
        var country_id: Int? = 0,
        var created_at: String = "",
        var currency_symbol: String = "",
        var email: String = "",
        var first_name: String = "",
        var gender: String = "",
        var id: Int? = 0,
        var language: String = "",
        var last_name: String = "",
        var latitude: Any = Any(),
        var login_by: String = "",
        var longitude: Any = Any(),
        var mobile: String = "",
        var payment_mode: String = "",
        var picture: Any = Any(),
        var rating: String = "",
        var state_id: Int? = 0,
        var status: Int? = 0,
        var user_type: String = "",
        var wallet_balance: Double? = 0.0
) : Serializable

data class RequestX(
        var admin_id: Any = Any(),
        var assigned_at: String = "",
        var assigned_time: String = "",
        var booking_id: String = "",
        var calculator: String = "",
        var cancel_reason: Any = Any(),
        var cancelled_by: Any = Any(),
        var city_id: Int? = 0,
        var company_id: Int? = 0,
        var country_id: Any = Any(),
        var created_at: String = "",
        var currency: Any = Any(),
        var d_address: String = "",
        var d_latitude: Double? = 0.0,
        var d_longitude: Double? = 0.0,
        var destination_log: String = "",
        var distance: Double? = 0.0,
        var finished_at: String = "",
        var finished_time: String = "",
        var id: Int? = 0,
        var invoice: Invoice = Invoice(),
        var is_scheduled: String = "",
        var is_track: String = "",
        var otp: String = "",
        var paid: Any = Any(),
        var payment: Payment = Payment(),
        var payment_mode: String = "",
        var peak_hour_id: Any = Any(),
        var pickup: Pickup = Pickup(),
        var promocode_id: Double? = 0.0,
        var provider_id: Int? = 0,
        var provider_rated: Double? = 0.0,
        var provider_service_id: Int? = 0,
        var provider_vehicle_id: Int? = 0,
        var request_type: String = "",
        var ride_delivery_id: Int? = 0,
        var ride_type_id: Int? = 0,
        var route_key: String = "",
        var s_address: String = "",
        var s_latitude: Double? = 0.0,
        var s_longitude: Double? = 0.0,
        var schedule_at: Any = Any(),
        var schedule_time: String = "",
        var started_at: String = "",
        var started_time: String = "",
        var status: String = "",
        var surge: Double? = 0.0,
        var timezone: String = "",
        var track_distance: Double? = 0.0,
        var track_latitude: Double? = 0.0,
        var track_longitude: Double? = 0.0,
        var travel_time: Int? = 0,
        var unit: String = "",
        var use_wallet: Int? = 0,
        var user: User = User(),
        var user_id: Int? = 0,
        var user_rated: Double? = 0.0,
        var vehicle_type: String = "",
        var ride_type: RideType = RideType(),
        var service: ServiceX = ServiceX(),
        var ride: Ride = Ride()

) : Serializable

data class Invoice(
        var commision: Double? = 0.0,
        var commision_percent: Double? = 0.0,
        var company_id: Int? = 0,
        var discount: Double? = 0.0,
        var discount_percent: Double? = 0.0,
        var distance: Double? = 0.0,
        var fixed: Double? = 0.0,
        var fleet_id: Any = Any(),
        var hour: Double? = 0.0,
        var id: Int? = 0,
        var minute: Double? = 0.0,
        var payable: Double? = 0.0,
        var payment_mode: String = "",
        var peak_amount: Double? = 0.0,
        var peak_comm_amount: Double? = 0.0,
        var provider_id: Int? = 0,
        var provider_pay: Double? = 0.0,
        var ride_request_id: Int? = 0,
        var round_of: Double? = 0.0,
        var tax: Double? = 0.0,
        var tax_percent: Double? = 0.0,
        var toll_charge: Double? = 0.0,
        var total: Double? = 0.0,
        var total_waiting_time: Int? = 0,
        var user_id: Int? = 0,
        var waiting_amount: Double? = 0.0,
        var waiting_comm_amount: Double? = 0.0
) : Serializable

data class Payment(
        var card: Double? = 0.0,
        var cash: Double? = 0.0,
        var commision: Double? = 0.0,
        var commision_percent: Double? = 0.0,
        var company_id: Int? = 0,
        var discount: Double? = 0.0,
        var discount_percent: Double? = 0.0,
        var distance: Double? = 0.0,
        var fixed: Double? = 0.0,
        var fleet: Double? = 0.0,
        var fleet_id: Any = Any(),
        var fleet_percent: Double? = 0.0,
        var hour: Double? = 0.0,
        var id: Int? = 0,
        var is_partial: Any = Any(),
        var minute: Double? = 0.0,
        var payable: Double? = 0.0,
        var payment_id: Any = Any(),
        var payment_mode: String = "",
        var peak_amount: Double? = 0.0,
        var peak_comm_amount: Double? = 0.0,
        var promocode_id: Double? = 0.0,
        var provider_id: Int? = 0,
        var provider_pay: Double? = 0.0,
        var ride_request_id: Int? = 0,
        var round_of: Double? = 0.0,
        var tax: Double? = 0.0,
        var tax_percent: Double? = 0.0,
        var tips: Double? = 0.0,
        var toll_charge: Double? = 0.0,
        var total: Double? = 0.0,
        var total_waiting_time: Int? = 0,
        var user_id: Int? = 0,
        var waiting_amount: Double? = 0.0,
        var waiting_comm_amount: Double? = 0.0,
        var wallet: Double? = 0.0
) : Serializable

class Pickup(
        var id: String = "",
        var picture: String = "",
        var contact_number: String = "",
        var store_type_idd: String = "",
        var latitude: String = "",
        var longitude: String = "",
        var store_location: String = "",
        var store_name: String = "",
        var storetype: StoreType = StoreType()
) : Serializable

data class Service(
        var admin_service: String = "",
        var base_url: String = "",
        var display_name: String = "",
        var id: Int? = 0,
        var status: Int? = 0
) : Serializable

data class Ride(
        var capacity: Int = 0,
        var company_id: Int = 0,
        var id: Int = 0,
        var ride_type_id: Int = 0,
        var status: Int = 0,
        var vehicle_image: String = "",
        var vehicle_marker: String = "",
        var vehicle_name: String = "",
        var vehicle_type: String = ""
) : Serializable

data class RideType(
        var id: Int = 0,
        var ride_name: String = "",
        var status: Int = 0
) : Serializable

data class StoreType(
        var category: String = "",
        var company_id: Int = 0,
        var id: Int = 0,
        var name: String = "",
        var status: Int = 0
) : Serializable

data class ServiceX(
        var allow_after_image: Int = 0,
        var allow_before_image: Int = 0,
        var allow_desc: Int = 0,
        var company_id: Int = 0,
        var id: Int = 0,
        var is_professional: Int = 0,
        var picture: String = "",
        var service_category: ServiceCategory = ServiceCategory(),
        var service_category_id: Int = 0,
        var service_name: String = "",
        var service_status: Int = 0,
        var service_subcategory_id: Int = 0,
        var servicesub_category: ServiceSubCategory = ServiceSubCategory()
) : Serializable

data class ServiceSubCategory(
        var company_id: Int = 0,
        var id: Int = 0,
        var picture: String = "",
        var service_category_id: Int = 0,
        var service_subcategory_name: String = "",
        var service_subcategory_order: Int = 0,
        var service_subcategory_status: Int = 0
) : Serializable

data class ServiceCategory(
        var alias_name: String = "",
        var company_id: Int = 0,
        var id: Int = 0,
        var picture: String = "",
        var price_choose: String = "",
        var service_category_name: String = "",
        var service_category_order: Int = 0,
        var service_category_status: Int = 0
) : Serializable