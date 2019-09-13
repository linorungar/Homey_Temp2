package com.example.myfirstapp

import java.util.concurrent.TimeUnit

object BackendClient {
    suspend fun fetchGroupStatuses(accountId : Int, groupId: Int) : Map<String, String>
    {
        Thread.sleep(TimeUnit.SECONDS.toMillis(10))
        val res = HashMap<String, String>()
        res["a"] = "1"
        res["b"] = "2"
        res["c"] = "3"
        return res
    }
}