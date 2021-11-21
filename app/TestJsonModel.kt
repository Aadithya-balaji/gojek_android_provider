
import com.google.gson.annotations.SerializedName

data class TestJsonModel(
    @SerializedName("error")
    var error: List<Any>,
    @SerializedName("message")
    var message: String,
    @SerializedName("responseData")
    var responseData: ResponseData,
    @SerializedName("statusCode")
    var statusCode: String,
    @SerializedName("title")
    var title: String
) {
    data class ResponseData(
        @SerializedName("account_status")
        var accountStatus: String,
        @SerializedName("provider_details")
        var providerDetails: ProviderDetails,
        @SerializedName("reasons")
        var reasons: List<Reason>,
        @SerializedName("referral_amount")
        var referralAmount: String,
        @SerializedName("referral_count")
        var referralCount: String,
        @SerializedName("referral_total_amount")
        var referralTotalAmount: Int,
        @SerializedName("referral_total_count")
        var referralTotalCount: String,
        @SerializedName("requests")
        var requests: List<Request>,
        @SerializedName("ride_otp")
        var rideOtp: Int,
        @SerializedName("service_status")
        var serviceStatus: String
    ) {
        data class ProviderDetails(
            @SerializedName("activation_status")
            var activationStatus: Int,
            @SerializedName("admin_id")
            var adminId: Any,
            @SerializedName("airport_at")
            var airportAt: Any,
            @SerializedName("city_id")
            var cityId: Int,
            @SerializedName("country_code")
            var countryCode: String,
            @SerializedName("country_id")
            var countryId: Int,
            @SerializedName("currency")
            var currency: String,
            @SerializedName("currency_symbol")
            var currencySymbol: String,
            @SerializedName("current_location")
            var currentLocation: String,
            @SerializedName("current_ride_vehicle")
            var currentRideVehicle: Int,
            @SerializedName("current_store")
            var currentStore: Any,
            @SerializedName("device_id")
            var deviceId: Any,
            @SerializedName("device_token")
            var deviceToken: String,
            @SerializedName("device_type")
            var deviceType: String,
            @SerializedName("email")
            var email: String,
            @SerializedName("first_name")
            var firstName: String,
            @SerializedName("gender")
            var gender: String,
            @SerializedName("id")
            var id: Int,
            @SerializedName("is_address")
            var isAddress: Int,
            @SerializedName("is_assigned")
            var isAssigned: Int,
            @SerializedName("is_bankdetail")
            var isBankdetail: Int,
            @SerializedName("is_document")
            var isDocument: Int,
            @SerializedName("is_online")
            var isOnline: Int,
            @SerializedName("is_profile")
            var isProfile: Int,
            @SerializedName("is_service")
            var isService: Int,
            @SerializedName("iso2")
            var iso2: String,
            @SerializedName("language")
            var language: String,
            @SerializedName("last_name")
            var lastName: String,
            @SerializedName("latitude")
            var latitude: Double,
            @SerializedName("login_by")
            var loginBy: String,
            @SerializedName("longitude")
            var longitude: Double,
            @SerializedName("mobile")
            var mobile: String,
            @SerializedName("otp")
            var otp: Any,
            @SerializedName("payment_gateway_id")
            var paymentGatewayId: Any,
            @SerializedName("payment_mode")
            var paymentMode: String,
            @SerializedName("picture")
            var picture: String,
            @SerializedName("picture_draft")
            var pictureDraft: String,
            @SerializedName("qrcode_url")
            var qrcodeUrl: String,
            @SerializedName("rating")
            var rating: Double,
            @SerializedName("referal_count")
            var referalCount: Int,
            @SerializedName("referral_unique_id")
            var referralUniqueId: String,
            @SerializedName("social_unique_id")
            var socialUniqueId: Any,
            @SerializedName("state_id")
            var stateId: Int,
            @SerializedName("status")
            var status: String,
            @SerializedName("stripe_cust_id")
            var stripeCustId: Any,
            @SerializedName("unique_id")
            var uniqueId: String,
            @SerializedName("wallet_balance")
            var walletBalance: Int,
            @SerializedName("zone_id")
            var zoneId: Int
        )

        data class Reason(
            @SerializedName("id")
            var id: Int,
            @SerializedName("reason")
            var reason: String
        )

        data class Request(
            @SerializedName("admin_service")
            var adminService: String,
            @SerializedName("company_id")
            var companyId: Int,
            @SerializedName("created_at")
            var createdAt: String,
            @SerializedName("created_type")
            var createdType: String,
            @SerializedName("id")
            var id: Int,
            @SerializedName("provider_id")
            var providerId: Int,
            @SerializedName("request")
            var request: Request,
            @SerializedName("request_id")
            var requestId: Int,
            @SerializedName("schedule_at")
            var scheduleAt: Any,
            @SerializedName("service")
            var service: Service,
            @SerializedName("service_type")
            var serviceType: String,
            @SerializedName("status")
            var status: String,
            @SerializedName("time_left_to_respond")
            var timeLeftToRespond: Int,
            @SerializedName("user")
            var user: User,
            @SerializedName("user_id")
            var userId: Int
        ) {
            data class Request(
                @SerializedName("admin_id")
                var adminId: Any,
                @SerializedName("admin_service")
                var adminService: String,
                @SerializedName("assigned_at")
                var assignedAt: String,
                @SerializedName("assigned_time")
                var assignedTime: String,
                @SerializedName("base_distance")
                var baseDistance: Any,
                @SerializedName("base_weight")
                var baseWeight: Any,
                @SerializedName("booking_id")
                var bookingId: String,
                @SerializedName("calculator")
                var calculator: String,
                @SerializedName("cancel_reason")
                var cancelReason: Any,
                @SerializedName("cancelled_by")
                var cancelledBy: Any,
                @SerializedName("city_id")
                var cityId: Int,
                @SerializedName("country_id")
                var countryId: Int,
                @SerializedName("created_time")
                var createdTime: String,
                @SerializedName("currency")
                var currency: String,
                @SerializedName("d_address")
                var dAddress: Any,
                @SerializedName("d_latitude")
                var dLatitude: Int,
                @SerializedName("d_longitude")
                var dLongitude: Int,
                @SerializedName("deliveries")
                var deliveries: List<Delivery>,
                @SerializedName("delivery_mode")
                var deliveryMode: String,
                @SerializedName("delivery_type_id")
                var deliveryTypeId: Int,
                @SerializedName("delivery_vehicle_id")
                var deliveryVehicleId: Int,
                @SerializedName("destination_log")
                var destinationLog: String,
                @SerializedName("discount_percent")
                var discountPercent: Any,
                @SerializedName("distance")
                var distance: Int,
                @SerializedName("finished_at")
                var finishedAt: Any,
                @SerializedName("finished_time")
                var finishedTime: String,
                @SerializedName("geofence_id")
                var geofenceId: Any,
                @SerializedName("id")
                var id: Int,
                @SerializedName("is_drop_location")
                var isDropLocation: Int,
                @SerializedName("is_scheduled")
                var isScheduled: String,
                @SerializedName("location_points")
                var locationPoints: String,
                @SerializedName("otp")
                var otp: Any,
                @SerializedName("paid")
                var paid: Int,
                @SerializedName("payable_amount")
                var payableAmount: Int,
                @SerializedName("payment_by")
                var paymentBy: String,
                @SerializedName("payment_mode")
                var paymentMode: String,
                @SerializedName("peak_hour_id")
                var peakHourId: Any,
                @SerializedName("promocode_id")
                var promocodeId: Int,
                @SerializedName("provider_id")
                var providerId: Int,
                @SerializedName("provider_rated")
                var providerRated: Int,
                @SerializedName("provider_service_id")
                var providerServiceId: Int,
                @SerializedName("provider_vehicle_id")
                var providerVehicleId: Any,
                @SerializedName("request_type")
                var requestType: String,
                @SerializedName("return_date")
                var returnDate: Any,
                @SerializedName("route_key")
                var routeKey: String,
                @SerializedName("s_address")
                var sAddress: String,
                @SerializedName("s_latitude")
                var sLatitude: Double,
                @SerializedName("s_longitude")
                var sLongitude: Double,
                @SerializedName("schedule_at")
                var scheduleAt: Any,
                @SerializedName("schedule_time")
                var scheduleTime: String,
                @SerializedName("started_at")
                var startedAt: Any,
                @SerializedName("started_time")
                var startedTime: String,
                @SerializedName("status")
                var status: String,
                @SerializedName("surge")
                var surge: Int,
                @SerializedName("timezone")
                var timezone: String,
                @SerializedName("total_amount")
                var totalAmount: Int,
                @SerializedName("track_distance")
                var trackDistance: Int,
                @SerializedName("track_latitude")
                var trackLatitude: Double,
                @SerializedName("track_longitude")
                var trackLongitude: Double,
                @SerializedName("travel_time")
                var travelTime: Any,
                @SerializedName("unit")
                var unit: String,
                @SerializedName("use_wallet")
                var useWallet: Int,
                @SerializedName("user_id")
                var userId: Int,
                @SerializedName("user_rated")
                var userRated: Int,
                @SerializedName("weight")
                var weight: Int
            ) {
                data class Delivery(
                    @SerializedName("admin_id")
                    var adminId: Any,
                    @SerializedName("admin_service")
                    var adminService: String,
                    @SerializedName("assigned_at")
                    var assignedAt: String,
                    @SerializedName("breadth")
                    var breadth: Int,
                    @SerializedName("currency")
                    var currency: String,
                    @SerializedName("d_address")
                    var dAddress: String,
                    @SerializedName("d_latitude")
                    var dLatitude: Double,
                    @SerializedName("d_longitude")
                    var dLongitude: Double,
                    @SerializedName("delivery_request_id")
                    var deliveryRequestId: Int,
                    @SerializedName("destination_log")
                    var destinationLog: String,
                    @SerializedName("distance")
                    var distance: Int,
                    @SerializedName("finished_at")
                    var finishedAt: Any,
                    @SerializedName("geofence_id")
                    var geofenceId: Any,
                    @SerializedName("height")
                    var height: Int,
                    @SerializedName("id")
                    var id: Int,
                    @SerializedName("instruction")
                    var instruction: String,
                    @SerializedName("is_fragile")
                    var isFragile: Int,
                    @SerializedName("length")
                    var length: Int,
                    @SerializedName("location_points")
                    var locationPoints: String,
                    @SerializedName("mobile")
                    var mobile: String,
                    @SerializedName("name")
                    var name: String,
                    @SerializedName("otp")
                    var otp: String,
                    @SerializedName("package_type_id")
                    var packageTypeId: Int,
                    @SerializedName("paid")
                    var paid: Int,
                    @SerializedName("payment_mode")
                    var paymentMode: Any,
                    @SerializedName("provider_id")
                    var providerId: Any,
                    @SerializedName("provider_rated")
                    var providerRated: Int,
                    @SerializedName("route_key")
                    var routeKey: Any,
                    @SerializedName("s_address")
                    var sAddress: Any,
                    @SerializedName("s_latitude")
                    var sLatitude: Int,
                    @SerializedName("s_longitude")
                    var sLongitude: Int,
                    @SerializedName("schedule_at")
                    var scheduleAt: Any,
                    @SerializedName("started_at")
                    var startedAt: Any,
                    @SerializedName("status")
                    var status: String,
                    @SerializedName("surge")
                    var surge: Int,
                    @SerializedName("timezone")
                    var timezone: Any,
                    @SerializedName("total_distance")
                    var totalDistance: Int,
                    @SerializedName("track_distance")
                    var trackDistance: Int,
                    @SerializedName("track_latitude")
                    var trackLatitude: Int,
                    @SerializedName("track_longitude")
                    var trackLongitude: Int,
                    @SerializedName("travel_time")
                    var travelTime: Any,
                    @SerializedName("unit")
                    var unit: String,
                    @SerializedName("user_id")
                    var userId: Any,
                    @SerializedName("weight")
                    var weight: Int
                )
            }

            data class Service(
                @SerializedName("admin_service")
                var adminService: String,
                @SerializedName("base_url")
                var baseUrl: String,
                @SerializedName("display_name")
                var displayName: String,
                @SerializedName("id")
                var id: Int,
                @SerializedName("status")
                var status: Int
            )

            data class User(
                @SerializedName("city_id")
                var cityId: Int,
                @SerializedName("company_id")
                var companyId: Int,
                @SerializedName("country_code")
                var countryCode: String,
                @SerializedName("country_id")
                var countryId: Int,
                @SerializedName("created_at")
                var createdAt: String,
                @SerializedName("currency_symbol")
                var currencySymbol: String,
                @SerializedName("device_token")
                var deviceToken: String,
                @SerializedName("device_type")
                var deviceType: String,
                @SerializedName("email")
                var email: String,
                @SerializedName("first_name")
                var firstName: String,
                @SerializedName("gender")
                var gender: String,
                @SerializedName("id")
                var id: Int,
                @SerializedName("iso2")
                var iso2: String,
                @SerializedName("language")
                var language: String,
                @SerializedName("last_name")
                var lastName: String,
                @SerializedName("latitude")
                var latitude: Any,
                @SerializedName("login_by")
                var loginBy: String,
                @SerializedName("longitude")
                var longitude: Any,
                @SerializedName("mobile")
                var mobile: String,
                @SerializedName("payment_mode")
                var paymentMode: String,
                @SerializedName("picture")
                var picture: Any,
                @SerializedName("rating")
                var rating: Double,
                @SerializedName("referral_unique_id")
                var referralUniqueId: String,
                @SerializedName("state_id")
                var stateId: Int,
                @SerializedName("status")
                var status: Int,
                @SerializedName("unique_id")
                var uniqueId: String,
                @SerializedName("user_type")
                var userType: String,
                @SerializedName("wallet_balance")
                var walletBalance: Int
            )
        }
    }
}