package com.example.milehigh

import com.example.milehigh.core.SentinelSecurity
import com.example.milehigh.data.CampaignDataLoader

class CampaignViewModelTest {

    fun testCampaignLoading() {
        val loader = CampaignDataLoader()
        val data = loader.getFallbackCampaignData()
        val audit = SentinelSecurity.runAudit(data)

        assert(data.sceneId == "MILEHIGH_INTO_THE_VOID_CAMPAIGN")
        assert(data.metadata.systemParity == 9)
        assert(audit.isAllPassed)
    }
}
