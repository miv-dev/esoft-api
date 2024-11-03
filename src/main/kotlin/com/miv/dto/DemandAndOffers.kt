package com.miv.dto

import com.miv.models.demand.DemandClass
import com.miv.models.offer.OfferClass
import kotlinx.serialization.Serializable

@Serializable
data class DemandAndOffers(
    val offers: List<OfferClass>,
    val demands: List<DemandClass>
)
