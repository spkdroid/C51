package com.check.coupon.repository

import android.util.Log
import com.check.coupon.di.component.DaggerNetworkComponent
import com.check.coupon.model.Offer
import com.check.coupon.service.CouponApi
import com.check.coupon.util.Constants
import io.paperdb.Paper
import javax.inject.Inject

/**
 *   CouponRepository - Reads the data from the internet or local cache based on the context
 */

class CouponRepository private constructor() {
    // Singleton implementation
    companion object {
        var coupon =  CouponRepository()
        var offerList:ArrayList<Offer> = ArrayList()
        
        fun getInstance(): CouponRepository {
            if (coupon == null)
                coupon = CouponRepository()
            return coupon
        }
    }

    init {
        DaggerNetworkComponent.builder().build().inject(this)
    }

    // Inject network module
    @Inject
    lateinit var api: CouponApi

    suspend fun initialize() {
        try {
            offerList = api.getCoupon().offers as ArrayList<Offer>
        } catch (e:Exception) {
            Log.d("Api Error","Error in remote source")
        }
        // store the data into the paper db
        if(offerList.isNotEmpty())
            Paper.book().write(Constants.OFFER_CACHE,offerList)
    }

    fun getOffers():ArrayList<Offer> {
        // read data from the cache
        if(offerList.isEmpty()) {
            return if(Paper.book().contains(Constants.OFFER_CACHE)) {
                val result = Paper.book().read(Constants.OFFER_CACHE) as ArrayList<Offer>
                result
            } else {
                ArrayList()
            }
        }
        // return the data from the internet
        return offerList
    }
}