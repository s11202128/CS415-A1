package com.bof.mobile.data.remote

import com.bof.mobile.model.AccountDetailsResponse
import com.bof.mobile.model.AccountItem
import com.bof.mobile.model.BillerItem
import com.bof.mobile.model.DashboardResponse
import com.bof.mobile.model.InitiateTransferRequest
import com.bof.mobile.model.InitiateTransferResponse
import com.bof.mobile.model.LoginRequest
import com.bof.mobile.model.LoginResponse
import com.bof.mobile.model.PaginatedTransactionsResponse
import com.bof.mobile.model.RecipientItem
import com.bof.mobile.model.RegisterRequest
import com.bof.mobile.model.RegisterResponse
import com.bof.mobile.model.TransactionItem
import com.bof.mobile.model.ValidateDestinationRequest
import com.bof.mobile.model.ValidateDestinationResponse
import com.bof.mobile.model.VerifyTransferRequest
import com.bof.mobile.model.VerifyTransferResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import retrofit2.http.Path

interface ApiService {
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse

    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): RegisterResponse

    @GET("dashboard")
    suspend fun getDashboard(@Query("customerId") customerId: Int): DashboardResponse

    @GET("accounts")
    suspend fun getAccounts(): List<AccountItem>

    @GET("accounts/{id}/details")
    suspend fun getAccountDetails(@Path("id") accountId: Int, @Query("limit") limit: Int = 20): AccountDetailsResponse

    @GET("transactions")
    suspend fun getTransactions(@Query("accountId") accountId: Int): List<TransactionItem>

    @GET("transactions")
    suspend fun getTransactionsPaginated(
        @Query("accountId") accountId: Int,
        @Query("paginated") paginated: Boolean = true,
        @Query("page") page: Int = 1,
        @Query("pageSize") pageSize: Int = 20,
        @Query("type") type: String? = null
    ): PaginatedTransactionsResponse

    @POST("transfers/validate-destination")
    suspend fun validateDestination(@Body request: ValidateDestinationRequest): ValidateDestinationResponse

    @POST("transfers/initiate")
    suspend fun initiateTransfer(@Body request: InitiateTransferRequest): InitiateTransferResponse

    @POST("otp/verify")
    suspend fun verifyTransfer(@Body request: VerifyTransferRequest): VerifyTransferResponse

    @GET("recipients/search")
    suspend fun searchRecipients(@Query("q") query: String): List<RecipientItem>

    @GET("billers")
    suspend fun getBillers(): List<BillerItem>
}
