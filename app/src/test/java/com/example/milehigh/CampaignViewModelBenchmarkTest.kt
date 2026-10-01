package com.example.milehigh

import com.example.milehigh.core.SentinelSecurity
import com.example.milehigh.data.CampaignDataLoader
import kotlin.system.measureNanoTime

class CampaignViewModelBenchmarkTest {

    fun benchmarkBlockingMainThreadLoad() {
        val loader = CampaignDataLoader()
        val elapsedNanos = measureNanoTime {
            val data = loader.getFallbackCampaignData()
            val audit = SentinelSecurity.runAudit(data)
        }
        println("Synchronous / Main Thread Load Execution Time: ${elapsedNanos / 1_000_000.0} ms")
    }
}
