package com.miv.utils

import kotlin.math.min

fun levenshtein(lhs: CharSequence?, rhs: CharSequence): Int {
    when {
        lhs == null -> return rhs.length
        lhs == rhs -> return 0
        lhs.isEmpty() -> return rhs.length
        rhs.isEmpty() -> return lhs.length
    }
    val lhsLength = lhs!!.length + 1
    val rhsLength = rhs.length + 1

    var cost = Array(lhsLength) { it }
    var newCost = Array(lhsLength) { 0 }

    for (i in 1..<rhsLength) {
        newCost[0] = i

        for (j in 1..<lhsLength) {
            val match = if (lhs[j - 1] == rhs[i - 1]) 0 else 1

            val costReplace = cost[j - 1] + match
            val costInsert = cost[j] + 1
            val costDelete = newCost[j - 1] + 1

            newCost[j] = min(min(costInsert, costDelete), costReplace)
        }

        val swap = cost
        cost = newCost
        newCost = swap
    }

    return cost[lhsLength - 1]
}
