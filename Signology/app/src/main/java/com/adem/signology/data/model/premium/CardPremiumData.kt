package com.adem.signology.data.model.premium

object CardPremiumData {
    val premiumData = listOf(
        PremiumModel(
            1,
            "Premium Individual",
            "15.000,00",
            listOf("• 1 bulan penggunaan", "• Maksimal 30 foto/translate", "• 7 riwayat translate terbaru")
        ),
        PremiumModel(
            2,
            "Premium Mini Organization",
            "100.000,00",
            listOf("•6 bulan penggunaan", "• Maksimal 30 foto/translate", "• 7 riwayat translate terbaru", "• Bisa digunakan untuk 15 perangkat berbeda")
        ),
        PremiumModel(
            3,
            "Premium Big Organization",
            "150.000,00",
            listOf("• 6 bulan penggunaan", "• Maksimal 30 foto/translate", "• 7 riwayat translate terbaru", "• Bisa digunakan untuk 30 perangkat berbeda")
        )
    )
}